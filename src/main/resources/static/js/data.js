$(function () {
    $(".container-fluid .content-block").each(function () {
        var type = $(this).data("type");
        var code = $(this).attr("code");
        var blockId = $(this).attr("block-id");
        switch (type){
            case "text":
                getArticle(code, blockId);
                break;
            case "picture":
                getPicture(code, blockId);
                break;
        }

    });
});

function getArticle(code, blockid) {
    var obj = $(".content-block[block-id='"+blockid+"']");
    $.ajax({
        type:"post",
        data:{
            code:code
        },
        dataType:"json",
        url:"/article/get",
        success:function (data) {
            if(data.code == 0){
                obj.html(data.content);
                obj.attr("title", data.title);
            }  else {
                obj.html("<h3 class='text text-muted center'>"+data.msg+"</h3>");
            }
        }
    });
}

function getPicture(code, blockid) {
    var obj = $(".content-block[block-id='"+blockid+"']");
    $.ajax({
        type:"post",
        data:{
            code:code
        },
        dataType:"json",
        url:"/picture/get",
        success:function (data) {
            if(data.code == 0){
                obj.html("<img class='image' src='"+data.url+"' title='"+data.intro+"'/>");
            }  else {
                obj.html("<h3 class='text text-muted center'>"+data.msg+"</h3>");
            }
        }
    });
}