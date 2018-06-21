var t = null;
jQuery(document).ready(function($){
    $('.widget').hide();
	//trigger the animation - open modal window
	$('[data-type="modal-trigger"]').on('click', function(){
		var actionBtn = $(this),
			scaleValue = retrieveScale(actionBtn.next('.cd-modal-bg'));
		
		actionBtn.addClass('to-circle');
		actionBtn.next('.cd-modal-bg').addClass('is-visible').one('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend', function(){
			animateLayer(actionBtn.next('.cd-modal-bg'), scaleValue, true);
		});

		//if browser doesn't support transitions...
		if(actionBtn.parents('.no-csstransitions').length > 0 ) animateLayer(actionBtn.next('.cd-modal-bg'), scaleValue, true);
		//隐藏表格
		$('.widget').hide('slow');
		//清空定时器
        window.clearInterval(t);
		var factoryId = $("#pp").html();
        sendAjax(factoryId);
	});

	//trigger the animation - close modal window
	$('.cd-section .cd-modal-close').on('click', function(){
		closeModal();
        // $('.widget').hide();//点X，隐藏蓝色框
	});
	$(document).keyup(function(event){
		if(event.which=='27') closeModal();
	});

	$(window).on('resize', function(){
		//on window resize - update cover layer dimention and position
		if($('.cd-section.modal-is-visible').length > 0) window.requestAnimationFrame(updateLayer);
	});

	function retrieveScale(btn) {
		var btnRadius = btn.width()/2,
			left = btn.offset().left + btnRadius,
			top = btn.offset().top + btnRadius - $(window).scrollTop(),
			scale = scaleValue(top, left, btnRadius, $(window).height(), $(window).width());

		btn.css('position', 'fixed').velocity({
			top: top - btnRadius,
			left: left - btnRadius,
			translateX: 0,
		}, 0);

		return scale;
	}

	function scaleValue( topValue, leftValue, radiusValue, windowW, windowH) {
		var maxDistHor = ( leftValue > windowW/2) ? leftValue : (windowW - leftValue),
			maxDistVert = ( topValue > windowH/2) ? topValue : (windowH - topValue);
		return Math.ceil(Math.sqrt( Math.pow(maxDistHor, 2) + Math.pow(maxDistVert, 2) )/radiusValue);
	}

	function animateLayer(layer, scaleVal, bool) {
		layer.velocity({ scale: scaleVal }, 400, function(){
			$('body').toggleClass('overflow-hidden', bool);
			(bool) 
				? layer.parents('.cd-section').addClass('modal-is-visible').end().off('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend')
				: layer.removeClass('is-visible').removeAttr( 'style' ).siblings('[data-type="modal-trigger"]').removeClass('to-circle');
		});
	}

	function updateLayer() {
		var layer = $('.cd-section.modal-is-visible').find('.cd-modal-bg'),
			layerRadius = layer.width()/2,
			layerTop = layer.siblings('.btn').offset().top + layerRadius - $(window).scrollTop(),
			layerLeft = layer.siblings('.btn').offset().left + layerRadius,
			scale = scaleValue(layerTop, layerLeft, layerRadius, $(window).height(), $(window).width());
		
		layer.velocity({
			top: layerTop - layerRadius,
			left: layerLeft - layerRadius,
			scale: scale,
		}, 0);
	}

	function closeModal() {
		// console.log('closemodal')
		var section = $('.cd-section.modal-is-visible');
		section.removeClass('modal-is-visible').one('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend', function(){
			animateLayer(section.find('.cd-modal-bg'), 1, false);
		});
		//if browser doesn't support transitions...
		if(section.parents('.no-csstransitions').length > 0 ) animateLayer(section.find('.cd-modal-bg'), 1, false);
	}
});

/**
 * 点击’导航‘，发送get请求
 */
function sendAjax(factoryId) {
    console.log('factoryId = '+factoryId);
    // var factoryId = 8;
    $.ajax({
        url : '/current/'+factoryId,
        type : "get",
        success : function(factories) {
            $('#sysPanel').empty();
            //    遍历数据，构造html，点击发送请求
            for (var i=0;i<factories.length;i++){
                var systemName = factories[i].systemName;
              	if(systemName!='报警系统'){
                    var modelName = 'tb2_model'+factories[i].modelNum;
                    var modelId = factories[i].modelId;
                    var url = '/current/tb2_model'+factories[i].modelNum+'/'+factories[i].modelId+'/'+factories[i].systemName;
                    var a = '<a class="btn btn-success" onclick="clickSys(\''+url+'\')">'+systemName+'</a>'
                    // console.log(a)
                    $('#sysPanel').append(a);
				}
            }
        }
    })
}

/**
 * 自动触发关闭按钮
 */
function  clickSys(url) {
    $('.widget').show();
    time(url);
    t=setInterval(function(){time(url)},5000);//以后每隔5s发一次请求
}

function time(url) {
    $('.cd-modal-close').trigger('click');
    $.ajax({
        url : url,
        success : function(data) {
            console.log(data);
            //动态创建表格
            var paraMap = data.paraMap;
            var dataMap = data.dataMap;
            $(".pull-center").html("实时数据("+timeStamp2String(dataMap.TIME.time)+")")
            $("#currentdata").empty();
            for(var i=1;i<=paraMap.para_num;i++){
                //得到参数名和参数坐标名
                var para_name = 'para'+i+'_name';
                var tr = null;
                tr +='<tr>'
                    + '<td>'+i+'</td>'
                    + '<td>'+paraMap[para_name]+'</td>'
                    + '<td>'+dataMap[paraMap[para_name]]+'</td>'
                    + '</tr>';
                $('#currentdata').append(tr)
            }
            $('.widget-head,.widget-content').show('slow');
        },
        error : function() {
            alert('服务器正忙，请稍后再试...');
        }
    })
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
