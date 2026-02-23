package com.example.demo.member;

import java.sql.Timestamp;

public class MemberDTO {
    private String memberId;
    private String socialType;
    private String nickname;
    private String profileImage;
    private Timestamp createdAt;

    // ⭐ 추가
    private String role;      // ADMIN / USER
    private String password;  // local 로그인용 (DB 컬럼)

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getSocialType() { return socialType; }
    public void setSocialType(String socialType) { this.socialType = socialType; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
