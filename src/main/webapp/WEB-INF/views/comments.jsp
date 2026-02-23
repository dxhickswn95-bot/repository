<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>댓글</title>

<link rel="stylesheet" href="/css/common.css" />
<link rel="stylesheet" href="/css/comments.css" />
</head>
<body>

	<%@ include file="/WEB-INF/views/common/common.jsp"%>

	<main class="app-content">

		<!-- ✅ 뒤로가기 위치를 video.jsp와 통일 (container 기준) -->
		<div class="container">
			<div class="back-row">
				<a class="back-link" href="/video?videoId=${videoId}">← 영상으로
					돌아가기</a>
			</div>
		</div>

		<!-- ✅ 댓글 본문은 comments-wrap(좁게)로 가운데 -->
		<div class="container comments-wrap">

			<div class="comments-header">
				<div class="comments-title">
					<span class="bubble">💬</span>
					<h1>댓글</h1>
				</div>
			</div>

			<!-- ===============================
           댓글 작성
      ================================ -->
			<section class="write-box">
				<div class="write-title">댓글 작성</div>

				<form class="write-form" action="/comments/create" method="post">
					<input type="hidden" name="videoId" value="${videoId}" />

					<!-- ✅ 공백/줄바꿈 안 들어가게 닫는 태그 붙여쓰기 -->
					<textarea class="textarea" name="content"
						placeholder="댓글을 입력하세요..." required></textarea>

					<button class="btn write-btn" type="submit">댓글 등록</button>
				</form>
			</section>

			<!-- ===============================
           전체 댓글 수
           ✅ PageController에서 model.addAttribute("commentCount", ...) 넣어야 출력됨
      ================================ -->
			<div class="total">
				전체 댓글 <b>${commentCount}</b>개
			</div>

			<!-- ===============================
           댓글 목록
      ================================ -->
			<div class="comment-list">
				<c:choose>
					<c:when test="${not empty commentList}">
						<c:forEach var="cmt" items="${commentList}">
							<div class="comment-card card">

								<div class="comment-top">
									<div class="comment-user">${cmt.nickname}</div>
									<c:if
										test="${
    not empty sessionScope.loginMemberId
    and sessionScope.loginMemberId eq cmt.memberId
}">
										<form action="/comments/delete" method="post">
											<input type="hidden" name="commentId"
												value="${cmt.commentId}" /> <input type="hidden"
												name="videoId" value="${videoId}" />
											<button type="submit" class="btn-delete"
												onclick="return confirm('댓글을 삭제할까요?');">🗑 삭제</button>
										</form>
									</c:if>






								</div>

								<div class="comment-content">${cmt.content}</div>

								<div class="comment-date">${cmt.createdAt}</div>

							</div>
						</c:forEach>

					</c:when>

					<c:otherwise>
						<div class="empty">아직 댓글이 없어요. 첫 댓글을 남겨보세요 🙂</div>
					</c:otherwise>

				</c:choose>
			</div>

		</div>
	</main>


</body>
</html>
