var url = "articleList1";
var columns = [
	            { "data": null },
	            { "data": "title" },
	            { "data": "issue" },
	            { "data": "updatetime" },
	            { "data": null }
	           ];
var columnDefs = [{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 0,
		    render: function(data, type, row, meta) {
		        return '<input style="position:static;opacity:1;" type="checkbox" name="id" value="' + row.id + '" />'
		    }
		},
		{
		    //   指定第一列，从0开始，0表示第一列，1表示第二列……
		    targets: 1,
		    render: function(data, type, row, meta) {
		        return '<a href="javascript:" id='+row.article_key+'>'+row.title+'</a>'
		    }
		},
		{
			   targets: 4,
		    render: function(data, type, row, meta) {
		        return '<a href="javascript:article_edit(' + row.article_key + ')" class="btn btn-default btn-xs purple"><i class="fa fa-edit"></i>编辑</a>'
                +'<a href="javascript:article_generate(' + row.article_key + ')" class="btn btn-default btn-xs black"><i class="fa fa-search-plus"></i> 生成静态页</a>';
		    }
		}
		];