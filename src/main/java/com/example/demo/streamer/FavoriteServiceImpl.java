package com.example.demo.streamer;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final PlusFavoriteDAO favoriteDAO;

    public FavoriteServiceImpl(PlusFavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    @Override
    public void toggle(String memberId, String streamerName) {
        boolean exists = favoriteDAO.isFavorite(memberId, streamerName);
        if (exists) favoriteDAO.removeFavorite(memberId, streamerName);
        else favoriteDAO.addFavorite(memberId, streamerName);
    }
    @Override
    public void removeFavorite(String memberId, String streamerName) { // ✅ 추가
        favoriteDAO.removeFavorite(memberId, streamerName);
    }

    @Override
    public Set<String> getFavoriteNames(String memberId) {
        return favoriteDAO.getFavoriteNames(memberId);
    }

    @Override
    public List<StreamerDTO> getFavoriteStreamers(String memberId) {
        return favoriteDAO.getFavoriteStreamers(memberId);
    }
}
