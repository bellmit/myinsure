var url = "funList1";
var columns = [
	            { "data": null },
	            { "data": "fun_name" },
	            { "data": "module_name" },
	            { "data": "url" },
	            { "data": "orders" },
	            { "data": "sfshow" },
	            { "data": "fun_id" }
	           ];
var columnDefs = [{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 0,
		    render: function(data, type, row, meta) {
		        return '<input style="position:static;opacity:1;" type="checkbox" name="funId" value="' + row.fun_id + '" />'
		    }
		},
		{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 5,
		    render: function(data, type, row, meta) {
		 	   if(row.sfshow == 1){
		 		   return "是";
		 	   }else{
		 		   return "否";
		 	   }
		    }
		},
		{
			   targets: 6,
		    render: function(data, type, row, meta) {
		        return '<a href="funEdit?fid=' + row.fun_id + '" class="btn btn-default btn-xs purple"><i class="fa fa-edit"></i>编辑</a>'
		    }
		}
		];