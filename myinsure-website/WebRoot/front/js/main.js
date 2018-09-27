(function(){
	//业务tab
	$(".ywly li").click(function(){
		$(this).find("span").addClass("hight-light").end().siblings().find("span").removeClass("hight-light");
		var _index=$(this).index();
		$(".mainB-content .child-content").eq(_index).removeClass("hidden").siblings().addClass("hidden");
	})
	
	//焦点新闻滚动
	/*$('.jdxw').flexslider({
		animation: "slide",
		slideshowSpeed: 7000,
		directionNav:false
      	});*/
      	
	new Swiper('.pcSwiper .swiper-container', {
	    pagination: '.paginationMain',
	    loop: true,
	    grabCursor: true,
	    paginationClickable: true,
	    autoplay:5000,
	    calculateHeight:true
	});
	
	new Swiper('.newhead .swiper-container', {
	    pagination: '.paginationHead',
	    loop: true,
	    grabCursor: true,
	    paginationClickable: true,
	    autoplay:5000,
	    calculateHeight:true
	}); 
	
	new Swiper('.newhead_mob .swiper-container', {
	    pagination: '.paginationHead_mob',
	    loop: true,
	    grabCursor: true,
	    paginationClickable: true,
	    autoplay:5000,
	    calculateHeight:true
	}); 
      	
      	//微信二维码
      	$(".weixin").hover(
	  function () {
	    $(".wxerwei").stop(true,true).fadeIn(500);
	  },
	  function () {
	    $(".wxerwei").stop(true,true).fadeOut(500);
	  }
	)    
})(jQuery);