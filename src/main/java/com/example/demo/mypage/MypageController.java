package com.example.demo.mypage;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.member.MemberDTO;
import com.example.demo.mypage.dto.FavoriteStreamerDTO;
import com.example.demo.mypage.dto.MycommentDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService myPageService;

    public MypageController(MypageService mypageService) {
        this.myPageService = mypageService;
    }

    /**
     * ✅ 마이페이지
     * GET /mypage
     * 세션: loginMemberId (String)
     */
    @GetMapping
    public String mypage(HttpSession session, Model model) {

        // ✅ 세션에서 로그인한 memberId 꺼내기
        String memberId = (String) session.getAttribute("loginMemberId");
        if (memberId == null || memberId.isBlank()) {
            return "redirect:/member/login";
        }

        // 1) 내 정보
        MemberDTO member = myPageService.getMyInfo(memberId);

        // 2) 내가 쓴 댓글
        List<MycommentDTO> myCommentList = myPageService.getMyComments(memberId);

        // 3) 좋아하는 스트리머 (없으면 빈 리스트로)
        List<FavoriteStreamerDTO> favoriteStreamerList = myPageService.getFavoriteStreamers(memberId);

        // ✅ JSP에서 쓰는 attribute 이름과 동일하게
        model.addAttribute("member", member);
        model.addAttribute("myCommentList", myCommentList);
        model.addAttribute("favoriteStreamerList", favoriteStreamerList);
       
        return "mypage"; // /WEB-INF/views/mypage.jsp
    }
}
