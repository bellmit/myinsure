var url = "getCarousels1";
var columns = [
	            { "data": null },
	            { "data": "carousel_name" },
	            { "data": "product_manage_id" },
	            { "data": "is_used" },
	            { "data": null }
	           ];
var columnDefs = [{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 0,
		    render: function(data, type, row, meta) {
		        return '<input style="position:static;opacity:1;" type="checkbox" name="carouselId" value="' + row.carousel_id + '" />'
		    }
		},
		{
			targets: 2,
		    render: function(data, type, row, meta) {
		    	var content = "";
		    	if(row.product_manage_id == 0){
		    		content = "首页轮播图";
		    	}else if(row.product_manage_id == 1){
		    		content = "新闻动态轮播图";
		    	}else if(row.product_manage_id == 2){
		    		content = "集团新闻图";
		    	}
		    	return content;
		    }
		},
		{
			targets: 3,
			render: function(data, type, row, meta) {
				var content = "";
				if(row.is_used == 0){
					content = '<span style="color: red;">未用</span>';
				}else{
					content ='<span style="color: green;">已用</span>';
				}
				return content;
			}
		},
		{
			targets: 4,
		    render: function(data, type, row, meta) {
		    	var content = '<a href="carouselEdit?carouselId=' + row.carousel_id + '" class="button button-3d  button-box button-tiny">'
							+ 		'<i class="layui-icon">&#xe642;</i> 编辑'
							+ '</a>&nbsp;&nbsp;';
		        if(row.product_manage_id == 0){
		        	content += '<a href="javascript:carouselUpper(' + row.carousel_id + ')" class="button button-3d button-primary button-box button-tiny">'
		        		+ '<i class="layui-icon">&#xe62f;</i> 启用'
		        		+ '</a>&nbsp;&nbsp;'
		        		+ '<a href="javascript:carouselUnder(' + row.carousel_id + ')" class="button button-3d button-caution button-box button-tiny">'
		        		+ '<i class="layui-icon">&#xe601;</i> 弃用'
		        		+ '</a>&nbsp;&nbsp;';
		        }else{
		        	content += '<a href="javascript:imageUpper(' + row.carousel_id + ')" class="button button-3d button-primary button-box button-tiny">'
		        		+ '<i class="layui-icon">&#xe62f;</i> 启用'
		        		+ '</a>&nbsp;&nbsp;';
		        }
		    	return content;
		    }
		}
		];