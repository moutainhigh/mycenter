$(document).ready(function(){
	$(".vendor").on('click',function(event){
		if(!$.cookie("type") || $.cookie("type") == "null"){
			$.cookie("type","1",{path:"/mywf"});
			gotoServer();
		}
	});
	$(".free").on('click',function(event){
		if(!$.cookie("type") || $.cookie("type") != "null"){
			$.cookie("type",null,{path:"/mywf"});
			gotoServer();
		}
	});
	function gotoServer(){
		var lastPage = $("#pageNo").children(".current").text();
		var currentPage = $.cookie("lastPage");
		$.cookie("lastPage",lastPage,{path:"/mywf"});
		if(!!currentPage && currentPage != "null"){
			window.location.href = "/mywf/index/"+currentPage+".html";
		}else{
			window.location.href = "/mywf/index.html";
		}
	}
})