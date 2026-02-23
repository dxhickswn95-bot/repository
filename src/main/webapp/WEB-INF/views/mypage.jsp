<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>마이페이지</title>
<link rel="stylesheet" href="/css/common.css" />
<link rel="stylesheet" href="/css/mypage.css" />
<link rel="stylesheet" href="/css/streamers.css" />
<script>
async function unfavOnMyPage(e, form) {
  e.preventDefault();

  const btn = form.querySelector(".fav-btn");
  const streamerName = form.querySelector("input[name='streamerName']").value;

  try {
    btn.disabled = true;

    const res = await fetch("/mypage/favorite/remove", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" },
      body: new URLSearchParams({ streamerName })
    });

    const data = await res.json();

    if (!data.success) {
      if (data.message === "LOGIN_REQUIRED") location.href = "/member/login";
      else alert("좋아요 해제 실패");
      btn.disabled = false;
      return false;
    }

    // ✅ 성공하면 해당 카드 삭제
    const wrap = form.closest(".streamer-card-wrap");
    if (wrap) wrap.remove();

    return false;
  } catch (err) {
    console.error(err);
    alert("통신 오류");
    btn.disabled = false;
    return false;
  }
}
</script>

</head>
<body>
	<%@ include file="/WEB-INF/views/common/common.jsp"%>

	<main class="app-content">
		<div class="container mypage-wrap">

			<!-- 상단 프로필 카드 -->
			<section class="profile-card card">
				<div class="profile-left">
					<div class="avatar">
						<c:choose>
							<c:when test="${not empty member.nickname}">
                ${fn:substring(member.nickname, 0, 1)}
              </c:when>
							<c:otherwise>나</c:otherwise>
						</c:choose>
					</div>

					<div class="profile-text">
						<div class="profile-title">마이페이지</div>
						<div class="profile-sub">
							<c:choose>
								<c:when test="${not empty member.nickname}">
                  ${member.nickname}님, 스트리머 서버 영상 즐겨보세요
                </c:when>
								<c:otherwise>
                  스트리머 서버에서 나의 활동을 확인해보세요
                </c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</section>

			<!-- 내가 쓴 댓글 -->
			<section class="section">
				<div class="section-head">
					<div class="section-title">내가 쓴 댓글</div>
				</div>


				<div class="comment-box card">
					<c:forEach var="cmt" items="${myCommentList}">
						<div class="my-comment-row">
							<div class="my-comment-main">
								<div class="my-comment-content">${cmt.content}</div>

								<div class="my-comment-tags">
									<span class="pill">📌 ${cmt.streamerName}</span> <span
										class="pill pill-blue">💬 ${cmt.videoTitle}</span>
									<c:if test="${cmt.highlight eq true}">
										<span class="pill pill-purple">⭐ 하이라이트</span>
									</c:if>
								</div>
							</div>

							<div class="my-comment-date">${cmt.createdAt}</div>
						</div>
					</c:forEach>

					<!-- 데이터 없을 때 디자인 확인용 -->
					<c:if test="${empty myCommentList}">
						<div class="my-comment-row">
							<div class="my-comment-main">
								<div class="my-comment-content">진짜 충격받았어요… ㄹㅇ 레전드 장면…</div>
								<div class="my-comment-tags">
									<span class="pill">📌 스트리머A</span> <span class="pill pill-blue">💬
										대형 서버 1화</span> <span class="pill pill-purple">⭐ 하이라이트</span>
								</div>
							</div>
							<div class="my-comment-date">2026-01-10</div>
						</div>

						<div class="my-comment-row">
							<div class="my-comment-main">
								<div class="my-comment-content">이 스트리머 방송 진짜… 웃기고 맛있다!</div>
								<div class="my-comment-tags">
									<span class="pill">📌 스트리머B</span> <span class="pill pill-blue">💬
										전설의 순간 모음</span>
								</div>
							</div>
							<div class="my-comment-date">2026-01-10</div>
						</div>

						<div class="my-comment-row">
							<div class="my-comment-main">
								<div class="my-comment-content">하이라이트 대박… 몰입감 장난없음</div>
								<div class="my-comment-tags">
									<span class="pill">📌 스트리머C</span> <span class="pill pill-blue">💬
										경제 시스템 분석</span>
								</div>
							</div>
							<div class="my-comment-date">2026-01-09</div>
						</div>

						<div class="my-comment-row">
							<div class="my-comment-main">
								<div class="my-comment-content">최고… 다음편도 기대됩니다</div>
								<div class="my-comment-tags">
									<span class="pill">📌 스트리머D</span> <span class="pill pill-blue">💬
										전투 발생</span> <span class="pill pill-purple">⭐ 하이라이트</span>
								</div>
							</div>
							<div class="my-comment-date">2026-01-07</div>
						</div>
					</c:if>
				</div>
			</section>

			<!-- ===============================
     내가 좋아하는 스트리머
================================ -->
			<section class="section">
				<div class="section-head">
					<div class="section-title">내가 좋아하는 스트리머</div>
				</div>

				<div class="streamer-list">
					<c:forEach var="s" items="${favoriteStreamerList}">

						<div class="streamer-card-wrap">

							<!-- (선택) 마이페이지에서는 좋아요 해제 버튼만 표시 -->
							<form action="/streamers/favorite/toggle" method="post"
								class="fav-form" onsubmit="return unfavOnMyPage(event, this);">
								<input type="hidden" name="streamerName" value="${s.name}" />
								<button type="submit" class="fav-btn" title="좋아요 해제">♥</button>
							</form>


							<a class="streamer-card card" href="/streamers?name=${s.name}">
								<div class="streamer-thumb">
									<img src="${s.profileImage}" alt="${s.name}"
										onerror="this.src='/images/dummy/streamers/default.jpg';" />
								</div>
								<div class="streamer-name">${s.name}</div>
							</a>

						</div>
					</c:forEach>

					<!-- 좋아요한 스트리머가 없을 때 -->
					<c:if test="${empty favoriteStreamerList}">
						<div class="empty-state card"
							style="padding: 20px; text-align: center;">
							아직 좋아요한 스트리머가 없어요 🙂<br /> 스트리머 목록에서 ♥ 버튼을 눌러 추가해보세요.
						</div>
					</c:if>
				</div>
			</section>


		</div>
	</main>
</body>
</html>
