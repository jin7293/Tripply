<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>트리플리,Tripply</title>
<!-- 화면 뼈대 설정용 css -->
<link rel="stylesheet" href="/resources/css/common-style.css">
</head>
<body>
<!-- 헤더-메뉴바 -->
	<div id="header">
		<jsp:include page="/WEB-INF/views/common/menuBar.jsp"></jsp:include>
	</div>
<!-- 컨텐츠 -->
	<div id="contents">
<!-- 사이드바	 -->
		<div id="sideBar" style="float:left">
			<div class="my-side">
				<div class="my-side-bar" onclick="location.href='#';">회원정보수정</div>
				<div class="my-side-bar" onclick="location.href='/free/list.kh';">작성글</div>
				<div class="my-side-bar">북마크</div>
				<div class="my-side-bar" onclick="pointNavi(this);">포인트관리</div>
<!-- 사이드바 아코디언			 -->
				<div id="point-navi" style="display:block;">
					<ul>
						<li onclick="location.href='/point/chargeView.kh';">포인트 충전</li>
						<li onclick="location.href='/point/historyView.kh';">포인트 내역확인</li>
						<li onclick="location.href='/point/send.kh';">포인트 전송</li>
					</ul>
				</div>
			</div>
		</div>
<!-- 본 컨텐츠 -->
		<div id="point-history-area" style="float:left">
			<h1>포인트 내역확인</h1><hr>
			<div></div>
			<table align="center" border="1px" width="500px">
				<tr>
					<th>날짜</th>
					<th>금액</th>
					<th>메시지</th>
				</tr>
				<c:forEach items="${pList }" var="point" varStatus="n">
					<tr>
						<td>${point.pCreateDate}</td>
						<td>${point.pointAmount }</td>
						<c:if test="${point.pointWorkType eq 'C' }">
							<td>
								포인트를 충전하였습니다.
							</td>
						</c:if>
						<c:if test="${point.pointWorkType eq 'R' }">
							<td>
								${point.pointFromUser }님에게 포인트를 받았습니다.
							</td>
						</c:if>
						<c:if test="${point.pointWorkType eq 'S' }">
							<td>
								${point.pointToUser }님에게 포인트를 보냈습니다.
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
<!-- 푸터 -->
	<div id="footer"></div>
</body>
<script>

//사이드 바 포인트관리 아코디언
	function pointNavi(target){
		var pointNaviDiv = target.nextElementSibling;
		var display = pointNaviDiv.style.display;
		if(display == 'none'){
			pointNaviDiv.style.display="block";
		}else{
			pointNaviDiv.style.display="none";
		}
	}
</script>
</html>