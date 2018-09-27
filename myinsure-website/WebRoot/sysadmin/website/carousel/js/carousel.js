	layui.use([ 'element','form', 'layedit', 'laydate','layer','laypage' ],function() {
			var element = layui.element;
			var form = layui.form; 
			var layer = layui.layer; 
			var layedit = layui.layedit; 
			var laydate = layui.laydate;
			var laypage = layui.laypage;
			form.render();
		
	});
//保存轮播图
function saveCarousel(){
	var carousel_name = $('#carousel_name').val();
	var carousel_url = $('#icon').val();
	if(carousel_name == ''){
		alert("轮播图名称不能为空！");
		return false;
	}
	if(carousel_url == ''){
		alert("图片不能为空！");
		return false;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "saveCarousel",
		data : $('#form').serialize(),
		async : false,
		error : function(request) {
			alert("连接错误");
		},
		success : function(data) {
			window.location.href = "getCarousels";	
		}
	});
}
//后台处理照片后重新显示
function upload(obj,ctx) {
	var fileName = $('#imageFile').val();
	$.ajaxFileUpload({
		url : 'uploadCarouselPic', //提交的路径
		secureuri : false, // 是否启用安全提交，默认为false
		fileElementId : 'imageFile', // file控件id
		dataType : 'json',
		data : {fileName:fileName},//传递参数，用于解析出文件名// 键:值，传递文件名
		success : function(data, status) {
			if (data.error == 0) {
				var src = data.src;
				//显示图片
				$('#image').attr('src',ctx + '/front/upload/' + src);
				//保存文件名
				$('#icon').val('/upload/' + src);
				layui.use('layer', function(){
				 	var layer = layui.layer;
				 	layer.msg("上传成功！");	
				 });
			} else {
				layui.use('layer', function(){
				 	var layer = layui.layer;
				 	layer.msg("上传失败！");
				 });
			}
		},
		error : function(data, status) {
			layui.use('layer', function(){
			 	var layer = layui.layer;
			 	layer.msg("连接错误");
			 });
		}
	});
}
//点击时，获取产品列表，并将结果添加到弹框中
function getProducts(pageNum){
	$.ajax({
		cache : true,
		type : "POST",
		url : "getProducts",
		data : {pageNum:pageNum},
		dataType:'json',
		async: false,
		error : function(request) {
			layer.msg("连结错误！");
		},
		success : function(data) {
			$('#product-list').find('tr').remove();//移除原来的，然后添加上新查出来的
			var products = data.products;
			$.each(products,function(i){
				var tr = '<tr>'
					       + '<td>' + products[i].product_name + '</td>'
					       + '<td>' + products[i].company_name + '</td>'
					       + '<td>'
					          + '<a href="javascript:addCarousel(' + products[i].product_id + ')"> <i class="layui-icon">&#xe681;</i> 选择</a>'
					          + '<input id="product_name_' + products[i].product_id + '" type="hidden" value="' + products[i].product_name + '"/>'
					          + '<input id="product_url_' + products[i].product_id + '" type="hidden" value="' + products[i].product_url + '"/>'
					       + '</td>'
					   + '/tr';
				$('#product-list').append(tr);
			});
			layui.use([ 'element','laypage', 'layer' ],function() {
				var laypage = layui.laypage; 
				var layer = layui.layer;
				var element = layui.element;

				var num = data.totalPage;
				var totalRow = data.totalRow;
				//调用分页
				laypage.render({
					  elem: 'pager',
					  count: totalRow, //数据总数，从服务端得到
					  limit:10,
					  groups:10,
					  prev:'上一页',
					  next:'下一页',
					  first:'首页',
					  last:'尾页',
					  layout:['count', 'prev', 'page','next', 'skip'],
					  jump: function(obj, first){
						var currentPage = obj.curr;//获取点击的页码   
						if (first != true) {//是否首次进入页面，如果不是首次进入 
							getProducts(currentPage);
						};
					  },
					  curr : function() { //获取当前页  
							var page = data.pageNumber;
							return page ? page : 1;
					 }()
				});
			});
		}
	});
	
}
//链接产品弹框
var layerindex = null;
function linkProduct(){
	getProducts();
	layerindex = layer.open({
	  title:'链接产品',
	  type: 1,
	  area: ['600px','550px'],
	  offset: '100px',
	  content: $('#products'),
	  cancel: function(index, layero){ 
		  $('#products').css('display','none');
		  layer.close(index);
	  }
	}); 
}
//选择链接产品
function addCarousel(productId){
	var product_name = $('#product_name_'+productId).val();
	var product_url = $('#product_url_'+productId).val();
	$('#product_name').val(product_name);
	$('#product_url').val(product_url);
	layer.close(layerindex);
}












