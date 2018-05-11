
/**
 * 1、日期选择器
 */

$(function(){
    //停止 仪表盘的定时器
    window.clearInterval(intervalTime);
    //页面开始 加载曲线
    var dateStart =timeStamp2String(yestoday())
    var dateEnd = getEndTime(dateStart);
    getHisCruve(dateStart,dateEnd);
})

/**
 * 1.1、点击事件选择器的确定按钮
 */
$("#confirm").click(function(){
    //判断选择过日期
    var dateStart = $("#test1").val();
    if (dateStart==''){
        alert.msg('请选择日期');
    }else {
        dateStart +=' 00:00:00';
        var dateEnd = getEndTime(dateStart);
        <!-- 2 报表功能暂时关闭 -->
        // getDailyTable(dateEnd,dateFrist);
        getHisCruve(dateStart,dateEnd);
    }
});


/*
 * 2、发送post请求，从服务器拿到报表数据
 */
function getDailyTable(dateEnd,dateFrist) {
    var factoryId = $("#factoryId").html();
    var jsonObj = {
        "factoryId" : factoryId,
        "dateEnd" : dateEnd,
        "dateFrist" : dateFrist
    };
    console.log(JSON.stringify(jsonObj));
    var url = "/anal/dailyReport/table";
    $.ajax({
        url : url,
        type : "POST",
        dataType : "json",
        data : JSON.stringify(jsonObj),
        contentType : "application/json",
        success : function(result) {
            // console.log(result);
        },
        error : function() {
            alert('服务器正忙，请稍后再试...')
        }
    })
}

/*
 * 3、发送post请求，从服务器拿到昨日曲线图数据
 */
function getHisCruve(dateStart,dateEnd){
    var factoryId =  $("#factoryId").html();
    var jsonObj = {
        "factoryId" : factoryId,
        "dateStart" : dateStart,
        "dateEnd" : dateEnd
    };
    console.log(JSON.stringify(jsonObj));
    var url = "/anal/dailyReport/line";
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        data: JSON.stringify(jsonObj),
        contentType: "application/json",
        success: function (yDataList) {
            // console.log(yDataList);
            //为画表格准备数据
            var paraMap = yDataList[yDataList.length - 1];
            var paraNum = paraMap.lineNum;
            //console.log('paraNum:'+paraNum);//1

            for (var i = 0; i < paraNum + 3; i++) {//动态创建变量名.arr0,arr1.arr2..,其中arr0用来存放所有的参数名，arr1存单位,arr2用来存TIME，arr4开始存数据
                var varName = 'arr' + i;
                window[varName] = [];
                // console.log('arr' + i);
            }

            //把字段存入arr0
            for ( var k in paraMap) {
                var regExp = new RegExp("lineNum");
                var regExp2 = new RegExp("0");
                if ((!regExp.test(k)) || (!regExp2.test(k))) {
                    var paraName = paraMap[k];
                    arr0.push(paraName);//TI1101
                }
            }

            //把时间存入arr2
            for (var j = 0; j < yDataList.length - 1; j++) {
                var TIME = yDataList[j]['TIME'];
                arr2.push(TIME); //将时间timestamp转Date再加到数组arr2里,因为数组是有序的，所以倒序后，第一个map的时间在最前面，依次索引+1
            }

            //将参数值放入对应数组
            for (var i = 0; i < paraNum; i++) {//动态添加数据到相应数组
                var varName = 'arr' + (i + 3);
                for (var j = 0; j < yDataList.length - 1; j++) {
                    window[varName].push(yDataList[j][arr0[i + 2]]);
                }
            }
            // console.log(arr0);
            // console.log("arr1是单位，我还没传入");
            // console.log(arr2.length);
            // console.log(arr3+','+arr4);

            //开始画表格了
            // 1、基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('cruve_container'));
            // 2、指定图表的配置项和数据
            var option = {
                title: {
                    text: '昨日曲线图',
                    left:'35%',
                    bottom:'2px'
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross'
                    }
                },
                legend: {
                    data:[]
                },
                xAxis: [{
                    // type:'time',
                    name:'时间',
                    data:[]
                }],
                yAxis: [],
                series: []
            };
            // 2.1、将时间push到xAxis中，
            for (var i = 0; i < arr2.length; i++) {
                option.xAxis[0].data.push(timeStamp2String(arr2[i].time));
            }
            // console.log('x轴数据：')
            // console.log(option.xAxis[0].data)
            // 2.2、将数值数组push到yAxis中，
            for (var m = 0; m < paraNum; m++) {//0,1,2,3,多级Y轴---将yAxisObj对象push到charts.yAxis数组中，
                // 2.2.1、y轴显示的方位
                var direction='';
                if (m % 2 == 0) {
                    direction = 'left';
                }else {
                    direction = 'right';
                }

                var yAxisObj = {
                    type:'value',
                    position:direction,
                };
                option.yAxis.push(yAxisObj);
                // 2.2.2、添加图例
                option.legend.data.push(arr0[m + 2]);
                // 2.2.3、添加series配置项数据
                var dataArray = "arr" + (m + 3);
                var seriesObj = {
                    "name" : arr0[m + 2],
                    "type" : "line",
                    "data" : eval(dataArray),
                };
                option.series.push(seriesObj);
            }
            // console.log('y轴数据：')
            // console.log(option.yAxis)
            // console.log('series数据：')
            // console.log(option.series)
            // 3、使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        },
        error: function () {
            alert('服务器正忙，请稍后再试...');
        }
    })
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

