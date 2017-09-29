var ajreq = function(url,data,callback){
	$.ajax({url:url,
		data:data,
		success:function(data){
			callback(data);
		}
		});
}
var choicelocal = function(obj){
	$(obj).next("ul").show();
	
}
var clicklocal = function(obj){
	$(obj).parent().hide();
	return $(obj).attr("data-value");
}
var numDec = function(obj){
	var o = $(obj).next("input");
	var mn = parseFloat(o.attr("siglev"));
	var ov = parseInt(o.attr("value"));
	if(ov <= 1){
		alert("数字不能小于1");
	}else{
		o.attr("value",--ov);
		return ov*mn;
	}
	
}
var addDec = function(obj){
	var o = $(obj).prev("input");
	var ov = parseInt(o.attr("value"));
	o.attr("value",++ov);
	var mn = parseFloat(o.attr("siglev"));
	return ov*mn;
}
