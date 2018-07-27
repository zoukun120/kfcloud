
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

            var out1 = result.out1;             //N2含量
            var out1_name = result.out1_name;
            var out1_warn = result.out1_warn;
            var out1_alarm = result.out1_alarm;
            var out1_upper = result.out1_upper;

            var out2 = result.out2;             //N2流量
            var out2_name = result.out2_name;
            var out2_warn = result.out2_warn;
            var out2_alarm = result.out2_alarm;
            var out2_upper = result.out2_upper;

            var out3 = result.out3;             //N2质量指数
            var out3_name = result.out3_name;
            var out3_warn = result.out3_warn;
            var out3_alarm = result.out3_alarm;
            var out3_upper = result.out3_upper;

            var out4 = result.out4;             //N2质量指数
            var out4_name = result.out4_name;
            var out4_warn = result.out4_warn;
            var out4_alarm = result.out4_alarm;
            var out4_upper = result.out4_upper;
            console.log(out1,out2,out3,out4);
            // 指定图表的配置项和数据
            option = {
                tooltip : {
                    formatter: "{a} <br/>{b} : {c}%"
                },
                series: [
                    {
                        name: 'N2质量指数',
                        type: 'gauge',
                        min: 0,                     // 最小值
                        max: out3_upper,                   // 最大值
                        precision: 0,               // 小数精度，默认为0，无小数点
                        splitNumber: 6,             // 分割段数，默认为5
                        splitLine: {
                            length :18,         // 属性length控制线长
                            lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                                width:3,
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },
                        radius: '40%',         //图表尺寸
                        axisLine: {            // 坐标轴线
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color: [[(out3_warn/out3_upper), '#00c738'], [(out3_alarm/out3_upper), '#e6b300'], [1, '#c20011']],
                                width: 6,
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },
                        axisLabel: {            // 坐标轴小标记
                            textStyle: {       // 属性lineStyle控制线条样式
                                fontWeight: 'bolder',
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },
                        axisTick: {            // 坐标轴小标记
                            length :14,        // 属性length控制线长
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color: 'auto',
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },

                        pointer: {//指针属性
                            shadowColor : '#ffffff', //默认透明
                            shadowBlur: 5,
                            length:'85%',
                            width: '5%',
                            itemStyle:{
                                color: '#000',
                            }
                        },

                        title: {   //仪表盘标题
                            offsetCenter: ['5%', '90%'],
                            textStyle: {
                                 color: '#000',
                                 fontSize: 14,
                            }
                        },
                        detail: {
                            formatter:'{value}',
                            // width: 50,
                            // height:50,
                            offsetCenter: [0, '61%'],       // x, y，单位px
                            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                fontWeight: 'bolder',
                                fontSize: 20,
                            }
                        },
                        data: [{value: 100, name: out3_name}]
                    },

                    {
                        name: 'N2压力刻度',
                        type: 'gauge',
                        min: 0,                     // 最小值
                        max: out4_upper,                   // 最大值
                        precision: 0,             // 小数精度，默认为0，无小数点
                        splitNumber: 5,          // 分割段数，默认为5
                        startAngle:235,
                        endAngle: 155,
                        splitLine: {
                            length :25,           // 属性length控制线长
                            lineStyle: {         // 属性lineStyle（详见lineStyle）控制线条样式
                                width:3,
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        radius: '83%',         //图表尺寸
                        axisLine: {            // 坐标轴线
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color: [[(out4_warn/out4_upper), '#00c738'], [(out4_alarm/out4_upper), '#e6b300'], [1, '#c20011']],
                                width: 2,
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },
                        axisLabel: {// 坐标轴小标记
                            textStyle: {       // 属性lineStyle控制线条样式
                                fontWeight: 'bolder',
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        axisTick: {            // 坐标轴小标记
                            length :25,        // 属性length控制线长
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color:'black',
                                // color: 'auto',
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },

                        pointer: {//指针属性
                            show: false,
                            shadowColor : '#fff', //默认透明
                            shadowBlur: 5,
                            length:'85%',
                            width: '4%',
                        },

                        title: {   //仪表盘标题
                            show: false,
                            offsetCenter: ['-60%', '103%'],
                            textStyle: {
                                color: '#000',
                            }
                        },
                        detail: {
                            show: false,
                            formatter:'{value}',
                            // width: 100,
                            // height:100,
                            offsetCenter: ["-90%", '87%'],       // x, y，单位px
                            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                fontWeight: 'bolder',
                                fontSize: 20,
                            }
                        },
                        data: [{value: 4, name: 'N2含量(ppm)'}]
                    },

                    {
                        name: 'N2压力',
                        type: 'gauge',
                        precision: 0,       // 小数精度，默认为0，无小数点
                        min:0,
                        max:out4_upper,
                        splitNumber: 5,          // 分割段数，默认为5
                        startAngle:234,//开始角度
                        endAngle: 156,
                        splitLine: {              //分隔线
                            show: false,
                            length :15,           // 属性length控制线长
                            lineStyle: {          // 属性lineStyle（详见lineStyle）控制线条样式
                                width:3,
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        radius: '80.5%',         //图表尺寸
                        axisLine: {            // 坐标轴线
                            lineStyle: {       // 属性lineStyle控制线条样式
                                width: 16,
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        axisLabel: {            // 坐标轴小标记
                            show: false,
                            textStyle: {        // 属性lineStyle控制线条样式
                                fontWeight: 'bolder',
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        axisTick: {         // 坐标轴小标记
                            show: false,
                            length :15,        // 属性length控制线长
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color: 'auto',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        pointer: {//指针属性
                            show: false,
                            shadowColor : '#fff', //默认透明
                            shadowBlur: 5,
                            length:'85%',
                            width: '4%',
                        },
                        title: {
                            offsetCenter: ['-60%', '103%'],
                            textStyle: {
                                color: '#000',
                                fontSize: 14,
                            }
                        },
                        detail: {
                            formatter:'{value}',
                            // width: 50,
                            // height:50,
                            offsetCenter: ["-90%", '87%'],       // x, y，单位px
                            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                fontWeight: 'bolder',
                                fontSize: 20,
                            }
                        },
                        data: [{value: 5, name: out4_name}]
                    },

                    {
                        name: 'N2流量刻度',
                        type: 'gauge',
                        min: out2_upper,                     // 最小值
                        max: 0,                   // 最大值
                        precision: 0,             // 小数精度，默认为0，无小数点
                        splitNumber: 5, // 分割段数，默认为5
                        startAngle:-55,//开始角度
                        endAngle: 25,
                        clockwise: false, //逆时针
                        splitLine: {
                            length :25,           // 属性length控制线长
                            lineStyle: {         // 属性lineStyle（详见lineStyle）控制线条样式
                                width:3,
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        radius: '83%',         //图表尺寸

                        axisTick: {            // 坐标轴小标记
                            length :25,        // 属性length控制线长
                            lineStyle: {       // 属性lineStyle控制线条样式
                                clockwise: false,
                                color:'black',
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },

                        axisLine: {            // 坐标轴线
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color: [[(out2_warn/out2_upper), '#00c738'], [(out2_alarm/out2_upper), '#e6b300'], [1, '#c20011']],
                                width: 2,
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10,
                                clockwise: false,
                            }
                        },
                        axisLabel: {// 坐标轴小标记
                            textStyle: {       // 属性lineStyle控制线条样式
                                fontWeight: 'bolder',
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },


                        pointer: {//指针属性
                            show: false,
                            shadowColor : '#fff', //默认透明
                            shadowBlur: 5,
                            length:'85%',
                            width: '4%',
                        },

                        title: {   //仪表盘标题
                            show: false,
                            offsetCenter: ['60%', '103%'],
                            textStyle: {
                                color: '#000',
                            }
                        },
                        detail: {
                            show: false,
                            formatter:'{value}',
                            // width: 50,
                            // height:50,
                            offsetCenter: ["90%", '87%'],       // x, y，单位px
                            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                fontWeight: 'bolder',
                                fontSize: 20,
                            }
                        },
                        data: [{value: 4, name: 'N2含量(ppm)'}]
                    },

                    {
                        name: 'N2流量',
                        type: 'gauge',
                        precision: 0,           // 小数精度，默认为0，无小数点
                        min:0,
                        max:out2_upper,
                        splitNumber: 5,          // 分割段数，默认为5
                        startAngle:-54,//开始角度
                        endAngle: 24,
                        clockwise: false, //逆时针
                        splitLine: {              //分隔线
                            show: false,
                            length :15,           // 属性length控制线长
                            lineStyle: {          // 属性lineStyle（详见lineStyle）控制线条样式
                                width:3,
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        radius: '80.5%',         //图表尺寸
                        axisLine: {            // 坐标轴线
                            clockwise: false,
                            lineStyle: {       // 属性lineStyle控制线条样式
                                // color: [[0.3, '#00c738'], [0.5, '#e6b300'], [1, '#c20011']],
                                width: 16,
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        axisLabel: {            // 坐标轴小标记
                            show: false,
                            textStyle: {        // 属性lineStyle控制线条样式
                                fontWeight: 'bolder',
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        axisTick: {         // 坐标轴小标记
                            show: false,
                            length :15,        // 属性length控制线长
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color: 'auto',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        pointer: {//指针属性
                            show: false,
                            shadowColor : '#fff', //默认透明
                            shadowBlur: 5,
                            length:'85%',
                            width: '4%',
                        },
                        title: {
                            offsetCenter: ['60%', '103%'],
                            textStyle: {
                                color: '#000',
                                fontSize: 14,
                            }
                        },
                        detail: {
                            formatter:'{value}',
                            // width: 50,
                            // height:50,
                            offsetCenter: ["90%", '87%'],       // x, y，单位px
                            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                fontWeight: 'bolder',
                                fontSize: 20,
                            }
                        },
                        data: [{value: 400, name: out2_name}]
                    },

                    {
                        name: 'N2含量刻度',
                        type: 'gauge',
                        min: 0,                     // 最小值
                        max: out1_upper,                   // 最大值
                        precision: 0,             // 小数精度，默认为0，无小数点
                        splitNumber: 5,          // 分割段数，默认为5
                        startAngle:140,
                        endAngle: 40,
                        splitLine: {
                            length :25,           // 属性length控制线长
                            lineStyle: {         // 属性lineStyle（详见lineStyle）控制线条样式
                                width:3,
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        radius: '82.5%',         //图表尺寸
                        axisLine: {            // 坐标轴线
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color: [[(out1_warn/out1_upper), '#00c738'], [(out1_alarm/out1_upper), '#e6b300'], [1, '#c20011']],
                                width: 2,
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },
                        axisLabel: {// 坐标轴小标记
                            textStyle: {       // 属性lineStyle控制线条样式
                                fontWeight: 'bolder',
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        axisTick: {            // 坐标轴小标记
                            length :25,        // 属性length控制线长
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color:'black',
                                // color: 'auto',
                                shadowColor : '#fff', //默认透明
                                shadowBlur: 10
                            }
                        },

                        pointer: {//指针属性
                            show: false,
                            shadowColor : '#fff', //默认透明
                            shadowBlur: 5,
                            length:'85%',
                            width: '4%',
                        },

                        title: {   //仪表盘标题
                            show: false,
                            offsetCenter: ['0%',  '-115%'],
                            textStyle: {
                                color: '#000',
                            }
                        },
                        detail: {
                            show: false,
                            formatter:'{value}',
                            // width: 50,
                            // height:50,
                            offsetCenter: ["70%", '-100%'],       // x, y，单位px
                            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                fontWeight: 'bolder',
                                fontSize: 20,
                            }
                        },
                        data: [{value: 4, name: 'N2含量(ppm)'}]
                    },

                    {
                        name: 'N2含量',
                        type: 'gauge',
                        precision: 0,       // 小数精度，默认为0，无小数点
                        min:0,
                        max:out1_upper,
                        splitNumber: 5,          // 分割段数，默认为5
                        startAngle:139,//开始角度
                        endAngle: 41,
                        splitLine: {              //分隔线
                            show: false,
                            length :15,           // 属性length控制线长
                            lineStyle: {          // 属性lineStyle（详见lineStyle）控制线条样式
                                width:3,
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        radius: '80%',         //图表尺寸
                        axisLine: {            // 坐标轴线
                            lineStyle: {       // 属性lineStyle控制线条样式
                                width: 16,
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        axisLabel: {            // 坐标轴小标记
                            show: false,
                            textStyle: {        // 属性lineStyle控制线条样式
                                fontWeight: 'bolder',
                                color: '#000',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        axisTick: {         // 坐标轴小标记
                            show: false,
                            length :15,        // 属性length控制线长
                            lineStyle: {       // 属性lineStyle控制线条样式
                                color: 'auto',
                                shadowColor : '#fff', //默认透明
                            }
                        },
                        pointer: {//指针属性
                            show: false,
                            shadowColor : '#fff', //默认透明
                            shadowBlur: 5,
                            length:'85%',
                            width: '4%',
                        },
                        title: {
                            offsetCenter: ['0%',  '-115%'],
                            textStyle: {
                                color: '#000',
                                fontSize: 14,
                            }
                        },
                        detail: {
                            formatter:'{value}',
                            // width: 50,
                            // height:50,
                            offsetCenter: ["70%", '-100%'],       // x, y，单位px
                            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                fontWeight: 'bolder',
                                fontSize: 20,
                            }
                        },
                        data: [{value: 3, name: out1_name}]
                    },
                ]
            };

            var myChart = echarts.init(document.getElementById('dashBoard_container'));
            option.series[0].data[0].value = out3;//N2质量指数
            //N2含量
            option.series[6].data[0].value = out1;
            var num1 = (out1/out1_upper);
            var zhi=Number(num1)+0.01;
            if(num1 < (out1_warn/out1_upper))
                    {
                        option.series[6].axisLine.lineStyle.color = [[num1, '#00c738'], [1, '#d1d1d1']];}
                else if ((num1 >= (out1_warn/out1_upper))&&(num1 < (out1_alarm/out1_upper)))
                    {option.series[6].axisLine.lineStyle.color = [[num1,'#e6b300'] ,[1,'#d1d1d1']];}

                    else
                        {option.series[6].axisLine.lineStyle.color = [[num1, '#c20011'],[1,'#d1d1d1']];}
            //N2流量
            option.series[4].data[0].value = out2;
            var num2 = (out2/out2_upper);
              if(num2 < (out2_warn/out2_upper))
             {   num2=num2+0.01;option.series[4].axisLine.lineStyle.color = [[num2, '#00c738'], [1, '#d1d1d1']];}
              else if ((num2 >= (out2_warn/out2_upper))&&(num2 < (out2_alarm/out2_upper)))
                 {option.series[4].axisLine.lineStyle.color = [[num2,'#e6b300'] ,[1,'#d1d1d1']];}
                  else {option.series[4].axisLine.lineStyle.color = [[num2, '#c20011'],[1,'#d1d1d1']];}
            //N2压力
            option.series[2].data[0].value = out4;
            var num3 = (out4/out4_upper);
            if(num3 < (out4_warn/out4_upper))
                   {option.series[2].axisLine.lineStyle.color = [[num3, '#00c738'], [1, '#d1d1d1']];}
                else if ((num3 >= (out4_warn/out4_upper))&&(num3 < (out4_alarm/out4_upper)))
                    {option.series[2].axisLine.lineStyle.color = [[num3,'#e6b300'] ,[1,'#d1d1d1']];}
                    else {option.series[2].axisLine.lineStyle.color = [[num3, '#c20011'],[1,'#d1d1d1']];}

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