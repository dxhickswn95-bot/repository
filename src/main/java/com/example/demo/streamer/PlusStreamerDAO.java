package com.example.demo.streamer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class PlusStreamerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // =========================================================
    // SQL
    // =========================================================

    // 전체 조회 (스트리머 목록 페이지용)
    private static final String SELECT_ALL =
        "SELECT name, platform, channel_id, profile_image " +
        "FROM streamer " +
        "ORDER BY name ASC";

    // 단일 조회 (name=PK)
    private static final String SELECT_ONE =
        "SELECT name, platform, channel_id, profile_image " +
        "FROM streamer " +
        "WHERE name=?";

    // 등록
    private static final String INSERT =
        "INSERT INTO streamer (name, platform, channel_id, profile_image) " +
        "VALUES (?, ?, ?, ?)";

    // 삭제
    private static final String DELETE =
        "DELETE FROM streamer " +
        "WHERE name=?";

    // 프로필 이미지 업데이트(단일)
    private static final String UPDATE_PROFILE_IMAGE =
        "UPDATE streamer " +
        "SET profile_image=? " +
        "WHERE name=?";

    // ✅ 유튜브 프로필 이미지 동기화 대상 조회
    // - platform='youtube'
    // - channel_id 있음
    // - profile_image 비어있음(NULL 또는 '')
    private static final String SELECT_YOUTUBE_NEED_IMAGE =
        "SELECT name, channel_id " +
        "FROM streamer " +
        "WHERE platform='youtube' " +
        "  AND channel_id IS NOT NULL AND channel_id <> '' " +
        "  AND (profile_image IS NULL OR profile_image='') " +
        "ORDER BY name ASC";

 // 스트리머 1명 조회 (정확히 일치)
    private static final String SELECT_STREAMER_ONE =
        "SELECT name, platform, profile_image " +
        "FROM streamer " +
        "WHERE name = ?";
    
    private static final String SELECT_ONE_BY_NAME =
    	    "SELECT name, platform, profile_image FROM streamer WHERE name = ?";
    // =========================================================
    // CRUD
    // =========================================================

    public boolean insertStreamer(StreamerDTO dto) {
        System.out.println("바뀐 InsertStreamer 로그");

        if (jdbcTemplate.update(
                INSERT,
                dto.getName(),
                dto.getPlatform(),
                dto.getChannelId(),
                dto.getProfileImage()
        ) <= 0) {
            return false;
        }
        return true;
    }

    public boolean deleteStreamer(StreamerDTO dto) {
        System.out.println("바뀐 DeleteStreamer 로그");

        if (jdbcTemplate.update(DELETE, dto.getName()) <= 0) {
            return false;
        }
        return true;
    }

    public boolean updateProfileImage(StreamerDTO dto) {
        System.out.println("바뀐 UpdateProfileImage 로그");

        if (jdbcTemplate.update(
                UPDATE_PROFILE_IMAGE,
                dto.getProfileImage(),
                dto.getName()
        ) <= 0) {
            return false;
        }
        return true;
    }

    // =========================================================
    // SELECT
    // =========================================================

    public StreamerDTO getStreamer(StreamerDTO dto) {
        System.out.println("바뀐 GetStreamer 로그");

        List<StreamerDTO> list = jdbcTemplate.query(
            SELECT_ONE,
            new StreamerRowMapper(),
            dto.getName()
        );

        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    public StreamerDTO getStreamerByName(String name) {
        List<StreamerDTO> list = jdbcTemplate.query(
            SELECT_ONE_BY_NAME,
            (rs, rowNum) -> {
                StreamerDTO s = new StreamerDTO();
                s.setName(rs.getString("name"));
                s.setPlatform(rs.getString("platform"));

                String img = rs.getString("profile_image");
                s.setProfileImage((img == null || img.isBlank())
                    ? "/images/dummy/streamers/default.jpg"
                    : img
                );
                return s;
            },
            name
        );

        return list.isEmpty() ? null : list.get(0);
    }

    public List<StreamerDTO> getStreamerList() {
        System.out.println("바뀐 GetStreamerList 로그");
        return jdbcTemplate.query(SELECT_ALL, new StreamerRowMapper());
    }

    // =========================================================
    // YouTube Sync Helper
    // =========================================================

    // ✅ 유튜브 프로필 이미지 업데이트 대상만 가져오기
    public List<StreamerDTO> getYouTubeStreamersNeedImage() {
        System.out.println("바뀐 GetYouTubeStreamersNeedImage 로그");

        return jdbcTemplate.query(SELECT_YOUTUBE_NEED_IMAGE, new RowMapper<StreamerDTO>() {
            @Override
            public StreamerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                StreamerDTO dto = new StreamerDTO();
                dto.setName(rs.getString("name"));
                dto.setChannelId(rs.getString("channel_id"));
                return dto;
            }
        });
    }

    // ✅ (Service에서 호출 편하게) 이름 기준 profile_image 업데이트
    public boolean updateProfileImageByName(StreamerDTO dto) {
        // updateProfileImage()와 역할이 동일하지만, 동기화용으로 이름을 분리해둔 버전
        return updateProfileImage(dto);
    }
    
}

// =========================================================
// RowMapper (BoardRowMapper 스타일)
// =========================================================
class StreamerRowMapper implements RowMapper<StreamerDTO> {

    @Override
    public StreamerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        StreamerDTO data = new StreamerDTO();

        data.setName(rs.getString("name"));
        data.setPlatform(rs.getString("platform"));
        data.setChannelId(rs.getString("channel_id"));

        // ✅ NULL/빈값이면 기본 이미지로 보정 (JSP onerror + 이중 안전장치)
        String img = rs.getString("profile_image");
        data.setProfileImage(
            (img == null || img.isBlank())
                ? "/images/dummy/streamers/default.jpg"
                : img
        );

        return data;
    }
}
