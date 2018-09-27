function ajax_add(roleId) {
	$.ajax({
		cache : true,
		type : "POST",
		url : "roleFunAddSave",
		data : $('#form').serialize(),
		async : false,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "roleFunList?id=" + roleId;

		}
	});

}
function ajax_add() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "roleFunAddSave",
		data : $('#form').serialize(),
		async : false,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {

			alert(data.msg);
			window.location.href = "roleList";
		}
	});
}
function ajax_delete(id, roleId) {
	if (!confirm("确定删除？")) {
		return;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "roleFunDelete",
		data : "id=" + id,
		async : false,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "roleFunList?id=" + roleId;
		}
	});
}

function ajax_edit(roleId) {
	$.ajax({
		cache : true,
		type : "POST",
		url : "roleFunEditSave",
		data : $('#form').serialize(),
		async : false,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {

			alert(data.msg);
			window.location.href = "roleFunList?id=" + roleId;

		}
	});
}

function fanhui() {
	// window.location.href = window.history.go(-1);
	window.location.href = "roleList";
}