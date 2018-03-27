<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8" />
    <title>打印模板</title>
	<style type="text/css">		
		body{
			margin: 0 auto;
			font-family: SimSun;
		}
		div#main{margin: 5px 5px 0 5px; width:300px;}
		
		#main table tr .left{
			text-align:right;
		}
		#main table tr .center{
			text-align:left;
		}
		#main table tr .right{
			text-align:left;
		}
		#main table tr .code{
			text-align:left;
		}
	</style>
</head>
<body>
	<div id="main">
		<table>
			<tr>
				<td class="left">供应商</td>
				<td class="center">${vBarcode}</td>
				<td class="code" rowspan="5">
					<img class="qrcode" src="" value="${barcode}" width="120px" />
				</td>
			</tr>
			<tr>
				<td class="left">日期</td>
				<td class="center">${date}</td>
			</tr>
			<tr>
				<td class="left">料号</td>
				<td class="center">${itemNo}</td>
			</tr>
			<tr>
				<td class="left">规格</td>
				<td class="center">${itemSpec}</td>
			</tr>
			<tr>
				<td class="left">批号</td>
				<td class="center">${lotNo}</td>
			</tr>
			<tr>
				<td class="left">数量</td>
				<td class="center">${number}</td>
				<td class="right">箱数 ${xiangshu}</td>
			</tr>
		</table>
	</div>
   
</body>
</html>
