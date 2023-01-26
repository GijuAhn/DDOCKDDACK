package com.ddockddack.domain.game.service;

import com.ddockddack.domain.game.entity.Game;
import com.ddockddack.domain.game.entity.GameImage;
import com.ddockddack.domain.game.entity.StarredGame;
import com.ddockddack.domain.game.repository.GameImageRepository;
import com.ddockddack.domain.game.repository.GameRepository;
import com.ddockddack.domain.game.repository.GameRepositorySupport;
import com.ddockddack.domain.game.repository.StarredGameRepository;
import com.ddockddack.domain.game.request.GameImageModifyReq;
import com.ddockddack.domain.game.request.GameImageParam;
import com.ddockddack.domain.game.request.GameModifyReq;
import com.ddockddack.domain.game.request.GameSaveReq;
import com.ddockddack.domain.game.response.GameDetailRes;
import com.ddockddack.domain.game.response.GameRes;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.entity.Role;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.global.error.AccessDeniedException;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.NotFoundException;
import com.ddockddack.global.error.exception.AlreadyExistResourceException;
import com.ddockddack.global.error.exception.ImageExtensionException;
import com.ddockddack.global.util.OrderCondition;
import com.ddockddack.global.util.PeriodCondition;
import com.ddockddack.global.util.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    private final GameImageRepository gameImageRepository;

    private final MemberRepository memberRepository;

    private final StarredGameRepository starredGameRepository;

    private final GameRepositorySupport gameRepositorySupport;

    /**
     * 게임 목록 조회
     *
     * @param orderCondition
     * @param period
     * @param keyword
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public List<GameRes> findAllGames(OrderCondition orderCondition, PeriodCondition period, SearchCondition searchCondition, String keyword, Long memberId) {
        return gameRepositorySupport.findAllGameBySearch(orderCondition, period, searchCondition, keyword, memberId);
    }

    /**
     * 게임 상세 조회
     *
     * @param gameId
     * @return
     */
    @Transactional(readOnly = true)
    public GameDetailRes findGame(Long gameId) {
        List<GameDetailRes> result = gameRepositorySupport.findGame(gameId);
        if (result.size() == 0) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return result.get(0);
    }

    /**
     * 게임 생성
     *
     * @param gameSaveReq
     * @return gameId
     */
    public Long saveGame(GameSaveReq gameSaveReq) {

        // memberId로 member 조회. 조회 결과가 null 이면 NotFoundException 발생.
        Member getMember = Optional.ofNullable(memberRepository.findById(gameSaveReq.getMemberId()).orElseThrow(() ->
                new com.ddockddack.global.error.NotFoundException(ErrorCode.MEMBER_NOT_FOUND))).get();

        // 게임 생성
        Game game = Game
                .builder()
                .member(getMember)
                .title(gameSaveReq.getGameTitle())
                .category(gameSaveReq.getGameCategory())
                .description(gameSaveReq.getGameDesc())
                .build();

        Long gameId = gameRepository.save(game).getId();

        // 게임 이미지 업로드
        String absolutePath = new File("").getAbsolutePath() + File.separator;

        String path = "images" + File.separator + gameId;
        File file = new File(path);

        // 디렉토리가 존재 하지 않는 경우
        if (!file.exists()) {
            file.mkdirs();
        }

        List<GameImage> gameImages = new ArrayList<>();
        for (GameImageParam gameImageParam : gameSaveReq.getImages()) {
            String imageExtension; // 이미지 확장자
            String contentType = gameImageParam.getGameImage().getContentType();

            // 이미지 확장자가 jpeg, png인 경우만 업로드 아닌경우 예외 발생
            if (contentType.contains("image/jpeg")) {
                imageExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
                imageExtension = ".png";
            } else {

                // 해당 게임 이미지가 업로드 된 폴더 삭제
                deleteDirectory(path);

                throw new ImageExtensionException(ErrorCode.EXTENSION_NOT_ALLOWED);
            }

            // UUID + 확장자, 중복을 피하기 위함
            String fileName = UUID.randomUUID().toString() + imageExtension;

            file = new File(absolutePath + path + File.separator + fileName);
            try {
                gameImageParam.getGameImage().transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // gameImage 엔티티 생성
            GameImage gameImage = GameImage.builder()
                    .game(game)
                    .imageUrl(fileName)
                    .description(gameImageParam.getGameImageDesc())
                    .build();

            // 리스트에 추가
            gameImages.add(gameImage);
        }

        // 리스트 안에 담긴 gameImage 객체 모두 등록
        gameImageRepository.saveAll(gameImages);

        return gameId;
    }

    /**
     * 게임 수정
     *
     * @param memberId
     * @param gameModifyReq
     */
    public void modifyGame(Long memberId, GameModifyReq gameModifyReq) {
        // 검증
        System.out.println("시작 1 ========================");
        checkAccessValidation(memberId, gameModifyReq.getGameId());

        System.out.println("시작 2 ----------------------------");
        Game getGame = gameRepository.getReferenceById(gameModifyReq.getGameId());

        System.out.println("끝 ===================================");
        // 게임 제목, 설명 수정
        getGame.updateGame(gameModifyReq.getGameTitle(), gameModifyReq.getGameDesc());

        String absolutePath = new File("").getAbsolutePath() + File.separator;
        String path = "images" + File.separator + getGame.getId();

        File file = new File(path);
        List<String> tempImage = new ArrayList<>();
        for (GameImageModifyReq gameImageModifyReq : gameModifyReq.getImages()) {
            GameImage getGameImage = gameImageRepository.getReferenceById(gameImageModifyReq.getGameImageId());

            String imageExtension; // 이미지 확장자
            String contentType = gameImageModifyReq.getGameImage().getContentType();

            if (contentType.contains("image/jpeg")) {
                imageExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
                imageExtension = ".png";
            } else {

                // 여태 업로드 되었던 이미지들 하나 하나 전부 삭제
                deleteImageFile(path, tempImage);

                throw new ImageExtensionException(ErrorCode.EXTENSION_NOT_ALLOWED);
            }

            String fileName = UUID.randomUUID().toString() + imageExtension;

            file = new File(absolutePath + path + File.separator + fileName);
            try {
                gameImageModifyReq.getGameImage().transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 업로드 중 실패 시 여태 업로드 되었던 이미지들을 날리기 위해 임시저장 하는 리스트
            tempImage.add(fileName);

            // 이미지 원본 삭제
            new File(absolutePath + path + File.separator + getGameImage.getImageUrl()).delete();
            getGameImage.updateGameImage(fileName, gameImageModifyReq.getGameImageDesc());

        }
    }

    /**
     * 게임 즐겨 찾기
     *
     * @param memberId
     * @param gameId
     */
    public void starredGame(Long memberId, Long gameId) {

        // 검증
        checkMemberAndGameValidation(memberId, gameId);

        boolean isExist = starredGameRepository.existsByMemberIdAndGameId(memberId, gameId);

        if (isExist) {
            throw new AlreadyExistResourceException(ErrorCode.ALREADY_EXIST_STTAREDGAME);
        }

        Member getMember = memberRepository.getReferenceById(memberId);
        Game getGame = gameRepository.getReferenceById(gameId);

        StarredGame starredGame = StarredGame.builder()
                .game(getGame)
                .member(getMember)
                .build();

        starredGameRepository.save(starredGame);
    }


    /**
     * 게임 수정, 삭제 권한 검증
     *
     * @param memberId
     * @param gameId
     */
    private void checkAccessValidation(Long memberId, Long gameId) {

        // 존재하는 유저인지 검증
        Member getMember = Optional.ofNullable(memberRepository.findById(memberId).orElseThrow(() ->
                new NotFoundException(ErrorCode.MEMBER_NOT_FOUND))).get();

        // 존재하는 게임 인지 검증
        Game getGame = Optional.ofNullable(gameRepository.findById(gameId).orElseThrow(() ->
                new NotFoundException(ErrorCode.GAME_NOT_FOUND))).get();

        // 관리자면 바로 리턴
        if (getMember.getRole().equals(Role.ADMIN)) {
            return;
        }
        // 삭제 권한을 가진 유저인지 검증
        if ((memberId != getGame.getMember().getId())) {
            throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZED);
        }
    }

    /**
     * 즐겨 찾기,신고 검증
     *
     * @param memberId
     * @param gameId
     */
    private void checkMemberAndGameValidation(Long memberId, Long gameId) {
        // 존재하는 유저인지 검증
        memberRepository.findById(memberId).orElseThrow(() ->
                new com.ddockddack.global.error.exception.NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 존재하는 게임 인지 검증
        gameRepository.findById(gameId).orElseThrow(() ->
                new com.ddockddack.global.error.exception.NotFoundException(ErrorCode.GAME_NOT_FOUND));
    }


    /**
     * 게임 이미지 수정 실패 시 업로드 됬던 이미지 개별 삭제
     *
     * @param path
     * @param list
     */
    private void deleteImageFile(String path, List<String> list) {

        String absolutePath = new File("").getAbsolutePath() + File.separator;
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                new File(absolutePath + path + File.separator + list.get(i)).delete();
            }
        }

    }

    /**
     * 해당 게임 이미지 업로드 디렉토리 삭제
     *
     * @param path
     */
    private void deleteDirectory(String path) {

        try {
            FileSystemUtils.deleteRecursively(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
