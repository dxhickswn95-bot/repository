package com.example.demo.streamer;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/streamers/favorite/toggle")
    public String toggle(@RequestParam("streamerName") String streamerName,
                         HttpSession session) {

        String memberId = (String) session.getAttribute("loginMemberId");
        if (memberId == null || memberId.isBlank()) {
            return "redirect:/member/login";
        }

        favoriteService.toggle(memberId, streamerName);
        return "redirect:/streamers";
    }
    
    @PostMapping("/mypage/favorite/remove")
    @ResponseBody
    public Map<String, Object> removeFavoriteAjax(@RequestParam("streamerName") String streamerName,
                                                  HttpSession session) {

        String memberId = (String) session.getAttribute("loginMemberId");
        if (memberId == null || memberId.isBlank()) {
            return Map.of("success", false, "message", "LOGIN_REQUIRED");
        }

        // 해제만 (toggle 말고 remove가 더 안전)
        favoriteService.removeFavorite(memberId, streamerName);

        return Map.of("success", true);
    }

}
