function ajax_add() {

	var roleName = $("#roleName").val();
	if (!roleName) {
		alert("角色名称不能为空");
		return false;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "roleAddSave",
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

function ajax_delete(id) {
	if (!confirm("确定删除？")) {
		return;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "roleDelete",
		data : "id=" + id,
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

function ajax_edit() {

	if ('' == $("#roleName").val() || undefined == $("#roleName").val()) {

		alert("角色名称不能为空");
		return;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "roleEditSave",
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

function check_all() {
	$("input[name='roleId']").prop("checked", $("#chk_all").prop("checked"));
}

function delBatch() {
	if ($("input[name='roleId']:checked").length < 1) {
		alert('请选择');
		return;
	}
	if (confirm('确定删除吗？')) {
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
				window.location.href = "roleList";
			}
		});
	}
}

function fanhui() {
	// window.location.href = window.history.go(-1);
	window.location.href = "roleList";
}