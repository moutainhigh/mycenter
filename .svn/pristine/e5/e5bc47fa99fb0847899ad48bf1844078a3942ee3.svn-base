var InterValObj; //timer变量，控制时间  
var count = 120; //间隔函数，1秒执行  
var curCount;//当前剩余秒数  
//var code = ""; //验证码
//var codeLength = 6;//验证码长度
var i = 0;
jQuery.extend({
	sendMessage : function(phoneNum){
		if(phoneNum == "" || !(/^1[3|4|5|7|8][0-9]\d{8}$/.test(phoneNum))){
			alert("输入的手机为空或格式不正确!");
			return false;
		}else{
			curCount = count;
			if (i == 0) {
				i++;
				// 设置button效果，开始计时  
				$(".get-code").text("剩余" + curCount + "s");
				InterValObj = window.setInterval(jQuery.SetRemainTime, 1000); // 启动计时器，1秒执行一次  
				// 向后台发送处理数据  
				$.ajax({
					type : "post", // 用POST方式传输  
					dataType : "json", // 数据格式:text
					url : "/common/sendPhoneCode", // 目标地址  
					data : "phoneNum=" + phoneNum,
					success : function(data) {
						if (data.flag == "-1") {
							alert("验证码发送失败!请重新发送");
						} else if (data.flag == "2") {
							alert("已为您发送语音验证码, 请注意查收!");
						}
					},
					error:function(){
						alert("获取手机号失败!");
					}
				});
			}
		}
	},
	SetRemainTime : function(){
		if (curCount == 0) {
			window.clearInterval(InterValObj); // 停止计时器  
			$(".get-code").text("重新发送");
			i = 0;
		} else {
			curCount--;
			$(".get-code").text("剩余" + curCount + "s");
		}
	},
	/***
	 * 校验手机发送的验证码
	 * @return true 输入验证码正确 false 输入验证码错误
	 */
	checkPhoneCode : function(phoneNum,code){
		if(phoneNum == "" || !(/^1[3|4|5|7|8][0-9]\d{8}$/.test(phoneNum))){
			alert("输入的手机为空或格式不正确!");
			return false;
		}
		if(code == "" && code.length != 6){
			alert("输入的验证码为空或格式不正确!");
			return false;
		}
		
		var flag = true;
		// 向后台发送处理数据  
		$.ajax({
			async : false, //同步
			type  : "post", // 用POST方式传输  
			dataType : "json", // 数据格式:text
			url : "/common/checkPhoneAndCode", // 目标地址  
			data : {"phoneNum":phoneNum,"code":code},
			success : function(data) {
				if (data.success == "false") {
					flag = false;
					alert("输入的验证码不正确!");
				}
			},
			error:function(){
				alert("获取手机号或验证码失败!");
			}
		});
		return flag;
	},
	/**
	 * 校验手机格式
	 */
	checkPhoneFormat : function(phoneNum){
		if(phoneNum == "" || !(/^1[3|4|5|7|8][0-9]\d{8}$/.test(phoneNum))){
			alert("输入的手机为空或格式不正确!");
			return false;
		}
		return true;
	}
});