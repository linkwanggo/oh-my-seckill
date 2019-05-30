<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>列表秒杀页</title>
    <%@include file="common/head.jsp" %>
    <%@include file="common/tag.jsp" %>
</head>
<body>
    <!-- 页面显示部分 -->
    <div class="container">
        <div class="panel panel-default">
            <div class="panel-heading text-center">
                <h2>列表秒杀页</h2>
                <div class="panel panel-body">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <td>名称</td>
                                <td>库存</td>
                                <td>开始时间</td>
                                <td>结束时间</td>
                                <td>创建时间</td>
                                <td>详情页</td>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="sk" items="${seckillList}">
                            <tr>
                                <td>${sk.name}</td>
                                <td>${sk.number}</td>
                                <td><fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td><fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td><fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td><a href="/seckill/${sk.seckillId}/detail" class="btn btn-info">link</a></td>
                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
<script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
</body>
</html>