<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已解决问题</title>
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<jsp:include page="_teacher.jsp" />
	</div>
	
	<div class="right_col" role="main" style="height:800px">
		<div class="row">
			<div class="col-md-8">
			<!-- <h4>已解决学生的问题</h4> -->
			<ul class="list-inline">
					<li>疑问管理</li>
					<li>/</li>
					<li>解决的方案</li>
				</ul>

				<table class="table">
					<tr>
						<th>问题</th>
						<th>回复</th>
					</tr>

					<c:forEach items="${doubtList }" var="doubt">
						<tr>
							<td>${doubt.studentDoubt }</td>
							<td>
								${doubt.answer }
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>

</body>
</html>