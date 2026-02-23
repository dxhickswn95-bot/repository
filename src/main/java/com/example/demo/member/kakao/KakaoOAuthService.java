package com.example.demo.member.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class KakaoOAuthService {

    private final WebClient kauthClient;
    private final WebClient kapiClient;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;
    
    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    public KakaoOAuthService(WebClient.Builder builder) {
        this.kauthClient = builder.baseUrl("https://kauth.kakao.com").build();
        this.kapiClient  = builder.baseUrl("https://kapi.kakao.com").build();
    }

    public KakaoTokenResponse requestToken(String code) {
        try {
            return kauthClient.post()
                    .uri("/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", kakaoClientId)
                            .with("client_secret", kakaoClientSecret)   // ✅ 추가
                            .with("redirect_uri", kakaoRedirectUri)
                            .with("code", code)
                    )
                    .retrieve()
                    .bodyToMono(KakaoTokenResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("카카오 토큰 요청 실패: " + e.getResponseBodyAsString(), e);
        }
    }

    public KakaoUserResponse requestUser(String accessToken) {
        try {
            return kapiClient.get()
                    .uri("/v2/user/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(KakaoUserResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("카카오 사용자 정보 요청 실패: " + e.getResponseBodyAsString(), e);
        }
    }
}
