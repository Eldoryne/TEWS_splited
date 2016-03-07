<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TEWS : integration</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="style.css"></c:url>" />
</head>
<body>
	<h1>TEWS : Twitter Events on the Semantic Web</h1>
	<h2>Event RDF transcription (step 2/3)</h2>
	<form action="" method="post">
		<table>
			<tr>
				<th>Position</th>
				<th>Date</th>
				<th>Main words</th>
				<th>Related words</th>
				<th>Transcript in RDF this event</th>
			</tr>
			<c:forEach var="event" items="${detectionResult.events }">
				<tr>
					<td><c:out value="${event.key }"></c:out></td>
					<td><c:out value="${event.value.date }"></c:out></td>
					<td><c:forEach var="mainWord" varStatus="status"
							items="${event.value.mainWord }">
							<c:out value="${mainWord }${status.last ? '' : ', '}"></c:out>
						</c:forEach></td>
					<td><c:forEach var="relatedWord" varStatus="status"
							items="${event.value.relatedWord }">
							<c:out
								value="${relatedWord.key } (${relatedWord.value })${status.last ? '' : ', '}"></c:out>
						</c:forEach></td>
					<td><input type="checkbox" name="${event.key }"
						id="${event.key }" checked />
				</tr>
			</c:forEach>
		</table>
		<input type="hidden" name="pageName" value="integration" /> <input
			class="submit" type="submit" value="Run transcription RDF" />
	</form>
	<span class="error">${integrationForm.errors}</span>
</body>
</html>