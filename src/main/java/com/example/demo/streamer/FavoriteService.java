package com.example.demo.streamer;

import java.util.List;
import java.util.Set;

public interface FavoriteService {
    void toggle(String memberId, String streamerName);
    void removeFavorite(String memberId, String streamerName); // ✅ 추가
    Set<String> getFavoriteNames(String memberId);
    List<StreamerDTO> getFavoriteStreamers(String memberId);
}
