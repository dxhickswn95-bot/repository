<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<header class="app-header">
	<div class="header-inner">
		<div class="header-left">
			<a class="brand" href="/main">스트리머 서버 영상</a>
		</div>

		<nav class="header-center">
			<a class="nav-link" href="/streamers">스트리머</a> <a class="nav-link"
				href="/search">검색</a>
		</nav>

		<div class="header-right">
			<!-- ✅ 로그인 객체를 어떤 이름으로 세션에 넣든 동작하도록 처리 -->
			<c:set var="user" value="${sessionScope.loginUser}" />
			<c:if test="${empty user}">
				<c:set var="user" value="${sessionScope.member}" />
			</c:if>

			<!-- ✅ 로그인 전 -->
			<c:if test="${empty sessionScope.loginMemberId}">
				<a class="nav-link" href="/member/login">로그인</a>
			</c:if>

			<c:if test="${not empty sessionScope.loginMemberId}">
				<a class="nav-link" href="/mypage">마이페이지</a>
				<form action="/member/logout" method="post" class="logout-form">
					<button type="submit" class="nav-link nav-btn">로그아웃</button>
				</form>
			</c:if>

		</div>
	</div>
</header>
