
$(document).ready(function(){

    $("#nav > li > a").on('click',function(e){
        if($(this).parent().hasClass("has_sub")) {
            e.preventDefault();
        }

        if(!$(this).hasClass("subdrop")) {
            // hide any open menus and remove all other classes
            $("#nav li ul").slideUp(350);
            $("#nav li a").removeClass("subdrop");

            // open our new menu and add the open class
            $(this).next("ul").slideDown(350);
            $(this).addClass("subdrop");
        }

        else if($(this).hasClass("subdrop")) {
            $(this).removeClass("subdrop");
            $(this).next("ul").slideUp(350);
        }

    });
    // 将数据库的报警参数读到前端
    if($("#stateValue").html()==="0"){
        $('#toggle-trigger').bootstrapToggle('off');
        $('#demo').mobiscroll().scroller({disabled:true,});//时间设置滚轴不显示
        $('#demo').css('color','#DEDCDC');
    }else{
        $('#toggle-trigger').bootstrapToggle('on');
         mobiscroll ();//时间设置滚轴显示
        $('#demo').css('color','#0a0a0a');
}
    //将前端的开关状态发送请求传给数据库
    $('#toggle-trigger').change(function() {
        var state = $(this).prop('checked');
      if(state==true)
        {   mobiscroll ();//时间设置滚轴显示
            $('#demo').css('color','#0a0a0a');}
        else{
            $('#demo').mobiscroll().scroller({disabled:true,});//时间设置滚轴不显示
            $('#demo').css('color','#DEDCDC');
      }

        var jsonObj = {
            "state" : state,
            "openid" : $("#openid").html()
        };
        $.ajax({
            type: "post",
            url:"/state",
            dataType:"json",
            data:JSON.stringify(jsonObj),
            contentType : "application/json",
            error: function () {
                alert('加载数据失败！');
            },
            success: function(data){
                //TODO
                console.log(data)
            }
        })
    })

    if($("#alarmtimeon").html()==="未设置")
    {
        document.getElementById("demo").value=("时间未设置");
    }
    else {
        document.getElementById("demo").value=$("#alarmtimeon").html();
    }
});

/*
    时间段选择器的设置适用于mobiscroll.custom-3.0.0-beta2.min.js
 */
// $(function () {
//         var hour=[];
//         for(var i=0;i<24;i++){
//             var c=i<10?"0"+i+"时":i+"时";
//             hour.push(c);
//         }
//         var minute=[];
//         for(var i=0;i<60;i++){
//             var c=i<10?"0"+i+"分":i+"分";
//             minute.push(c);
//         }
//     $('#demo').mobiscroll().scroller({
//         theme: 'ios',
//         rows: 5,
//         lang:'zh',
//         wheels: [
//             [{
//                 circular: false,
//                 data: hour,
//                 label: '开始'
//             }, {
//                 circular: false,
//                 data: minute,
//                 label: ' '
//             },{
//                 circular: false,
//                 data: hour,
//                 label: '结束'
//             }, {
//                 circular: false,
//                 data: minute,
//                 label: ' '
//             }]
//         ],
//         showLabel: true,
//         minWidth: 30,
//         cssClass: 'md-daterange',
//         formatValue: function (data) {
//             return data[0].replace("时",":") + data[1].replace("分","")+'--'+data[2].replace("时",":")+data[3].replace("分","")
//         },
//     });
//
//
//
//
//     // triggerCurve();
// });

/*
    时间段选择器的设置适用于mobiscroll.custom.min.js
 */
function mobiscroll ( ) {
    var hour=[];
        for(var i=0;i<24;i++){
            var c=i<10?"0"+i+"时":i+"时";
            hour.push(c);
        }
        var minute=[];
        for(var i=0;i<60;i++){
            var c=i<10?"0"+i+"分":i+"分";
            minute.push(c);
        }
    var wheels=[
        [{values:hour,label:"开始时间"}],
        [{values:minute,label:""}],
        [{values:hour,label:"结束时间"}],
        [{values:minute,label:""}]
        ];
    // var i=wheels[0][0].values[6];
    // var j=wheels[1][0].values[12];
    // var k=wheels[2][0].values[3];
    // var l=wheels[3][0].values[5];

    console.log( $("#alarmtimeon").html());

    // $('#demo').mobiscroll('setValue', [[i], [j],[k],[l]],true);
    // $('#demo').mobiscroll('setValue', $("#alarmtimeon").html(),true);

    $('#demo').mobiscroll().scroller({
        theme:'ios',
        display:'bottom',
        lang:'zh',
        showLabel:true,
        rows:5,
        wheels:wheels,
        minWidth: 30,
        disabled:false,
        // defaultValue:j+k+j+k,
        formatValue: function (data) {
            return data[0].replace("时",":") + data[1].replace("分","")+'--'+data[2].replace("时",":")+data[3].replace("分","")
        },
    });
    triggerCurve();
    }

/*
将修改的报警时间传值到后端存入数据库
 */
function triggerCurve() {
    $("#demo").change(function(){
        var inputValue = document.getElementById("demo").value;
        var jsonObj = {
            "alarmtimeon" : inputValue,
            "openid" : $("#openid").html()
        };
        $.ajax({
            type: "post",
            url:"/alarmtimeon",
            dataType:"json",
            data:JSON.stringify(jsonObj),
            contentType : "application/json",
            error: function () {
                alert('加载数据失败！');
            },
            success: function(data){
                //TODO
                console.log(data)
            }
        })
    });

}


