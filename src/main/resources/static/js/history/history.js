/**
 * history.html页面加载时执行
 */
$(document).ready(function () {
    $(".widget-head").hide();
    $("#nav").click(function(){
        // 1 判断是否隐藏 下拉系统菜单选择项
        var attrValue = $('#nav i').attr('class');
            console.log(attrValue)
        if (attrValue.indexOf("up")!= -1){
            $('.dropdown-menu').show();
            $('#nav i').attr('class','icon-chevron-down');
        }
        // 2 隐藏图表
        // $("#container").empty();
        $(".widget-head").hide();
        // 3 加载系统菜单选项
        loadSysName();
    });
    //点击系统名，隐藏下拉菜单
    $(".dropdown-menu").click(function(){
        //判断是否隐藏 下拉系统菜单选择项
        console.log('点击系统名，隐藏下拉菜单')
        $(".widget-head").show();
        var attrValue = $('#nav i').attr('class');
        if (attrValue.indexOf("down")!= -1){
            $('.dropdown-menu').hide();
            $('#nav i').attr('class','icon-chevron-up');
        }

    });

    $(".dropdown-menu li").click(function(){
        //清空图表
        // $("#container").empty();
        $(".widget-head").show();
        // var time_selector = '<div class="widget-head"><div class="pull-left"><div class="section">' +
        //     ' <label>时间:</label> <input type="text" id="start_datetime"/>    -   <input type="text" id="end_datetime"/>' +
        //     '<label> <button id="confirm" class="btn btn-default btn-sm" onclick="triggerCurve()"> 确定 </button></label>' +
        //     '</div></div><div class="clearfix"></div></div>';
        // console.log(time_selector);
        // $("#xxx").append(time_selector);

    });

})

/**
 * 获取系统名称
 */
function loadSysName() {
    var factoryId =  $("#factoryId").html();
    $.ajax({
        url : '/history/'+factoryId,
        type : "get",
        success : function(factories) {
                // console.log(factories)
                $('.dropdown-menu').empty();
                // 遍历数据，构造html，点击发送请求

                var result = "<li id='firstli'><ul>"
                // $('.dropdown-menu').append(prefix);
                for (var i=0;i<factories.length;i++){
                    if (factories[i].systemName!='报警系统') {
                        var li = '';
                        li += '<li>'
                            + '<a onclick="loadHisData(\'tb2_model'+ factories[i].modelNum +'\','+factories[i].modelId+',\''+factories[i].systemName+'\')">' + factories[i].systemName + '</a>'
                            + '</li>';
                        result +=li;
                    }
                }
                result += "</ul></li>";
                // console.log(result)
                $('.dropdown-menu').append(result);
        }
    })
}
var modelName='';
var modelId='';
/**
 * 获取历史数据
 */
function loadHisData(modelName,modelId,systemName) {
    //1 选择系统后才显示图标
    $('#mainContent').show('slow');
    this.modelName = modelName;
    this.modelId = modelId;
    console.log(modelName+','+modelId+','+systemName)//tb2_model1,11,预冷系统
    //2 默认加载一小时数据
    triggerCurve();
}

function triggerCurve() {
    //1 时间选择器 拼接上秒
    var dateStart = $('#start_datetime').val();
    var dateEnd = $('#end_datetime').val();
    if (dateStart != ''){
        dateStart+=':00';
    }
    if (dateEnd != ''){
        dateEnd += ':00';
    }
    // console.log(dateStart)
    // console.log(dateEnd)
    // console.log(modelName)
    // console.log(modelId)
    //2 发送ajax请求，获取历史数据，构造DOM元素，显示图表
    if(dateEnd==""||dateStart <= dateEnd){//输入正常，将输入的开始，结束时间传给服务器
        /* 下面开始全自动化读取数据什么的了 */
        printCharts(modelName,modelId,dateStart,dateEnd);//什么都不说了，千言万语都在这个方法里了！
    }else{
        alert("开始时间不能大于结束时间！");
    }
}
    /*
		使用Highcharts，绘制表格
	*/
