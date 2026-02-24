package com.example.demo.comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("commentDAO")
public class PlusCommentDAO {

    private final JdbcTemplate jdbcTemplate;

    public PlusCommentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // video_id 기준 댓글 목록 (JOIN)
    private static final String SELECT_BY_VIDEO =
    	    "SELECT c.comment_id, c.video_id, c.member_id, m.nickname, m.profile_image, " +
    	    "       c.content, c.created_at " +
    	    "FROM comments c " +
    	    "JOIN `member` m ON c.member_id = m.member_id " +
    	    "WHERE c.video_id = ? " +
    	    "ORDER BY c.created_at DESC";

    // 댓글 작성
    private static final String INSERT =
        "INSERT INTO COMMENTS (video_id, member_id, content) VALUES (?, ?, ?)";

    // 댓글 삭제
    private static final String DELETE_ONE =
        "DELETE FROM COMMENTS WHERE comment_id = ?";

    // 최신 댓글 limit (옵션)
    private static final String SELECT_RECENT_LIMIT =
    	    "SELECT c.comment_id, c.video_id, c.member_id, m.nickname, m.profile_image, " +
    	    "       c.content, c.created_at " +
    	    "FROM comments c " +
    	    "JOIN `member` m ON c.member_id = m.member_id " +
    	    "ORDER BY c.created_at DESC " +
    	    "LIMIT ?";

    // (옵션) 댓글 1개 조회
    private static final String SELECT_ONE =
        "SELECT c.comment_id, c.video_id, c.member_id, m.nickname, m.profile_image, " +
        "       c.content, c.created_at " +
        "FROM COMMENTS c " +
        "JOIN MEMBER m ON c.member_id = m.member_id " +
        "WHERE c.comment_id = ?";

 // ✅ 본인 댓글만 삭제
    private static final String DELETE_ONE_BY_OWNER =
        "DELETE FROM COMMENTS WHERE comment_id = ? AND member_id = ?";

    public List<CommentDTO> getCommentListByVideoId(int videoId) {
        return jdbcTemplate.query(SELECT_BY_VIDEO, new CommentRowMapper(), videoId);
    }

    public boolean insertComment(CommentDTO dto) {
        int result = jdbcTemplate.update(INSERT, dto.getVideoId(), dto.getMemberId(), dto.getContent());
        return result > 0;
    }

    public boolean deleteComment(CommentDTO dto) {
        int result = jdbcTemplate.update(DELETE_ONE, dto.getCommentId());
        return result > 0;
    }

    public List<CommentDTO> getRecentComments(int limit) {
        return jdbcTemplate.query(SELECT_RECENT_LIMIT, new CommentRowMapper(), limit);
    }

    public CommentDTO getComment(CommentDTO dto) {
        // queryForObject는 결과 없으면 예외 나니까, 필요하면 try/catch로 null 처리도 가능
        return jdbcTemplate.queryForObject(SELECT_ONE, new CommentRowMapper(), dto.getCommentId());
    }
    public boolean deleteCommentByOwner(CommentDTO dto) {
        int result = jdbcTemplate.update(
            DELETE_ONE_BY_OWNER,
            dto.getCommentId(),
            dto.getMemberId()
        );
        return result > 0;
    }


    private static class CommentRowMapper implements RowMapper<CommentDTO> {
        @Override
        public CommentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommentDTO dto = new CommentDTO();
            dto.setCommentId(rs.getInt("comment_id"));
            dto.setVideoId(rs.getInt("video_id"));
            dto.setMemberId(rs.getString("member_id"));
            dto.setNickname(rs.getString("nickname"));
            dto.setProfileImage(rs.getString("profile_image"));
            dto.setContent(rs.getString("content"));
            dto.setCreatedAt(rs.getTimestamp("created_at"));
            return dto;
        }
    }
}
