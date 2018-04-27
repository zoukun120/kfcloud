/**
 * 1、日期选择器
 */
layui.use(['laydate','table'], function(){
    // 1 日期选择器
    var date = layui.laydate;
    date.render({
        elem: '#test1', //指定元素
        value:yestoday()
    });
});

/**
 * 点击事件选择器的确定按钮
 */
$("#confirm").click(function(){
    var dateStart = $("#test1").val()+' 00:00:00';
    var dateEnd = getEndTime(dateStart);
    var dateFrist = addTime(dateEnd);
    sendAjax(dateStart, dateEnd)
});

/*
 * 发送post请求，从服务器拿数据
 */
function sendAjax(dateStart, dateEnd) {
    var factoryId = 8;
    var dateFrist = addTime(dateEnd);
    var jsonObj = {
        "factoryId" : factoryId,
        "dateStart" : dateStart,
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
            console.log(result);
            // 2 表格
            var table = layui.table;
            var data = [
                {field: 'groupId', fixed: 'left'}
                ,{field: 'out01', title: '合格率'}
                , {field: 'out02', title: '出塔量'}
                , {field: 'out03', title: '放散量'}
                , {field: 'out04', title: '放散率'}
                , {field: 'out05', title: '利用量'}
                , {field: 'out06', title: '消耗量'}
                , {field: 'out07', title: '单位能耗'}
                , {field: 'out08', title: '停气时间'}
            ];
            table.render({
                elem: '#anal_table',
                cols: [data],
                data: result
            })
        },
        error : function() {
            alert('服务器正忙，请稍后再试...');
        }
    })
}
/**
 * 获取当天凌晨的时间
 * @returns {Date}
 */
function yestoday(){
    var datetime = new Date();
    datetime.setHours(0, 0, 0, 0);
    datetime.setDate(datetime.getDate()-1);
    return datetime;
}


function timeStamp2String(time) {// timestamp转datetime
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    var date = datetime.getDate();
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
    return date + "日" + hour + ":" + minute + ":" + second;
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
/*
    昨日结束时间
 */
function getEndTime(startTime) {
    var datetime = new Date(startTime);
    datetime.setHours(0, 0, 0, 0);
    var year = datetime.getFullYear();
    var month = datetime.getMonth()+1;// 月份从0开始,日期+1
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