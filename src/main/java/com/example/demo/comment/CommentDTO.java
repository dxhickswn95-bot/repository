package com.example.demo.comment;

import java.sql.Timestamp;

public class CommentDTO {
    private int commentId;
    private int videoId;
    private String memberId;
    private String nickname;       // JOIN으로 가져올 때 사용
    private String profileImage;   // JOIN으로 가져올 때 사용
    private String content;
    private Timestamp createdAt;

    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getVideoId() { return videoId; }
    public void setVideoId(int videoId) { this.videoId = videoId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "CommentDTO [commentId=" + commentId + ", videoId=" + videoId +
               ", memberId=" + memberId + ", nickname=" + nickname +
               ", content=" + content + ", createdAt=" + createdAt + "]";
    }
}
