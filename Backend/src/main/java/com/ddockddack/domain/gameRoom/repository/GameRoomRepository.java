package com.ddockddack.domain.gameRoom.repository;

import com.ddockddack.domain.game.entity.Game;
import com.ddockddack.domain.game.entity.GameImage;
import com.ddockddack.domain.gameRoom.request.GameSignalReq;
import com.ddockddack.domain.gameRoom.response.GameMemberRes;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openvidu.java.client.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.client.RestTemplate;

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

    public void saveScore(String pinNumber, String sessionId, byte[] byteImage, int score)
            throws JsonProcessingException {
        GameRoom gameRoom = this.gameRooms.get(pinNumber);
        GameMember gameMember = gameRoom.getMembers().get(sessionId);
        gameMember.getImages().add(byteImage);
        gameMember.setRoundScore(score);
        gameMember.setTotalScore(gameMember.getTotalScore()+score);
        gameRoom.increaseScoreCnt();
        if(gameRoom.getScoreCount() == gameRoom.getMembers().size()){
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:4443/api/signal";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic T1BFTlZJRFVBUFA6TVlfU0VDUkVU");
            headers.setContentType(MediaType.APPLICATION_JSON);


            ObjectMapper mapper = new ObjectMapper();
            String resultData = mapper.writeValueAsString(findRoundResult(gameRoom));

            GameSignalReq req = GameSignalReq.builder()
                    .session(pinNumber)
                    .type("roundResult")
                    .data(resultData)
                    .build();

            String stringReq = mapper.writeValueAsString(req);
            HttpEntity<String> httpEntity = new HttpEntity<>(stringReq, headers);
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            gameRoom.resetScoreCnt();
            gameRoom.increaseRound();
        }

    }

    public byte[] findByImageIndex(String pinNumber, String sessionId, int index) {
        GameMember gameMember = gameRooms.get(pinNumber).getMembers().get(sessionId);
        return gameMember.getImages().get(index);
    }

    public Map<String, GameMember> findGameMembers(String pinNumber) {
        return gameRooms.get(pinNumber).getMembers();
    }

    public List<GameMemberRes> findRoundResult(GameRoom gameRoom) {
        List<GameMember> members = new ArrayList<>(gameRoom.getMembers().values());
        PriorityQueue<GameMember> pq = new PriorityQueue<>((a, b) -> b.getRoundScore() - a.getRoundScore());
        pq.addAll(members);
        List<GameMemberRes> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (pq.isEmpty()) break;
            result.add(GameMemberRes.from(pq.poll(), gameRoom.getRound()));
        }
        return result;
    }

}