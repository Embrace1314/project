$(function () {
    $(".nav_bar_side ul li").click(function(){
        $(".nav_bar_side ul li ul").prev("a").removeClass("nav_bar_menu");
        $("ul", this).prev("a").addClass("nav_bar_menu");
        $(this).children("ul").slideDown("fast");
        $(this).siblings().children("ul").slideUp("fast");
    })
});