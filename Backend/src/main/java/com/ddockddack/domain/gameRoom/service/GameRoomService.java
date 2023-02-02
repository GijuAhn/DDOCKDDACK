package com.ddockddack.domain.gameRoom.service;

import com.ddockddack.domain.game.entity.Game;
import com.ddockddack.domain.game.repository.GameRepository;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepository gameRoomRepository;
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;

    /**
     * 방 생성
     *
     * @param gameId
     * @return
     * @throws OpenViduJavaClientException
     * @throws OpenViduHttpException
     */
    public String createRoom(Long gameId)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.GAME_NOT_FOUND));

        return gameRoomRepository.create(game);
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
    public GameRoomRes joinRoom(String pinNumber, Long memberId, String nickname)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Member member = null;
        //로그인 한 유저면 memberId로 검색해서 넘겨줌
        if (memberId != null) {
            member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        }

        String token = gameRoomRepository.join(pinNumber, member, nickname);
        GameRoom gameRoom = this.findGameRoom(pinNumber);

        if (gameRoom.isStarted()) {
            throw new AccessDeniedException(ErrorCode.ALREADY_EXIST_REPORTEDGAME);
        }

        GameRoomRes gameRoomRes = GameRoomRes.of(gameRoom);
        gameRoomRes.setToken(token);
        return gameRoomRes;
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
     *
     * @param pinNumber
     * @param sessionId
     * @param data
     */
    public void saveGameMemberImage(String pinNumber, String sessionId, String data) {

        Optional.ofNullable(gameRoomRepository.findById(pinNumber).orElseThrow(() ->
                new NotFoundException(ErrorCode.GAME_ROOM_NOT_FOUND)));

        byte[] byteImage = Base64.decodeBase64(data);
        gameRoomRepository.saveMemberImageUrl(pinNumber, sessionId, byteImage);

    }

}