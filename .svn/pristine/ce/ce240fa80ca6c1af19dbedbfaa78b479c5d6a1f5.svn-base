$(document).ready(function(){	
    /**
     * 已审核 查看信息切换
     */
    $(".steps.checkNameReason a").click(function() {
    	$(this).siblings().each(function(index, ele){
    		var dataClass = $(ele).attr("data-class");
    		$(ele).attr("class", dataClass + "-over")
    	});
    	
    	$(this).attr("class", $(this).attr("data-class") + "-on");
    	$(".named-div").hide();
        var activeTab = $(this).attr("data-href"); 
        $(activeTab).fadeIn(); 
        return false;
    });
    
    /**
     * 投资人个数切换
     */
    $('input[name="partnerClick"]').click(function(){
        var liId = $(this).attr('id');
        $('#'+ liId +'s').show().siblings('div.ch-add-box').hide();
        $("#partnerNumFlag").val($('#'+ liId).val());
    });
    
    /**
     * 初始化 投资人个数
     */
    var partnerNumFlag = $("#partnerNumFlag").val();
    if(partnerNumFlag == undefined || partnerNumFlag == ""){
    	partnerNumFlag = 1;
    }
    $("[name='partnerClick']").each(function(index, element){
    	if($(element).val() == partnerNumFlag){
    		$(element).attr("checked", true);
    		var liId = $(element).attr('id');
    	    $('#'+ liId +'s').show().siblings('div.ch-add-box').hide();
            $("#partnerNumFlag").val($('#'+ liId).val());
    	}
    });
    
    /**
     * 是否有账号
     */
    $('input[name="accClick"]').click(function(){
    	 var liId = $(this).attr('id');
         $('#'+ liId +'Div').show().siblings('div.acc').hide();
         $("#registUserStatus").val($('#'+ liId).val());
         
    });
    /**
     * 初始化 是否有账号
     */
    var registUserStatus = $("#registUserStatus").val();
    if(registUserStatus == undefined || registUserStatus == ""){
    	registUserStatus = 1;
    }
    $("[name='accClick']").each(function(index, element){
    	if($(element).val() == registUserStatus){
    		$(element).attr("checked", true);
    		var liId = $(element).attr('id');
            $('#'+ liId +'Div').show().siblings('div.acc').hide();
            $("#registUserStatus").val($('#'+ liId).val());
    	}
    });
    
    
    /**
     * 一人投资 - 初始化
     */
    LV.selectPartnerType();
    
    /**
     * 多投资人 - 增加投资人
     */
    $("#add_gd").click(function(){
    	var gd_name = $("#add_gd_name").val();
    	if(gd_name == undefined || gd_name.replace(/\s/g,"") == ""){
    		alert("请填写投资人姓名或名称");
    		return;
    	}
    	var partnerType = "";
    	var roleType = "";
    	$("#twos #twosPartnerType").next("div[name='n-select']").find("li").each(function(index, obj){
    		if($(obj).attr("data-value") ==  $("#twos #twosPartnerType").val()){
    			partnerType = $(obj).text();
    			roleType = $(obj).attr("data-role");
    		}
    	});
        $('<div class="add-show" data-info="partnerInfo" data-roleType="' + roleType + '" >'
        		+ '<input type="hidden" id="name" value="' + gd_name + '" data-info="partnerInfo:main" />'
        		+ '<p class="text">' + partnerType + '：' + gd_name + '</p>'
        		+ '<a href="javascript:void(0);" class="del" onclick="del_gd(this)" ></a>'
        +'</div>').clone(true).appendTo("#gd_area")   
        $("#add_gd_name").val("");
    });
    
    /**
	 * 点击 - 暂存
	 */
    $(".btn-box .btn-g.save").click(function(){
    	$("[data-info='partnerInfo']:hidden").each(function(index, element){
    		LV.delPartner(element);
    	});
    	LV.enterpriseRoleSave();
    	LV.enterpriseSave();
    	alert("操作成功");
    	return true;
    });
    /**
     * 点击 - 提交
     */
    $(".btn.pop_box1111.collection.submit").click(function(){
    	var validateArray = new Array();
    	var partnerNumFlag = $("#partnerNumFlag").val();
    	if(partnerNumFlag == "1"){
    		// 一人投资
        	var partnerType = $("#partnerType").val();
        	if(partnerType == "20"){
        		// 自然人股东
        		validateArray.push($("#naturalPartnerDiv"));
        	}else{
        		validateArray.push($("#legalPartnerDiv"));
        	}
    	}else{
    		// 多人投资
    		if($("#gd_area div[data-info='partnerInfo']").length < 2){
    			alert("多人投资，投资人数需多于两个。");
    			return;
    		}
    	}
    	
    	var registUserStatus = $("#registUserStatus").val();
    	if(registUserStatus == "1"){
    		// 有账号
    		validateArray.push($("#accYesDiv"));
    		if(!JX.validateArray(validateArray)){
    			return;
    		}
    		var checkNameAccount = $("#checkNameAccount").val();
    		$("#checkNameAccount").val(checkNameAccount.trim());
    		var checkNamePassword = $("#checkNamePassword").val();
    		$("#checkNamePassword").val(checkNamePassword.trim());
    		
    		if(!GOV.checkUserAndPwd(checkNameAccount.trim(), checkNamePassword.trim())){
    			alert("用户名或者密码错误");
    			return;
    		}
    		$(".cd-popup.pop_box1111").addClass('is-visible');
    	}else{
    		// 没有账号
    		validateArray.push($("#accNoDiv"));
    		if(!JX.validateArray(validateArray)){
    			return;
    		}
    		var registUserCerNo = $("#registUserCerNo").val();
        	var registUserName = $("#registUserName").val();
        	GOV.postRegistUser(registUserCerNo, registUserName);
    	}

    });
    
    $(".btn1.account.submit").click(function(){
    	var validateArray = new Array();
		validateArray.push($(this).parents(".cd-popup[role='alert']"));
		if(!JX.validateArray(validateArray)){
			return;
		}
		var checkNameAccount = $("#checkNameAccount").val();
		$("#checkNameAccount").val(checkNameAccount.trim());
		var checkNamePassword = $("#checkNamePassword").val();
		$("#checkNamePassword").val(checkNamePassword.trim());
		
		if(!GOV.checkUserAndPwd(checkNameAccount.trim(), checkNamePassword.trim())){
			alert("用户名或者密码错误");
			return;
		}
		$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
		$(".cd-popup.pop_box1111").addClass('is-visible');
    });
    
    $(".btn2.account.cancel").click(function(){
    	$(this).parents(".cd-popup[role='alert']").hide();
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    });
    
	/**
	 * 点击 - 提交 -确定
	 */
    $(".btn1.collect.alter.submit").click(function(){
    	// 防止二次点击
		$(this).unbind("click");
		
    	$("#submitDataDiv").show();
    	LV.enterpriseSave();
    	LV.enterpriseRoleSave();
    	LV.enterpriseSubmit();
    	window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId;
    	return false;
    });
    
    /**
     * 点击 - 提交 -取消
     */
    $(".btn2.collect.alter.cancel").click(function(){
    	$(this).parents("[role='alert']").removeClass('is-visible');
    });
});

/**
 * 多投资人 - 删除投资人
 */
function del_gd(obj){
	LV.delPartner($(obj).parent());
	$(obj).parent().remove();
}

function selectMainBusiness(liObj){
//	$(liObj).parents(".td-ul").hide();
	
	var name = $(liObj).attr("data-codeName");
	var code = $(liObj).attr("data-code");
	var uniteCode = $(liObj).attr("data-uniteCode");
	var industry = $(liObj).attr("data-industry");
	var scope = $(liObj).attr("data-scope");
	
	if(name == undefined){
		name = "";
	}
	if(code == undefined){
		code = "";
	}
	if(uniteCode == undefined){
		uniteCode = "";
	}
	if(industry != undefined){
		$("#industryCharacteristics").val(industry);
		
		$(liObj).parents('[name="n-select"]').find('input').val(industry);
	}
	$("#mainBusinessText").text("主营业务：" + name);
	$("#mainBusiness").val(name);
	$("#mainBusinessCode").val(code);
	$("#mainBusinessUniteCode").val(uniteCode);
	if(scope == undefined || scope == ""){
		scope = name;
	}
	$("#operatingRange").val(scope + "；");
	LV.fullName();
}
