<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>아이디 찾기</title>
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

<h2>아이디 찾기</h2>

<form action="${pageContext.request.contextPath}/find-id" method="post">
    <div>
        이름:
        <input name="name" value="${name}" required>
    </div>
    <br>

    <div>
        이메일:
        <input name="email" value="${email}" required>
        <button type="submit"
                formaction="${pageContext.request.contextPath}/find-id-email-send"
                formmethod="post"
                formnovalidate>인증번호 받기</button>
    </div>
    <br>

    <div>
        인증번호:
        <input name="emailCode" value="${emailCode}">
        <button type="submit"
                formaction="${pageContext.request.contextPath}/find-id-email-check"
                formmethod="post"
                formnovalidate>인증확인</button>
    </div>
    <br>

    <input type="hidden" name="emailVerified" value="${emailVerified}">
    <input type="hidden" name="verifiedEmail" value="${verifiedEmail}">

    <button type="submit">아이디 찾기</button>
</form>

<%
    String foundId = (String) request.getAttribute("foundId");
    if (foundId != null) {
%>
<hr>
<h3>찾은 아이디: <%= foundId %></h3>
<%
    }
%>

<br>
<a href="${pageContext.request.contextPath}/login">로그인으로 돌아가기</a>

</body>
</html>