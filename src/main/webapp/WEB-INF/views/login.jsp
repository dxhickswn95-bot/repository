<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>로그인</title>
<link rel="stylesheet" href="/css/common.css" />
<link rel="stylesheet" href="/css/login.css" />
</head>
<body>
	<%@ include file="/WEB-INF/views/common/common.jsp"%>

	<main class="app-content">
		<div class="container login-wrap">
			<div class="login-card card">
				<h1 class="login-title">로그인</h1>
				<div class="login-sub">계정에 로그인하여 영상을 시청하세요</div>

				<!-- 카카오 OAuth 시작 URL로 연결 -->
				<a class="kakao-btn" href="/member/kakao/login"> 카카오 로그인 </a>

			</div>
		</div>
	</main>
</body>
</html>
