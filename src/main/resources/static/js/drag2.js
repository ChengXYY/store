$(function () {
    $(".blocks").draggable({
        revers: "invalid",
        helper: "clone",
        connectToSortable: "div[region-id]",
        start: function (event, ui) {
            var block = ui.helper;
            block.removeClass("btn btn-default");
            var max = maxBlockId();
            switch (block.data("type")) {
                case "text":
                    block.attr("block-id", ++max);
                    break;
                case "picture":
                    block.attr("block-id", ++max);
                    break;
            }
        },
        stop: function (event, ui) {
            var block = ui.helper;
            var target = block.parent();
            if(target.attr("region-id")==null || target.attr("region-id")==undefined){
                return;
            }
            var blockId = block.attr("block-id");
            switch (block.data("type")) {
                case "text":
                    showTextBox(blockId, "");
                    break;
                case "picture":
                    showPictureBox(blockId, "");
                    break;
            }
            //样式修改
            block.removeAttr("style").removeClass("ui-draggable ui-draggable-handle").removeClass("blocks").addClass("content-block");
            //添加相关数据
            html = createED();
            block.prepend(html);
        }
    });

    //添加
    $("#textBox .submit").click(function () {
        var code = $("#textBox> input[name='articleCode']").val();
        var blockId = $("#textBox> input[name='blockId']").val();
        if($("div[block-id='"+blockId+"']").data("type") != "text"){
            layer.msg("Error！Please retry！");
            return;
        }
        $("div[block-id='"+blockId+"']").data("code", code);
        html = "<div class='block-title'>"+code+"</div>";
        $("div[block-id='"+blockId+"']").prepend(html);
        layer.closeAll();
    });
    $("#pictureBox .submit").click(function () {
        var code = $("#pictureBox> input[name='pictureCode']").val();
        var blockId = $("#pictureBox> input[name='blockId']").val();
        if($("div[block-id='"+blockId+"']").data("type") != "picture"){
            layer.msg("Error！Please retry！");
            return;
        }
        $("div[block-id='"+blockId+"']").data("code", code);
        html = createT(code);
        $("div[block-id='"+blockId+"']").prepend(html);
        layer.closeAll();
    });

    $("#init").on("click", ".block-edit", function () {
        var type = $(this).parent().data("type");
        var code = $(this).parent().data("code");console.log(code);
        var blockId = $(this).parent().attr("block-id");
        switch (type) {
            case "text":
                showTextBox(blockId, code);
                break;
            case "picture":
                showPictureBox(blockId, code);
                break;
        }
    });

    $("#init").on("click", ".block-delete", function () {
        var block = $(this).parent();
        layer.confirm('确认删除该内容吗？', {
                btn: ['确认','取消']
            }, function(){
                block.remove();
                layer.closeAll();
            }, function(){}
        );
    });

});

//
function createED() {
    return "<a class='block-edit'><i class='fa fa-edit'></i></a><a class='block-delete'><i class='fa fa-close'></i></a>";
}
function createT(code) {
    return "<div class='block-title'>"+code+"</div>";
}

//获取最大的blockid
function maxBlockId() {
    var max = 1;
    $('*[block-id]').each(function (i, e) {
        var current = eval($(this).attr('block-id'));
        if (current > max) max = current;
    });

    return max;
}

//初始化
function updateStatus() {
    $("div[region-id]").sortable({
        dropOnEmpty: true,
        revert: true,
        connectWith: 'div[region-id]'
    });
}

function showTextBox(blockId, code) {
    $("#textBox input[name='blockId']").val(blockId);
    $("#textBox input[name='articleCode']").val(code);
    layer.open({
        type: 1,
        title: '添加文章',
        shadeClose: false,
        shade: 0.3,
        area: '500px',
        content: $('#textBox')
    });
}

function showPictureBox(blockId, code) {
    $("#pictureBox input[name='blockId']").val(blockId);
    $("#pictureBox input[name='pictureCode']").val(code);
    layer.open({
        type: 1,
        title: '添加图片',
        shadeClose: false,
        shade: 0.3,
        area: '500px',
        content: $('#pictureBox')
    });
}

function save(){
    var html = $("#init").clone();
    html.find(".block-edit").remove();
    html.find(".block-delete").remove();
    html.find(".block-title").remove();
    $("#myForm textarea[name='content']").val(html.html());

}