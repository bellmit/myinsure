function ajax_add() {
	var usercode = $("#userCode").val();
	if (!usercode) {
		alert("登录账号不能为空");
		return;
	}
	var userDuty = $("#userDuty").val();
	if (userDuty > 48) {
		alert("职责输入过长");
		return;
	}
	var userMemo = $("#userMemo").val();
	if (userMemo > 480) {
		alert("备注输入过长");
		return;
	}

	$.ajax({
		cache : true,
		type : "POST",
		url : "userAddSave",
		data : $('#form').serialize(),
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "userlist";
		}
	});
}

// 批量删除
function delBatch() {
	if ($("input[name='userId']:checked").length < 1) {
		alert('请选择');
		return;
	}
	if (!confirm('确定删除吗？')) {
		return;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "delBatch",
		data : $('#form').serialize(),
		async : false,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "userlist";
		}
	});
}

// 刷新页面
function refreshView() {
	window.location.reload();
}

function ajax_delete(id, pageNumber) {
	if (!confirm("确定删除？")) {
		return;
	}
	this.pageNumb = pageNumber || $("#pageNumber").val();

	$.ajax({
		cache : true,
		type : "POST",
		url : "userDelete",
		data : "id=" + id,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			if (null != data.error) {
				return false;
			} else {
				window.location.href = "userlist";
			}
		}
	});
}

function ajax_edit() {
	// var userPwd = $("#userPwd").val();
	/*
	 * if(!userPwd){ alert("密码不能为空"); return; }
	 */
	var userDuty = $("#userDuty").val();
	if (userDuty > 48) {
		alert("职责输入过长");
		return;
	}
	var userMemo = $("#userMemo").val();
	if (userMemo > 480) {
		alert("备注输入过长");
		return;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "userEditSave",
		data : $('#form').serialize(),
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "userlist";
		}
	});
}

function reload() {

	window.location.href = "userlist?pageNumber=" + pageNumb;
}

function check_all() {
	$("input[name='userId']").prop("checked", $("#chk_all").prop("checked"));
}

function ajax_edit_role() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "userRoleEditSave",
		data : $('#form').serialize(),
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "userlist";
		}
	});
}

function ajax_userPwd(uid) {
	$.ajax({
		cache : true,
		type : "POST",
		url : "userPwd?userId=" + uid,
		data : $('#form').serialize(),
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "userlist";
		}
	});
}

function fanhui() {
	window.location.href = window.history.go(-1);
	// window.location.href="userlist";
}