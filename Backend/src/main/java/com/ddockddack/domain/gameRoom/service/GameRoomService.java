package com.ddockddack.domain.gameRoom.service;

import com.ddockddack.domain.game.response.GameDetailRes;
import com.ddockddack.domain.game.service.GameService;
import com.ddockddack.domain.gameRoom.repository.GameRoom;
import com.ddockddack.domain.gameRoom.repository.GameRoomRepository;
import com.ddockddack.domain.gameRoom.response.GameRoomRes;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.AccessDeniedException;
import com.ddockddack.global.error.exception.NotFoundException;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepository gameRoomRepository;
    private final GameService gameService;
    private final MemberRepository memberRepository;
    @Value("${memberImageUploadPath}")
    private String memberImageUploadPath;

    /**
     * 방 생성
     *
     * @param gameId
     * @return
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    public String createRoom(Long gameId) throws OpenViduJavaClientException, OpenViduHttpException {
        //repository로 요청 전달
        return gameRoomRepository.create(gameId);
    }

    /**
     * 방 참가
     *
     * @param pinNumber
     * @param memberId
     * @param nickname
     * @return
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    public GameRoomRes joinRoom(String pinNumber, Long memberId, String nickname) throws OpenViduJavaClientException, OpenViduHttpException {
        Member member = null;
        //로그인 한 유저면 memberId로 검색해서 넘겨줌
        if (memberId != null) {
            member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        }


        String token = gameRoomRepository.join(pinNumber, member, nickname);
        GameRoom gameRoom = this.findGameRoom(pinNumber);

        if(gameRoom.isStarted()) {
            throw new AccessDeniedException(ErrorCode.ALREADY_EXIST_REPORTEDGAME);
        }

        GameDetailRes game = gameService.findGame(gameRoom.getGameId());

        return GameRoomRes.builder()
                .token(token)
                .gameDetailRes(game)
                .pinNumber(pinNumber)
                .build();
    }

    /**
     * 게임방 멤버 삭제
     *
     * @param pinNumber
     * @param sessionId
     */
    public void removeGameMember(String pinNumber, String sessionId) {

        gameRoomRepository.findSessionByPinNumber(pinNumber).orElseThrow(() ->
                new NotFoundException(ErrorCode.GAME_ROOM_NOT_FOUND));

        gameRoomRepository.deleteGameMember(pinNumber, sessionId);
    }

    /**
     * 게임방 삭제
     *
     * @param pinNumber
     */
    public void removeGameRoom(String pinNumber) {
        gameRoomRepository.findSessionByPinNumber(pinNumber).orElseThrow(() ->
                new NotFoundException(ErrorCode.GAME_ROOM_NOT_FOUND));

        // 게임 삭제 콬드
        gameRoomRepository.deleteById(pinNumber);
    }

    /**
     * 방 정보 조회
     *
     * @param pinNumber
     * @return GameRoom
     */
    public GameRoom findGameRoom(String pinNumber) {

        return Optional.ofNullable(gameRoomRepository.findById(pinNumber)).orElseThrow(() ->
                new NotFoundException(ErrorCode.GAME_ROOM_NOT_FOUND)).get();
    }

    /**
     * 게임 시작
     *
     * @param pinNumber
     */
    public void startGame(String pinNumber) {

        Optional.ofNullable(gameRoomRepository.findSessionByPinNumber(pinNumber).orElseThrow(() ->
                new NotFoundException(ErrorCode.GAME_ROOM_NOT_FOUND)));

        gameRoomRepository.updateGameRoom(pinNumber);

    }

    /**
     * 게임 시작 여부 조회
     *
     * @param pinNumber
     * @return
     */
    public Boolean isStartedGame(String pinNumber) {
        GameRoom gameRoom = Optional.ofNullable(gameRoomRepository.findById(pinNumber).orElseThrow(() ->
                new NotFoundException(ErrorCode.GAME_ROOM_NOT_FOUND))).get();
        return gameRoom.isStarted();
    }

    /**
     * 게임 멤버 이미지 저장
     * @param pinNumber
     * @param sessionId
     * @param data
     */
    public void saveGameMemberImage(String pinNumber, String sessionId, String data) {

        Optional.ofNullable(gameRoomRepository.findById(pinNumber).orElseThrow(() ->
                new NotFoundException(ErrorCode.GAME_ROOM_NOT_FOUND)));

        String path = memberImageUploadPath + pinNumber;

        File file = new File(path);
        // 디렉토리가 존재 하지 않는 경우
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        try (FileOutputStream stream = new FileOutputStream(file.getAbsolutePath() + File.separator + fileName)) {
            byte[] image = Base64.decodeBase64(data);
            stream.write(image);
            gameRoomRepository.saveMemberImageUrl(pinNumber, sessionId, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}