<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>영상 재생 | 스트리머 서버 영상</title>

<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/video.css">
</head>
<body>

	<!-- ✅ 공통 헤더 -->
	<%@ include file="/WEB-INF/views/common/common.jsp"%>

	<main class="app-content">
		<div class="container video-wrap">

			<!-- ===============================
                 ✅ 뒤로가기
                 - 메인으로 다시 이동
            ================================ -->
			<div class="back-row">
				<a class="back-link" href="/main">← 메인으로 돌아가기</a>
			</div>

			<!-- ===============================
                 ✅ 영상 영역
                 - 처음엔 썸네일(포스터) + 가운데 재생 버튼
                 - 클릭하면 iframe으로 전환 + autoplay
            ================================ -->
			<section class="player-section">

				<div class="player-box">

					<!-- ✅ 처음엔 src 비워둠 + 숨김 -->
					<iframe id="ytFrame" class="player-iframe"
						allow="autoplay; encrypted-media; picture-in-picture"
						allowfullscreen></iframe>

					<!-- ✅ 처음에 보이는 썸네일 -->
					<div id="poster" class="player-poster"
						style="background-image:url('${empty video.thumbnailUrl ? '/images/dummy/videos/default.jpg' : video.thumbnailUrl}');">
						<button type="button" class="play-btn" aria-label="재생"></button>
					</div>

				</div>

				<!-- ===============================
                     ✅ 영상 메타 영역
                     - 스트리머 버튼 + 조회수
                     - video가 비어도 안전하게 출력
                ================================ -->
				<div class="meta-row">
					<c:choose>
						<c:when test="${not empty video}">

							<!-- ✅ 1줄: 조회수 -->
							<div class="meta">
								<div class="meta-label">조회수 ${video.viewCount}회</div>
							</div>

							<!-- ✅ 2줄: 버튼들 -->
							<div class="streamer-btn-row">
								<c:forEach var="s" items="${streamers}">
									<a class="btn btn-dark streamer-btn"
										href="/streamers?name=${s.name}"> 🎥 ${s.name} 영상 모아보기 </a>
								</c:forEach>
							</div>

						</c:when>

						<c:otherwise>

							<!-- ✅ 1줄: 조회수 -->
							<div class="meta">
								<div class="meta-label">조회수 0회</div>
							</div>

							<!-- ✅ 2줄: 버튼 -->
							<div class="streamer-btn-row">
								<a class="btn btn-dark streamer-btn"
									href="/streamers?name=스트리머A"> 🎥 스트리머A 영상 모아보기 </a>
							</div>

						</c:otherwise>
					</c:choose>
				</div>


			</section>

			<!-- ===============================
                 ✅ 댓글 요약 + 전체 댓글 보기
                 - /comments?videoId=... 로 이동
            ================================ -->
			<section class="comment-preview">

				<div class="comment-head">
					<h2 class="section-title">댓글</h2>

					<div class="action-row">
						<!-- ✅ 현재 페이지의 videoId를 그대로 넘김 -->
						<a class="action-link" href="/comments?videoId=${param.videoId}">
							💬 모든 댓글 보기 </a>
					</div>
				</div>

				<!-- ✅ 댓글 개수는 여기서 한 번만 출력 -->
				<div class="comment-count">
					댓글 <b>${commentCount}</b>개
				</div>

				<!-- ✅ 댓글 미리보기 (최대 3개) -->
				<c:choose>
					<c:when test="${not empty commentList}">
						<c:forEach var="cmt" items="${commentList}" begin="0" end="2">
							<div class="comment-item">
								<div class="comment-user">${cmt.nickname}</div>
								<div class="comment-content">${cmt.content}</div>
							</div>
						</c:forEach>
					</c:when>

					<c:otherwise>
						<div>아직 댓글이 없습니다.</div>
					</c:otherwise>
				</c:choose>

			</section>

		</div>
	</main>

	<!-- ===============================
         ✅ 썸네일 클릭 시 iframe으로 전환 + 재생
         - youtubeVideoId가 있어야 정상 동작
         - autoplay 정책 때문에 mute=1을 같이 주면 안정적
    ================================ -->
	<script>
		(function() {
			const youtubeId = "${empty video ? '' : video.youtubeVideoId}";
			const poster = document.getElementById("poster");
			const frame = document.getElementById("ytFrame");

			if (!youtubeId || !poster || !frame)
				return;

			poster.addEventListener("click", function() {
				frame.src = "https://www.youtube.com/embed/" + youtubeId
						+ "?autoplay=1&mute=1&rel=0";
				frame.style.display = "block";
				poster.style.display = "none";
			});
		})();
	</script>


</body>
</html>
