$(function () {
    $('.blocks').draggable({
        revers: "invalid",
        helper: "clone",
        connectToSortable: "div[region-id]",
        start: function (event, ui) {

        },
        stop: function (event, ui) {


        }
    });
})

//初始化
function updateStatus() {
    $("div[region-id]").sortable({
        dropOnEmpty: true,
        revert: true,
        connectWith: 'div[region-id]'
    });
}