<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Insert title here</title>
    <meta charset="UTF-8">
</head>
<body>
<h1> 지역(시/도) 날씨 및 공기질 검색 </h1>
<form action="search" method="post">
<input type="hidden" name="returnType" value="json">
<input type="hidden" name="serviceKey" value="ca808a3f716ac213873d4dcc9b9105dd883d5b674063153011bb1944142b5703">
지역명: <input type="textbox" name="sidoName">
<input type="submit" value="검색">
</form>
</body>

</html>

