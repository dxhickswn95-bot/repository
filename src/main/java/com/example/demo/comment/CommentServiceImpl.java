package com.example.demo.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PlusCommentDAO commentDAO;

    @Override
    public boolean insertComment(CommentDTO dto) {
        return commentDAO.insertComment(dto);
    }

    // ✅ 기존 메서드는 그대로 (관리자 전용/테스트용 등으로 남겨둠)
    @Override
    public boolean deleteComment(CommentDTO dto) {
        return commentDAO.deleteComment(dto); // comment_id만으로 삭제
    }

    @Override
    public CommentDTO getComment(CommentDTO dto) {
        return commentDAO.getComment(dto);
    }

    @Override
    public List<CommentDTO> getCommentListByVideoId(int videoId) {
        return commentDAO.getCommentListByVideoId(videoId);
    }

    @Override
    public List<CommentDTO> getRecentCommentList(int limit) {
        return commentDAO.getRecentComments(limit);
    }
    
    
}

