<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>지역 날씨 및 공기질 검색</title>
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

        .search-card {
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
            background: #eff6ff;
            border-radius: 16px;
            font-size: 30px;
            margin-bottom: 20px;
        }

        h1 {
            font-size: 24px;
            font-weight: 700;
            color: #0f172a;
            margin-bottom: 8px;
        }

        p.subtitle {
            color: var(--text-sub);
            font-size: 14px;
            margin-bottom: 28px;
        }

        .form-group {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .input-text {
            flex: 1;
            padding: 13px 18px;
            border: 1px solid var(--border-color);
            border-radius: 12px;
            font-size: 15px;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .input-text:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
        }

        .btn-submit {
            background: var(--primary);
            color: #ffffff;
            border: none;
            border-radius: 12px;
            padding: 13px 24px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s, transform 0.1s;
            white-space: nowrap;
        }

        .btn-submit:hover {
            background: var(--primary-hover);
            transform: translateY(-1px);
        }

        .quick-tags {
            text-align: left;
            margin-top: 10px;
        }

        .quick-label {
            font-size: 13px;
            color: var(--text-sub);
            margin-bottom: 8px;
            display: block;
        }

        .tags-container {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
        }

        .tag-btn {
            background: #f1f5f9;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            padding: 6px 12px;
            font-size: 13px;
            color: #475569;
            cursor: pointer;
            transition: all 0.15s;
        }

        .tag-btn:hover {
            background: #e2e8f0;
            color: #0f172a;
            border-color: #cbd5e1;
        }
    </style>
</head>
<body>
<div class="search-card">
    <div class="icon-badge">🌤️</div>
    <h1>지역 날씨 및 공기질 검색</h1>
    <p class="subtitle">시/도명을 입력하여 실시간 미세먼지와 날씨를 확인하세요</p>

    <form action="search" method="post">
        <input type="hidden" name="returnType" value="json">
        <input type="hidden" name="serviceKey" value="***">
        
        <div class="form-group">
            <input type="text" name="sidoName" id="sidoInput" class="input-text" placeholder="예: 인천, 서울, 경기" required autofocus>
            <input type="submit" value="검색" class="btn-submit">
        </div>
    </form>

    <div class="quick-tags">
        <span class="quick-label">빠른 선택:</span>
        <div class="tags-container">
            <button type="button" class="tag-btn" onclick="selectRegion('인천')">인천</button>
            <button type="button" class="tag-btn" onclick="selectRegion('서울')">서울</button>
            <button type="button" class="tag-btn" onclick="selectRegion('경기')">경기</button>
            <button type="button" class="tag-btn" onclick="selectRegion('부산')">부산</button>
            <button type="button" class="tag-btn" onclick="selectRegion('대구')">대구</button>
            <button type="button" class="tag-btn" onclick="selectRegion('제주')">제주</button>
        </div>
    </div>
</div>

<script>
function selectRegion(name) {
    document.getElementById("sidoInput").value = name;
    document.forms[0].submit();
}
</script>
</body>
</html>
