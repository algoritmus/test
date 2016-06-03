<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script src="./javascripts/jquery.dynatable.js"></script>

<link rel="stylesheet" media="all" href="./css/jquery.dynatable.css">

<script>
$.ajaxSetup ({
    // Disable caching of AJAX responses
    cache: false
});
$(document).ready(function(){
	
	$("#results").click(function(){
		$.ajax({
			url:"./ju/result",
			async:false,
			success:function(data){
				$("#result-table").dynatable({
					  dataset: {
					    records: data
					  }}
				  )
				},
		});
				
	});
});
</script>
</head>
<body>
<div>Select menu</div>
<ul>
<li id="form">Submit new request</li>
<li id="requests">Show requests</li>
<li id="results">show results</li>
</ul>
<table id="result-table" class="table table-bordered">
<thead>
<tr>
<th>Query_time</th>
<th>emailaddr</th>
<th>origin</th>
<th>destination</th>
<th>fromdate</th>
<th>todate</th>
<th>adults</th>
<th>infants</th>
<th>departure</th>
<th>arrival</th>
<th>price</th>
</tr>
</thead>
<tbody>
</tbody>
</table>
</body>
</html>