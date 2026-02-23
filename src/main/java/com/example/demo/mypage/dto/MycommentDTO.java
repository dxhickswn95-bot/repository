package com.example.demo.mypage.dto;

import java.sql.Timestamp;

public class MycommentDTO {

    // =========================================================
    // 기본 댓글 정보
    // =========================================================
    private int commentId;
    private int videoId;
    private String memberId;
    private String content;
    private Timestamp createdAt;

    // =========================================================
    // 마이페이지 전용 표시용 정보 (JOIN 결과)
    // =========================================================
    private String streamerName;
    private String videoTitle;

    // =========================================================
    // UI용 옵션 값
    // =========================================================
    private boolean highlight; // 하이라이트 여부 (기본 false)

    // =========================================================
    // Getter / Setter
    // =========================================================

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStreamerName() {
        return streamerName;
    }

    public void setStreamerName(String streamerName) {
        this.streamerName = streamerName;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}
