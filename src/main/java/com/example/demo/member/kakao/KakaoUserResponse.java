package com.example.demo.member.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserResponse {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public KakaoAccount getKakaoAccount() { return kakaoAccount; }
    public void setKakaoAccount(KakaoAccount kakaoAccount) { this.kakaoAccount = kakaoAccount; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private Profile profile;

        public Profile getProfile() { return profile; }
        public void setProfile(Profile profile) { this.profile = profile; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        private String nickname;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }

        public String getProfileImageUrl() { return profileImageUrl; }
        public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    }
}
