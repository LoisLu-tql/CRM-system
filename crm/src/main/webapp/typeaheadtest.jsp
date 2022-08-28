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

    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <title>TypeAheadTestPage</title>

    <script type="text/javascript">
        $(function () {
           $("#customerName").typeahead({
                // source:['Shopee', 'Alibaba', 'Baidu', 'Taobao', 'Meituan']
                source:function (jquery, process) {
                    $.ajax({
                        url:'workbench/transaction/queryCustomerNameByName.do',
                        data:{
                            customerName:jquery
                        },
                        type:'post',
                        dataType:'json',
                        success:function (data) {
                            process(data);
                        }
                    });
                }
           });
        });
    </script>
</head>
<body>
<input type="text" id="customerName">
</body>
</html>
