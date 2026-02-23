package com.example.demo.comment;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.member.MemberDTO;
import com.example.demo.member.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    public CommentController(CommentService commentService, MemberService memberService) {
        this.commentService = commentService;
        this.memberService = memberService;
    }

    /* =========================
       공통: 로그인/권한 유틸
       ========================= */

    // ✅ 로그인한 memberId (없으면 로그인 페이지로)
    private String requireLoginMemberId(HttpSession session) {
        return (String) session.getAttribute("loginMemberId");
    }

    // ✅ role 세션 캐시 (없으면 DB에서 조회해서 세션 저장)
    private String getOrLoadLoginRole(HttpSession session) {
        String role = (String) session.getAttribute("loginRole");
        if (role != null) return role;

        String loginMemberId = (String) session.getAttribute("loginMemberId");
        if (loginMemberId == null) return null;

        MemberDTO member = memberService.getMember(loginMemberId);
        if (member == null) return null;

        role = member.getRole(); // ⚠️ MemberDTO에 role getter가 getRole()인지 확인!
        if (role == null) role = "USER";

        session.setAttribute("loginRole", role);
        return role;
    }

    /* =========================
       댓글 목록
       ========================= */

    @GetMapping
    public String comments(@RequestParam(value = "videoId", defaultValue = "1") int videoId,
                           Model model,
                           HttpSession session) {

        // (선택) JSP에서 role 비교할 때 필요하면 미리 세팅
        // 로그인 안 했으면 role은 null이어도 됨
        getOrLoadLoginRole(session);

        model.addAttribute("videoId", videoId);

        List<CommentDTO> list = commentService.getCommentListByVideoId(videoId);
        model.addAttribute("commentList", list);
        model.addAttribute("commentCount", list.size());

        return "comments";
    }

    /* =========================
       댓글 등록
       ========================= */

    @PostMapping("/create")
    public String create(@RequestParam("videoId") int videoId,
                         @RequestParam("content") String content,
                         HttpSession session) {

        String loginMemberId = requireLoginMemberId(session);
        if (loginMemberId == null) return "redirect:/member/login";

        CommentDTO dto = new CommentDTO();
        dto.setVideoId(videoId);
        dto.setContent(content);
        dto.setMemberId(loginMemberId);

        commentService.insertComment(dto);
        return "redirect:/comments?videoId=" + videoId;
    }

    /* =========================
       댓글 삭제
       ========================= */

    @PostMapping("/delete")
    public String deleteComment(@RequestParam("commentId") int commentId,
                                @RequestParam("videoId") int videoId,
                                HttpSession session) {

        String loginMemberId = requireLoginMemberId(session);
        if (loginMemberId == null) return "redirect:/member/login";

        String role = getOrLoadLoginRole(session);
        if (role == null) return "redirect:/member/login";

        CommentDTO dto = new CommentDTO();
        dto.setCommentId(commentId);
        dto.setMemberId(loginMemberId);

        // commentService.deleteComment(dto, role) 가 ADMIN/USER 정책 처리한다고 가정
        commentService.deleteComment(dto);

        return "redirect:/comments?videoId=" + videoId;
    }
}
