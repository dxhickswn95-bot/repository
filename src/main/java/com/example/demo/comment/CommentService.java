package com.example.demo.comment;

import java.util.List;

public interface CommentService {

    boolean insertComment(CommentDTO dto);

    // ✅ 기존 유지 (혹시 다른 데서 쓰는 중이면)
    boolean deleteComment(CommentDTO dto);

    CommentDTO getComment(CommentDTO dto);

    List<CommentDTO> getCommentListByVideoId(int videoId);

    List<CommentDTO> getRecentCommentList(int limit);
}

