package com.ddockddack.domain.gameRoom.service;

import com.ddockddack.domain.game.response.GameDetailRes;
import com.ddockddack.domain.game.service.GameService;
import com.ddockddack.domain.gameRoom.repository.GameRoom;
import com.ddockddack.domain.gameRoom.repository.GameRoomRepository;
import com.ddockddack.domain.gameRoom.response.GameRoomRes;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.NotFoundException;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepository gameRoomRepository;
    private final GameService gameService;
    private final MemberRepository memberRepository;

    public GameRoomRes createRoom(Long gameId)
            throws OpenViduJavaClientException, OpenViduHttpException {
        //repository로 요청 전달

        //gameId로 game 정보 가져오기

        return null;
    }


    public String joinRoom(String pinNumber, Long memberId, String nickname)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Member member = null;
        //로그인 한 유저면 memberId로 검색해서 넘겨줌

        return null;
    }
}
