var tooltipcustomcontainerid = 0;
var largerZIndex = 5000;
$(function () {
    $('[title]').qtip({
        position:{
            viewport:$(window)
        }
    });

    $('[tooltip]').addClass("pointer");

    $('[tooltip]').click(function () {
        tooltipcustomcontainerid++;
        largerZIndex++;
        var currentId = tooltipcustomcontainerid;
        //create container
        $('body').append("<div id='tooltip-container" + tooltipcustomcontainerid + "' onmousedown='setLargerZIndex(this);' style='display: none;position: fixed; left: 0px;top: 0px; z-index: " + largerZIndex + ";'></div>");
        var ttip = $(this).attr("tooltip");
        //add content to container
        $("#tooltip-container" + tooltipcustomcontainerid).html(ttip);
        $("#tooltip-container" + tooltipcustomcontainerid).show();
        //draggable it
        $(".draggablePopup").draggable();
        $('.draggablePopup').draggable({
            cursor:'move', zIndex:20000, handle:'#handleDrag'
        });
        //enable close functionality
        $("#tooltip-container" + tooltipcustomcontainerid + " .draggableCloser").click(function () {
            hidePopup(currentId);
        });

//        $("#tooltip-container").center(true);
    });
    //(re)init external tooltips
    initExternalTooltips();


});

function setLargerZIndex(el) {
    if (el.style.zIndex != largerZIndex) {
        largerZIndex++;
        el.style.zIndex = largerZIndex
    }
}

function initExternalTooltips() {
    $('[externalTooltip]').unbind('click');
    $('[externalTooltip]').click(function () {
        tooltipcustomcontainerid++;
        largerZIndex++;
        var currentId = tooltipcustomcontainerid;
        //create container
        $('body').append("<div id='tooltip-container" + tooltipcustomcontainerid + "' onmousedown='setLargerZIndex(this);' style='display: none;position: fixed; left: 0px;top: 0px; z-index: " + largerZIndex + ";'></div>");
        var ttip = $("#" + $(this).attr("externalTooltip")).html();
        //add content to container
        $("#tooltip-container" + tooltipcustomcontainerid).html(ttip);
        $("#tooltip-container" + tooltipcustomcontainerid).show();
        //draggable it
        $(".draggablePopup").draggable();
        $('.draggablePopup').draggable({
            cursor:'move', zIndex:20000, handle:'#handleDrag'
        });
        //enable close functionality
        $("#tooltip-container" + tooltipcustomcontainerid + " .draggableCloser").click(function () {
            hidePopup(currentId);
        });

//        $("#tooltip-container").center(true);
    });
}

jQuery.fn.center = function (parent) {
    if (parent) {
        parent = this.parent();
    } else {
        parent = window;
    }
    this.css({
        "position":"absolute",
        "top":((($(parent).height() - this.outerHeight()) / 2) + $(parent).scrollTop() + "px"),
        "left":((($(parent).width() - this.outerWidth()) / 2) + $(parent).scrollLeft() + "px")
    });
    return this;
}

$(document).keydown(function (e) {
    // ESCAPE key pressed
    if (e.keyCode == 27) {
        for (var index=1;index<=tooltipcustomcontainerid;index++) {
            $("#tooltip-container" + index).hide();
        }
    }
});


function hidePopup(containerId) {
    $("#tooltip-container" + containerId).hide();
}




