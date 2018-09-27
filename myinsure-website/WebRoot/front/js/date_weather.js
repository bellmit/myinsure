Date.prototype.format = function(format){//日期转字符串方法
    var o = {
    "M+" : this.getMonth()+1, //month
    "d+" : this.getDate(), //day
    "h+" : this.getHours(), //hour
    "m+" : this.getMinutes(), //minute
    "s+" : this.getSeconds(), //second
    "q+" : Math.floor((this.getMonth()+3)/3), //quarter
    "S" : this.getMilliseconds() //millisecond
    };

    if(/(y+)/.test(format)) {
    format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }

    for(var k in o) {
            if(new RegExp("("+ k +")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
            }
    }
    return format;
};
function getDate(){
	var date = new Date().format("yyyy年MM月dd日");
	return date;
}
function getWeekday(){
	var today=new Date();
	var today=today.getDay();//获取存储当前日期
	var weekday=["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
	return weekday[today];
}
//天气API调用js
function weather(){
	var weatherinfo = "";
	var nowcity = Mlocation();
    $.ajax({
        type: "get",
        async: false,
        url: "https://bird.ioliu.cn/weather",
        data: {"city":nowcity},
        dataType: "json",
        success:function (data) {
        	var messinfo=data.result.weather;
            weatherinfo = nowcity+"&nbsp;&nbsp;"+messinfo+"&nbsp;&nbsp;"+data.result.temphigh+"℃--"+data.result.templow+"℃";
        },
        error:function () {
        	weatherinfo = "获取天气失败!";
        }
    });
    return weatherinfo;
}

// 获取用户位置信息
function Mlocation() {
    var nowcity = "";
    $.ajax({
        type: "get",
        async: false,
        url: "https://bird.ioliu.cn/ip",
        dataType: "json",
        success:function (data) {
            nowcity=data.data.area;
            var strindex;
            if(nowcity!=undefined){
                strindex=nowcity.indexOf("省");
                nowcity=nowcity.substring(strindex+1,nowcity.length);
            }else{
                nowcity="石家庄市";
            }
        },
        error:function () {
            console.log("error!");
            nowcity="石家庄市";
        }
    });
    return nowcity;
}
var weatherinfo = weather();
var date = getDate();
var weekday = getWeekday();
$(".today").html(date + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ weekday + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ weatherinfo);