// JavaScript Document
/**定义命名空间**/
yuexiu = {} || window.yuexiu;


﻿
/**方法定义，单例模式**/
yuexiu.ui = {
   isActive : false 	
   ,isOpen : false
   ,autoSwitch : null
   ,switchMember : function(method){
	  if(jQuery(".yuexiu-member-header").html() == "") {
	    jQuery(".yuexiu-member-header").append("<div class='yueiu-loading'></div>");
		jQuery(".yuexiu-member-header").append('<iframe scrolling="no" allowtransparency="true" onreadystatechange="yuexiu.ui.iframeReady()" onload="yuexiu.ui.iframeReady()" src="'+yuexiu.memberURL+'" id="yuexiu-member-IFrame" frameborder="0"></iframe>'); 
		
	  }
	  var target = jQuery("#yuexiu-member-wrap");
	  if(target.css("display") == "none") {
		jQuery(".yuexiu-topLineToggle").hide();  
		target.slideDown("fast",function(){
		  if(typeof(method) != "undefined") {
			 method();  
		  }								 
		});  
		   jQuery("#yuexiu-showMember").addClass("yuexiu-sWR-open");
		   jQuery("#yuexiu-topLine").animate({top:"113px"},"fast");
		   jQuery("#yuexiu-showMember").animate({top:"113px"},"fast");
	    } else {
	    target.slideUp("fast",function(){
		  jQuery(".yuexiu-sWR").removeClass("yuexiu-sWR-open");							   
		  jQuery(".yuexiu-topLineToggle").show();	
		});
		jQuery(".yuexiu-topLineToggle").show();
		jQuery("#yuexiu-topLine").animate({top:"0"},"fast",function(){
			if($.browser.version == "6.0"){
				jQuery("#yuexiu-showMember").removeClass("yuexiu-sWR-open-gif");
	  		}else{
				jQuery("#yuexiu-showMember").removeClass("yuexiu-sWR-open");
	  		}
		});
		jQuery("#yuexiu-showMember").animate({top:"0"},"fast");
	  }
   }
   ,hideNav : function(){
		jQuery(".yuexiu-default").show();		
		jQuery(".yuexiu-subNavMenu").hide();
		yuexiu.ui.isActive = false;
		jQuery(".yuexiu-mainMenu").find("td").removeClass("active");	   
   }
   ,iframeReady : function(){
     jQuery(".yueiu-loading").remove();
   }
}
jQuery(function(){
	/**请问服务器看是否需要自动打开**/		
		setTimeout(function(){
			jQuery.ajax({
				url : yuexiu.URL + "/autoOpen.js"
				,data : ""
				,dataType : "script"
				,type : "get"
				,success : function(){
					if(typeof(callBack) != "undefined") {
						if(callBack.isOpen && yuexiu.ui.isOpen == false) {
							yuexiu.ui.switchMember(function(){
								yuexiu.ui.autoSwitch = setTimeout(function(){yuexiu.ui.switchMember()},parseInt(callBack.closeTime) * 1000)
							});
						}
					}
				}
			});	
		},5000)		
	/**打开或关闭集团成员菜单**/			
    jQuery("#yuexiu-showMember").bind("click",function(){
		yuexiu.ui.switchMember();		
		yuexiu.ui.isOpen = true;
		clearInterval(yuexiu.ui.autoSwitch);
	});
	jQuery(".yuexiu-mainMenu").find("td").hover(function(){
	   if(!jQuery(this).is(".yuexiu-seperate")) {
	     jQuery(this).addClass("active");  
		 jQuery(".yuexiu-mainMenu").find("td").removeClass("active");
		 if(jQuery(this).attr("rel") != undefined) {
		   var str = jQuery(this).attr("rel");
		   var left = jQuery(this).offset().left - $(".yuexiu-header").offset().left - 8;
		   jQuery(".yuexiu-default").hide();
		   jQuery(".yuexiu-subNavMenu").hide();
		   
		   jQuery("div."+str).show()
		   //alert(jQuery("div."+str).find("span").width());
		   if(left + jQuery("div."+str).find("span").width() > jQuery(".yuexiu-subNav").width())	{
			     jQuery("div."+str).find("span").css({"right":"0px"});
		   } else {
			  jQuery("div."+str).find("span").css({"left":left+"px"});   
		   }
		   jQuery(this).addClass("active"); 	
		   yuexiu.ui.isActive = true;
		   
		 } else {
			yuexiu.ui.hideNav();
			jQuery(".yuexiu-mainMenu").find("td").removeClass("active");
			jQuery(this).addClass("active");  
		 }
	   }
	},function(e){
		if(!yuexiu.ui.isActive) {
		  jQuery(".yuexiu-mainMenu").find("td").removeClass("active");
		}
	})

    jQuery(".yuexiu-subNav").bind("mouseleave",function(){
		yuexiu.ui.hideNav();
	})
	
});
