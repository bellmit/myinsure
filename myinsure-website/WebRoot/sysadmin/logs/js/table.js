var url = "getLogs1";
var columns = [
	            { "data": null },
	            { "data": "fun_name" },
	            { "data": "real_name" },
	            { "data": "role_name" },
	            { "data": "clientsideIp" },
	            { "data": "establishTime" },
	           ];
var columnDefs = [{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 0,
		    render: function(data, type, row, meta) {
		        return '<input style="position:static;opacity:1;" type="checkbox" name="id" value="' + row.id + '" />'
		    }
		}
		];