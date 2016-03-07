<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TEWS : visualisation</title>
<link type="text/css" rel="stylesheet"
	href="<c:url value="style.css"></c:url>" />
</head>
<body>
	<h1>TEWS : Twitter Events on the Semantic Web</h1>
	<h2>Visualisation and serialization (step 3/3)</h2>
	<form action="" method="post">
		<table>
			<tr>
				<th>Position</th>
				<th>Date</th>
				<th>Locations</th>
				<th>Involved entities</th>
				<th>Sources</th>
				<th>Description</th>
			</tr>
			<c:forEach var="eventRDF" items="${integrationResult.eventsRDF }">
				<tr>
					<td><c:out value="${eventRDF.key }"></c:out></td>
					<td><c:out value="${eventRDF.value.date }"></c:out></td>
					<td><c:out value="${eventRDF.value.location }"></c:out></td>
					<td><c:forEach var="entity" varStatus="status"
							items="${eventRDF.value.entities }">
							<c:out value="${entity }${status.last ? '' : ', '}"></c:out>
						</c:forEach></td>
					<td><c:forEach var="source" varStatus="status"
							items="${eventRDF.value.sources }">
							<c:out value="${source }${status.last ? '' : ', '}"></c:out>
						</c:forEach></td>
					<td><c:out value="${eventRDF.value.description }"></c:out></td>
				</tr>
			</c:forEach>
		</table>
	</form>
	<a href="<c:url value="/run"></c:url>">Run another detection</a>
	<c:forEach var="eventRDF" items="${integrationResult.eventsRDF }">
		<table>
			<tr>
				<th>Subject</th>
				<th>Predicate</th>
				<th>Object</th>
			</tr>
			<tr>
				<td>http://data.lirmm.fr/event/${eventRDF.key }</td>
				<td>http://wikitimes.l3s.de/rdf/wikitimes.owl#happenedOnDate</td>
				<td>${eventRDF.value.date }</td>
			</tr>
			<tr>
				<td>http://data.lirmm.fr/event/${eventRDF.key }</td>
				<td>http://wikitimes.l3s.de/rdf/wikitimes.owl#happenedIn</td>
				<td>${eventRDF.value.location }</td>
			</tr>
			<c:forEach var="entity" varStatus="status"
				items="${eventRDF.value.entities }">
				<tr>
					<td>http://data.lirmm.fr/event/${eventRDF.key }</td>
					<td>http://wikitimes.l3s.de/rdf/wikitimes.owl#involvesEntity</td>
					<td>${entity }${status.last ? '' : ''}</td>
				</tr>
			</c:forEach>

			<c:forEach var="entity" varStatus="status"
				items="${eventRDF.value.sources }">
				<tr>
					<td>http://data.lirmm.fr/event/${eventRDF.key }</td>
					<td>http://wikitimes.l3s.de/rdf/wikitimes.owl#mentionedIn</td>
					<td>${entity }${status.last ? '' : ''}</td>
				</tr>
			</c:forEach>
			<tr>
				<td>http://data.lirmm.fr/event/${eventRDF.key }</td>
				<td>http://wikitimes.l3s.de/rdf/wikitimes.owl#summary</td>
				<td>${eventRDF.value.description }</td>
			</tr>
		</table>
	</c:forEach>
</body>
</html>