$(document).ready(function(){
	
	/**
	 * 选择经营范围
	 */
	$("#operatingRangeSelectBtn").click(function(){
		$(".pop_box113126[role='alert']").addClass('is-visible');
	});
	
    /**
     * 企业基本信息 - 经营范围 - 取消
     */
    $(".btn-g.businessAreaSelect.cancel").click(function(){
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    });
    /**
     * 企业基本信息 - 经营范围 - 保存
     */
    $(".btn.businessAreaSelect.save").click(function(){
    	var operatingRange = $("#operatingRange").val();
    	if(operatingRange != undefined && operatingRange != "" && operatingRange.charAt(operatingRange.length - 1) == "。"){
    		operatingRange = operatingRange.substr(0, operatingRange.length - 1) + "；";
    	}
    	$("ul#scopeBusiness li").each(function(index, ele){
    		if(operatingRange.indexOf($(ele).text()) < 0){
    			operatingRange += $(ele).text() + "；";
    		}
    	});
    	if(operatingRange.charAt(operatingRange.length - 1) == "；"){
    		operatingRange = operatingRange.substr(0, operatingRange.length - 1) + "。"
    	}
    	$("#operatingRange").text(operatingRange);
    	$("#operatingRange").val(operatingRange);
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    });
    
    
    /**
     * 企业基本信息 - 保存
     */
    $(".btn.basinfo.save").click(function(){
    	// 保存
    	LV.enterpriseSave();
    	alert("操作成功")
    });
    
    /**
     * 企业基本信息 - 下一步
     */
    $(".btn.basinfo.submit").click(function(){
    	// 校验
		var validateArray = new Array();
		validateArray.push($(".dj-online"));
		if(!JX.validateArray(validateArray)){
			return;
		}
    	
    	// 下一步
    	LV.enterpriseSave();
		LV.enterpriseSubmit();
		window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId;
    	return true;
    });
	
    
	$('.pop_box1person').on('click', function(event){
		event.preventDefault();
		$('.cd-box1person').addClass('is-visible');
	});
	//close popup
	$('.pop_box1person').on('click', function(event){
		if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-box1person') ) {
			event.preventDefault();
			$(this).removeClass('is-visible');
		}
	});
	//close popup when clicking the esc keyboard button
	$(document).keyup(function(event){
    	if(event.which=='27'){
    		$('.cd-box1person').removeClass('is-visible');
	    }
    });
	
	
	$('.pop_box2company').on('click', function(event){
		event.preventDefault();
		$('.cd-box2company').addClass('is-visible');
	});
	//close popup
	$('.pop_box2company').on('click', function(event){
		if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-box2company') ) {
			event.preventDefault();
			$(this).removeClass('is-visible');
		}
	});
	//close popup when clicking the esc keyboard button
	$(document).keyup(function(event){
    	if(event.which=='27'){
    		$('.cd-box2company').removeClass('is-visible');
	    }
    });
    
    /**
     * 完善股东信息 - 修改股东
     */
    $(".nbox.parnter").click(function(){
    	var roleId = $(this).attr("data-id");
    	var roleType = $(this).attr("data-roleType");
    	if(roleId == ""){
    		return;
    	}
    	var roleTypeDiv = $("[data-roleType='" + roleType + "'][role='alert']");
    	$(roleTypeDiv).siblings("[role='alert'][data-roleType]").hide();
    	$(roleTypeDiv).show();
    	
    	
    	$(roleTypeDiv).find("[data-info='payInfo']:first").siblings("[data-info='payInfo']").remove();
    	$(roleTypeDiv).find("[data-info='partnerInfo:main'][data-validate]").val("");
    	$(roleTypeDiv).find("[data-info='partnerInfo:ext'][data-validate]").val("");
    	$(roleTypeDiv).find("[data-info='payInfo']:first").find("input").val("");
    	
    	var roleInfo = LV.getRoleInfo(roleId, roleType);
    	$.each(roleInfo,function(name,value) {
    		if(name == "partnerPayArray"){
    			// 初始化 出资额
    			if(value == undefined || value.length <= 0){
    				return;
    			}
				var partnerPayArray = $.parseJSON(value);
				var payInfoObj = $(roleTypeDiv).find("[data-info='payInfo']:first");
				$.each(partnerPayArray, function(index, obj){
					var capitalArea = $(roleTypeDiv).find("[name='capitalArea']:first");
					if(index > 0){
						capitalArea = capitalArea.clone();
						$(capitalArea).find("td:last").append("<a href=\"javascript:void(0);\" class=\"del\" onclick=\"$(this).parents('tr').remove();changePaySize('" + roleType + "');LV.enterprisePartnerPayDel('"+ obj.payId +"');\" ></a>");
						$(capitalArea).find("input").val("");
						$(roleTypeDiv).find("[name='capitalArea']:last").after(capitalArea);
					}
					$.each(obj,function(n,v) {
						$(capitalArea).find("#" + n).val(v);
					});
					
					var dataVal = $(capitalArea).find("#capitalMethod").val();
					var areaHtml = "";
					if(dataVal == 31 || dataVal == 32){
						areaHtml += "<li data-value=\"1\">不实施</li><li data-value=\"2\">技术成果入股</li>";
				    }else{
				    	areaHtml += "<li data-value=\"1\">不实施</li>";
				    }
					$(capitalArea).find("#techStockFlag").next().find("ul").html(areaHtml);
					initCapitalArea(capitalArea);
				});
    		}else{
    			var spanTextObj = $(roleTypeDiv).find("span[data-id='" + name + "']");
    			if(spanTextObj != undefined && value != ""){
    				$(spanTextObj).text(value);
    			}
    			var valObj = $(roleTypeDiv).find("#" + name);
    			if(valObj != undefined && value != ""){
    				$(valObj).val(value);
    			}
    		}
    	});
    	initParnter(roleType);
    	LV.initSelect();
    });
    
    // 提示信息 - 取消
    $(".btn-g.concal").click(function(){
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    });
    // 提示信息 - 确定
    $(".btn1.confirm").click(function(){
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    });
    
    // 自然人股东 - 保存
    $(".btn.partner.save").click(function(){
    	// 校验
    	var validateArray = new Array();
		validateArray.push($("[data-info='partnerInfo']:visible"));
		if(!JX.validateArray(validateArray)){
			return;
		}
		
    	LV.enterpriseRoleSave();
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    	window.location.reload();
    	return true;
    });
    
    /**
     * 删除主要成员
     */
    $(".del.mainMen").click(function(e){
    	$(this).parent().remove();
    	LV.delPartner($(this));
    	initMeetDisable();
    	return false;
    });
    
    initMeetDisable();
    function initMeetDisable(){
    	if($("div[data-type='dongshi'] .mainMember").length <= 0){
        	$("[name='isDongShiMeeting']").attr("disabled", false);
    	}else{
        	$("[name='isDongShiMeeting']").attr("disabled", true);
    	}
    	
    	if($("div[data-type='jianshi'] .mainMember").length <= 0){
        	$("[name='isJianShiMeeting']").attr("disabled", false);
    	}else{
        	$("[name='isJianShiMeeting']").attr("disabled", true);
    	}
    }
    
    
    /**
     * 添加主要成员
     */
    $(".add-name.pop_box112").click(function(){
    	var type = $(this).parent().attr("data-type");
    	var alertClass = "pop_box112";
    	$("." + alertClass).find("[data-info='partnerInfo:main'][data-validate]").val("");
    	$("." + alertClass).find("[data-info='partnerInfo:ext'][data-validate]").val("");
    	initMainType(type, alertClass);
    	
    	$("." + alertClass).siblings("[role='alert'][data-roleType]").hide();
    	$("." + alertClass).show();
    });
    
    var isDongShiMeeting = $("#isDongShiMeeting").val();
    if(isDongShiMeeting == undefined || isDongShiMeeting == "" ){
    	isDongShiMeeting = 2;
    	$("#isDongShiMeeting").val(isDongShiMeeting);
    }
    
    if(isDongShiMeeting != undefined && isDongShiMeeting != "" ){
    	if(isDongShiMeeting == "1"){
    		$("#dyses").show();
    	}else{
    		$("#dyses").hide();
    	}
    	$("#isDongShiMeeting-" + isDongShiMeeting).attr("checked", true);
    }
    var isJianShiMeeting = $("#isJianShiMeeting").val();
    if(isJianShiMeeting == undefined || isJianShiMeeting == "" ){
    	isJianShiMeeting = 2;
    	$("#isJianShiMeeting").val(isJianShiMeeting);
    }
    if(isJianShiMeeting != undefined && isJianShiMeeting != "" ){
    	if(isJianShiMeeting == "1"){
    		$("#jyses").show();
    		$("#jnos").hide();
    	}else{
    		$("#jyses").hide();
    		$("#jnos").show();
    	}
    	$("#isJianShiMeeting-" + isJianShiMeeting).attr("checked", true);
    }
    
    /**
     * 是否设置董事会变化
     */
    $("[name='isDongShiMeeting']").change(function(){
    	var isDongShiMeeting = $("[name='isDongShiMeeting']:checked").val();
    	if(isDongShiMeeting == "1"){
    		$("#dyses").show();
    	}else{
    		$("#dyses").hide();
    	}
    	$("#isDongShiMeeting").val($("[name='isDongShiMeeting']:checked").val());
    });
    /**
     * 是否设置监事会变化
     */
    $("[name='isJianShiMeeting']").change(function(){
    	var isJianShiMeeting = $("[name='isJianShiMeeting']:checked").val();
    	if(isJianShiMeeting == "1"){
    		$("#jyses").show();
    		$("#jnos").hide();
    	}else{
    		$("#jyses").hide();
    		$("#jnos").show();
    	}
    	$("#isJianShiMeeting").val(isJianShiMeeting);
    });
    
    /**
     * 初始化董事会
     */
    function initDongShiInfo(alertClass, isDongShiMeeting){
    	var html = "";
    	if(isDongShiMeeting == "1"){
    		html += LV.buildSelectLiHtml('{"directorManager": "董事长","secondDirectorManager": "副董事长", "director": "董事"}');
    	}else if(isDongShiMeeting == "2"){
    		html += LV.buildSelectLiHtml('{"directorManager-S":"执行董事"}');
    	}
    	$("." + alertClass + " #roleType").next().find("ul").html(html);
    }
    /**
     * 初始化监事会
     */
    function initJianShiInfo(alertClass, isJianShiMeeting){
    	var html = "";
    	if(isJianShiMeeting == "1"){
    		html += LV.buildSelectLiHtml('{"supervisorChairman": "监事会主席","supervisor": "监事"}');
    	}else if(isJianShiMeeting == "2"){
    		html += LV.buildSelectLiHtml('{"supervisor-S": "监事"}');
    	}
    	$("." + alertClass + " #roleType").next().find("ul").html(html);
    }
    
    /**
     * 点击设定法人
     */
    $(".tag.cd-box113").click(function(){
    	var alertClass = "cd-box113";
    	$('.' + alertClass).addClass('is-visible');
		LV.initBox(alertClass);
		// 传递角色ID
		var roleId = $(this).parent().attr("data-id");
		var roleType = $(this).parent().attr("data-type");
		$(".cd-box113").find("div[data-roleId]").attr("data-roleId", roleId);
		$(".cd-box113").find("div[data-roleType]").attr("data-roleType", roleType);
		return false;
    });
    
    /**
     * 点击设定法人 - 确定
     */
    $(".cd-box113 .btn1").click(function(){
    	var alertClass = "cd-box113";
    	var roleId = $(this).parent().attr("data-roleId");
    	var roleType = $(this).parent().attr("data-roleType");
    	var roleData = {};
    	if(roleType != undefined && roleType != ""){
    		roleData["legalPersonMappingRoleType"] = roleType;
    	}
    	LV.addPersonRole(roleId, "legalPerson", roleData);
    	$('.' + alertClass).removeClass('is-visible');
    	window.location.reload();
    	return false;
    });
    
    /**
     * 点击设定法人 - 取消
     */
    $(".cd-box113 .btn2").click(function(){
    	var alertClass = "cd-box113";
    	
    	$('.' + alertClass).removeClass('is-visible');
    });
    
    /**
     * 完善股东信息 - 主要人员 - 修改
     */
    $("p.mainMember").click(function(e){
    	var alertClass = "pop_box112";
    	var roleId = $(this).attr("data-id");
    	var roleType = $(this).attr("data-type");
    	var type = $(this).parent().attr("data-type");
    	
    	initMainMember(roleId, roleType, $("." + alertClass));
    	initMainType(type, alertClass);
    	
    	$("." + alertClass).siblings("[role='alert'][data-roleType]").hide();
    	$("." + alertClass).show();
    	return true;
    });
    
    
    function initMainType(type, alertClass){
    	if(type == "dongshi"){
    		// 董事
    		//设置董事默认产生方式
    		var entType = $("#enterpriseType").val();
    		if(entType == "有限责任公司" || entType == "有限责任公司(自然人独资)" || entType == "有限责任公司(法人独资)"){
    			$("#posBrForm").val("02");
    		}else{
    			$("#posBrForm").val("03");
    		}
    		//设置董事默认任职期限
    		$("#offYears").val("3");
    		var isDongShiMeeting = $("[name='isDongShiMeeting']:checked").val();
    		if(isDongShiMeeting == undefined){
    			$('.cd-box111').addClass('is-visible');
    			LV.initBox("cd-box111");
    			return;
    		}
    		initDongShiInfo(alertClass, isDongShiMeeting)
    	}else if(type == "jianshi"){
    		// 监事
    		//设置监事默认产生方式
    		var entType = $("#enterpriseType").val();
    		if(entType == "有限责任公司" || entType == "有限责任公司(自然人独资)" || entType == "有限责任公司(法人独资)"){
    			$("#posBrForm").val("02");
    		}else{
    			$("#posBrForm").val("03");
    		}
    		//设置董事默认任职期限
    		$("#offYears").val("3");
    		var isJianShiMeeting = $("[name='isJianShiMeeting']:checked").val();
    		if(isJianShiMeeting == undefined){
    			$('.cd-box110').addClass('is-visible');
    			LV.initBox("cd-box110");
    			return;
    		}
    		initJianShiInfo(alertClass, isJianShiMeeting);
    	} else if(type == "jingli"){
    		// 经理
    		//设置经理默认产生方式
    		$("#posBrForm").val("04");
    		//设置董事默认任职期限
    		$("#offYears").val("3");
    		$("." + alertClass + " #roleType").next().find("ul").html(LV.buildSelectLiHtml('{"manager":"经理"}'));
    	}
    	
    	var htmlLi = "";
    	var naturalParts = LV.getValideNaturalParts(type);
    	if(naturalParts != undefined && naturalParts.length > 0){
    		$.each(naturalParts, function(index, ele){
    			htmlLi += "<li data-id=\"" + ele.id + "\" data-type=\"" + ele.roleType + "\" >" + ele.name + "</li>";
    		})
    		$("#naturalPartSelect ul").html(htmlLi);
    	}
    	$("#naturalPartSelect ul").html(htmlLi);
    	
        /**
         * 完善股东信息 - 主要人员 - 提示股东信息
         */
        $(".pop_box112 #name").mouseover(function(){
        	$(this).next().find("ul").show();
        });
        $(".pop_box112 #name").next().find("li").click(function(){
        	var roleId = $(this).attr("data-id");
        	var roleType = $(this).attr("data-type");
        	var alertClass = "pop_box112";
        	initMainMember(roleId, roleType, $("." + alertClass));
        	LV.initSelect($("." + alertClass));
        	$(this).parent().hide();
        });
        
    	var html = LV.getPosBrFormHtml($("." + alertClass).find("#roleType").val());
    	$("." + alertClass).find("#posBrForm").next().find("ul").html(html);
    	$('.' + alertClass).addClass('is-visible');
		LV.initBox(alertClass);
    	LV.initSelect();
    }
    
    function initMainMember(roleId, roleType, roleTypeDiv){
    	var roleInfo = LV.getRoleInfo(roleId, roleType);
    	$.each(roleInfo,function(name,value) {
    		var spanTextObj = $(roleTypeDiv).find("span[data-id='" + name + "']");
			if(spanTextObj != undefined && value != ""){
				$(spanTextObj).text(value);
			}else{
				$(spanTextObj).text("");
			}
			var valObj = $(roleTypeDiv).find("#" + name);
			if(valObj != undefined && value != ""){
				$(valObj).val(value);
			}else{
				$(valObj).val("");
			}
    	});
    	
        var dataVal = $(roleTypeDiv).find("#residenceProv").val();
        var dicData = LV.getDicData("enterpriseDicData", dataVal);
    	var areaHtml = "";
    	$.each(dicData, function(value, text){
    		areaHtml += "<li data-value=\"" + value + "\">" + text + "</li>"
    	})
    	$(roleTypeDiv).find("#residenceCity").next().find("ul").html(areaHtml);
    }
   
    /**
     * 完善股东信息 - 主要人员 - 保存
     */
    $(".btn.meminfo.save").click(function(){
    	// 校验
    	var validateArray = new Array();
		validateArray.push($("[data-info='partnerInfo'].is-visible"));
		if(!JX.validateArray(validateArray)){
			return;
		}
    	
    	var checkFlag = false;
    	$("[data-info='partnerInfo'].is-visible").each(function(index, element){
			var partnerInfo = {};
			// 角色
			var roleType = $(element).attr("data-roleType");
			// 主体信息
			var idNum = $(element).find("input[data-info='partnerInfo:main']#idNum").val();
			var name = $(element).find("input[data-info='partnerInfo:main']#name").val();
			var obj = LV.entMainMemberRule(idNum, roleType);
			if(obj.result == false){
				alert(name + "不能担任此职务");
				checkFlag = true;
				return;
			}
    	});
    	if(checkFlag == true){
    		return false;
    	}
    	
    	LV.enterpriseRoleSave();
    	LV.enterpriseSave();
    	$(this).parents(".cd-popup[role='alert']").removeClass('is-visible');
    	window.location.reload();
    	return true;
    });
    
    /**
     * 完善股东信息 - 保存
     */
    $(".btn.mainMember.save").click(function(){
    	// 更新出资总额
    	LV.updateRegCapital();
    	
    	// 保存
    	LV.enterpriseSave();
    	alert("保存成功");
    	return true;
    });
    
    /**
     * 完善股东信息 - 下一步
     */
    $(".btn.mainMember.next").click(function(){
    	// 更新出资总额
    	LV.updateRegCapital();
    	LV.enterpriseSave();
    	var roleRuleInfo = LV.entAllRoleRule();
    	// 后台校验
    	if(roleRuleInfo != undefined && roleRuleInfo != ""){
    		alert(roleRuleInfo);
    		return;
    	}
		LV.enterpriseSubmit();
		window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId;
    	return true;
    });
    
    /**
     * 完善地址 - 保存
     */
    $(".btn.address.save").click(function(){
    	LV.enterpriseSave();
    	alert("保存成功");
    	return true;
    });
    
    /**
     * 完善地址 - 下一步
     */
    $(".btn.address.next").click(function(){
    	// 校验
    	var validateArray = new Array();
		validateArray.push($(".dj-online"));
		if(!JX.validateArray(validateArray)){
			return;
		}
    	LV.enterpriseSave();
		LV.enterpriseSubmit();
		window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId;
    	return true;
    });
    
    /**
     * 补充信息 - 保存
     */
    $(".btn.other.save").click(function(){
    	LV.enterpriseRoleSave();
    	LV.enterpriseSave();
    	alert("保存成功");
    	return true;
    });
    
    /**
     * 补充信息 - 下一步
     */
    $(".btn.other.next").click(function(){
    	// 校验
    	var validateArray = new Array();
		validateArray.push($(".fill-con.clear"));
		if(!JX.validateArray(validateArray)){
			return;
		}
    	var staffTotalNum = $("#staffTotalNum").val(); // 总人数
    	var staffLocalNum = $("#staffLocalNum").val(); // 本地人数
    	var staffOutNum = $("#staffOutNum").val(); // 外地人数
    	if(Number(staffLocalNum) + Number(staffOutNum) != Number(staffTotalNum)){
    		alert("企业总人数 = 外地人数 + 本地人数");
    		return;
    	}
    	
    	LV.enterpriseRoleSave();
    	LV.enterpriseSave();
    	LV.enterpriseSubmit();
//		LV.wfSignlReceiveTask("lockBusinessKey" , nextBusinessKey);
		window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId;
    	return true;
    });
    
    /**
     * 补充信息 - 提交
     */
    $(".btn.other.submit").click(function(){
    	$("#submitDataDiv").show();
    	LV.enterpriseSave();
    	LV.enterpriseSubmit("checkReason");
//		LV.wfSignlReceiveTask("lockBusinessKey" , nextBusinessKey);
		window.location.href = "/mywf/company/detail/" + procInstId + "/" + taskId;
    	return true;
    });
    
    /**
     * 文件下载 - 我已完成
     */
    $(".btn.setupDownload.submit").click(function(){
		LV.wfSignlReceiveTask();
		LV.enterpriseSubmit();
		window.location.href = "/mywf/company/reg/" + procInstId;
    	return true;
    });

});



