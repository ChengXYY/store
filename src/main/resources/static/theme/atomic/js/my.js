$(function () {
   $(".block-thumbnail-1 img").each(function () {
       var height = $(this).height();
       var width = $(this).width();
       var obj = $(this).parent();
       var pWidth = obj.width();
       var pHeigh = obj.height();
       if(obj.hasClass("one-third") || obj.hasClass("one-whole")){
            if(height > width){
                $(this).css("width", "100%");
            }else{
                $(this).css("height", "100%");
            }
       }
       if(obj.hasClass("two-third")){
            if(height*2 > width){
                $(this).css("width", "100%");
            }else{
                $(this).css("height", "100%");
            }
       }
       height = $(this).height();
       width = $(this).width();
       if(height > pHeigh){
           var h = (height - pHeigh)/2;
           $(this).css("margin-top", "-"+h+"px");
       }
       if(width > pWidth){
           var w = (width - pWidth)/2;
           $(this).css("margin-left", "-"+w+"px");
       }
   })
});