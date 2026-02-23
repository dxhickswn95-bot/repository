package com.example.demo.video;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.streamer.StreamerDTO;

@Service("videoService")
public class VideoServiceImpl implements VideoService {

    @Autowired
    private PlusVideoDAO videoDAO;
    
    @Override
    public List<VideoDTO> getPopularVideoList(int limit) {
        return videoDAO.getPopularVideos(limit);
    }

    @Override
    public boolean insertVideo(VideoDTO dto) {
        return videoDAO.insertVideo(dto);
    }

    @Override
    public boolean deleteVideo(VideoDTO dto) {
        return videoDAO.deleteVideo(dto);
    }

    @Override
    public VideoDTO getVideo(VideoDTO dto) {
        VideoDTO video = videoDAO.getVideo(dto);
        if (video == null) return null;

        // ✅ 등장 스트리머 리스트 붙이기
        List<StreamerDTO> streamers = videoDAO.getStreamersByVideoId(video.getVideoId());

        // (선택) 연결테이블 데이터가 없을 때 대표 스트리머라도 하나 넣는 fallback
        if (streamers == null || streamers.isEmpty()) {
            StreamerDTO s = new StreamerDTO();
            s.setName(video.getStreamerName());
            streamers = List.of(s);
        }

        video.setStreamers(streamers);
        return video;
    }

    @Override
    public List<VideoDTO> getVideoList() {
        return videoDAO.getVideoList();
    }
    
    @Override
    public void increaseViewCount(int videoId) {
        videoDAO.increaseViewCount(videoId);
    }
    
    @Override
    public List<VideoDTO> getVideosByStreamerName(String streamerName) {
        return videoDAO.getVideosByStreamerName(streamerName);
    }

    @Override
    public List<VideoDTO> searchVideos(String keyword) {
        return videoDAO.searchVideos(keyword);
    }

}
