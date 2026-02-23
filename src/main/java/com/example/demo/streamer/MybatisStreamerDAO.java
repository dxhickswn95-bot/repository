package com.example.demo.streamer;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisStreamerDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String NAMESPACE = "Streamer.";

    // 등록
    public boolean insertStreamer(StreamerDTO dto) {
        System.out.println("바뀐 MybatisStreamerDAO.InsertStreamer 로그");
        return sqlSession.insert(NAMESPACE + "insert", dto) > 0;
    }

    // 삭제
    public boolean deleteStreamer(StreamerDTO dto) {
        System.out.println("바뀐 MybatisStreamerDAO.DeleteStreamer 로그");
        return sqlSession.delete(NAMESPACE + "delete", dto) > 0;
    }

    // 프로필 이미지 업데이트
    public boolean updateProfileImage(StreamerDTO dto) {
        System.out.println("바뀐 MybatisStreamerDAO.UpdateProfileImage 로그");
        return sqlSession.update(NAMESPACE + "updateProfileImage", dto) > 0;
    }

    // 단일 조회 (name=PK)
    public StreamerDTO getStreamer(StreamerDTO dto) {
        System.out.println("바뀐 MybatisStreamerDAO.GetStreamer 로그");
        return sqlSession.selectOne(NAMESPACE + "getOne", dto);
    }

    // 이름으로 조회(필요하면)
    public StreamerDTO getStreamerByName(String name) {
        System.out.println("바뀐 MybatisStreamerDAO.GetStreamerByName 로그");
        return sqlSession.selectOne(NAMESPACE + "getOneByName", name);
    }

    // 전체 조회
    public List<StreamerDTO> getStreamerList() {
        System.out.println("바뀐 MybatisStreamerDAO.GetStreamerList 로그");
        return sqlSession.selectList(NAMESPACE + "getList");
    }

    // 유튜브 프로필 이미지 동기화 대상 조회
    public List<StreamerDTO> getYouTubeStreamersNeedImage() {
        System.out.println("바뀐 MybatisStreamerDAO.GetYouTubeStreamersNeedImage 로그");
        return sqlSession.selectList(NAMESPACE + "getYouTubeNeedImage");
    }
}
