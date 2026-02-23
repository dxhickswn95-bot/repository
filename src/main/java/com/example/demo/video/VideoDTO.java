package com.example.demo.video;

import java.sql.Timestamp;
import java.util.List;

import com.example.demo.streamer.StreamerDTO;

public class VideoDTO {

    // =========================
    // 기본 컬럼 (video 테이블)
    // =========================
    private int videoId;                 
    private String memberId;             
    private String streamerName;         
    private String platform;             
    private String youtubeVideoId;        
    private String videoUrl;              
    private String title;                 
    private String thumbnailUrl;           
    private String content;               
    private int viewCount;                
    private int likeCount;                
    private int isFeatured;               
    private Timestamp createdAt;           
    private List<StreamerDTO> streamers;

    // ✅ 추가: 서버 관련 (video 테이블에 존재한다면)
    private String serverName;            // video.server_name
    private int serverSeason;             // video.server_season
    private String serverTopic;   // video.server_topic


    // =========================
    // JOIN 전용 컬럼 (streamer)
    // =========================
    private String streamerProfileImage;  // streamer.profile_image

    // =========================
    // Getter / Setter
    // =========================
    public int getVideoId() {
        return videoId;
    }
    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStreamerName() {
        return streamerName;
    }
    public void setStreamerName(String streamerName) {
        this.streamerName = streamerName;
    }

    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }
    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public int getViewCount() {
        return viewCount;
    }
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsFeatured() {
        return isFeatured;
    }
    public void setIsFeatured(int isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStreamerProfileImage() {
        return streamerProfileImage;
    }
    public void setStreamerProfileImage(String streamerProfileImage) {
        this.streamerProfileImage = streamerProfileImage;
    }

    public List<StreamerDTO> getStreamers() {
        return streamers;
    }
    public void setStreamers(List<StreamerDTO> streamers) {
        this.streamers = streamers;
    }

    // ✅ 추가 Getter/Setter (serverName, serverSeason)
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerSeason() {
        return serverSeason;
    }
    public void setServerSeason(int serverSeason) {
        this.serverSeason = serverSeason;
    }
    public String getServerTopic() {
        return serverTopic;
    }
    public void setServerTopic(String serverTopic) {
        this.serverTopic = serverTopic;
    }

}
