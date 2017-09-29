$(document).ready(function(){	
	//名称中行政区划所在的位置初始化
	var regionLocationInName = $("#regionLocationInName").val();
	if(regionLocationInName == undefined || regionLocationInName == "" || regionLocationInName == "0"){
		regionLocationInName = "1";
		$("#regionLocationInName" + regionLocationInName).attr("checked", true);
		$("#regionLocationInName").val(regionLocationInName);
	}else{
		$("#regionLocationInName" + regionLocationInName).attr("checked", true);
	}
	LV.fullName();
	
	// 名称核准选择变化
	$("[name='regionLocationInName']").change(function(e){
		$("#regionLocationInName").val($(this).val());
		LV.fullName();
	});
	  // 行业特点显示隐藏
    $("#industryCharacteristics").mouseover(function(){
        $("#ermblock").show();
    });
    $(document).click(function(e){
        var target = $(e.target);
        if(target.closest("#industryCharacteristics").length == 0){
            $("#ermblock").hide();
        }
    }); 
	/**
	 * 字号变化
	 */
	$("#shopName").keyup(function(e){
		LV.fullName();
	});
	
	$("#namefull").keyup(function(){
		$("#name").val($(this).val());
	}).change(function(){
		$("#name").val($(this).val());
	});
	
	/**
	 * 行业特点联想提示
	 */
	$("#industryCharacteristics").keyup(function(e){
		$(this).next(".td-ul").show();
		var val = $(this).val();
		var html = "";
		var mainData = LV.getMainbusinessByText(val);
		if(val != undefined && val != ""){
			html += "<li class=\"pop_box1\">手动选择[" + val + "]的主营业务</li>";
		}
		if(mainData != undefined && mainData.length > 0){
			for(var i=0; i<mainData.length; i++ ){
				var scope = mainData[i]["scope"];
				if(scope ==undefined || scope == "" ){
					scope = mainData[i]["codeName"];
				}
				html += "<li data-industry=" + mainData[i]["industryText"] 
				+ " data-code=" + mainData[i]["code"] 
				+ " data-codeName=" + mainData[i]["codeName"] 
				+ " data-uniteCode=" + mainData[i]["parentUniteCode"] 
				+ " data-scope=" + scope 
				+ " >" + mainData[i]["industryText"] + "（主营业务：" + mainData[i]["codeName"] + "）</li>";
			}
		}
		$(this).next(".td-ul").find("ul").html(html);
		/**
		 * 点击li事件
		 */
		$(this).next(".td-ul").find("ul li").click(function(){
			selectMainBusiness($(this));
		});
		
		$('.pop_box1').unbind("click").on('click', function(event){
			$('.cd-box12434').addClass('is-visible');
			initBusiness();
		});
		
		LV.fullName();
	}).focus(function(){
		$(this).next(".td-ul").show();
	});
	
	$(".td-ul").find("ul li").click(function(){
		selectMainBusiness($(this));
	});

    /**
     * 有无名称 初始化
     */
	var checkNameStatus = $("#checkNameStatus").val();
	if(checkNameStatus == undefined || checkNameStatus == "" 
		|| checkNameStatus == 0 || checkNameStatus == 3 
		|| checkNameStatus == 4 || checkNameStatus == 5){
		checkNameStatus = 2;
	}
    $(".tab-con").hide(); 
	var checkA = $("a[checkNameStatus-value='" + checkNameStatus + "']");
    $(checkA).parent().addClass("active").show();
    $(".tab-box "+ $(checkA).attr('href')).show(); 
    /**
     * 有无名称 切换
     */
    $("ul.name-list div").click(function() {
        $("ul.name-list div").removeClass("active"); 
        $(this).addClass("active"); 
        $(".tab-con").hide(); 
        var activeTab = $(this).find("a").attr("href"); 
        $(activeTab).fadeIn(); 
        
        var checkNameStatus = $(this).find("a").attr("checkNameStatus-value"); 
        $("#checkNameStatus").val(checkNameStatus);
        return false;
    });

	/**
	 * 点击检查是否可用
	 */
	$("#foundcheckBtn").click(function(){
		var validateArray = new Array();
		validateArray.push($("#name1"));
		if(!JX.validateArray(validateArray)){
			return;
		}
		LV.fullName();
		var shopName = $("#shopName").val();
		var industryCharacteristics = $("#industryCharacteristics").val();
		var mainBusinessCode = $("#mainBusinessCode").val();
		var mainBusinessUniteCode = $("#mainBusinessUniteCode").val();
		var fullname = $("#name").val();
		GOV.foundCheck(shopName, industryCharacteristics, fullname, mainBusinessCode, mainBusinessUniteCode);
		
	});
	
	/**
	 * 继续填写补充信息
	 */
	$("#name1 .tj-btn.continue").click(function(){
		LV.enterpriseSave();
		window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId + "/" + nextBusinessKey;
		return false;
	});
	
	/**
	 * 已有名称 - 提交
	 */
	$("#name2 .tj-btn.submit").click(function(){
		var validateArray = new Array();
		validateArray.push($("#name2"));
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
		
		// 保存信息
		nextBusinessKey = "checkNameReason";
		LV.enterpriseSave();
		LV.enterpriseRoleSave();
		LV.enterpriseSubmit();
		window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId;
		return false;
	});
})