/*
    昨日开始时间
 */
function getStartTime() {
    //今日凌晨 年月日
    var datetime = new Date();
    datetime.setHours(0, 0, 0, 0);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    var date = datetime.getDate();
    var hour = datetime.getHours();
    var minute = datetime.getMinutes();
    var second = datetime.getSeconds();
    //获取每个月的天数
    var d = new Date(year,month,0);
    var daysEveryMonth = d.getDate();
    //昨日凌晨
    if((month==1)&&(date==1)){//1月1号的前一天 （每年的12月份都有31天）
        month =12;
        year -=1;
        date =31;
    }else{
        if(date==1){//每月1号的前一天（除1月份）
            month -=1;
            date = daysEveryMonth;
        }
        if(date!=1){//昨日凌晨
            date -=1;
        }
    }
    //统一将 月份、 小时、分、秒  显示成两位数
    if (date < 10) {
        date = '0' + date;
    }
    if (hour < 10) {
        hour = '0' + hour;
    }
    if (minute < 10) {
        minute = '0' + minute;
    }
    if (second < 10) {
        second = '0' + second;
    }
    var todayStartTime = year + "-" + month + "-" + date + " " + hour + ":"
        + minute + ":" + second;
    return todayStartTime;
}

/**
 * 时间戳 转 字符串
 * @param time
 * @returns {string}
 */
function timeStamp2String(time) {// timestamp转datetime
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    var date = datetime.getDate();
    if (month < 10) {
        month = '0' + month;
    }
    if (date < 10) {
        date = '0' + date;
    }
    var hour = datetime.getHours();
    if (hour < 10) {
        hour = '0' + hour;
    }
    var minute = datetime.getMinutes();
    if (minute < 10) {
        minute = '0' + minute;
    }
    var second = datetime.getSeconds();
    if (second < 10) {
        second = '0' + second;
    }
    return year+"-"+month+"-"+date+" "+hour + ":" + minute + ":" + second;
}

/*
    昨日结束时间
 */
function getEndTime(startTime) {
    var datetime = new Date(startTime);
    datetime.setHours(0, 0, 0, 0);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;// 月份从0开始,日期+1
    var date = datetime.getDate();
    //获取每个月的天数
    var d = new Date(year,month,0);
    var daysEveryMonth = d.getDate();
    //如果是12月最后一天 就 进位到下一年
    if(month==12 && date == daysEveryMonth){
        year +=1;
        month =1;
        date  =1;
    }else{
        //每个月的最后一天 就进位到 下个月第一天
        if(date == daysEveryMonth){
            month +=1;
            date  =1;
        }
        else{
            date +=1;
        }
    }
    //统一将 月份、 小时、分、秒  显示成两位数
    if (month < 10) {
        month = '0' + month;
    }
    if (date < 10) {
        date = '0' + date;
    }
    var hour = datetime.getHours();
    if (hour < 10) {
        hour = '0' + hour;
    }
    var minute = datetime.getMinutes();
    if (minute < 10) {
        minute = '0' + minute;
    }
    var second = datetime.getSeconds();
    if (second < 10) {
        second = '0' + second;
    }
    var todayEndTime = year + "-" + month + "-" + date + " " + hour + ":"
        + minute + ":" + second;
    return todayEndTime;
}

/*
    时间+1天
 */
function addTime(time) {
    //获取当前时间的年月日
    var datetime = new Date(time);
    datetime.setHours(0, 0, 0, 0);
    var year = datetime.getFullYear();
    var month = datetime.getMonth()+1;
    var date = datetime.getDate();
    var hour = datetime.getHours();
    var minute = datetime.getMinutes();
    var second = datetime.getSeconds();
    //获取每个月的天数
    var d = new Date(year,month,0);
    var daysEveryMonth = d.getDate();
    //年末
    if(month==12 && date==31){
        year +=1;
        month =1;
        date  =1;
    }else{
        if(date == daysEveryMonth){//每个月的最后一天（除12月）
            month +=1;
            date  =1;
        }
        else{
            date +=1;
        }
    }
    //统一将 月份、 小时、分、秒  显示成两位数
    if (date < 10) {
        date = '0' + date;
    }
    if (hour < 10) {
        hour = '0' + hour;
    }
    if (minute < 10) {
        minute = '0' + minute;
    }
    if (second < 10) {
        second = '0' + second;
    }
    return year + "-" + month + "-" + date + " " + hour + ":" + minute
        + ":" + second;
}