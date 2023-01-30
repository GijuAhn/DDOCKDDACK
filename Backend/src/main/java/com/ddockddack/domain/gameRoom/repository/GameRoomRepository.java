package com.ddockddack.domain.gameRoom.repository;

import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.NotFoundException;
import io.openvidu.java.client.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
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
    }

    public GameRoom create(Long gameId) throws OpenViduJavaClientException, OpenViduHttpException {
        //핀 넘버 생성
        String pinNumber = createPinNumber();

        //생성한 pin으로 openvidu에 session 생성 요청

        //방 객체 생성 후 map에 저장

        return null;
    }

    public String join(String pinNumber, Member member, String nickname) throws OpenViduJavaClientException, OpenViduHttpException {
        //존재하는 pin인지 확인

        //openvidu에 connection 요청

        //member를 gameMember로 변환하여 gameRoom에 저장

        return null;
    }

    public Optional<Session> findSessionByPinNumber(String pinNumber){
        return Optional.ofNullable(openvidu.getActiveSession(pinNumber));
    }

    public String createPinNumber() {
        String pin = intToPin(random.nextInt(PIN_NUMBER_BOUND));
        while(gameRooms.containsKey(pin)){
            pin = intToPin(random.nextInt(PIN_NUMBER_BOUND));
        }
        return pin;
    }

    public String intToPin(int num){
        return String.format("%06d", num);
    }
}
