package com.example.demo.mypage.dto;

public class FavoriteStreamerDTO {
    private String name;
    private String category;      // 옵션
    private boolean live;         // 옵션
    private int likeCount;        // 옵션
    private int viewCount;        // 옵션

    private String profileImage;  // ✅ JSP에서 필요

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isLive() { return live; }
    public void setLive(boolean live) { this.live = live; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    // ✅ 핵심: JSP는 getProfileImage()를 찾는다
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
