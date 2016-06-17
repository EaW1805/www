/*
 * A jQuery extension to provide scrollbars to a container
 *
 * @author Andrew Lowndes
 * @date 08/12/10
 *
 * @requires jquery.js
 * @requires jquery.event.drag.js
 * @requires jquery.resize.js
 * @requires jquery.mousehold.js
 * @requires jquery.mousewheel.js
 */
(function($) {
    $.fn.scrollbars = function() {
        return $(this).each(function() {
            //transform the selected dom elements
            var $scrollable = $(this);

            $scrollable.addClass("scrollable");

            var $scrollcontent = $('<div class="scrollcontent"></div>');
            var $scrollwrap = $('<div class="scrollwrap"></div>');

            $scrollable.wrapInner($scrollwrap).wrapInner($scrollcontent);

            //as wrapinner actually dereferences the jquery object, we need to reference here
            $scrollcontent = $scrollable.find(".scrollcontent");
            $scrollwrap = $scrollable.find(".scrollwrap");

            var $vscroller = $('<div class="scroller vscroller"></div>');
            var $vscrollblock = $('<div class="scrollblock"></div>');
            var $vscrollarea = $('<div class="scrollarea"></div>');

            var $scrolltop = $('<div class="scrollbtn scrolltop"></div>');
            var $scrolldown = $('<div class="scrollbtn scrolldown"></div>');

            $vscrollarea.append($vscrollblock);
            $vscroller.append($vscrollarea).append($scrolltop).append($scrolldown);

            var $hscrollblock = $('<div class="scrollblock"></div>');
            var $hscrollarea = $('<div class="scrollarea"></div>');

            $hscrollarea.append($hscrollblock);


            var $filler = $('<div class="filler"></div>');

            $scrollable.append($vscroller).append($filler);

            //START
            updateScrollers();

            //EVENTS
            $scrollcontent.add($scrollwrap).resize(function() {
                updateScrollers();
            });

            //allow the directional buttons to scroll the content
            $scrolltop.mousehold(100, function() {
                $scrollcontent.scrollTop($scrollcontent.scrollTop() - 40);
                updateScrollers();
            });

            $scrolldown.mousehold(100, function() {
                $scrollcontent.scrollTop($scrollcontent.scrollTop() + 40);
                updateScrollers();
            });


            //allow the block to be dragged
            $vscrollblock.drag("start", function(e, dd) {
                dd.origTop = $(this).position().top;
            }).drag(function(e, dd) {
                    var maxTop = $(this).parent().innerHeight() - $(this).outerHeight();
                    var newTop = Math.max(0, Math.min(maxTop, dd.origTop + dd.deltaY));

                    $(this).css({top: newTop});
                    $scrollcontent.scrollTop((newTop / maxTop) * ($scrollwrap.outerHeight() - $scrollcontent.innerHeight()));
                });

            //allow the mouse wheel to scroll the content
            $scrollable.mousewheel(function(e, delta) {
                $scrollcontent.scrollTop($scrollcontent.scrollTop() - (delta*30));
                updateScrollers();

                return false;
            });

            //allow pressing the scrollbox to move the scroll bar
            $vscrollarea.mousedown(function(e) {
                var maxTop = $(this).innerHeight() - $vscrollblock.outerHeight();
                var newTop = Math.max(0, Math.min(maxTop, e.pageY - $(this).offset().top - ($vscrollblock.outerHeight()/2.0)));

                $vscrollblock.css({top: newTop});
                $scrollcontent.scrollTop((newTop / maxTop) * ($scrollwrap.outerHeight() - $scrollcontent.innerHeight()));
            });

            $scrollcontent.scroll(function() {
                updateScrollers();
            });

            function updateScrollers() {
                //determine the size and position of the scrollbars based on the scroll position and view size/scroll size
                var amountHeight = $scrollcontent.innerHeight() / $scrollwrap.outerHeight();

                if (amountHeight >= 1) {
                    //if we do not need some scrollbars, then we simply set the class of the scrollable element and let css remove them
                    $scrollable.addClass("no_scroll_v");
                } else {
                    $scrollable.removeClass("no_scroll_v");

                    //otherwise, do the maths
                    var vscrollHeight = 31;

                    var availableHeight = $scrollwrap.outerHeight() - $scrollcontent.innerHeight();
                    var amountTop = $scrollcontent.scrollTop() / availableHeight;

                    var vscrollTop = amountTop * ($vscrollarea.innerHeight()-vscrollHeight);

                    //and assign the new values
                    $vscrollblock.css({top: vscrollTop, height: vscrollHeight});
                }
            };
        });
    };
})(jQuery);
