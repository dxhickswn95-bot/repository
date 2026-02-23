package com.example.demo.streamer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PlusFavoriteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // =========================================================
    // SQL
    // =========================================================
    private static final String EXISTS =
        "SELECT COUNT(*) FROM FAVORITE_STREAMER " +
        "WHERE member_id=? AND streamer_name=?";

    private static final String INSERT =
        "INSERT INTO FAVORITE_STREAMER(member_id, streamer_name) VALUES(?, ?)";

    private static final String DELETE =
        "DELETE FROM FAVORITE_STREAMER WHERE member_id=? AND streamer_name=?";

    // 스트리머 목록 페이지에서: 내가 좋아요한 스트리머 이름들(Set)
    private static final String SELECT_FAVORITE_NAMES =
        "SELECT streamer_name FROM FAVORITE_STREAMER WHERE member_id=?";

    // 마이페이지에서: 좋아요한 스트리머 리스트 (스트리머 테이블 join)
    private static final String SELECT_FAVORITE_STREAMERS =
        "SELECT s.name, s.platform, s.profile_image " +
        "FROM FAVORITE_STREAMER f " +
        "JOIN streamer s ON f.streamer_name = s.name " +
        "WHERE f.member_id=? " +
        "ORDER BY f.created_at DESC";

    // =========================================================
    // CRUD
    // =========================================================

    public boolean isFavorite(String memberId, String streamerName) {
        Integer cnt = jdbcTemplate.queryForObject(EXISTS, Integer.class, memberId, streamerName);
        return cnt != null && cnt > 0;
    }

    public void addFavorite(String memberId, String streamerName) {
        jdbcTemplate.update(INSERT, memberId, streamerName);
    }

    public void removeFavorite(String memberId, String streamerName) {
        jdbcTemplate.update(DELETE, memberId, streamerName);
    }

    public Set<String> getFavoriteNames(String memberId) {
        List<String> names = jdbcTemplate.query(
            SELECT_FAVORITE_NAMES,
            (rs, rowNum) -> rs.getString("streamer_name"),
            memberId
        );
        return new HashSet<>(names);
    }

    public List<StreamerDTO> getFavoriteStreamers(String memberId) {
        return jdbcTemplate.query(SELECT_FAVORITE_STREAMERS, new StreamerFavRowMapper(), memberId);
    }
    
}

// =========================================================
// RowMapper
// =========================================================
class StreamerFavRowMapper implements RowMapper<StreamerDTO> {
    @Override
    public StreamerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        StreamerDTO dto = new StreamerDTO();
        dto.setName(rs.getString("name"));
        dto.setPlatform(rs.getString("platform"));

        String img = rs.getString("profile_image");
        dto.setProfileImage(
            (img == null || img.isBlank())
                ? "/images/dummy/streamers/default.jpg"
                : img
        );
        return dto;
    }
}
