package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.comment.CommentDTO;
import com.example.demo.comment.CommentService;
import com.example.demo.member.MemberDTO;
import com.example.demo.streamer.FavoriteService;
import com.example.demo.streamer.StreamerDTO;
import com.example.demo.streamer.StreamerService;
import com.example.demo.video.VideoDTO;
import com.example.demo.video.VideoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    private final StreamerService streamerService;
    private final VideoService videoService;
    private final CommentService commentService;
    private final FavoriteService favoriteService;

    // ✅ 생성자 주입 (Spring이 자동 주입)
    public PageController(StreamerService streamerService,
                          VideoService videoService,
                          CommentService commentService,
                          FavoriteService favoriteService) {
        this.streamerService = streamerService;
        this.videoService = videoService;
        this.commentService = commentService;
        this.favoriteService = favoriteService;
    }
    
    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main(Model model) {
        List<VideoDTO> popular = videoService.getPopularVideoList(12);
        model.addAttribute("popularVideoList", popular);
        return "main";
    }

    @GetMapping("/streamers")
    public String streamers(@RequestParam(value = "name", required = false) String name,
                            Model model,
                            HttpSession session) {

        // ✅ 항상: 스트리머 목록
        model.addAttribute("streamerList", streamerService.getStreamerList());

        // ✅ 로그인 시: 좋아요 표시용
        String memberId = (String) session.getAttribute("loginMemberId");
        if (memberId != null && !memberId.isBlank()) {
            model.addAttribute("favoriteNames", favoriteService.getFavoriteNames(memberId));
        }

        // ✅ name이 있으면: 스트리머 상세(출연 영상 카드 리스트)
        if (name != null && !name.isBlank()) {
            model.addAttribute("selectedName", name);
            model.addAttribute("videoList", videoService.getVideosByStreamerName(name));
        }

        return "streamers";
    }


    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false) String q,
                         Model model) {

        // 검색어 없을 때: 검색 화면만
        if (q == null || q.trim().isEmpty()) {
            model.addAttribute("q", "");
            model.addAttribute("videos", java.util.List.of());
            model.addAttribute("count", 0);

            // ✅ 추가: 스트리머 카드 null로
            model.addAttribute("streamer", null);

            return "search";
        }

        String keyword = q.trim();

        // ✅ 기존: 영상 검색
        List<VideoDTO> videos = videoService.searchVideos(keyword);

        // ✅ 추가: 스트리머 카드(정확히 일치하는 스트리머가 있으면 1명 반환)
        StreamerDTO streamer = streamerService.getStreamerByName(keyword);

        model.addAttribute("q", keyword);
        model.addAttribute("videos", videos);
        model.addAttribute("count", videos.size());

        // ✅ 추가
        model.addAttribute("streamer", streamer);

        return "search";
    }



    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping
    public String comments(
            @RequestParam(value = "videoId", defaultValue = "1") int videoId,
            Model model,
            HttpSession session) {

        // ✅ 세션 값 준비 (JSP에서 바로 쓰게)
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        String role = (String) session.getAttribute("loginRole");
        if (role == null) {
            role = "USER";
            session.setAttribute("loginRole", role);
        }

        model.addAttribute("videoId", videoId);

        List<CommentDTO> list = commentService.getCommentListByVideoId(videoId);
        model.addAttribute("commentList", list);
        model.addAttribute("commentCount", list.size());

        // (선택) JSP에서 보기 편하게 모델에도 내려줄 수 있음
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("loginRole", role);

        return "comments";
    }

    @GetMapping("/video")
    public String video(@RequestParam int videoId,
                        Model model,
                        HttpSession session) {

        // 1️⃣ 조회수 증가 (세션 기준 1회)
    	// ✅ 조회수는 세션 기준으로 영상당 1회만 증가
    	// - 새로고침(F5) / 반복 진입으로 조회수 왜곡 방지
    	// - viewed_video_{videoId} 키로 해당 영상이 이미 집계됐는지 체크

        String key = "viewed_video_" + videoId;
        if (session.getAttribute(key) == null) {
            videoService.increaseViewCount(videoId);
            session.setAttribute(key, true);
        }

        // 2️⃣ VideoDTO 생성 후 조회
        VideoDTO param = new VideoDTO();
        param.setVideoId(videoId);

        VideoDTO video = videoService.getVideo(param);

        // ✅ 영상이 없으면 예외 페이지/메인으로 보내기 (선택)
        if (video == null) {
            return "redirect:/main";
        }

        // 3️⃣ 댓글 조회
        List<CommentDTO> commentList = commentService.getCommentListByVideoId(videoId);

        // 4️⃣ Model 세팅
        model.addAttribute("video", video);
        model.addAttribute("commentCount", commentList.size());
        model.addAttribute("commentList", commentList);

        // ✅ 이 한 줄만 추가하면 끝: JSP에서 ${streamers}로 쓰고 싶다면
        model.addAttribute("streamers", video.getStreamers());

        return "video";
    }

	
	
	


}
