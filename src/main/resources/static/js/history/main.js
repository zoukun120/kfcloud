
$(document).ready(function () {
    var theme = "ios";
    var mode = "scroller";
    var display = "bottom";
    var lang="zh";
// Date & Time demo initialization
    $('#start_datetime,#end_datetime').mobiscroll().datetime({
        theme: theme,
        mode: mode,
        display: display,
        lang: lang,
        dateFormat:"yyyy-mm-dd",
        minDate: new Date(2000,3,10,9,22),
        maxDate: new Date(2050,7,30,15,44),
        stepMinute: 1
    });


    /* Widget minimize */

    $('.wminimize').click(function(e){
        e.preventDefault();
        var $wcontent = $(this).parent().parent().next('.widget-content');
        if($wcontent.is(':visible'))
        {
            $(this).children('i').removeClass('icon-chevron-up');
            $(this).children('i').addClass('icon-chevron-down');
        }
        else
        {
            $(this).children('i').removeClass('icon-chevron-down');
            $(this).children('i').addClass('icon-chevron-up');
        }
        $wcontent.toggle(500);
    });

})