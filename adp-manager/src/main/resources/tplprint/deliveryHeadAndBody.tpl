<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8" />
    <title>打印模板</title>
	<style type="text/css">		
		body{margin: 0 auto;font-family: SimSun;}
		div#main{margin: 5px 5px 0 5px; width:700px;}
		h1{width:98%;text-align:center; font-size: 2em;margin: 0 0 .444em;}
		h2{width:98%;text-align:center;font-size: 1.5em;margin: 0 0 .666em;}
		h1, h2 {
			font-weight: bold;
			padding: 0;
			line-height: 1.2;
			clear: left;
		}
		
		table.detail{
			width:98%;
			border-collapse: collapse;
			empty-cells: show;
			border-spacing: 0;
			margin:10px 0 0 0;
			border-width:0;
		}
		table.detail td{
			padding: .3em .5em;
			margin: 0;
			vertical-align: top;
			border-width:0;
		}
		table.detail td span.tit{
			font-weight: bold;
		}
		table.detail td span.val{
			border-width:0 0 1px 0;
			border-bottom: 1px solid #CCC;
			font-size: 14px;
		}
		table.detail .col1{width:220px;}
		table.detail .col2{width:220px;}
		table.detail .col3{width:220px;}
		
		table.list{
			width:98%;
			border-collapse: collapse;
			empty-cells: show;
			border-spacing: 0;
			margin:10px 0 0 0;
			border: 1px solid #CCC;
			table-layout:fixed; word-break:break-strict; 
		}
		table.list th{font-weight: bold;background-color: #EEE;}
		table.list th,td{
			padding: .3em .5em;
			margin: 0;
			vertical-align: top;
			border: 1px solid #CCC;
		}
		table.list td{
			font-size: 14px;
		}
		table.list .col1{width:15%;}
		table.list .col2{width:25%;}
		table.list .col3{width:10%;}
		table.list .col4{width:10%;text-align: right;}
		table.list .col5{width:10%;text-align: right;}
		table.list .col6{width:10%;text-align: right;}
		table.list .col7{width:20%;}
		table.list .col8{width:8%;}
		table.list .col9{width:8%;text-align:center}
		
		.qrcodeDiv{
			position: absolute;
    		right: 20px;
    		top: 0px;
		}
		.titleDiv{
			text-align: center;
    		font-size: 30px;
    		position: inherit;
    		font-weight: bold;
		}
		.headDiv{
			position: inherit;
    		height: 80px
		}
	</style>
</head>
<body>
    <div id="main">
		<div class="headDiv">
		<div class="titleDiv">送货单</div>
		<div class="qrcodeDiv">
			<img class="qrcode" src="" value="${detail.deliveryNo}" width="120px" />
		</div>
		</div>
		<table class="detail">
			<tr>
				<td class="col2"><span class="tit">送货单号：</span><span class="val">${DeliveryBodyHead.deliveryNo}</span></td>
				<td class="col2"><span class="tit">送货日期：</span><span class="val">${DeliveryBodyHead.deliveryDate}</span></td>
			</tr>
			<tr>
				<td class="col2" colspan="2"><span class="tit">供应商：</span><span class="val">${DeliveryBodyHead.supplierNo}(${DeliveryBodyHead.supplierName})</span></td>
			</tr>
			<tr>
				<td class="col2" colspan="2"><span class="tit">送货地址：</span><span class="val">${DeliveryBodyHead.receiptAddress}</span></td>
			</tr>
		</table>
		
		<table class="list">
			<thead>
				<tr >
					<th class="col9">项次</th>
					<th class="col2">采购单号</th>
					<th class="col1">料件编号</th>
					<th class="col1">料件名称</th>
					<th class="col1">规格</th>
					<th class="col8">单位</th>
					<th class="col1">送货数量</th>
					
				</tr>
			</thead>
			<tbody>
				#foreach ($element in $DeliveryBodyList)
				<tr >
					<td class="col9">${element.deliverySeq}</td>
					<td class="col2">${element.purchaseNo}</td>
					<td class="col1">$PrintUtils.pdfPrintManulWrap2($element.itemNo,12)</td>
					<td class="col1">$PrintUtils.pdfPrintManulWrap2($element.itemName,12)</td>
					<td class="col1">$PrintUtils.pdfPrintManulWrap2($element.itemSpec,12)</td>
					<td class="col8">$!{element.unitNo}</td>
					<td class="col1">$!{element.deliveryQty}</td>
				</tr>
				#end
			</tbody>
		</table>
		<br/>
	</div>
</body>
</html>