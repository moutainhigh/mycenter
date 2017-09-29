
$(function(){
  $.fn.hhDrop = function(options){
    var options = jQuery.extend({
        preLoadSrc:"images/loading.gif"
    }, options || {}); 


    var defaults = {};

    return this.each(function(){

      //默认
      var options = $.extend(defaults,options);
      var $this = $(this);

      var $boxSearch = $this.find('.boxSearch');
      var $lineSearchbg = $this.nextAll().find('.lineSearchbg');

      

      //出发城市  到达城市
      $boxSearch.click(function(){
        var _this = $(this);
        //点击本身显示隐藏
        if(_this.hasClass('boxSearchHover') ){
          _this.removeClass('boxSearchHover');
          _this.children('.btn-search').removeClass('btn-search-current');
          _this.parent().find('.search-form-suggest').hide();

        }else{
          _this.addClass('boxSearchHover');
          _this.children('.btn-search').addClass('btn-search-current');
          _this.parent().find('.search-form-suggest').show();
        }

        _this.next().find('.clr-after a').click(function(){
          
          _this.find('span.key-word b').text($(this).text());

        });


        _this.next().find('.search-city-result a').click(function(){
          
          _this.find('span.key-word b').text($(this).text());

        });

        //阻止冒泡
        $(document).bind('click',function(event){
          if(!$(event.target).parent().hasClass('boxSearch' )  && !$(event.target).hasClass('boxSearch') && !$(event.target).parent().parent().hasClass('boxSearch') && !$(event.target).hasClass('input-city') ){
            _this.children('.btn-search').removeClass('btn-search-current');
            _this.removeClass('boxSearchHover');
            _this.parent().find('.search-form-suggest').hide();
          }
        });
        
      });
      

  });
    
} 

});


// ie 下 placeholder 兼容性
$(function(){       
  supportPlaceholder='placeholder'in document.createElement('input'),   
  placeholder=function(input){     
    var text = input.attr('placeholder'),    
    defaultValue = input.defaultValue;     
    if(!defaultValue){       
      input.val(text).addClass("phcolor");    
    }     
    input.focus(function(){       
      if(input.val() == text){           
        $(this).val("");      
      }    
    });       
    input.blur(function(){       
      if(input.val() == ""){               
        $(this).val(text).addClass("phcolor");      
      }    
    });   

    //输入的字符不为灰色    
    input.keydown(function(){       
      $(this).removeClass("phcolor");    
    });  
  };   //当浏览器不支持placeholder属性时，调用placeholder函数  

  if(!supportPlaceholder){     
    $('input').each(function(){       
      text = $(this).attr("placeholder");       
      if($(this).attr("type") == "text"){         
        placeholder($(this));     
      }   
    });  
  } 
});