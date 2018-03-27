<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="输入框值"%>
<i id="${id}Icon" class="fa fa-${not empty value?value:' hide'}"></i>&nbsp;<label id="${id}IconLabel">${not empty value?value:'无'}</label>&nbsp;
<input id="${id}" name="${name}" type="hidden" value="${value}"/><a id="${id}Button" class="btn"
data-toggle="modal" href="${ctx}/tag/iconselect" data-target="#${id}Modal" >${fns:i18nMessage('common.select')}</a>&nbsp;&nbsp;

<div class="modal inmodal fade" id="${id}Modal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">${fns:i18nMessage('icon.selectIcon')}</h4>
            </div>
            <div class="modal-body modal-office-body">
			</div> 
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">${fns:i18nMessage('common.close')}</button>
                <button type="button" class="btn btn-primary">${fns:i18nMessage('common.save')}</button>
            </div>
        </div>
      </div>
</div>
<script type="text/javascript">
	$(function () { $('#${id}Modal').on('hide.bs.modal', function () {
		var icon=$("#icon").val();
		var oper=$("#oper").val();
		if(icon&&icon.length>0){
			if(oper=="success"){
		       	$("#${id}Icon").attr("class", "fa fa-"+icon.trim());
		        $("#${id}IconLabel").text(icon);
		        $("#${id}").val(icon);
			}
		 }
	 })});
/* 	$("#${id}Button").click(function(){
		
		
		top.$.jBox.open("iframe:${ctx}/tag/iconselect?value="+$("#${id}").val(), "选择图标", 700, $(top.document).height()-180, {
            buttons:{"确定":"ok", "清除":"clear", "关闭":true}, submit:function(v, h, f){
                if (v=="ok"){
                	var icon = h.find("iframe")[0].contentWindow.$("#icon").val();
                	icon = $.trim(icon).substr(5);
                	$("#${id}Icon").attr("class", "icon-"+icon);
	                $("#${id}IconLabel").text(icon);
	                $("#${id}").val(icon);
                }else if (v=="clear"){
	                $("#${id}Icon").attr("class", "icon- hide");
	                $("#${id}IconLabel").text("无");
	                $("#${id}").val("");
                }
            }, loaded:function(h){
                $(".jbox-content", top.document).css("overflow-y","hidden");
            }
        });
	}); */
</script>
