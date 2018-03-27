<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8" />
    <title>打印</title>
	<style type="text/css">		
		body{margin: 0 0 0 0;font-family: SimSun;}
		div#main{margin: 0 0 0 0; width:290px;}
		h1{width:98%;text-align:center; font-size: 2em;margin: 0 0 .444em;}
		h2{width:98%;text-align:center;font-size: 1.5em;margin: 0 0 .666em;}
		h1, h2 {
			font-weight: bold;
			padding: 0;
			line-height: 1.2;
			clear: left;
		}

		@page{size:100mm 80mm;}
		
		.showDiv1{
			padding-top:15px;
		}
		
		.showDiv2{
			padding-top:3px;
			table-layout:fixed;
			width:280px;
			word-break:break-strict; 
		}
		.showDiv3{
			padding-top:3px;
			width:200px;
		}
		.qrDiv{
			position:absolute;
    		top: 0px;
    		right: 0px;
    		z-index: -1;
		}
		span{
			word-break: keep-all;
		}
	</style>
</head>
<body>
    <div id="main">
		#foreach ($detail in $list)	
		<div style="position:relative;">
		<div class="showDiv1">数量：${detail.deliveryQty}</div>
		<div class="showDiv2">料号：${detail.itemNo}</div>
		<div class="showDiv3">品名：${detail.itemName}</div>
		<div class="showDiv2">规格：$PrintUtils.pdfPrintManulWrap2($detail.itemSpec,24)</div>
		<div class="showDiv2">供应商：${detail.supplierName}(${detail.supplierNo})</div>
		<div class="showDiv2">批号：${detail.lotNo}</div>
		<div class="qrDiv">
			<img class="qrcode" src="" value="${detail.qrCodeValue}" width="90px" />
		</div>
		</div>
		<br/>
		<div style="page-break-after: always;"></div>
		#end
	</div>
</body>
</html>


