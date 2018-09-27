$(document).ready(function() {
	var mores = $('.more');
	mores.each(function(i) {
		var more = mores[i];
		$(more).mouseover(function() {
			$(this).find('.tip').css('display', 'block');
		});
		$(more).mouseleave(function() {
			$(this).find('.tip').css('display', 'none');
			$(this).find('.tip').find('.sub_tip').css('display', 'none');
		});
	});
	var moveTos = $('.move-to');
	moveTos.each(function(i) {
		$(this).click(function() {
			var offsetTop = $(this).position().top;
			$(this).find('.sub_tip').css({
				'display' : 'block',
				'top' : offsetTop + 3
			});

		});
	});
	$('.sub_tip').mouseleave(function() {
		$('.sub_tip').css('display', 'none');
	});
	var liItems = $('.sub_tip').find('li');
	liItems.each(function(i) {
		$(this).click(function() {
			var parentid = $(this).attr('parentid');
			var childid = $(this).attr('childid');
			alert(parentid);
			alert(childid);
			$.ajax({
				cache : true,
				type : "POST",
				url : "moveToOther",
				data : {
					parentid : parentid,
					childid : childid
				},
				dataType : json,
				async : false,
				error : function(request) {
					alert("连接错误");
				},
				success : function(data) {
					alert(data.msg);
					window.location.href = "columnList";
				}
			});
		});
	});

});
// 子菜单的打开关闭
function change_pic(columnid) {
	$('.grade-' + columnid).toggle();// 当前点击元素下的子菜单class选择器
	var parentid = $($('.grade-' + columnid).find('input')[0]).val();// 当前点击元素的子菜单的id值，其作为下一级菜单的class的后缀
	if ($('.grade-' + columnid).is(":visible")) {// 如果内容可见
		$(event.target).attr('src', '/myinsure-website/sysadmin/images/anxia.gif');
	} else {
		$(event.target).attr('src', '/myinsure-website/sysadmin/images/anyou.gif');
		$('.grade-' + parentid).hide();
		$('.grade-' + columnid).find('.parent').attr('src',
				'/myinsure-website/sysadmin/images/anyou.gif');
	}
}

function check_all() {
	$("input[class='column']").prop("checked", $("#chk_all").prop("checked"));
}

// 批量删除
function delBatch() {
	if ($("input[class='column']:checked").length < 1) {
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
			alert(data);
			window.location.href = "columnList";
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
		url : "columnDel",
		data : "id=" + id,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			if (null != data.error) {
				return false;
			} else {
				layui.use('layer', function() {
					var layer = layui.layer;

					layer.msg('删除成功');
				});
				window.location.href = "columnList";
			}
		}
	});
}

// 添加一级菜单
var count = 0;// a:代表添加菜单的个数
function addMenu(parentid, level) {
	count++;
	var tr = "<tr id='tr_" + count + "'>"
			+ "<td><input type='checkbox' name='id_" + count + "' value='' lay-ignore/>";
	if (level != 1) {
		tr += "<input type='hidden' name='wap_nav_" + count + "' value='"
				+ parentid + "'>";
	}
	tr += "</td>" + "<td><input type='text' class='layui-input' name='orders_"
			+ count + "' value='' /></td>" + "<td>";
	if (level == 1) {
		tr += "<img src='../../images/anxia.gif' class='parent' onclick='change_pic()' id='row123' style='margin-right:9px;visibility:hidden;'/>";
	}
	if (level == 2) {
		tr += "<img src='../../images/bg_column.gif' style='vertical-align:middle;margin-right:3px;' />";
	}
	if (level == 3) {
		tr += "<img src='../../images/bg_column1.gif' style='vertical-align:middle;margin-right:4px;'/>";
	}
	tr += "<input type='text' class='layui-input' name='name_"
			+ count
			+ "' value='' style='display:inline; width:30%;'/>"
			+ "<div style='clear:both;'></div>"
			+ "<input type='hidden'  class='id' value=''/>"
			+ "</td>"
			+ "<td>"
			+ "<select name='nav_"
			+ count
			+ "' lay-verify='required'>"
			+ "<option value='0'>不显示</option>"
			+ "<option value='1'>导航栏显示</option>"
			+ "</select>"
			+ "</td>"
			+ "<td>"
			+ "<select name='faclass_"
			+ count
			+ "' lay-verify='required'>"
			+ "<option value='首页模版'>首页模版</option>"
			+ "<option value='列表模版1'>列表模版1</option>"
			+ "<option value='列表模版2'>列表模版2</option>"
			+ "<option value='文章模版'>文章模版</option>"
			+ "</select>"
			+ "</td>"
//			+ "<td>"
//			+ "<input type='text' class='layui-input' name='out_url_"
//			+ count
//			+ "' value='' />"
//			+ "</td>"
			+ "<td>"
			+ "<input type='text' class='layui-input' name='foldername_"
			+ count
			+ "' value='' />"
			+ "</td>"
			+ "<td onclick='deltr(this)'>"
			+ "<a href='#' class='button button-3d button-caution button-box button-tiny'><i class='layui-icon'>&#xe642;</i> 撤销</a>"
			+ "</td>" + "</tr>";
	if (level == 1) {
		$('#maint').append(tr);
	} else {
		$('#tr' + parentid).after(tr);
	}
	layui.use([ 'element', 'form' ], function() {
		var element = layui.element, form = layui.form;
		form.render();
	})
}
// 撤销添加节点
function deltr(obj) {
	count--;
	$(obj).parent().remove();
}
// 保存菜单
function ajax_save() {
	$('#count').val(count);
	$.ajax({
		cache : true,
		type : "POST",
		url : "saveorupdateColumn",
		data : $('#form').serialize(),
		error : function(request) {
			layui.use('layer', function() {
				var layer = layui.layer;
				layer.msg("连接错误");
			});
		},
		success : function(data) {
			if (null != data.error) {
				return false;
			} else {
				layui.use('layer', function() {
					var layer = layui.layer;
					layer.msg('修改成功');
				});
				window.location.reload();// ="columnList";
			}
			;
		}
	});
}
