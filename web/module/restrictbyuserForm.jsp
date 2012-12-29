<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Manage User Restrictions" otherwise="/login.html" redirect="/module/restrictbyuser/restrictbyuser.form" />
<spring:message var="pageTitle" code="restrictbyuser.titleBar" scope="page" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/dojo/dojo.js" />

<script type="text/javascript">
	$j(document).ready(function() {
		$j('#addDialog').dialog({
			autoOpen: false,
			modal: true,
			title: '<spring:message code="restrictbyuser.form.add"/>',
			width: '30%',
			zIndex: 100,
			buttons: { '<spring:message code="general.add"/>': function() { handleAdd(); },
					   '<spring:message code="general.cancel"/>': function() { $j(this).dialog("close"); }
					 }
			});
		$j('#addLink').click(function() {
			$j('#addError').hide();
			$j('#addDialog').dialog('open');
		});	
	});

	function handleAdd() {
		$j('#addDialog').dialog('close');
		$j('#addForm').submit();
	}

</script>
<h2><spring:message code="restrictbyuser.form.title" /></h2>
<br />
<h4><a href="restrictbyuser.list"><spring:message code="restrictbyuser.form.back" /></a></h4>
<fieldset>
	<legend>
		<spring:message code="restrictbyuser.form.details" />
	</legend>
	<table>
		<tr>
			<td><spring:message code="User.username" /></td>
			<td>${user.username}</td>
		</tr>
		<tr>
			<td><spring:message code="PersonName.givenName" /></td>
			<td>${user.person.givenName}</td>
		</tr>
		<tr>
			<td><spring:message code="PersonName.middleName" /></td>
			<td>${user.person.middleName}</td>
		</tr>
		<tr>
			<td><spring:message code="PersonName.familyName" /></td>
			<td>${user.person.familyName}</td>
		</tr>
		<tr>
			<td><spring:message code="Person.gender" /></td>
			<td>${user.person.gender}</td>
		</tr>
	</table>
</fieldset>
<br />
<br />
<h3>
	<spring:message code="restrictbyuser.form.restrictionDetails" />
	<h4>
		<a style="cursor: pointer;" id="addLink"><spring:message code="restrictbyuser.form.add" /></a>
	</h4>
</h3>
<spring:message code="restrictbyuser.form.description" />
<br />
<br />

<b class="boxHeader"><spring:message code="restrictbyuser.form.list" /></b>
<form id="removeForm" method="post" class="box">
	<table cellpadding="2" cellspacing="0">
		<tr>
			<th></th>
			<th><spring:message code="general.name" /></th>
			<th><spring:message code="Person.age" /></th>
			<th><spring:message code="Person.gender" /></th>
			<th><spring:message code="Person.birthdate" /></th>
		</tr>
		<c:forEach var="patient" items="${user.patients}" varStatus="status">
			<tr class='${status.index % 2 == 0 ? "evenRow" : "oddRow" }'>
				<td><input type="checkbox" name="patientId" value="${patient.patientId}"></td>
				<td>${patient.personName}</td>
				<td>${patient.age}</td>
				<td>${patient.gender}</td>
				<td><openmrs:formatDate date="${patient.birthdate}" format="dd/MMM/yyyy"/></td>
			</tr>
		</c:forEach>
	</table><br/>
	<input type="submit" name="remove" value="<spring:message code="general.remove" />"/>
</form>

<!-- Assign user restriction -->
<div id="addDialog" style="display:none">
	<div id="addError" class="error"></div>
	<form id="addForm" method="post">
		<table width="50%" cellpadding="2">
			<tr>
				<th><spring:message code="general.patient"/></th>
				<td>
					<openmrs_tag:patientField formFieldName="patientId" searchLabelCode="Patient.find" />
				</td>
			</tr>
		</table>
	</form>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>

