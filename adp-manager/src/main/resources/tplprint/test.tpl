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
		
	</style>
</head>
<body>
    <div id="main">
		<h1 >采购订单</h1>
		<!--<h2 >这是副标题</h2>-->
		<table class="detail">
			<tr>
				<td class="col1"><span class="tit">订单号：</span><span class="val">${code}</span></td>
				<td class="col2"><span class="tit">单据日期：</span><span class="val">${date}</span></td>
				<td class="col3"><span class="tit">单据类型：</span><span class="val">${type}</span></td>
			</tr>
			<tr>
				<td><span class="tit">客户名称：</span><span class="val">${custName}</span></td>
				<td><span class="tit">客户联系人：</span><span class="val">${custLink}</span></td>
				<td><span class="tit">客户电话：</span><span class="val">${custTel}</span></td>
			</tr>
			<tr>
				<td><span class="tit">供应商名称：</span><span class="val">${vendorName}</span></td>
				<td><span class="tit">供应商联系人：</span><span class="val">${vendorLink}</span></td>
				<td><span class="tit">供应商电话：</span><span class="val">${vendorTel}</span></td>
			</tr>
		</table>
		
		<table class="list">
			<thead>
				<tr >
					<th class="col1">商品编码</th>
					<th class="col2">商品名称</th>
					<th class="col3">单位</th>
					<th class="col4">数量</th>
					<th class="col5">单价</th>
					<th class="col6">金额</th>
					<th class="col7">备注</th>
				</tr>
			</thead>
			<tbody>
				#foreach ($element in $list)
				<tr >
					<td class="col1">${element.id}</td>
					<td class="col2">${element.name}</td>
					<td class="col3">${element.unit}</td>
					<td class="col4">${element.qty}</td>
					<td class="col5">${element.price}</td>
					<td class="col6">${element.amount}</td>
					<td class="col7">${element.memo}</td>
				</tr>
				#end
			</tbody>
		</table>
		<br/>
		<div>
			<img class="qrcode" src="" value="${code}" width="120px" />
			<span>带二维码</span>
		</div>
		<br/>
		<div>
			<img class="barcode" src="" value="6923450657713" width="120px" height="50px" />
			<span>带条形码</span>
		</div>
		
	</div>
</body>
</html>
