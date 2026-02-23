<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 | 스트리머 서버 영상</title>

<link rel="stylesheet" href="/css/common.css">
<link rel="stylesheet" href="/css/main.css">
</head>
<body>

	<!-- 공통 헤더 -->
	<%@ include file="/WEB-INF/views/common/common.jsp"%>

	<main class="app-content">
		<div class="container main-wrap">

			<!-- ===============================
         메인 타이틀 영역
    ================================ -->
			<section class="hero">
				<h1 class="hero-title">스트리머 대형 서버 영상 모아보기</h1>
				<p class="hero-sub">다양한 스트리머들의 대형 서버 플레이 영상을 한 곳에서 만나보세요</p>
				<a href="/streamers" class="btn btn-dark hero-btn"> 스트리머 둘러보기 </a>
			</section>

			<!-- ===============================
         인기 영상 섹션
    ================================ -->
			<section class="section">
				<h2 class="section-title">🔥 인기 영상</h2>

				<div class="video-grid">

					<!-- 실제 DB 연동 시 -->
					<c:forEach var="v" items="${popularVideoList}">
						<a class="video-card card" href="/video?videoId=${v.videoId}">
							<div class="thumb">
								<img src="${v.thumbnailUrl}"
									onerror="this.src='/images/dummy/default_thumb.jpg';" />
							</div>

							<div class="video-info">
								<div class="video-title">${v.title}</div>
								<div class="video-meta">${v.streamerName} · 조회수
									${v.viewCount}</div>
							</div>
						</a>
					</c:forEach>


					<!-- 🔧 데이터 없을 때 디자인 확인용 더미 -->
					<c:if test="${empty popularVideoList}">
						<a class="video-card card" href="/video?videoId=1">
							<div class="thumb">
								<img src="/images/dummy/dummy1.jpg" alt="썸네일">
							</div>
							<div class="video-info">
								<div class="video-title">대형 서버 첫 날 생존기</div>
								<div class="video-meta">스트리머 A · 조회수 120,000</div>
							</div>
						</a>

						<a class="video-card card" href="/video?videoId=2">
							<div class="thumb">
								<img src="/images/dummy/dummy2.jpg" alt="썸네일">
							</div>
							<div class="video-info">
								<div class="video-title">역대급 전투 발생</div>
								<div class="video-meta">스트리머 B · 조회수 98,000</div>
							</div>
						</a>

						<a class="video-card card" href="/video?videoId=3">
							<div class="thumb">
								<img src="/images/dummy/dummy3.jpg" alt="썸네일">
							</div>
							<div class="video-info">
								<div class="video-title">경제 시스템 완벽 분석</div>
								<div class="video-meta">스트리머 C · 조회수 76,500</div>
							</div>
						</a>

						<a class="video-card card" href="/video?videoId=4">
							<div class="thumb">
								<img src="/images/dummy/dummy4.jpg" alt="썸네일">
							</div>
							<div class="video-info">
								<div class="video-title">전설의 순간 모음</div>
								<div class="video-meta">스트리머 D · 조회수 150,300</div>
							</div>
						</a>
					</c:if>


				</div>
			</section>

		</div>
	</main>

</body>
</html>
