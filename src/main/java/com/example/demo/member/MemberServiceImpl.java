package com.example.demo.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

    private final MemberDAO memberDAO;

    public MemberServiceImpl(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    /**
     * 소셜 로그인/가입 공통 처리
     * member_id는 "socialType_socialId" 형태로 통일 추천
     * 예) kakao_1234567890
     */
    @Override
    @Transactional
    public MemberDTO loginOrJoinBySocial(String socialType, String socialId, String nickname, String profileImage) {

        String memberId = socialType + "_" + socialId;

        MemberDTO dto = new MemberDTO();
        dto.setMemberId(memberId);
        dto.setSocialType(socialType);
        dto.setNickname(nickname);
        dto.setProfileImage(profileImage);

        // 없으면 INSERT / 있으면 UPDATE (DAO가 처리)
        memberDAO.saveOrUpdateSocial(dto);

        // 최신 정보 재조회 (role 등 포함)
        return memberDAO.findById(memberId);
    }

    /**
     * 카카오 전용 편의 메서드
     */
    @Override
    public MemberDTO upsertKakaoMember(String socialId, String nickname, String profileImage) {
        return loginOrJoinBySocial("kakao", socialId, nickname, profileImage);
    }

    @Override
    public MemberDTO getMember(String memberId) {
        return memberDAO.findById(memberId);
    }

    @Override
    public void updateNickname(String memberId, String nickname) {
        memberDAO.updateNickname(memberId, nickname);
    }

    @Override
    public void deleteMember(String memberId) {
        memberDAO.deleteById(memberId);
    }

    @Override
    public MemberDTO loginLocal(String memberId, String password) {
        return memberDAO.findLocalByIdAndPassword(memberId, password);
    }
}
