package com.example.demo.streamer;

import java.util.List;

public interface StreamerService {

    // 스트리머 등록
    boolean insertStreamer(StreamerDTO dto);

    // 스트리머 프로필 이미지 업데이트(유튜브 API로 가져온 이미지 넣을 때 사용)
    boolean updateProfileImage(StreamerDTO dto);

    // 스트리머 삭제
    boolean deleteStreamer(StreamerDTO dto);

    // 스트리머 1명 조회
    StreamerDTO getStreamer(StreamerDTO dto);

    // 스트리머 전체 목록 조회
    List<StreamerDTO> getStreamerList();
    
    int syncYouTubeProfileImages();

    StreamerDTO getStreamerByName(String name);

}
