package com.ddockddack.domain.gameRoom.repository;

import com.ddockddack.domain.game.entity.Game;
import com.ddockddack.domain.game.entity.GameImage;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.NotFoundException;
import io.openvidu.java.client.*;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
@RequiredArgsConstructor
public class GameRoomRepository {

    private final Integer PIN_NUMBER_BOUND = 1_000_000;
    private final Random random = new Random();
    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;
    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;
    private Map<String, GameRoom> gameRooms = new ConcurrentHashMap<>();
    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
        log.info("OPENVIDU_URL" + OPENVIDU_URL);
    }

    public String create(Game game) throws OpenViduJavaClientException, OpenViduHttpException {
        //핀 넘버 생성
        String pinNumber = createPinNumber();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("customSessionId", pinNumber);

        List<GameImage> gameImages = game.getImages();
        Collections.shuffle(gameImages);

        //생성한 pin으로 openvidu에 session 생성 요청
        SessionProperties properties = SessionProperties.fromJson(paramMap).build();
        openvidu.createSession(properties);


        //방 객체 생성 후 map에 저장
        GameRoom gameRoom = GameRoom.builder()
            .gameId(game.getId())
            .gameTitle(game.getTitle())
            .gameDescription(game.getDescription())
            .gameImages(gameImages)
            .pinNumber(pinNumber)
            .build();
        gameRooms.put(pinNumber, gameRoom);

        return pinNumber;
    }

    public String join(String pinNumber, Member member, String nickname)
        throws OpenViduJavaClientException, OpenViduHttpException {
        //존재하는 pin인지 확인
        Session session = findSessionByPinNumber(pinNumber).orElseThrow(
            () -> new NotFoundException(ErrorCode.GAME_ROOM_NOT_FOUND));

        //openvidu에 connection 요청
        ConnectionProperties properties = ConnectionProperties.fromJson(new HashMap<>()).build();
        Connection connection = session.createConnection(properties);

        //member를 gameMember로 변환하여 gameRoom에 저장
        String socketId = connection.getConnectionId();
        GameMember gameMember = new GameMember(socketId, member, nickname);
        gameRooms.get(pinNumber).getMembers().put(socketId, gameMember);

        return connection.getToken();
    }

    public Optional<Session> findSessionByPinNumber(String pinNumber) {
        return Optional.ofNullable(openvidu.getActiveSession(pinNumber));
    }

    public String createPinNumber() {
        String pin = intToPin(random.nextInt(PIN_NUMBER_BOUND));
        while (gameRooms.containsKey(pin)) {
            pin = intToPin(random.nextInt(PIN_NUMBER_BOUND));
        }
        return pin;
    }

    public String intToPin(int num) {
        return String.format("%06d", num);
    }

    public void deleteGameMember(String pinNumber, String sessionId) {
        gameRooms.get(pinNumber).getMembers().remove(sessionId);
    }

    public void deleteById(String pinNumber) {
        gameRooms.remove(pinNumber);
    }

    public Optional<GameRoom> findById(String pinNumber) {
        return Optional.ofNullable(gameRooms.get(pinNumber));
    }

    public void updateGameRoom(String pinNumber) {
        GameRoom gameRoom = this.gameRooms.get(pinNumber);
        gameRoom.start();
        this.gameRooms.put(pinNumber,gameRoom);
    }

    public void saveMemberImageUrl(String pinNumber, String sessionId, byte[] byteImage) {
        GameRoom gameRoom = this.gameRooms.get(pinNumber);
        GameMember gameMember = gameRoom.getMembers().get(sessionId);
        gameMember.getImages().add(byteImage);
        this.gameRooms.put(pinNumber, gameRoom);
    }

    public byte[] findByImageIndex(String pinNumber, String sessionId, int index) {
        GameMember gameMember = gameRooms.get(pinNumber).getMembers().get(sessionId);
        return gameMember.getImages().get(index);
    }

}