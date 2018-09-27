var url = "userlist1";
var columns = [
	            { "data": null },
	            { "data": "realName" },
	            { "data": "code" },
	            { "data": null },
	            { "data": null }
	           ];
var columnDefs = [{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 0,
		    render: function(data, type, row, meta) {
		        return '<input style="position:static;opacity:1;" type="checkbox" name="userId" value="' + row.user_id + '" />'
		    }
		},
		{
			targets: 3,
		    render: function(data, type, row, meta) {
		        return '<a color="blue"'
				+ 'href="userRoleEdit?userId=' + row.user_id + '&pageNumber=${pageNumber}">' + row.roleName 
				+ ' </a></td>';
		    }
		},
		{
			targets: 4,
		    render: function(data, type, row, meta) {
		        return '<a href="userEdit?userId=' + row.user_id + '" class="btn btn-default btn-xs purple"><i class="fa fa-edit"></i>编辑</a>'
                     + '<a href="javascript:ajax_userPwd(' + row.user_id + ')" class="btn btn-default btn-xs black"><i class="fa fa-search-plus"></i> 初始化密码</a> ';
		    }
		}
		];