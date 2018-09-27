var url = "moduleList1";
var columns = [
	            { "data": null },
	            { "data": "module_code" },
	            { "data": "module_name" },
	            { "data": "order_numb" },
	            { "data": "icons" },
	            { "data": null }
	           ];
var columnDefs = [{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 0,
		    render: function(data, type, row, meta) {
		        return '<input style="position:static;opacity:1;" type="checkbox" name="moduleId" value="' + row.module_id + '" />'
		    }
		},
		{
			targets: 5,
		    render: function(data, type, row, meta) {
		        return '<a href="moduleEdit?id=' + row.module_id + '" class="btn btn-default btn-xs purple"><i class="fa fa-edit"></i>编辑</a>';
		    }
		}
		];