(function ($) {
	$.extend($.validator.messages, {
		required: "必填",
		remote: "請修正此欄位",
		email: "請輸入正確的電子信箱",
		url: "請輸入合法的URL",
		date: "請輸入合法的日期",
		dateISO: "請輸入合法的日期 (ISO).",
		number: "請輸入數字",
		digits: "請輸入整數",
		creditcard: "請輸入合法的信用卡號碼",
		equalTo: "請重複輸入一次",
		accept: "請輸入有效的後缀字串",
		ip:"請輸入合法的IP地址",
		abc:"請輸入字母數字或下劃線",
		username:"3-20位字母或數字開頭，允許字母數字下劃線",
		noEqualTo:"請再次輸入不同的值",
		realName:"姓名只能為2-30個漢字",
		userName:"登錄名只能包括中文字、英文字母、數字和下劃線",
		mobile:"請正確填寫您的手機號碼",
		simplePhone: "請正確填寫您的電話號碼",
		phone:"格式為:固話為區號(3-4位)號碼(7-9位),手機為:13,15,18號段",
		zipCode:"請正確填寫您的郵政編碼",
		qq:"請正確填寫您的QQ號碼",
		card:"請輸入正確的身份證號碼(15-18位)",
		maxlength: $.validator.format("請輸入長度不大於{0} 的字串"),
		minlength: $.validator.format("請輸入長度不小於 {0} 的字串"),
		rangelength: $.validator.format("請輸入長度介於 {0} 和 {1} 之間的字串"),
		range: $.validator.format("請輸入介於 {0} 和 {1} 之間的數值"),
		max: $.validator.format("請輸入不大於 {0} 的數值"),
		min: $.validator.format("請輸入不小於 {0} 的數值")
	});
}(jQuery));