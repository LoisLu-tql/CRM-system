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
    <!--pagination-->
    <link rel="stylesheet" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <title>TestPagination</title>

    <script type="text/javascript">
        $(function () {
            $("#demo_pag1").bs_pagination({
                currentPage:1,
                rowsPerPage:10,
                totalPages:100,
                totalRows:1000,
                visiblePageLinks:5,
                showGoToPage:false,
                showRowsPerPage:true,
                showRowsInfo:false,

                onChangePage: function() {

                }
            });
        })
    </script>

</head>
<body>

<div id="demo_pag1"></div>

</body>
</html>
