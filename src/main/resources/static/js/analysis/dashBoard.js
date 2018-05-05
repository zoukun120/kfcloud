/**
 * 1、日期选择器
 */
layui.use(['laydate','table','layer'], function(){
    // 1 日期选择器
    var date = layui.laydate;
    date.render({
        elem: '#test1', //指定元素
        value:yestoday()
    });
});

/**
 * 1.1、页面加载就画仪表盘
 */
$(function(){
    // 先拿数据
    printDashBoard();
})


/**
 * 2 画仪表盘
 */
function printDashBoard(){
    // 1、基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('cruve_container'));
    // 2、指定图表的配置项和数据
    option = {
        tooltip : {
            formatter: "{a} <br/>{b} : {c}%"
        },
        toolbox: {
            feature: {
                restore: {},
                saveAsImage: {}
            }
        },
        series: [
            {
                name: '业务指标',
                type: 'gauge',
                min: 0,                     // 最小值
                max: 150,                   // 最大值
                precision: 0,               // 小数精度，默认为0，无小数点
                splitNumber: 5,             // 分割段数，默认为5
                axisLine: {            // 坐标轴线
                    lineStyle: {       // 属性lineStyle控制线条样式
                        color: [[0.667, '#91c7ae'], [0.734, '#E1E685'], [1, '#c23531']]
                    }
                },
                detail: {formatter:'{value}%'},
                data: [{value: 100, name: 'N2质量状态指数'}]
            }
        ]
    };

    //第一次显示dashboard
    option.series[0].data[0].value = getDashBoardData();
    myChart.setOption(option, true);

    // 以后每20s更新一次仪表盘数据
    setInterval(function () {
        option.series[0].data[0].value = getDashBoardData();
        myChart.setOption(option, true);
    },20000);
}

/*
 * 3、发送post请求，从服务器拿到仪表盘数据
 */
function getDashBoardData() {
    var factoryId = $("#factoryId").html();
    var jsonObj = {
        "factoryId" : factoryId
    };
    // console.log(JSON.stringify(jsonObj));
    var dash_value = 0;
    var url = "/anal/OperateCondition/board";
    $.ajax({ //设置同步，让函数返回ajax的获取的数据
        url : url,
        type : "POST",
        dataType : "json",
        async : false,
        data : JSON.stringify(jsonObj),
        contentType : "application/json",
        success : function(result) {
            dash_value = result;
        },
        error : function() {
            var layer = layui.layer;
            layer.msg('服务器正忙，请稍后再试...')
        }
    });
    return dash_value;
}


/**
 * 5、获取当天凌晨的时间
 * @returns {Date}
 */
function yestoday(){
    var datetime = new Date();
    datetime.setHours(0, 0, 0, 0);
    datetime.setDate(datetime.getDate()-1);
    return datetime;
}