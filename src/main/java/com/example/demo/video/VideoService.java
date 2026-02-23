package com.example.demo.video;

import java.util.List;

public interface VideoService {

    // 영상 등록
    boolean insertVideo(VideoDTO dto);

    // 영상 삭제
    boolean deleteVideo(VideoDTO dto);

    // 영상 1개 조회 (상세 페이지용)
    VideoDTO getVideo(VideoDTO dto);

    // 영상 전체 목록 조회 (전체 목록 페이지용)
    List<VideoDTO> getVideoList();

    // ✅ 메인 인기 영상 조회
    List<VideoDTO> getPopularVideoList(int limit);
    
    // ✅ 조회수 증가
    void increaseViewCount(int videoId);
    
    // ✅ 추가: 스트리머별 출연 영상 목록
    List<VideoDTO> getVideosByStreamerName(String streamerName);
    
    List<VideoDTO> searchVideos(String keyword);
}
