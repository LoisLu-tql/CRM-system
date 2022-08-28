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

    <script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>
    <title>EchartsTestPage</title>

    <script type="text/javascript">
        $(function () {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: 'Funnel'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b} : {c}%'
                },
                toolbox: {
                    feature: {
                        dataView: { readOnly: false },
                        restore: {},
                        saveAsImage: {}
                    }
                },
                series: [
                    {
                        name: 'Expected',
                        type: 'funnel',
                        left: '10%',
                        width: '80%',
                        label: {
                            formatter: '{b}Expected'
                        },
                        labelLine: {
                            show: false
                        },
                        itemStyle: {
                            opacity: 0.7
                        },
                        emphasis: {
                            label: {
                                position: 'inside',
                                formatter: '{b}Expected: {c}%'
                            }
                        },
                        data: [
                            { value: 60, name: 'Visit' },
                            { value: 40, name: 'Inquiry' },
                            { value: 20, name: 'Order' },
                            { value: 80, name: 'Click' },
                            { value: 100, name: 'Show' }
                        ]
                    },
                    {
                        name: 'Actual',
                        type: 'funnel',
                        left: '10%',
                        width: '80%',
                        maxSize: '80%',
                        label: {
                            position: 'inside',
                            formatter: '{c}%',
                            color: '#fff'
                        },
                        itemStyle: {
                            opacity: 0.5,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        emphasis: {
                            label: {
                                position: 'inside',
                                formatter: '{b}Actual: {c}%'
                            }
                        },
                        data: [
                            { value: 30, name: 'Visit' },
                            { value: 10, name: 'Inquiry' },
                            { value: 5, name: 'Order' },
                            { value: 50, name: 'Click' },
                            { value: 80, name: 'Show' }
                        ],
                        // Ensure outer shape will not be over inner shape when hover.
                        z: 100
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        });
    </script>

</head>
<body>
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
