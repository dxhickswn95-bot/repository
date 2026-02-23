package com.example.demo.mypage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.member.MemberDTO;
import com.example.demo.mypage.dto.MycommentDTO;

@Repository
public class PlusMypageDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // =========================================================
    // SQL
    // =========================================================

    // ✅ 내 정보 조회
    private static final String SELECT_MEMBER =
        "SELECT member_id, social_type, nickname, profile_image, created_at " +
        "FROM MEMBER " +
        "WHERE member_id=?";

    // ✅ 내가 쓴 댓글 목록 (COMMENTS + VIDEO JOIN)
    // - VIDEO 테이블에 title 컬럼이 있다고 가정 (없으면 아래 주석 참고)
    private static final String SELECT_MY_COMMENTS =
        "SELECT c.comment_id, c.video_id, c.member_id, c.content, c.created_at, " +
        "       v.streamer_name, v.title AS video_title " +
        "FROM COMMENTS c " +
        "JOIN VIDEO v ON c.video_id = v.video_id " +
        "WHERE c.member_id=? " +
        "ORDER BY c.created_at DESC";

    /*
     * ⚠️ 만약 VIDEO에 title 컬럼이 없다면:
     * v.title AS video_title 부분을
     * v.content AS video_title 로 바꾸거나,
     * 또는 실제 존재하는 컬럼명으로 바꿔줘!
     */

    // =========================================================
    // SELECT
    // =========================================================

    public MemberDTO selectMember(String memberId) {
        System.out.println("바뀐 SelectMember 로그");

        List<MemberDTO> list = jdbcTemplate.query(
            SELECT_MEMBER,
            new MemberRowMapper(),
            memberId
        );

        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<MycommentDTO> selectMycomments(String memberId) {
        System.out.println("바뀐 SelectMycomments 로그");

        return jdbcTemplate.query(
            SELECT_MY_COMMENTS,
            new MyCommentRowMapper(),
            memberId
        );
    }
}


// =========================================================
// RowMapper
// =========================================================

class MemberRowMapper implements RowMapper<MemberDTO> {

    @Override
    public MemberDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        MemberDTO data = new MemberDTO();

        // ✅ MemberDTO 필드명/세터는 네 프로젝트에 맞춰져 있어야 함
        // (memberId, socialType, nickname, profileImage, createdAt)
        data.setMemberId(rs.getString("member_id"));
        data.setSocialType(rs.getString("social_type"));
        data.setNickname(rs.getString("nickname"));

        String img = rs.getString("profile_image");
        data.setProfileImage(
            (img == null || img.isBlank())
                ? "/images/dummy/profile/default.jpg"
                : img
        );

        // created_at 타입이 DATETIME/TIMESTAMP면 getTimestamp가 무난
        try {
            data.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (Exception e) {
            // MemberDTO에 createdAt 세터가 없거나 타입이 다르면 여기서 컴파일/런타임 에러 날 수 있음
            // 필요 시 MemberDTO의 필드 타입에 맞춰 수정해줘.
        }

        return data;
    }
}

class MyCommentRowMapper implements RowMapper<MycommentDTO> {

    @Override
    public MycommentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        MycommentDTO data = new MycommentDTO();

        data.setCommentId(rs.getInt("comment_id"));
        data.setVideoId(rs.getInt("video_id"));
        data.setMemberId(rs.getString("member_id"));
        data.setContent(rs.getString("content"));
        data.setCreatedAt(rs.getTimestamp("created_at"));

        data.setStreamerName(rs.getString("streamer_name"));
        data.setVideoTitle(rs.getString("video_title"));

        // highlight 컬럼/로직이 아직 없으면 기본 false
        data.setHighlight(false);

        return data;
    }
}
