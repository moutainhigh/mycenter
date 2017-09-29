$(document).ready(function(){
	var GOV = {
			
			/**
			 * 注册丰台用户
			 */
			postRegistAppointUser:function(){
				var param = "&enterpriseId=" + enterpriseId;
				var value = true;
				$.ajax({
					url:"/gov/business/appoint/registUser",
					type:'post',    
				    cache:false,
				    data: param,
				    dataType:'json',
				    async:false,
				    complete:function(data){
						// 丰台用户注册失败
				    	if(data != undefined && data.responseText == "error" ){
				    		value = false;
				    	}
				    }
				});
				return value;
			},
			
			postRegistUser:function(cerNo, userName){
				var param = "";
				if(cerNo != undefined && cerNo != ""){
					param += "&cerNo=" + cerNo;
				}
				if(userName != undefined && userName != ""){
					param += "&userName=" + userName;
				}
				if(businessKey != undefined && businessKey != ""){
					param += "&businessKey=" + businessKey;
				}
				if(enterpriseId != undefined && enterpriseId != ""){
					param += "&enterpriseId=" + enterpriseId;
				}
				
				var value = "";
				$.ajax({
					url:"/gov/business/postRegistUser",
					type:'post',    
				    cache:false,
				    data: param,
				    dataType:'json',
				    async:true,
				    beforeSend:function(){
						$(".cd-popup1.cd-box").addClass('is-visible');
				    },
					complete:function(data){
						$(".cd-popup1.cd-box").removeClass('is-visible');
						
						if(data != undefined && data != "" 
							&& data.responseText != undefined 
							&& data.responseText != "undefined"
							&& data.responseText != ""){
							var reason = $.parseJSON(data.responseText);
				        	$(".cd-popup.cd-box1").removeClass('is-visible');
				        	if(reason != undefined && reason != "" && reason.result == "true"){
				        		$(".cd-popup.pop_box1111").addClass('is-visible');
				        	}else{
				        		if(reason.msg == "当前身份证号已有用户，请重新输入身份证号。"){
				        			$(".cd-popup.cd-box13233").show();
				        			$(".cd-popup.cd-box13233").addClass('is-visible');
				        		}else{
				        			alert(reason.msg);
				        		}
				        	}
						}else{
							alert("系统错误，请刷新后重试。")
						}
					}
				});
			},
			
			checkUserAndPwd:function(userName, passWord){
				var param = "";
				if(userName != undefined && userName != ""){
					param += "&userName=" + userName
				}
				if(passWord != undefined && passWord != ""){
					param += "&passWord=" + passWord
				}
				var value = false;
				$.ajax({
					url:"/gov/business/checkUserAndPwd",
					type:'post',    
				    cache:false,
				    data: param,
				    dataType:'json',
				    async:false,
					complete:function(data){
						if(data != undefined && data != "" 
							&& data.responseText != undefined 
							&& data.responseText != "undefined" ){
							if(data.responseText == "true"){
								value= true;
							}
						}
					}
				});
				return value;
			},
			
			
			postGovLogin:function(userName, passWord){
				var param = "";
				if(userName != undefined && userName != ""){
					param += "&userName=" + userName
				}
				if(passWord != undefined && passWord != ""){
					param += "&passWord=" + passWord
				}
				$.ajax({
					url:"/gov/business/postGovLogin",
					type:'post',    
				    cache:false,
				    data: param,
				    dataType:'json',
				    async:false,
					complete:function(data){
						if(data != undefined && data != "" 
							&& data.responseText != undefined 
							&& data.responseText != "undefined" ){
							var data = $.parseJSON(data.responseText);
							$.removeCookie(data.key);
							$.cookie(data.key, data.value, {path:'/', domain:'.'} ); 
						}
					}
				});
			},
			foundCheck:function(shopName, industryCharacteristics, fullname, mainBusinessCode, mainBusinessUniteCode){
				var nowDate = new Date().Format("yyyy-MM-dd hh:mm:ss");
				var param = "&shijian=" + nowDate + "&superNameId=''";
				if(shopName != undefined ){
					param += "&zihao=" + shopName;
				}
				if(industryCharacteristics != undefined){
					param += "&hangye=" + industryCharacteristics;
				}
				if(fullname != undefined){
					param += "&quanming=" + fullname;
				}
				if(mainBusinessCode != undefined){
					param += "&hydm=" + mainBusinessCode;
				}
				if(mainBusinessUniteCode != undefined){
					param += "&hbdm=" + mainBusinessUniteCode;
				}
				if(shopName != undefined){
					param += "&zihao=" + shopName;
				}
				if(shopName != undefined && industryCharacteristics != undefined){
					param += "&zihaohangye=" + shopName + industryCharacteristics;
				}
				var value = "";
				$.ajax({
					url:"/gov/business/postGovFoundcheck",
					type:'post',    
				    data:param,
				    dataType : 'json',  
				    async:true,
				    timeout:30000,
				    beforeSend:function(){
						$(".cd-popup1.cd-box").addClass('is-visible');
				    },
				    error:function(){
				    	alert("网络超时，请稍后再试。");
				    	$(".cd-popup1.cd-box").removeClass('is-visible');
				    },
					complete:function(data){
						$(".cd-popup1.cd-box").removeClass('is-visible');
						if(data != undefined && data != "" 
							&& data.responseText != undefined 
							&& data.responseText != "undefined" ){
							var reason = $.parseJSON(data.responseText);
							$("#foundCheckOkDiv").hide();
							var html = "";
							if(reason.errorNum == 0){
								// 预核名通过
								html += "<p class=\"che-on\">您的名称初步检查通过,名称审查结果以提交后人工审查结果为准!</p>";
								$("#foundCheckOkDiv").show();
							}else if(reason.cc != undefined && reason.cc.length > 0){
								// 不通过
								html += "<p class=\"che-off\">您的名称检查未通过，有如下问题：</p>";
								for(var i=0; i<reason.cc.length;i++){
									html += "<p class=\"che-off-text\">" + reason.cc[i].rule + "：" + reason.cc[i].entName + "</p>";
								}
							}else if(reason.jyz != undefined && reason.jyz.length > 0){
								// 不通过
								html += "<p class=\"che-off\">您的名称检查未通过，有如下问题：</p>";
								for(var i=0; i<reason.jyz.length;i++){
									html += "<p class=\"che-off-text\">" + reason.jyz[i].rule + "：" + reason.jyz[i].msg + "</p>";
								}
							}
							$("#foundCheckReasonDiv").show();
							$("#foundCheckReasonDiv").html(html);
						}
					}
				});
			},
			/**
			 * 取消工商预约
			 */
			appointCancel:function(){
				var param = "&enterpriseId=" + enterpriseId;
				$.ajax({
					url:"/gov/business/appointCancel",
					type:'post',    
				    data:param,
				    dataType : 'json',  
				    async:false,
					complete:function(data){
						
					}
				});
			}
			
			
	};
	window.location.GOV =window.GOV = GOV;
}());