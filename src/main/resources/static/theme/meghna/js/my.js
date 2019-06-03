$(function () {
   $(".og-grid .mix a img").each(function () {
       var height = $(this).height();
       var width = $(this).width();
       var obj = $(this).parent();
       var pWidth = obj.width();
       var pHeigh = obj.height();
       var rate = pWidth/pHeigh;

       if(width > height){
           $(this).css("height", "100%");
       }else {
           $(this).css("width", "100%");
       }
   })
});