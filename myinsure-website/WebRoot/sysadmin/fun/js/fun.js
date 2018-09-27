function ajax_add() {
	var funName = $("#funName").val();
	var re = /^[0-9]*$/;
	var pai = form.funOrder.value;
	var obj = form.funUrl.value;
	if (!funName) {
		alert("页面名称不能为空");
		return false;
	}
	if (!re.test(pai)) {
		alert("排序项请输入数字");
		return false;
	}
	if (/.*[\u4e00-\u9fa5]+.*$/.test(obj)) {
		alert("页面链接不能含有汉字！");
		return false;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "funAddSave",
		data : $('#form').serialize(),
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {

			alert(data.msg);
			window.location.href = "funList";
			
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
		url : "funDelete",
		data : "fid=" + id,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
//			window.location.href = "funList";
			$("#commontable").DataTable().ajax.reload();
		}
	});
}

function ajax_edit() {
	var funName = $("#funName").val();
	var re = /^[0-9]*$/;
	var pai = form.funOrder.value;
	var obj = form.funUrl.value;
	if (!funName) {
		alert("页面名称不能为空");
		return false;
	}
	if (!re.test(pai)) {
		alert("排序项请输入数字");
		return false;
	}
	if (/.*[\u4e00-\u9fa5]+.*$/.test(obj)) {
		alert("页面链接不能含有汉字！");
		return false;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "funEditSave",
		data : $('#form').serialize(),
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			window.location.href = "funList";
		}
	});
}

function check_all() {
	$("input[name='funId']").prop("checked", $("#chk_all").prop("checked"));
}

function delBatch() {
	if ($("input[name='funId']:checked").length < 1) {
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
//			window.location.href = "funList";
			$("#commontable").DataTable().ajax.reload();
		}
	});
}

function sort(id){
	var sort=$("#queryText_"+id).val();
	if(!sort){
		 alert("排序项不能为空")
		  return false
	}
	$.ajax({
		cache:true,
		type:"POST",
		url:"sortUp",
		data:{"id":id,"sort":sort},
		error :function(request){
			aletr("链接错误");
		},
		success:function(data){
			if(data.msg==="修改成功"){
				alert('修改成功');
			}else{
				alert('修改失败');
			}
			window.location.href = "funList";
		}
	})
}

function fanhui() {
	// window.location.href = window.history.go(-1);
	window.location.href = "funList";
}