function printCharts(modelName,modelId,dateStart,dateEnd){
    /* console.log('dateStart='+dateStart+',dateEnd='+dateEnd); */
    if(dateStart=='' ||dateEnd==''){//初始化开始和结束时间
        start_stamp=new Date(new Date()-60*60*1000).getTime();
        end_stamp=new Date().getTime();
        console.log('默认开始时间='+end_stamp+',结束时间='+end_stamp);
        dateStart=date2String(start_stamp);//一个月前
        dateEnd=date2String(end_stamp);
    }
    var jsonObj = {"modelName":modelName,"modelId":modelId,"dateStart":dateStart,"dateEnd":dateEnd};
    $.ajax({
        url:'/history',
        type:"post",
        data:JSON.stringify(jsonObj),
        dataType : 'json',
        contentType:'application/json; charset=utf-8',
        success:function(result){
            // $("#container").empty();
            // console.log(result)
            //(1)初始化
            var paraMap = result.paraMap;
            var paraNum = paraMap.para_num;//获取当前系统的字段个数，有几个就创建（几+3）个数组
            //console.log('paraNum='+paraNum);
            var historyData = result.hisDataList;
            if(historyData==null){
                alert("当前时间不可查!");
            }
            for(var i=0;i<=paraNum+2;i++){//动态创建变量名.arr0,arr1.arr2..,其中arr0用来存放所有的参数名，arr1存单位,arr2用来存TIME
                var varName = 'arr'+i;
                window[varName]=[];
            }
            //(2)获取所有参数名，存arr0
            for(var k in paraMap){
                var regExp = new RegExp("name");
                if(regExp.test(k)){//如果字段中有name,对应的数据，存入arr0
                    var paraName = paraMap[k];
                    arr0.push(paraName);
                }
                var regExp2 = new RegExp("suffix");//如果字段中有suffix,取出单位值，存入arr1
                if(regExp2.test(k)){
                    var paraName = paraMap[k];
                    //console.log(paraName);
                    if(paraName=='C'){//用C代替摄氏度符号
                        paraName='\u2103';
                    }
                    arr1.push(paraName);
                }
            }
            //(3)获取对应参数的历史数据，存入对应数组
            var hisDataNum = historyData.length;
            var varTime = 'arr'+2;
            for (var j = 0; j < hisDataNum; j++) {
                var TIME = historyData[j]['TIME'];
                window[varTime].push(timeStamp2String(TIME.time)); //将时间timestamp转Date再加到数组arr2里,因为数组是有序的，所以倒序后，第一个map的时间在最前面，依次索引+1
            }

            for (var i = 0; i < paraNum; i++) {//动态添加数据到相应数组
                var varName = 'arr'+(i+3);
                for (var j = 0; j < hisDataNum; j++) {
                    window[varName].push(historyData[j][arr0[i]]);
                }
            }
            //开始画表格了
            option = {
                legend: {
                    type:'scroll',
                    x: 'left', // 'center' | 'left' | {number},
                    y: 'bottom', // 'center' | 'bottom' | {number}
                    data:[]
                },
                xAxis: [{
                    name:'时间',
                    data:[]
                }],
                yAxis: {
                    type: 'value'
                },
                series: []
            };
            option.legend.data = arr0;
            for(var i=0;i<arr2.length;i++){//将时间push到categories数组中，
                option.xAxis[0].data.push(arr2[i]);
            }

            for(var m = 0;m<paraNum;m++){//0,1,2,3,多级Y轴---将yAxisObj对象push到charts.yAxis数组中，
                var dataArray = "arr"+(m+3);
                var seriesObj={"name":arr0[m],"type":"line","data":eval(dataArray)};
                option.series.push(seriesObj);
            }
            // console.log(option)
            var myChart = echarts.init(document.getElementById('container'));
            myChart.setOption(option);
        },
        error:function(){
            alert('请求数据有误！');
        }
    });
}

function timeStamp2String (time){// timestamp转datetime
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    var date = datetime.getDate();
    var hour = datetime.getHours();
    var minute = datetime.getMinutes();
    var second = datetime.getSeconds();
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
    return  date+"/"+hour+":"+minute+":"+second;
};

function date2String(time) {
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    var date = datetime.getDate();
    var hour = datetime.getHours();
    var minute = datetime.getMinutes();
    var second = datetime.getSeconds();
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
    return year + '-' + month + '-' + date+' '+hour+':'+minute+':'+second;
};