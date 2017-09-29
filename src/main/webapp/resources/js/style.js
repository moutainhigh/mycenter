$(document).ready(function(){
    // 蓝色显示区域
    function showOrRemoveSelector($checkbox,$showArea,namespace,text) {
        var value = $checkbox.val();
        if ($checkbox.get(0).checked) {
            var str = '<li class="bgBlue" bindValue="' + value +'" namespace="' + namespace + '">' + text + '</li>';
            $showArea.append(str);
            // var str = '<li class="bgRed" bindValue="' + value +'" namespace="' + namespace + '">' + text + '</li>';
            // $showArea.append(str);
        } else {
            $showArea.children('[namespace="'+namespace+'"][bindValue="'+value+'"]').remove();
        }
    }

    $('.type input').on('click',function(e){
        var $this = $(this);
        var $span = $this.parent();
        var $parent = $span.parent();
        var $showArea = $('#' + $parent.attr('bindShowArea'));
        var namespace = $parent.attr('namespace');
        var text = $span.text();
        showOrRemoveSelector($this,$showArea,namespace,text);
        e.stopPropagation();
    });

    // 选中联动
    $('.type p').click(function() {
        var $this = $(this);
        var $checkbox = $this.children('input[type="checkbox"]');
        var $parent = $this.parent();
        var $showArea = $('#' + $parent.attr('bindShowArea'));
        var namespace = $parent.attr('namespace');
        var text = $this.text();
        $checkbox.prop('checked',!$checkbox.prop('checked'));
        showOrRemoveSelector($checkbox,$showArea,namespace,text);
    });

    $('.b_choiceArea ul').on('click','li',function(){
        var $this = $(this).remove();
        var value = $this.attr('bindValue');
        var namespace = $this.attr('namespace');
        $('.type[namespace="'+namespace+'"] p input[value="'+value+'"]').attr('checked',false);
    });
    // 蓝色显示区域

})