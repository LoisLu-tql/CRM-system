<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">

    <!--jquery-->
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <!--bootstrap-->
    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <!--date time picker-->
    <link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <title>Try_datetimePicker</title>

    <script type="text/javascript">
    $(function () {
        $("#myDate").datetimepicker({
            language:'en',
            format:'yyyy-mm-dd',
            minView:'month',
            autoclose:true,
            initialDate:new Date(),
            todayBtn:true,
            clearBtn:true
        });
    })
    </script>
</head>
<body>
<input type="text" id="myDate" readonly>

</body>
</html>
