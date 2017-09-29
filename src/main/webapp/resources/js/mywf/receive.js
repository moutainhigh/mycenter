$(document).ready(function(){
    
	//open popup
	$('.pop_box1yuehao').on('click', function(event){
		event.preventDefault();
		$('.cd-box1yuehao').addClass('is-visible');
	});
	//close popup
	$('.cd-box1yuehao').on('click', function(event){
		if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-box1yuehao') ) {
			event.preventDefault();
			$(this).removeClass('is-visible');
		}
	});
	//close popup when clicking the esc keyboard button
	$(document).keyup(function(event){
    	if(event.which=='27'){
    		$('.cd-box1yuehao').removeClass('is-visible');
	    }
    });
	
	
	//open popup
	$('.pop_box2quxiao').on('click', function(event){
		event.preventDefault();
		$('.cd-box2quxiao').addClass('is-visible');
	});
	//close popup
	$('.cd-box2quxiao').on('click', function(event){
		if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-box2quxiao') ) {
			event.preventDefault();
			$(this).removeClass('is-visible');
		}
	});
	//close popup when clicking the esc keyboard button
	$(document).keyup(function(event){
    	if(event.which=='27'){
    		$('.cd-box2quxiao').removeClass('is-visible');
	    }
    });
	
	
    /**
     * 预约工商 - 确定预约
     */
    $(".btn.appoint.submit").click(function(){
    	var validateArray = new Array();
    	validateArray.push($(".cd-box1yuehao"));
		if(!JX.validateArray(validateArray)){
			return;
		}
    	LV.enterpriseSave();
    	var registFlag = GOV.postRegistAppointUser();
    	if(registFlag){
    		$("#gov-appointStatus").val("2"); // 预约中
    		LV.enterpriseSave();
    		$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    		window.location.reload();
    	}else{
    		$(".cd-box1yuehao").removeClass('is-visible');
    		$(".cd-box198912").addClass('is-visible');
    	}
    	return true;
    });
    /**
     * 预约工商 - 取消 - 确定
     */
    $(".btn1.cancelAppoint.submit").click(function(){
    	$("#gov-appointStatus").val("1"); // 取消，未预约
    	var appointNum = $("#gov-appointNum").val();
    	if(appointNum == undefined || appointNum == ""){
    		appointNum = 1;
    	}else{
    		appointNum = Number(appointNum) + 1;
    	}
    	$("#gov-appointNum").val(appointNum);
    	
    	GOV.appointCancel();
    	LV.enterpriseSave();
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    	
    	window.location.reload();
    	return true;
    });
    
    /**
     * 预约工商 - 取消 - 取消
     */
    $(".btn2.cancelAppoint.cancel").click(function(){
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    });
    
    /**
     * 预约工商 - 账号 - 确定
     */
    $(".btn1.ftaccount.submit").click(function(){
    	var validateArray = new Array();
    	validateArray.push($(".cd-box198912"));
		if(!JX.validateArray(validateArray)){
			return;
		}
    	
    	LV.enterpriseSave();
    	var registFlag = GOV.postRegistAppointUser();
    	if(registFlag){
    		$("#gov-appointStatus").val("2"); // 预约中
    		LV.enterpriseSave();
    		$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    		window.location.reload();
    	}else{
    		alert("用户名或者密码错误")
    	}
    	return true;
    });
    
    /**
     * 预约工商 - 账号 - 取消
     */
    $(".btn2.ftaccount.cancel").click(function(){
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    });
    
    
    /**
     * 预约工商 - 下一步
     */
    $(".next-btn.order.next").click(function(){
    	LV.enterpriseSubmit();
		window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId;
    	return true;
    });
    
    /**
     * 领取证照 - 完成
     */
    $(".btn.receive.submit").click(function(){
    	LV.enterpriseSubmit();
    	LV.wfSignlReceiveTask();
    	window.location.href = "/mywf/company/reg/" + procInstId;
    	return true;
    });
});