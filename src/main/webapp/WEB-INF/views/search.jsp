<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>검색</title>
<link rel="stylesheet" href="/css/common.css" />
<link rel="stylesheet" href="/css/search.css" />
</head>
<body>
	<%@ include file="/WEB-INF/views/common/common.jsp"%>
	<main class="app-content">
		<div class="container search-wrap">
			<!-- ✅ 검색 입력 영역 -->
			<div class="search-center">
				<div class="search-icon">🔎</div>
				<div class="search-title">검색</div>
				<div class="search-sub">스트리머 RP 이름, 서버 이름, 영상 제목을 검색해보세요</div>
				<form class="search-form" action="/search" method="get">
					<input class="input search-input" type="text" name="q" value="${q}"
						placeholder="검색어를 입력하세요 (예: 스트리머 이름, RP 이름, 서버 이름)" />
					<button class="btn btn-dark search-btn" type="submit">검색</button>
				</form>
			</div>
			<!-- ✅ 검색어가 있을 때만 결과 영역 출력 -->
			<c:if test="${not empty q}">
				<div class="search-result-head">
					<div class="search-result-title">검색 결과</div>
					<div class="search-result-count">총 ${count}개</div>
				</div>
				<c:choose>
					<c:when test="${empty videos}">
						<div class="search-empty">
							검색 결과가 없습니다.<br /> 스트리머 이름 / 서버 이름 / 영상 제목으로 다시 검색해보세요.
						</div>
					</c:when>
					<c:otherwise>
						<div class="video-grid">
							<c:forEach var="v" items="${videos}">
								<a class="video-card" href="/video?videoId=${v.videoId}">
									<div class="thumb">
										<img src="${v.thumbnailUrl}" alt="${v.title}" />
									</div>
									<div class="card-body">
										<div class="card-title">${v.title}</div>
										<div class="card-meta">
											<c:if test="${not empty v.serverName}">
												<span class="tag">🏷 ${v.serverName}</span>
											</c:if>
											<c:if test="${v.serverSeason gt 0}">
												<span class="tag">🌟 시즌 ${v.serverSeason}</span>
											</c:if>
											<c:if test="${not empty v.streamerName}">
												<span class="tag">🎥 ${v.streamerName}</span>
											</c:if>
											<c:if test="${v.viewCount gt 0}">
												<span class="tag">👀 ${v.viewCount}회</span>
											</c:if>
										</div>
									</div>
								</a>
							</c:forEach>
						</div>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</main>
</body>
</html>