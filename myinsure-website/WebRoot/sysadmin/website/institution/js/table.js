var url = "getLists";
var columns = [
	            { "data": null },
	            { "data": "ins_name" },
	            { "data": "ins_address" },
	            { "data": "ins_phone" },
	            { "data": null }
	           ];
var columnDefs = [{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 0,
		    render: function(data, type, row, meta) {
		        return '<input style="position:static;opacity:1;" type="checkbox" name="id" value="' + row.id + '" />'
		    }
		},{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 1,
		    render: function(data, type, row, meta) {
		        return  row.ins_name;
		    }
		},
		{
			targets: 2,
		    render: function(data, type, row, meta) {
		    	return row.ins_address;
		    }
		},
		{
			targets: 3,
			render: function(data, type, row, meta) {
				return row.ins_phone;
			}
		},
		{
			targets: 4,
		    render: function(data, type, row, meta) {
		    	var content = '<a href="update_page?id=' + row.id + '" class="button button-3d  button-box button-tiny">'
							+ 		'<i class="layui-icon">&#xe642;</i> 编辑'
							+ '</a>&nbsp;&nbsp;';
		    	return content;
		    }
		}
		];