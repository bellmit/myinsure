function ajax_edit() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "userRoleEditSave",
		data : $('#form').serialize(),
		async : false,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {

			alert(data);
			window.location.href = "userlist";

		}
	});
}