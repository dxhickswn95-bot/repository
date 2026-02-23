package com.example.demo.mypage;

import java.util.List;

import com.example.demo.member.MemberDTO;
import com.example.demo.mypage.dto.FavoriteStreamerDTO;
import com.example.demo.mypage.dto.MycommentDTO;

public interface MypageService {

    /**
     * ✅ 내 정보 조회
     * @param memberId 로그인한 회원 ID
     * @return 회원 정보
     */
    MemberDTO getMyInfo(String memberId);

    /**
     * ✅ 내가 작성한 댓글 목록 조회
     * @param memberId 로그인한 회원 ID
     * @return 댓글 리스트
     */
    List<MycommentDTO> getMyComments(String memberId);

    /**
     * ✅ 내가 좋아하는 스트리머 목록 조회
     * (아직 테이블 없으면 빈 리스트 반환)
     * @param memberId 로그인한 회원 ID
     * @return 스트리머 리스트
     */
    List<FavoriteStreamerDTO> getFavoriteStreamers(String memberId);
}
