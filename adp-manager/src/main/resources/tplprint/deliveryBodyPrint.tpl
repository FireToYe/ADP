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
		<h1 >物料标签</h1>
		<!--<h2 >这是副标题</h2>-->
		<table class="detail">
			<tr>
				<td class="col1"><span class="tit">料号：</span><span class="val">${itemNo}</span></td>
				<td class="col2"><span class="tit">品名：</span><span class="val">${itemName}</span></td>
				<td class="col3"><span class="tit">规格：</span><span class="val">${itemSpec}</span></td>
			</tr>
			<tr>
				<td><span class="tit">供应商商：</span><span class="val">${legalNo}</span></td>
				<td><span class="tit">批号：</span><span class="val">${lotNo}</span></td>
				<td><span class="tit">特征码：</span><span class="val">${itemFeatureNo}</span></td>
			</tr>

		</table>
		<br/>
		<div>
			<span>二维码</span>
			<img class="qrcode" src="" value="${lotNo}" width="120px" />
		</div>
		<br/>	
	</div>
</body>
</html>
