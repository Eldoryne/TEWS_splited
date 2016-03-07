<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TEWS : detection</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="style.css"></c:url>" />
</head>
<body>
	<h1>TEWS : Twitter Events on the Semantic Web</h1>
	<h2>Event detection (step 1/3)</h2>
	<form method="post" action="">
		<fieldset>
			<legend>Corpus selection</legend>
			<table>
				<tr>
					<th><label for="year">Year</label></th>
					<th><label for="month">Month</label></th>
					<th><label for="day">Day</label></th>
				</tr>
				<tr>
					<td><select name="year" id="year">
							<option value="2015">2015</option>
					</select></td>
					<td><select name="month" id="month">
							<c:forEach var="i" begin="1" end="12">
								<option value="${i}">${i}</option>
							</c:forEach>
					</select></td>
					<td><select name="day" id="day">
							<c:forEach var="i" begin="1" end="31">
								<option value="${i}">${i}</option>
							</c:forEach>
					</select></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>Detection parameters</legend>
			<table>
				<tr>
					<td><label for="timeSliceLength">Time slice length
							(unit : minute)</label></td>
					<td><input type="number" min="5" max="120" value="10"
						name="timeSlice" id="timeSlice" /></td>
				</tr>
				<tr>
					<td><label for="numberEvent">Maximum desired number of
							events</label></td>
					<td><input type="number" min="5" max="80" value="10"
						name="numberEvent" id="numberEvent" /></td>
				</tr>
				<tr>
					<td><label for="numberWord">Maximum number of related
							words describing each event</label></td>
					<td><input type="number" min="1" max="40" value="10"
						name="numberWord" id="numberWord" /></td>
				</tr>
				<tr>
					<td><label for="weightWord">Minimum weight of each
							related word</label></td>
					<td><input type="number" min="0.01" max="1" step="0.01"
						value="0.70" name="weightWord" id="weightWord" /></td>
				</tr>
				<tr>
					<td><label for="mergerThreshold">Merger threshold
							between events</label></td>
					<td><input type="number" min="0.01" max="1" step="0.01"
						value="0.50" name="mergerThreshold" id="mergerThreshold" /></td>
				</tr>
			</table>
		</fieldset>
		<input type="hidden" name="pageName" value="detection" /> <input
			class="submit" type="submit" value="Run detection" />
	</form>
	<span class="error">${integrationForm.errors}</span>
</body>
</html>