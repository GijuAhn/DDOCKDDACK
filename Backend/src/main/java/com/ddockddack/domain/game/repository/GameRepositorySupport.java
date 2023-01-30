package com.ddockddack.domain.game.repository;

import com.ddockddack.domain.game.response.*;
import com.ddockddack.global.util.OrderCondition;
import com.ddockddack.global.util.PageCondition;
import com.ddockddack.global.util.PeriodCondition;
import com.ddockddack.global.util.SearchCondition;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.ddockddack.domain.game.entity.QGame.game;
import static com.ddockddack.domain.game.entity.QGameImage.gameImage;
import static com.ddockddack.domain.game.entity.QStarredGame.starredGame;
import static com.ddockddack.domain.member.entity.QMember.member;
import static com.ddockddack.domain.report.entity.QReportedGame.reportedGame;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.types.ExpressionUtils.as;
import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@RequiredArgsConstructor
public class GameRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    // 검색 목록 조회
    public PageImpl<GameRes> findAllGameBySearch(Long memberId, PageCondition pageCondition) {

        List<GameRes> list = jpaQueryFactory.select(
                        new QGameRes(game.id.as("gameId"),
                                game.category.as("gameCategory").stringValue(),
                                game.title.as("gameTitle"),
                                game.description.as("gameDesc"),
                                game.member.nickname.as("creator"),
                                isStarred(memberId),
                                getStarredCnt(),
                                game.playCount.as("popularity"),
                                gameImage.imageUrl.as("gameImageUrl")
                        ))
                .from(game)
                .innerJoin(game.member, member)
                .innerJoin(game.images, gameImage)
                .where(searchCond(pageCondition.getSearchCondition(), pageCondition), periodCond(pageCondition.getPeriodCondition()))
                .offset(pageCondition.getPageable().getOffset())
                .limit(pageCondition.getPageable().getPageSize())
                .groupBy(game.id)
                .orderBy(orderCond(pageCondition.getPageable()))
                .fetch();


        return new PageImpl<>(list, pageCondition.getPageable(), getTotalPageCount());
    }

    private long getTotalPageCount() {
        return jpaQueryFactory.select(Wildcard.count).from(game).fetch().get(0);
    }

    // 게임 상세 조회
    public List<GameDetailRes> findGame(Long gameId) {
        return jpaQueryFactory
                .from(game)
                .innerJoin(gameImage).on(gameImage.game.id.eq(game.id))
                .where(game.id.eq(gameId))
                .transform(groupBy(game.id).list(new QGameDetailRes(
                        game.id.as("gameId"),
                        game.title.as("gameTitle"),
                        game.description.as("gameDesc"),
                        list(new QGameImageRes(
                                gameImage.id.as("gameImageId"),
                                gameImage.imageUrl.as("gameImageUrl"),
                                gameImage.description.as("gameImageDesc")))
                )));
    }

    // 내가 만든 게임 전체 조회
    public List<GameRes> findAllByMemberId(Long memberId) {
        return jpaQueryFactory.select(
                        new QGameRes(game.id.as("gameId"),
                                game.category.as("gameCategory").stringValue(),
                                game.title.as("gameTitle"),
                                game.description.as("gameDesc"),
                                game.member.nickname.as("creator"),
                                isStarred(memberId),
                                getStarredCnt(),
                                game.playCount.as("popularity"),
                                gameImage.imageUrl.as("gameImageUrl")
                        ))
                .from(game)
                .innerJoin(game.member, member)
                .innerJoin(game.images, gameImage)
                .where(game.member.id.eq(memberId))
                .groupBy(game.id)
                .orderBy(game.id.desc())
                .fetch();
    }

    // 즐겨찾기 목록 조회
    public List<StarredGameRes> findAllStarredGame(Long memberId) {
        return jpaQueryFactory
                .select(new QStarredGameRes(
                        game.id.as("gameId"),
                        game.category.as("gameCategory").stringValue(),
                        game.title.as("gameTitle"),
                        game.member.nickname.as("creator"),
                        game.createdAt.as("regDate").stringValue(),
                        isStarred(memberId),
                        game.playCount.as("popularity"),
                        gameImage.imageUrl.as("gameImageUrl")
                ))
                .from(game)
                .innerJoin(game.member, member)
                .innerJoin(game.images, gameImage)
                .join(starredGame).on(starredGame.game.id.eq(game.id)
                        .and(starredGame.member.id.eq(memberId)))
                .groupBy(game.id)
                .orderBy(starredGame.id.desc())
                .fetch();

    }

    // 신고된 게임 목록 조회
    public List<ReportedGameRes> findAllReportedGame() {
        return jpaQueryFactory
                .select(new QReportedGameRes(
                        reportedGame.id.as("reportId"),
                        reportedGame.reportMember.id.as("reportMemberId"),
                        reportedGame.reportedMember.id.as("reportedMemberId"),
                        reportedGame.game.id.as("gameId"),
                        reportedGame.reportType.as("reason").stringValue()
                ))
                .from(reportedGame)
                .innerJoin(reportedGame.reportMember, member)
                .innerJoin(reportedGame.reportedMember, member)
                .orderBy(reportedGame.id.desc())
                .fetch();
    }


    // 나만 쓸 거야
    // 정렬
    private OrderSpecifier orderCond(Pageable pageable) {
        Sort.Order order = pageable.getSort().iterator().next();
        if(order.getProperty().equals("createdDate")) {
            return game.id.desc();
        } else {
            return game.playCount.desc();
        }
    }

    // memberId 가 null 이면 isStarred 0 반환
    private Expression<Integer> isStarred(Long memberId) {
        if (memberId != null) {
            return as(
                    select(starredGame.count().intValue())
                            .from(starredGame)
                            .where(starredGame.game.id.eq(game.id).and(
                                    starredGame.member.id.eq(memberId))),
                    "isStarred"
            );
        } else {
            return Expressions.as(Expressions.constant(0), "isStarred");
        }
    }

    // 게임의 즐겨찾기 수 구하기
    private Expression<Integer> getStarredCnt() {
        return as(
                select(starredGame.count().intValue())
                        .from(starredGame)
                        .where(starredGame.game.id.eq(game.id)),
                "starredCnt"
        );
    }

    // 검색 조건
    private BooleanExpression searchCond(SearchCondition searchCondition, PageCondition pageCondition) {
        if (searchCondition == null) {
            return null;
        }

        if (SearchCondition.GAME.equals(searchCondition)) {
            return game.title.contains(pageCondition.getKeyword());
        }

        return game.member.nickname.contains(pageCondition.getKeyword());
    }

    // 기간 구하기
    private BooleanExpression periodCond(PeriodCondition periodCondition) {
        if (periodCondition == null || periodCondition.equals(PeriodCondition.ALL)) {
            return null;
        }

        return game.createdAt.goe(
                LocalDateTime.now().minusDays((long) periodCondition.getPeriod()));
    }

}