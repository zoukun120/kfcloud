
/**
 * 1.1、页面加载就画仪表盘
 */
$(function(){
    var factoryId = $("#factoryId").html();
    var jsonObj = {
        "factoryId" : factoryId
    };
    getDashBoardData(jsonObj);
})


/*
 * 2、发送post请求，从服务器拿到仪表盘数据
 */
function getDashBoardData(jsonObj) {
    var url = "/anal/OperateCondition/board";
    // 基于准备好的dom，初始化echarts实例
    $("#dashBoard_container").empty();
    // var myChart = echarts.init(document.getElementById('cruve_container'));
    // 先执行一次，以后每20s更新一次仪表盘数据
    printDashBoard(url,jsonObj);

    window.setInterval(function () {
        printDashBoard(url,jsonObj);
    },10000);

}

/**
 * 3 画仪表盘
 */
function printDashBoard(url,jsonObj){
    $.ajax({ //设置同步，让函数返回ajax的获取的数据
        url : url,
        type : "POST",
        dataType : "json",
        async : false,
        data : JSON.stringify(jsonObj),
        contentType : "application/json",
        success : function(result) {
            console.log(result);
            var time = result.TIME;
            $("#dashBoard_time").html(time);
            var dash_value = result.out01;
            // 指定图表的配置项和数据
            option = {
                tooltip : {
                    formatter: "{a} <br/>{b} : {c}%"
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
                        detail: {formatter:'{value}'},
                        data: [{value: 100, name: 'N2质量状态指数'}]
                    }
                ]
            };
            option.series[0].data[0].value = dash_value;
            var myChart = echarts.init(document.getElementById('dashBoard_container'));
            myChart.setOption(option, true);
        },
        error : function() {
            alert('服务器正忙，请稍后再试...')
        }
    });

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