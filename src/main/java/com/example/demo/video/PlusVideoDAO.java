package com.example.demo.video;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.streamer.StreamerDTO;

@Repository
public class PlusVideoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // =========================================================
    // SQL
    // =========================================================

    // ✅ 전체 조회 (영상 목록 페이지용)
    // - VideoRowMapper가 server_* 를 읽으므로 SELECT에도 반드시 포함
    private static final String SELECT_ALL =
        "SELECT " +
        "  video_id, member_id, streamer_name, " +
        "  server_name, server_season, server_topic, " +
        "  platform, youtube_video_id, video_url, " +
        "  title, thumbnail_url, is_featured, content, " +
        "  view_count, like_count, created_at " +
        "FROM video " +
        "ORDER BY created_at DESC, video_id DESC";

    // ✅ 단일 조회 (video_id=PK)
    private static final String SELECT_ONE =
        "SELECT " +
        "  video_id, member_id, streamer_name, " +
        "  server_name, server_season, server_topic, " +
        "  platform, youtube_video_id, video_url, " +
        "  title, thumbnail_url, is_featured, content, " +
        "  view_count, like_count, created_at " +
        "FROM video " +
        "WHERE video_id = ?";

    // 등록
    private static final String INSERT =
        "INSERT INTO video " +
        " (member_id, streamer_name, platform, youtube_video_id, video_url, " +
        "  title, thumbnail_url, is_featured, content, view_count, like_count) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // 삭제
    private static final String DELETE =
        "DELETE FROM video " +
        "WHERE video_id=?";

    // 수정 (필요한 것들만)
    private static final String UPDATE =
        "UPDATE video SET " +
        "  streamer_name=?, " +
        "  platform=?, " +
        "  youtube_video_id=?, " +
        "  video_url=?, " +
        "  title=?, " +
        "  thumbnail_url=?, " +
        "  is_featured=?, " +
        "  content=?, " +
        "  view_count=?, " +
        "  like_count=? " +
        "WHERE video_id=?";

    // ✅ video_id로 출연 스트리머 목록 조회
    private static final String SELECT_STREAMERS_BY_VIDEO =
        "SELECT s.name, s.platform, s.profile_image " +
        "FROM video_streamer vs " +
        "JOIN streamer s ON vs.streamer_name = s.name " +
        "WHERE vs.video_id = ? " +
        "ORDER BY s.name";

    // ✅ 스트리머 이름으로 출연 영상 조회 (video_streamer 조인)
    // - VideoRowMapper가 server_* 를 읽으므로 SELECT에도 반드시 포함
    private static final String SELECT_BY_STREAMER =
        "SELECT DISTINCT " +
        "  v.video_id, v.member_id, v.streamer_name, " +
        "  v.server_name, v.server_season, v.server_topic, " +
        "  v.platform, v.youtube_video_id, v.video_url, " +
        "  v.title, v.thumbnail_url, v.is_featured, v.content, " +
        "  v.view_count, v.like_count, v.created_at " +
        "FROM video_streamer vs " +
        "JOIN video v ON v.video_id = vs.video_id " +
        "WHERE vs.streamer_name = ? " +
        "ORDER BY v.created_at DESC, v.video_id DESC";

    // ✅ 인기 영상 조회
    private static final String SELECT_POPULAR =
        "SELECT " +
        "  video_id, member_id, streamer_name, " +
        "  server_name, server_season, server_topic, " +
        "  platform, youtube_video_id, video_url, " +
        "  title, thumbnail_url, is_featured, content, " +
        "  view_count, like_count, created_at " +
        "FROM video " +
        "ORDER BY (view_count + like_count * 50) DESC, created_at DESC " +
        "LIMIT ?";

    private static final String UPDATE_VIEW_COUNT =
        "UPDATE video SET view_count = view_count + 1 WHERE video_id=?";

    // ✅ title / server_name / streamer_name 로 검색
    // - 검색 페이지 카드에 필요한 컬럼만 뽑아도 OK (RowMapper 안 쓰니까)
    private static final String SEARCH_SQL =
    	    "SELECT DISTINCT " +
    	    "  v.video_id, v.title, v.thumbnail_url, v.video_url, v.youtube_video_id, " +
    	    "  v.view_count, v.server_name, v.server_season, v.streamer_name " +
    	    "FROM video v " +
    	    "LEFT JOIN video_streamer vs ON vs.video_id = v.video_id " +
    	    "WHERE (v.title LIKE ?) " +
    	    "   OR (v.server_name LIKE ?) " +
    	    "   OR (v.streamer_name LIKE ?) " +      // 업로더/대표 스트리머
    	    "   OR (vs.streamer_name LIKE ?) " +     // ✅ 출연 스트리머
    	    "ORDER BY v.video_id DESC";


    // =========================================================
    // CRUD
    // =========================================================

    public boolean insertVideo(VideoDTO dto) {
        System.out.println("바뀐 InsertVideo 로그");

        int rows = jdbcTemplate.update(
            INSERT,
            dto.getMemberId(),
            dto.getStreamerName(),
            dto.getPlatform(),
            dto.getYoutubeVideoId(),
            dto.getVideoUrl(),
            dto.getTitle(),
            dto.getThumbnailUrl(),
            dto.getIsFeatured(),
            dto.getContent(),
            dto.getViewCount(),
            dto.getLikeCount()
        );

        return rows > 0;
    }

    public boolean deleteVideo(VideoDTO dto) {
        System.out.println("바뀐 DeleteVideo 로그");
        int rows = jdbcTemplate.update(DELETE, dto.getVideoId());
        return rows > 0;
    }

    public boolean updateVideo(VideoDTO dto) {
        System.out.println("바뀐 UpdateVideo 로그");

        int rows = jdbcTemplate.update(
            UPDATE,
            dto.getStreamerName(),
            dto.getPlatform(),
            dto.getYoutubeVideoId(),
            dto.getVideoUrl(),
            dto.getTitle(),
            dto.getThumbnailUrl(),
            dto.getIsFeatured(),
            dto.getContent(),
            dto.getViewCount(),
            dto.getLikeCount(),
            dto.getVideoId()
        );

        return rows > 0;
    }

    // =========================================================
    // SELECT
    // =========================================================

    public VideoDTO getVideo(VideoDTO dto) {
        System.out.println("바뀐 GetVideo 로그");

        List<VideoDTO> list = jdbcTemplate.query(
            SELECT_ONE,
            new VideoRowMapper(),
            dto.getVideoId()
        );

        if (list.isEmpty()) return null;

        VideoDTO video = list.get(0);

        // ✅ 출연 스트리머 리스트 붙이기
        List<StreamerDTO> streamers = getStreamersByVideoId(video.getVideoId());
        video.setStreamers(streamers);

        return video;
    }

    public List<VideoDTO> getVideoList() {
        System.out.println("바뀐 GetVideoList 로그");
        return jdbcTemplate.query(SELECT_ALL, new VideoRowMapper());
    }

    public List<VideoDTO> getPopularVideos(int limit) {
        System.out.println("바뀐 GetPopularVideos 로그");
        return jdbcTemplate.query(SELECT_POPULAR, new VideoRowMapper(), limit);
    }

    public int increaseViewCount(int videoId) {
        return jdbcTemplate.update(UPDATE_VIEW_COUNT, videoId);
    }

    public List<StreamerDTO> getStreamersByVideoId(int videoId) {
        return jdbcTemplate.query(
            SELECT_STREAMERS_BY_VIDEO,
            (rs, rowNum) -> {
                StreamerDTO s = new StreamerDTO();
                s.setName(rs.getString("name"));
                s.setPlatform(rs.getString("platform"));

                String img = rs.getString("profile_image");
                s.setProfileImage(
                    (img == null || img.isBlank())
                        ? "/images/dummy/streamers/default.jpg"
                        : img
                );

                return s;
            },
            videoId
        );
    }

    public List<VideoDTO> getVideosByStreamerName(String streamerName) {
        System.out.println("바뀐 GetVideosByStreamerName 로그");

        return jdbcTemplate.query(
            SELECT_BY_STREAMER,      // ✅ 오타 수정: SELECT_VIDEOS_BY_STREAMER X
            new VideoRowMapper(),
            streamerName
        );
    }

    public List<VideoDTO> searchVideos(String keyword) {
        String like = "%" + keyword + "%";

        return jdbcTemplate.query(
            SEARCH_SQL,
            new Object[]{ like, like, like, like },   // ✅ 4개!
            (rs, rowNum) -> {
                VideoDTO v = new VideoDTO();
                v.setVideoId(rs.getInt("video_id"));
                v.setTitle(rs.getString("title"));
                v.setThumbnailUrl(rs.getString("thumbnail_url"));
                v.setVideoUrl(rs.getString("video_url"));
                v.setYoutubeVideoId(rs.getString("youtube_video_id"));
                v.setViewCount(rs.getInt("view_count"));
                v.setServerName(rs.getString("server_name"));
                v.setServerSeason(rs.getInt("server_season"));
                v.setStreamerName(rs.getString("streamer_name"));
                return v;
            }
        );
    }

}


