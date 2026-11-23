<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>오류 발생 - 지역 날씨 및 공기질 안내</title>
    <style>
        :root {
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --bg-color: #f8fafc;
            --card-bg: #ffffff;
            --border-color: #e2e8f0;
            --text-main: #1e293b;
            --text-sub: #64748b;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Noto Sans KR", sans-serif;
            background-color: var(--bg-color);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .error-card {
            background: var(--card-bg);
            border: 1px solid var(--border-color);
            border-radius: 20px;
            padding: 40px 36px;
            width: 100%;
            max-width: 480px;
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05), 0 8px 10px -6px rgba(0, 0, 0, 0.02);
            text-align: center;
        }

        .icon-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 64px;
            height: 64px;
            background: #fef2f2;
            border-radius: 16px;
            font-size: 30px;
            margin-bottom: 20px;
        }

        .status-code {
            font-size: 14px;
            font-weight: 700;
            color: #ef4444;
            letter-spacing: 0.05em;
            margin-bottom: 6px;
        }

        h1 {
            font-size: 22px;
            font-weight: 700;
            color: #0f172a;
            margin-bottom: 8px;
        }

        p.subtitle {
            color: var(--text-sub);
            font-size: 14px;
            line-height: 1.5;
            margin-bottom: 24px;
        }

        .error-detail-box {
            background: #f8fafc;
            border: 1px solid var(--border-color);
            border-radius: 10px;
            padding: 12px 16px;
            font-size: 13px;
            color: #64748b;
            text-align: left;
            word-break: break-all;
            margin-bottom: 28px;
        }

        .actions {
            display: flex;
            gap: 10px;
            justify-content: center;
        }

        .btn {
            display: inline-block;
            padding: 12px 20px;
            border-radius: 10px;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.2s;
            cursor: pointer;
        }

        .btn-primary {
            background: var(--primary);
            color: #ffffff;
            border: none;
        }

        .btn-primary:hover {
            background: var(--primary-hover);
        }

        .btn-secondary {
            background: #f1f5f9;
            color: #334155;
            border: 1px solid #cbd5e1;
        }

        .btn-secondary:hover {
            background: #e2e8f0;
            color: #0f172a;
        }
    </style>
</head>
<body>
<div class="error-card">
    <div class="icon-badge">⚠️</div>

    <c:choose>
        <c:when test="${pageContext.errorData.statusCode == 404}">
            <div class="status-code">404 NOT FOUND</div>
            <h1>페이지를 찾을 수 없습니다</h1>
            <p class="subtitle">요청하신 페이지가 존재하지 않거나 주소가 변경되었습니다.</p>
        </c:when>
        <c:otherwise>
            <div class="status-code">ERROR <c:out value="${pageContext.errorData.statusCode}" default="500"/></div>
            <h1>요청 처리 중 문제가 발생했습니다</h1>
            <p class="subtitle">서비스 이용 중 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.</p>
        </c:otherwise>
    </c:choose>

    <c:if test="${not empty pageContext.exception.message}">
        <div class="error-detail-box">
            <c:out value="${pageContext.exception.message}"/>
        </div>
    </c:if>

    <div class="actions">
        <a href="index.jsp" class="btn btn-primary">메인으로 이동</a>
        <a href="javascript:history.back()" class="btn btn-secondary">이전 페이지</a>
    </div>
</div>
</body>
</html>
