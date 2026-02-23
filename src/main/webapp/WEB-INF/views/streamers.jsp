<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>스트리머 목록</title>

<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/streamers.css">
</head>
<body>

	<%@ include file="/WEB-INF/views/common/common.jsp"%>

	<main class="app-content">
		<div class="container streamer-wrap">

			<c:set var="targetName" value="${param.name}" />

			<div class="page-top">
				<c:choose>
					<c:when test="${empty targetName}">
						<a class="back-btn" href="/main">← 메인</a>
					</c:when>
					<c:otherwise>
						<a class="back-btn" href="/streamers">← 스트리머 목록</a>
					</c:otherwise>
				</c:choose>
			</div>


			<!-- ✅ 페이지 타이틀: 목록 / 상세 모드 분기 -->
			<h1 class="page-title">
				<c:choose>
					<c:when test="${not empty targetName}">
                        ${targetName} 출연 영상
                    </c:when>
					<c:otherwise>
                        스트리머 목록
                    </c:otherwise>
				</c:choose>
			</h1>

			<!-- =========================================
                 ✅ 스트리머 카드 리스트 (목록/필터)
            ========================================== -->
			<div class="streamer-list">

				<!-- ✅ 실제 DB 데이터가 있을 때 -->
				<c:forEach var="s" items="${streamerList}">

					<!-- ✅ 상세 모드면 해당 스트리머만 / 목록 모드면 전체 -->
					<c:if test="${empty targetName or s.name eq targetName}">

						<div class="streamer-card-wrap">

							<!-- ✅ 좋아요 버튼 -->
							<c:if test="${not empty sessionScope.loginMemberId}">
								<c:set var="isFav"
									value="${not empty favoriteNames and favoriteNames.contains(s.name)}" />

								<form action="/streamers/favorite/toggle" method="post"
									class="fav-form">
									<input type="hidden" name="streamerName" value="${s.name}" />
									<button type="submit" class="fav-btn" title="좋아요">
										<c:choose>
											<c:when test="${isFav}">♥</c:when>
											<c:otherwise>♡</c:otherwise>
										</c:choose>
									</button>
								</form>
							</c:if>

							<!-- ✅ 스트리머 카드 -->
							<a class="streamer-card card" href="/streamers?name=${s.name}">
								<div class="streamer-thumb">
									<img src="${s.profileImage}" alt="스트리머 이미지"
										onerror="this.src='/images/dummy/streamers/default.jpg';" />
								</div>
								<div class="streamer-name">${s.name}</div>
							</a>

						</div>
					</c:if>
				</c:forEach>

				<!-- ✅ 더미 데이터(디자인 확인용) -->
				<c:if test="${empty streamerList}">

					<c:if test="${empty targetName or targetName eq '스트리머A'}">
						<a class="streamer-card card" href="/streamers?name=스트리머A">
							<div class="streamer-thumb">
								<img src="/images/dummy/streamers/streamer1.jpg" alt="스트리머A"
									onerror="this.src='/images/dummy/streamers/default.jpg';" />
							</div>
							<div class="streamer-name">스트리머A</div>
						</a>
					</c:if>

					<c:if test="${empty targetName or targetName eq '스트리머B'}">
						<a class="streamer-card card" href="/streamers?name=스트리머B">
							<div class="streamer-thumb">
								<img src="/images/dummy/streamers/streamer2.jpg" alt="스트리머B"
									onerror="this.src='/images/dummy/streamers/default.jpg';" />
							</div>
							<div class="streamer-name">스트리머B</div>
						</a>
					</c:if>

					<c:if test="${empty targetName or targetName eq '스트리머C'}">
						<a class="streamer-card card" href="/streamers?name=스트리머C">
							<div class="streamer-thumb">
								<img src="/images/dummy/streamers/streamer3.jpg" alt="스트리머C"
									onerror="this.src='/images/dummy/streamers/default.jpg';" />
							</div>
							<div class="streamer-name">스트리머C</div>
						</a>
					</c:if>

					<c:if test="${empty targetName or targetName eq '스트리머D'}">
						<a class="streamer-card card" href="/streamers?name=스트리머D">
							<div class="streamer-thumb">
								<img src="/images/dummy/streamers/streamer4.jpg" alt="스트리머D"
									onerror="this.src='/images/dummy/streamers/default.jpg';" />
							</div>
							<div class="streamer-name">스트리머D</div>
						</a>
					</c:if>

					<c:if test="${empty targetName or targetName eq '스트리머E'}">
						<a class="streamer-card card" href="/streamers?name=스트리머E">
							<div class="streamer-thumb">
								<img src="/images/dummy/streamers/streamer5.jpg" alt="스트리머E"
									onerror="this.src='/images/dummy/streamers/default.jpg';" />
							</div>
							<div class="streamer-name">스트리머E</div>
						</a>
					</c:if>

					<c:if test="${empty targetName or targetName eq '스트리머F'}">
						<a class="streamer-card card" href="/streamers?name=스트리머F">
							<div class="streamer-thumb">
								<img src="/images/dummy/streamers/streamer6.jpg" alt="스트리머F"
									onerror="this.src='/images/dummy/streamers/default.jpg';" />
							</div>
							<div class="streamer-name">스트리머F</div>
						</a>
					</c:if>

				</c:if>

			</div>

			<!-- =========================================
                 ✅ 상세 모드일 때: 출연 영상 카드 리스트
                 - 컨트롤러에서 videoList를 내려줘야 함
            ========================================== -->
			<c:if test="${not empty targetName}">
				<section class="streamer-videos">
					<h2 class="section-title">${targetName}영상 모아보기</h2>

					<c:choose>
						<c:when test="${not empty videoList}">
							<div class="video-grid">
								<c:forEach var="v" items="${videoList}">
									<a class="video-card card" href="/video?videoId=${v.videoId}">
										<div class="video-thumb">
											<img src="${v.thumbnailUrl}" alt="${v.title}"
												onerror="this.src='/images/dummy/default_thumb.jpg';" />
										</div>

										<div class="video-title">${v.title}</div>

										<div class="video-meta">조회수 ${v.viewCount} · 좋아요
											${v.likeCount}</div>
									</a>
								</c:forEach>
							</div>
						</c:when>

						<c:otherwise>
							<div class="empty-state">아직 이 스트리머가 출연한 영상이 없어요.</div>
						</c:otherwise>
					</c:choose>
				</section>
			</c:if>

		</div>
	</main>

</body>
</html>