/**
 * 初始化经营范围事件
 */
function initOperatingRangeEvent(){
   $('.type input').on('click',function(e){
       var $this = $(this);
       var $span = $this.parent();
       var $parent = $span.parent();
       var $showArea = $('#' + $parent.attr('bindShowArea'));
       var namespace = $parent.attr('namespace');
       var text = $span.find("label").text();
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
       var text = $this.find("label").text();
       $checkbox.prop('checked',!$checkbox.prop('checked'));
       showOrRemoveSelector($checkbox,$showArea,namespace,text);
   });
   // 蓝色显示区域
}
/**
* 初始化，选中的经营范围
*/
function initSelectedOperatingRange(){
	// 判断是否已经选中
	$(".jy-box .main .right p").each(function(index, ele){
		var val = $(ele).find("input").val();
		var selecedObj = $("#scopeBusiness").find("li[bindvalue='" + val + "']");
		if(selecedObj != undefined && selecedObj.length > 0){
			$(ele).find("input").attr("checked", true);
		}
	});
}

//蓝色显示区域
function showOrRemoveSelector($checkbox,$showArea,namespace,text) {
	var value = $checkbox.val();
	if ($checkbox.get(0).checked) {
		var selectedVal = $("#scopeBusiness").find("li[bindvalue='" + value + "']");
		if(selectedVal != null && selectedVal.length > 0){
			return;
		}
		var str = '<li class="bgBlue" bindValue="' + value +'" namespace="' + namespace + '">' + text + '</li>';
		$showArea.append(str);
	} else {
		$showArea.children('[namespace="'+namespace+'"][bindValue="'+value+'"]').remove();
	}
}



