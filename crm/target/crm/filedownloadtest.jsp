<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <title>Title</title>
    <script type="text/javascript">
        $(function () {
            $("#fileDownloadBtn").click(function () {
               // all file download request can only send synchronized request
                // the file is hard to be parsed by ajax
                window.location.href="workbench/activity/fileDownload.do";
            });

        });
    </script>
</head>
<body>
<input type="button" value="Download" id="fileDownloadBtn">
</body>
</html>
