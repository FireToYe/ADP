$(function(){
    //菜单点击 J_iframe
    $(".J_menuItem").on('click',function(){
        var url = $(this).attr('href');
        if(url&&url.indexOf("://")==-1){
        	url=ctx+url;
        }
        $("#J_iframe").attr('src',url);
        return false;
    });
	$("#menu a.menu").click(function(){
		// 一级菜单焦点
		$("#menu li.menu").removeClass("active");
		$(this).parent().addClass("active");
		var curId=$(this).attr("data-id");
		//左侧菜单隐藏
		var firstMenu=true;
		$("#side-menu .nav-first-level,.hidden-folded").each(function(i,n){
			var parentId=$(n).attr("data-parent");
			if(curId==parentId){
				$(this).show();
				//第一个菜单展开
				if(firstMenu){
					var eleLink=$(this).find("#side-menu li")[1];
					//$("#J_iframe").attr("src",eleLink.href);
				//	$(eleLink).addClass("active");
					firstMenu=false;
				//	menuClick(eleLink);
					
				}	
			}else{
				$(this).hide();
			}
		});
	});
	$("#left-nav .nav-link").click(function(){
		menuClick(this);
		
	});
	//$("#menu a:first").click();	
});