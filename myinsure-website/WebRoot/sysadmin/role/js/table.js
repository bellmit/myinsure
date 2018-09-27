var url = "roleList1";
var columns = [
	            { "data": null },
	            { "data": "role_name" },
	            { "data": "role_memo" },
	            { "data": null },
	            { "data": null }
	           ];
var columnDefs = [{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 0,
		    render: function(data, type, row, meta) {
		        return '<input style="position:static;opacity:1;" type="checkbox" name="roleId" value="' + row.role_id + '" />'
		    }
		},
		{
			targets: 3,
		    render: function(data, type, row, meta) {
		        return '<a href="roleFunList?id=' + row.role_id + '">查看编辑权限</a>';
		    }
		},
		{
			targets: 4,
		    render: function(data, type, row, meta) {
		        return '<a href="roleEdit?rid=' + row.role_id + '" class="btn btn-default btn-xs purple"><i class="fa fa-edit"></i>编辑</a>';
		    }
		}
		];