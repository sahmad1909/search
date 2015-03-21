<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Result</title>

<link href="javascript/jquery-ui.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="javascript/jquery-2.1.1.min.js"></script>
<script type="text/javascript"
	src="javascript/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="javascript/jquery-ui.js"></script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(function() {
							$("#search")
									.autocomplete(
											{

												source : function(request,
														response) {
													$
															.ajax({
																url : "autosearch.html",
																type : "GET",
																data : {
																	searchString : request.term
																},
																dataType : "json",

																success : function(
																		data) {
																	response($
																			.map(
																					data,
																					function(
																							v,
																							i) {
																						return {
																							label : v.label,
																							value : v.value,
																							id : v.id
																						};
																					}));
																}
															});
												},
												select : function(event, ui) {
													if (ui.item)
														window.location.href = "result.html?searchString="
																+ ui.item.label;
												}

											});
						});
					});
</script>

<style type="text/css">
#header {
	background-color: black;
	color: white;
	text-align: center;
	padding: 20px;
	font-size: xx-large;
	font-style: oblique;
}

#footer {
	background-color: black;
	color: white;
	clear: both;
	text-align: center;
	padding: 5px;
}

#nav {
	line-height: 30px;
	background-color: #FFFFFF;
	height: 70vh;
	width: 190px;
	float: left;
	padding: 10px;
}

#searchnav {
	line-height: 30px;
	background-color: #F7F3F3;
	height: 18vh;
	width: 185vh;
	float: left;
	padding: 10px;
}

.button-2 {
	text-align: center;
	text-decoration: none;
	font-family: sans-serif;
	-webkit-font-smoothing: antialiased;
	font-size: 100%;
	color: #FFF;
	background: #008AB8;
	padding: 4px 15px;
	display: inline-block;
	white-space: nowrap;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	margin: 10px 0;
	-webkit-transition: all 0.2s ease-in-out;
	-ms-transition: all 0.2s ease-in-out;
	-moz-transition: all 0.2s ease-in-out;
	-o-transition: all 0.2s ease-in-out;
	transition: all 0.2s ease-in-out;
}

.button-2:hover {
	background: #B2E0F0;
}

.Large {
	font-size: 16pt;
	height: 28px;
}
</style>
</head>
<body>

	<div id="header">
		<h1>CS 454 Serach Engine</h1>
	</div>
	<form action="result.html" method="post">
		</br> &nbsp;&nbsp;<input type="text" name="searchString" id="search"
			size="50" value=${searchString } class="Large" /> <input
			class="button-2" type="submit" name="search" Value="Search" />
	</form>

	<c:if test="${empty pagedata}">
		<h1 color="red">
			<font color="red">No Data Found </font>
		</h1>
	</c:if>

	<c:if test="${not empty pagedata}">
		<!-- 	 <h1 color="green"> <font color="green"> Data Found </font></h1> -->


		<c:forEach items="${pagedata}" var="pagedata">

			<h3>
				<a href="${pagedata.url}" target="_blank">${pagedata.url}</a>
			</h3>

			<div id="searchnav">
				<p>${pagedata.pageDate}</p>
			</div>
		</c:forEach>


	</c:if>


	<!--  <div id="nav"></div>
	<div id="footer" align="bottom">Copyright © CS-454</div> -->


</body>
</html>