// =========================================================
// RowMapper
// =========================================================
class VideoRowMapper implements RowMapper<VideoDTO> {

    @Override
    public VideoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        VideoDTO data = new VideoDTO();

        data.setVideoId(rs.getInt("video_id"));
        data.setMemberId(rs.getString("member_id"));
        data.setStreamerName(rs.getString("streamer_name"));

        // ✅ 서버 정보
        data.setServerName(rs.getString("server_name"));
        data.setServerSeason(rs.getInt("server_season"));
        data.setServerTopic(rs.getString("server_topic"));

        data.setPlatform(rs.getString("platform"));
        data.setYoutubeVideoId(rs.getString("youtube_video_id"));
        data.setVideoUrl(rs.getString("video_url"));
        data.setTitle(rs.getString("title"));

        // ✅ 썸네일 기본값 보정
        String thumb = rs.getString("thumbnail_url");
        data.setThumbnailUrl(
            (thumb == null || thumb.isBlank())
                ? "/images/dummy/default_thumb.jpg"
                : thumb
        );

        data.setIsFeatured(rs.getInt("is_featured"));
        data.setContent(rs.getString("content"));
        data.setViewCount(rs.getInt("view_count"));
        data.setLikeCount(rs.getInt("like_count"));
        data.setCreatedAt(rs.getTimestamp("created_at"));

        return data;
    }
}
