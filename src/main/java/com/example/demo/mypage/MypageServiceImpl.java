package com.example.demo.mypage;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.member.MemberDTO;
import com.example.demo.mypage.dto.FavoriteStreamerDTO;
import com.example.demo.mypage.dto.MycommentDTO;
import com.example.demo.streamer.FavoriteService;

@Service
public class MypageServiceImpl implements MypageService {

    private final PlusMypageDAO myPageDAO;
    private final FavoriteService favoriteService;

    public MypageServiceImpl(PlusMypageDAO myPageDAO,
    						 FavoriteService favoriteService) {
        this.myPageDAO = myPageDAO;
        this.favoriteService = favoriteService;
    }

    @Override
    public MemberDTO getMyInfo(String memberId) {
        return myPageDAO.selectMember(memberId);
    }

    @Override
    public List<MycommentDTO> getMyComments(String memberId) {
        return myPageDAO.selectMycomments(memberId);
    }

    @Override
    public List<FavoriteStreamerDTO> getFavoriteStreamers(String memberId) {

        return favoriteService.getFavoriteStreamers(memberId).stream()
            .map(s -> {
                FavoriteStreamerDTO dto = new FavoriteStreamerDTO();
                dto.setName(s.getName());

                dto.setCategory(s.getPlatform());   // 임시로 platform 표시
                dto.setLive(false);                 // 추후 확장
                dto.setLikeCount(0);
                dto.setViewCount(0);

                // ✅ 이거 추가!!!
                dto.setProfileImage(s.getProfileImage());

                return dto;
            })
            .toList();
    }


}
