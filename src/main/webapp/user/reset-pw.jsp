<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>비밀번호 재설정</title>
</head>
<body>

<%
  String msg = (String) request.getAttribute("msg");
  if (msg != null) {
%>
<script>
  alert("<%= msg %>");
</script>
<%
  }
%>

<h2>새 비밀번호 설정</h2>

<form action="${pageContext.request.contextPath}/reset-pw" method="post">
  <input type="hidden" name="id" value="${id}">
  <input type="hidden" name="name" value="${name}">
  <input type="hidden" name="email" value="${email}">

  <div>
    새 비밀번호:
    <input type="password" name="newPw" required>
  </div>
  <br>

  <div>
    새 비밀번호 확인:
    <input type="password" name="newPwChk" required>
  </div>
  <br>

  <button type="submit">비밀번호 변경</button>
</form>

</body>
</html>