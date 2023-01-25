package com.ddockddack.domain.game.service;

import com.ddockddack.domain.game.entity.Game;
import com.ddockddack.domain.game.entity.GameImage;
import com.ddockddack.domain.game.repository.GameImageRepository;
import com.ddockddack.domain.game.repository.GameRepository;
import com.ddockddack.domain.game.repository.StarredGameRepository;
import com.ddockddack.domain.game.request.GameImageParam;
import com.ddockddack.domain.game.request.GameSaveReq;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.ImageExtensionException;
import com.ddockddack.global.error.exception.NotFoundException;
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


    /**
     * 게임 생성
     *
     * @param gameSaveReq
     * @return gameId
     */
    public Long saveGame(GameSaveReq gameSaveReq) {

        // memberId로 member 조회. 조회 결과가 null 이면 NotFoundException 발생.
        Member getMember = Optional.ofNullable(memberRepository.findById(gameSaveReq.getMemberId()).orElseThrow(() ->
                new NotFoundException(ErrorCode.MEMBER_NOT_FOUND))).get();

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
