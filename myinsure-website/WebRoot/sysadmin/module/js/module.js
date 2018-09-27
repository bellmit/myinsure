function ajax_add() {
	var moduleName = $("#moduleName").val();
	alert(moduleName);
	if (!moduleName) {
		alert("模块名称不能为空");
		return false;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "moduleAddSave",
		data : $('#form').serialize(),
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "moduleList";
		}
	});

}

function delPic() {
	$('#pic').val(img);
	var img = "${images_ctx}" + '/wzgl.png';
	$('#fileName').attr('src', img);
}

function ajax_delete(id) {
	if (!confirm("确定删除？")) {
		return;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "moduleDelete",
		data : "id=" + id,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "moduleList";
		}
	});
}

function check_all() {
	$("input[name='moduleId']").prop("checked", $("#chk_all").prop("checked"));
}

function delBatch() {
	if ($("input[name='moduleId']:checked").length < 1) {
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
			window.location.href = "moduleList";
		}
	});
}

function ajax_update() {
	var moduleName = $("#moduleName").val();
	if (!moduleName) {
		alert("模块名称不能为空");
		return false;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "moduleEditSave",
		data : $('#form').serialize(),
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			alert(data.msg);
			window.location.href = "moduleList";
		}
	});
}

function sorts(id){
	var sorts=$("#queryText_"+id).val();
	  if(!sorts){
		  alert("排序项不能为空")
		  return false
	  }
	  var str="id="+id+"&sorts="+sorts
	$.ajax({
		cache:true,
		type:"POST",
		url:"sortUpdate",
		data:{"id":id,"sorts":sorts},
		error :function(request){
			aletr("链接错误");
		},
		success:function(data){
			if(data.msg==="修改成功"){
				layer.msg('修改成功');
			}else{
				var layer = layui.layer;
				layer.msg('修改失败');
			}
			window.location.href = "moduleList";
		}
	})
	}

function fanhui() {
	// window.location.href = window.history.go(-1);
	window.location.href = "moduleList";
}
