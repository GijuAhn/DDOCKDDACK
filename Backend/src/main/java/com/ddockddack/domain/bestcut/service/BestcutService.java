package com.ddockddack.domain.bestcut.service;

import com.ddockddack.domain.bestcut.entity.Bestcut;
import com.ddockddack.domain.bestcut.repository.BestcutRepository;
import com.ddockddack.domain.bestcut.request.BestcutImageReq;
import com.ddockddack.domain.bestcut.request.BestcutSaveReq;
import com.ddockddack.domain.bestcut.response.BestcutRes;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.entity.Role;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.domain.report.entity.ReportType;
import com.ddockddack.domain.report.entity.ReportedBestcut;
import com.ddockddack.domain.report.repository.ReportedBestcutRepository;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.AccessDeniedException;
import com.ddockddack.global.error.exception.AlreadyExistResourceException;
import com.ddockddack.global.error.exception.NotFoundException;
import com.ddockddack.global.util.PageCondition;
import com.ddockddack.global.util.PageConditionReq;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BestcutService {

    private final BestcutRepository bestcutRepository;
    private final MemberRepository memberRepository;
    private final ReportedBestcutRepository reportedBestcutRepository;


    @Transactional
    public void saveBestcut(BestcutSaveReq saveReq) {

        Member member = memberRepository.findById(saveReq.getMemberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        for (BestcutImageReq imageReq : saveReq.getImages()) {
            MultipartFile imageFile = imageReq.getBestcutImg();

            try {
                imageFile.transferTo(new File(imageFile.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bestcut bestcut = Bestcut.builder()
                    .member(member)
                    .gameTitle(saveReq.getGameTitle())
                    .gameImageUrl(imageReq.getGameImgUrl())
                    .gameImgDesc(imageReq.getGameImgDesc())
                    .imageUrl(imageFile.getOriginalFilename())
                    .title(imageReq.getBestcutImgTitle())
                    .build();

            bestcutRepository.save(bestcut);
        }
    }

    /**
     * 삭제하려는 member의 id와 베스트컷이 참조하는 member의 id가 다르면 예외 발생
     *
     * @param bestcutId
     * @param memberId
     */
    @Transactional
    public void removeBestcut(Long bestcutId, Long memberId) {
        Bestcut bestcut = bestcutRepository.findById(bestcutId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BESTCUT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != Role.ADMIN && bestcut.getMember().getId() != memberId) {
            throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZED);
        }

        bestcutRepository.delete(bestcut);
    }

    @Transactional
    public void reportBestcut(Long memberId, Long bestcutId, ReportType reportType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new org.webjars.NotFoundException("존재하지 않는 멤버"));
        Bestcut bestcut = bestcutRepository.findById(bestcutId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BESTCUT_NOT_FOUND));
        if (reportedBestcutRepository.findOne(bestcutId, memberId).isPresent()) {
            throw new AlreadyExistResourceException(ErrorCode.ALREADY_EXIST_REPORT);
        }

        ReportedBestcut reportedBestcut = ReportedBestcut.builder()
                .reportMember(member)
                .reportedMember(bestcut.getMember())
                .bestcut(bestcut)
                .reportType(reportType)
                .build();

        reportedBestcutRepository.save(reportedBestcut);
    }

    /**
     * @param my               내 베스트컷 조회시 true
     * @param loginMemberId
     * @param pageConditionReq
     * @return
     */
    public PageImpl<BestcutRes> findAll(Boolean my, Long loginMemberId,
            PageConditionReq pageConditionReq) {
        PageCondition pageCondition = pageConditionReq.toEntity();
        return bestcutRepository.findAllBySearch(my, loginMemberId, pageCondition);
    }

    public BestcutRes findOne(Long loginMemberId, Long bestcutId) {
        return bestcutRepository.findOne(loginMemberId, bestcutId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BESTCUT_NOT_FOUND));
    }
}
