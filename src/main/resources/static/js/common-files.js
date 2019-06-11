$(function () {
    if($(".select-item").prop("checked") == true){
        $(".select-all").prop("checked",true);
    }
    $("input[class='select-all']").click(function(){ //全选框勾选
        if($(this).prop("checked")==true){
            $("input[class='select-item']").prop("checked",true);
        }else{
            $("input[class='select-item']").prop("checked",false);
        }
    });

    $("input[class='select-item']").click(function(){
        if($(this).prop("checked")==true){
            var allChecked = true; //是否勾上全选按钮
            $("input[class='select-item']").each(function(){
                if($(this).prop("checked")==false){
                    allChecked = false;
                }
            });
            if(allChecked){
                $("input[class='select-all']").prop("checked",true);
            }else{
                $("input[class='select-all']").prop("checked",false);
            }
        }else{
            $("input[class='select-all']").prop("checked",false); //全选按钮不能勾选
        }
    });

    $(".show-image").click(function () {
        var obj = $(this);
        var width = obj.width;
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: width+"px",
            skin: 'layui-layer-nobg', //没有背景色
            shadeClose: true,
            content: obj.html()
        });
    });
});
function getSelectedIds(){
    var ids = "";
    $(".select-item").each(function () {
        if($(this).prop("checked")==true){
            ids += $(this).val()+",";
        }
    });
    if(ids.length > 1){
        ids = ids.substring(0, ids.length-1);
    }else {
        ids = "";
    }
    return ids;
}

function uploadFile(url) {
    var result=[];
    $.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data: new FormData($('#uploadForm')[0]),
        processData: false,
        contentType: false,
        dataType:"json",
        async:false,
        beforeSend: function(){
            layer.load(1, {shade: [0.5,'#fff']});
        },
        success : function(data) {
            layer.closeAll();
            if (data.retCode == 0) {
                result = data;
            } else {
                layer.msg(data.retMsg);
            }
        }
    });
    return result;
}