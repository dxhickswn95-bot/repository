package com.example.demo.member;

public interface MemberDAO {
    MemberDTO findById(String memberId);

    void saveOrUpdateSocial(MemberDTO dto);

    int updateNickname(String memberId, String nickname);

    int deleteById(String memberId);

    // ⭐ 추가: 로컬 로그인 조회
    MemberDTO findLocalByIdAndPassword(String memberId, String password);
}
