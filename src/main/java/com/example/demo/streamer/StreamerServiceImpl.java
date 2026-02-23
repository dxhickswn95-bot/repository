package com.example.demo.streamer;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


@Service
public class StreamerServiceImpl implements StreamerService {

    private final MybatisStreamerDAO streamerDAO;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${youtube.api.key:}")
    private String youtubeApiKey;

    @Value("${youtube.api.base:https://www.googleapis.com/youtube/v3}")
    private String youtubeApiBase;

    public StreamerServiceImpl(MybatisStreamerDAO streamerDAO, RestTemplate restTemplate) {
        this.streamerDAO = streamerDAO;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // =========================================================
    // 기본 CRUD
    // =========================================================

    @Override
    public boolean insertStreamer(StreamerDTO dto) {
        System.out.println("StreamerService - insertStreamer");
        return streamerDAO.insertStreamer(dto);
    }

    @Override
    public boolean deleteStreamer(StreamerDTO dto) {
        System.out.println("StreamerService - deleteStreamer");
        return streamerDAO.deleteStreamer(dto);
    }

    @Override
    public boolean updateProfileImage(StreamerDTO dto) {
        System.out.println("StreamerService - updateProfileImage");
        return streamerDAO.updateProfileImage(dto);
    }

    @Override
    public StreamerDTO getStreamer(StreamerDTO dto) {
        System.out.println("StreamerService - getStreamer");
        return streamerDAO.getStreamer(dto);
    }

    @Override
    public StreamerDTO getStreamerByName(String name) {
        System.out.println("StreamerService - getStreamerByName");
        return streamerDAO.getStreamerByName(name);
    }

    @Override
    public List<StreamerDTO> getStreamerList() {
        System.out.println("StreamerService - getStreamerList");
        return streamerDAO.getStreamerList();
    }

    // =========================================================
    // YouTube 프로필 이미지 동기화
    // =========================================================
    @Override
    public int syncYouTubeProfileImages() {
        System.out.println("StreamerService - syncYouTubeProfileImages");

        // 1) API 키 체크
        if (youtubeApiKey == null || youtubeApiKey.isBlank()) {
            System.out.println("❌ youtube.api.key 설정이 없습니다. application.properties 확인!");
            return 0;
        }

        // 2) 대상 조회
        List<StreamerDTO> targets = streamerDAO.getYouTubeStreamersNeedImage();
        if (targets == null || targets.isEmpty()) {
            System.out.println("✅ 동기화 대상 스트리머가 없습니다.");
            return 0;
        }

        int updatedCount = 0;

        for (StreamerDTO s : targets) {
            String name = s.getName();
            String channelId = s.getChannelId();

            try {
                if (channelId == null || channelId.isBlank()) {
                    System.out.println("채널ID 없음: " + name);
                    continue;
                }

                // 3) YouTube API 호출 URL
                String url = youtubeApiBase + "/channels"
                        + "?part=snippet"
                        + "&id=" + URLEncoder.encode(channelId, StandardCharsets.UTF_8)
                        + "&key=" + URLEncoder.encode(youtubeApiKey, StandardCharsets.UTF_8);

                // 4) raw JSON
                String raw = restTemplate.getForObject(url, String.class);
                if (raw == null || raw.isBlank()) {
                    System.out.println("YouTube 응답 비어있음: " + name);
                    continue;
                }

                // 5) JSON 파싱
                String imgUrl = extractThumbnailUrl(raw);
                if (imgUrl == null || imgUrl.isBlank()) {
                    System.out.println("이미지 URL 추출 실패: " + name + " / " + channelId);
                    continue;
                }

                // 6) DB 업데이트 (이름 기준 업데이트)
                StreamerDTO updateDto = new StreamerDTO();
                updateDto.setName(name);
                updateDto.setProfileImage(imgUrl);

                boolean ok = streamerDAO.updateProfileImage(updateDto);
                if (ok) {
                    updatedCount++;
                    System.out.println("업데이트 성공: " + name + " -> " + imgUrl);
                } else {
                    System.out.println("업데이트 실패(영향 row 0): " + name);
                }

            } catch (Exception e) {
                System.out.println("동기화 실패: " + name);
                e.printStackTrace();
            }
        }

        System.out.println("✅ 동기화 완료. updatedCount=" + updatedCount);
        return updatedCount;
    }

    // =========================================================
    // Helper: 썸네일 URL 추출 (high > medium > default)
    // =========================================================
    private String extractThumbnailUrl(String rawJson) throws Exception {
        JsonNode root = objectMapper.readTree(rawJson);
        JsonNode items = root.path("items");

        if (!items.isArray() || items.size() == 0) return null;

        JsonNode thumbnails = items.get(0).path("snippet").path("thumbnails");
        if (thumbnails.isMissingNode()) return null;

        String high = pickThumb(thumbnails, "high");
        if (high != null) return high;

        String medium = pickThumb(thumbnails, "medium");
        if (medium != null) return medium;

        return pickThumb(thumbnails, "default");
    }

    private String pickThumb(JsonNode thumbnails, String key) {
        JsonNode node = thumbnails.path(key).path("url");
        if (node.isMissingNode()) return null;

        String url = node.asText();
        return (url == null || url.isBlank()) ? null : url;
    }
}
