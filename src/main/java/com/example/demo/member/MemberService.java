package com.example.demo.member;

public interface MemberService {

    /**
     * 소셜 로그인/가입 (없으면 가입, 있으면 프로필/닉네임 업데이트)
     * @param socialType kakao/google/local ...
     * @param socialId   소셜에서 받은 고유 ID (카카오는 숫자 id)
     */
    MemberDTO loginOrJoinBySocial(String socialType, String socialId, String nickname, String profileImage);

    // (추가) 카카오 전용 upsert 편의 메서드 - 컨트롤러에서 쓰기 좋게
    MemberDTO upsertKakaoMember(String socialId, String nickname, String profileImage);

    // 마이페이지 등 조회
    MemberDTO getMember(String memberId);

    // 닉네임 변경
    void updateNickname(String memberId, String nickname);

    // 회원 삭제
    void deleteMember(String memberId);

    // 로컬 로그인
    MemberDTO loginLocal(String memberId, String password);
}
