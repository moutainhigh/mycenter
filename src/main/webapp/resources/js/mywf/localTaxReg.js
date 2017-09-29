$(document).ready(function(){
	//导航栏切换
	 $("#s2").click(function(){
        $("#s1").addClass("s1-un").removeClass("s1-on");
        $("#s2").addClass("s2-on").removeClass("s2-un");
        $(".dsbox1").hide();
        $(".dsbox2").show();
    });
    $("#s1").click(function(){
        $("#s1").addClass("s1-on").removeClass("s1-un");
        $("#s2").addClass("s2-un").removeClass("s2-on");
        $(".dsbox1").show();
        $(".dsbox2").hide();
    }); 
    
    $("#clickbtn").click(function(){
        $("#s1").addClass("s1-un").removeClass("s1-on");
        $("#s2").addClass("s2-on").removeClass("s2-un");
        $(".dsbox1").hide();
        $(".dsbox2").show();
    });
    
    //初始化隐藏
    $("div.sear-result").hide();
    $("div.none-result").hide();
	
	/**
	 * 地税报到 - 完成
	 */
	$(".btn.local.submit").click(function(){
		LV.wfSignlReceiveTask();
		window.location.href = "/mywf/company/reg/" + procInstId;
		return true;
	});
})

//查询
function searchInfo(){
	var searchVal = $("#searchInput").val();
	if($.trim(searchVal) == ''){
		alert("请输入信用代码！");
		return;
	}
	
	 $("div.none-sear").hide();
	 $("div.sear-result").show();
	
	//调用接口查询数据
	$.ajax({
		url:"/mywf/localTaxReg/getComanyInfo",
		type:"post",
		dataType:"json",
		async:true,
		data:{
			"code":searchVal
		},
		beforeSend:function(){
			$(".cd-popup1.cd-box").addClass('is-visible');
	    },
		success:function(data){
			$(".cd-popup1.cd-box").removeClass('is-visible');
			if(data != null && data.length > 0){
				$("div.none-result").hide();//隐藏"抱歉，未查询套相关结果.." div
				$("div.sear-result div.text").show();
				$("#companyName").text(data[0]);
				$("#companyInfo").text(data[1]);
				$("#code").text(data[2]);
				$("#address").text(data[3]);
				$("#tel").text(data[4]);
			}else{//无查询结果
				$("div.none-result").show();//显示"抱歉，未查询套相关结果.." div
				$("div.sear-result div.text").hide();
				$("#companyName").text("");
				$("#companyInfo").text("");
				$("#code").text("");
				$("#address").text("");
				$("#tel").text("");
			}
		},
		error:function(XMLHttpRequest,textStatus,errorThrown){
  			alert("无响应");
  		},
	});
}