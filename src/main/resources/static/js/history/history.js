
$(document).ready(function () {
    // $("#nav").hover(function(){
    //     var attrValue = $('#nav i').attr('class');
    //     console.log(attrValue)
    //     if (attrValue.indexOf("up")!= -1){
    //         $('#nav i').attr('class','glyphicon glyphicon-chevron-down');
    //     } else {
    //         $('#nav i').attr('class','glyphicon glyphicon-chevron-up');
    //     }
    // });
    $("#nav").click(function(){
        loadSysName();
    });
})

function loadSysName() {
    var factoryId = 8;
    $.ajax({
        url : '/history/'+factoryId,
        type : "get",
        success : function(factories) {
                console.log(factories)
                $('.submenu').empty();
                // 遍历数据，构造html，点击发送请求
                for (var i=0;i<factories.length;i++){
                    var li = '';
                    li += '<li>'
                        + '<a href="#">' + factories[i].systemName + '</a>'
                        + '</li>';
                    console.log(li)
                    $('.submenu').append(li);
                }
        }
    })
}