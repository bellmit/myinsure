$(function(){
//	var categories = ['北京','北京','北京','北京','北京','北京','北京','北京'];
//	var series = [{
//        name: '中介公司',
//        data: [934, 2503, {y:3177,color:""}, 4658, 5031, 6031, 7133, 8175]
//    }];
	var categories1 = [];
	var categories2 = [];
	var categories3 = [];
	var series1 = [];
	var series2 = [];
	var series3 = [];
	$.ajax({
		cache : true,
		type : "POST",
		url : "showCharts",
		data : {},
		async : false,
		error : function(request) {
			layer.msg("连接错误");
		},
		success : function(data) {
			//按省统计
			var provinces = data.provinces;
			var obj1 = {};
			obj1.name = "公司数量";
			var data1 = [];
			$.each(provinces,function(i){
				categories1.push(provinces[i].area_name);
				data1.push(provinces[i].count);
			});
			obj1.data = data1;
			series1.push(obj1);
			//按年统计
			var years = data.years;
			var obj2 = {};
			obj2.name = "公司数量";
			var data2 = [];
			$.each(years,function(i){
				categories2.push(years[i].year);
				data2.push(years[i].count);
			});
			obj2.data = data2;
			series2.push(obj2);
			//按注册资金统计
			var capitals = data.capitals;
			var obj3 = {};
			obj3.name = "公司数量";
			var data3 = [];
			$.each(capitals,function(i){
				categories3.push(capitals[i].regist_capital);
				data3.push(capitals[i].count);
			});
			obj3.data = data3;
			series3.push(obj3);
		}
	});
	changeChart('container1','column',categories1,series1,'省');//line、bar、column、area
	changeChart('container2','column',categories2,series2,'年');//line、bar、column、area
	changeChart('container3','column',categories3,series3,'资金(万元)');//line、bar、column、area
});
function changeChart(id,t,categories,series,xTitle){
    var chart = Highcharts.chart(id, {
       chart:{
           type:t,
           animation:{
               duration: 2,
               easing: 'easeOutBounce'
           },
           backgroundColor:'#fff',
           borderColor:'#000',
           borderWidth:2,
           colorCount:5,
           zoomType: 'xy',
           panning: true,
           panKey: 'shift',
           events: {
               addSeries: function () {
                   var label = this.renderer.label('A series was added, about to redraw chart', 100, 220)
                   .attr({
                       fill: Highcharts.getOptions().colors[2],
                       padding: 10,
                       r: 5,
                       zIndex: 8
                   })
                   .css({
                       color: '#efefef'
                   })
                   .add();
                   setTimeout(function () {
                       label.fadeOut();
                   }, 1000);
               }
           }
       },
       title: {
           text: '代理公司统计'
       },
       xAxis: {
           categories:categories,
           title: {
               text: xTitle
           }
       },
       yAxis: {
           title: {
               text: '数量'
           }
       },
       series: series,
       responsive: {
           rules: [{
               condition: {
                   maxWidth: 500
               },
               chartOptions: {
                   legend: {
                       layout: 'horizontal',
                       align: 'center',
                       verticalAlign: 'bottom'
                   }
               }
           }]
       }
   });
}