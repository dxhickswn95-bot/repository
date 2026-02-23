package com.example.demo.member;

import com.example.demo.member.kakao.KakaoOAuthService;
import com.example.demo.member.kakao.KakaoTokenResponse;
import com.example.demo.member.kakao.KakaoUserResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final KakaoOAuthService kakaoOAuthService;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    public MemberController(MemberService memberService, KakaoOAuthService kakaoOAuthService) {
        this.memberService = memberService;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    /* =========================
       로그인 / 마이페이지 (기존 기능)
       ========================= */

    // ✅ 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @PostMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/main";
	}

    // ✅ 닉네임 변경
    @PostMapping("/updateName")
    public String updateName(@RequestParam String nickname, HttpSession session) {
        String loginMemberId = (String) session.getAttribute("loginMemberId");
        if (loginMemberId == null) return "redirect:/member/login";

        memberService.updateNickname(loginMemberId, nickname);
        session.setAttribute("loginNickname", nickname);

        return "redirect:/member/mypage";
    }

    // ✅ 회원 삭제
    @PostMapping("/delete")
    public String deleteMember(HttpSession session) {
        String loginMemberId = (String) session.getAttribute("loginMemberId");
        if (loginMemberId == null) return "redirect:/member/login";

        memberService.deleteMember(loginMemberId);
        session.invalidate();

        return "redirect:/main";
    }

    /* =========================
       카카오 로그인
       ========================= */

    // 1️⃣ 카카오 로그인 시작 (인가 페이지로 이동)
    @GetMapping("/kakao/login")
    public String kakaoLogin() {
        String redirectUri = URLEncoder.encode(kakaoRedirectUri, StandardCharsets.UTF_8);

        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + kakaoClientId
                + "&redirect_uri=" + redirectUri;

        return "redirect:" + url;
    }

    // 2️⃣ 카카오 콜백 (code → token → user → DB upsert → 세션 로그인)
    @GetMapping("/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code, HttpSession session) {

        System.out.println("카카오 인가 코드: " + code);

        // 1) 토큰 요청
        KakaoTokenResponse token = kakaoOAuthService.requestToken(code);
        String accessToken = token.getAccessToken();
        System.out.println("access_token = " + accessToken);

        // 2) 사용자 정보 요청
        KakaoUserResponse user = kakaoOAuthService.requestUser(accessToken);

        String socialId = String.valueOf(user.getId());
        String nickname = "kakao_user";
        String profileImage = null;

        if (user.getKakaoAccount() != null && user.getKakaoAccount().getProfile() != null) {
            if (user.getKakaoAccount().getProfile().getNickname() != null) {
                nickname = user.getKakaoAccount().getProfile().getNickname();
            }
            profileImage = user.getKakaoAccount().getProfile().getProfileImageUrl();
        }

        System.out.println("kakao socialId=" + socialId + ", nickname=" + nickname);

        // 3) DB upsert (없으면 가입/있으면 업데이트) + 로그인 회원 반환
        MemberDTO loginMember = memberService.upsertKakaoMember(socialId, nickname, profileImage);

        // 4) 세션 로그인 (너 프로젝트 표준 키)
        session.setAttribute("loginMemberId", loginMember.getMemberId());
        session.setAttribute("loginNickname", loginMember.getNickname());
        session.setAttribute("loginProfileImage", loginMember.getProfileImage());

        return "redirect:/main";
    }

}
