<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<c:choose>
<c:when test="${!documentNamesList.isEmpty()}">
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
	<spring:message code="lbl.encloseddocuments"/> - <spring:message code="lbl.checklist"/>
	</div>
</div>


<div class="form-group view-content header-color hidden-xs">
	<div class="col-sm-3 text-center"><spring:message code="lbl.documentname"/></div>											
	<div class="col-sm-3 text-center"><spring:message code="lbl.documentnumber"/> (<span class="mandatory"></span> )</div>										
	<div class="col-sm-3 text-center"><spring:message code="lbl.documentdate"/> (<span class="mandatory"></span> )</div>
	<div class="col-sm-3 text-center"><spring:message code="lbl.attachdocument"/>(<span class="mandatory"></span> )</div>																							
</div>

<c:forEach var="docs" items="${documentNamesList}" varStatus="status">	

<div class="form-group">	
 	<div class="col-sm-3 add-margin check-text text-center">
		<c:choose>
			<c:when test="${docs.required}">
				<input type="checkbox" checked disabled>&nbsp;<c:out value="${docs.documentName}" /> 
			</c:when>
			<c:otherwise>
				<input type="checkbox" disabled>&nbsp;<c:out value="${docs.documentName}"/> 
			</c:otherwise>		
		</c:choose> 
		<form:hidden id="applicationDocs${status.index}documentNames.id" path="applicationDocs[${status.index}].documentNames.id" value="${docs.id}" /> 
		<form:hidden id="applicationDocs${status.index}documentNames" path="applicationDocs[${status.index}].documentNames.required" value="${docs.required}" /> 
		<form:hidden id="applicationDocs${status.index}documentNames.documentName" path="applicationDocs[${status.index}].documentNames.documentName" value="${docs.documentName}" /> 
	</div>
	<div class="col-sm-3 add-margin text-center">
		<c:choose>
			<c:when test="${docs.required}">
				<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="applicationDocs${status.index}documentNumber" path="applicationDocs[${status.index}].documentNumber" min="3" maxlength="50" required="required" />
			</c:when>
			<c:otherwise>
				<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="applicationDocs${status.index}documentNumber" path="applicationDocs[${status.index}].documentNumber" min="3" maxlength="50" />
			</c:otherwise>		
		</c:choose> 
		<form:errors path="applicationDocs[${status.index}].documentNumber" cssClass="add-margin error-msg" />
	</div>
	<div class="col-sm-3 add-margin text-center">
		<c:choose>
			<c:when test="${docs.required}">
				<form:input class="form-control datepicker" data-date-end-date="0d" id="applicationDocs${status.index}documentDate" path="applicationDocs[${status.index}].documentDate" required="required"/>
			</c:when>
			<c:otherwise>
				<form:input class="form-control datepicker" data-date-end-date="0d" id="applicationDocs${status.index}documentDate" path="applicationDocs[${status.index}].documentDate" />
			</c:otherwise>		
		</c:choose> 
			<form:errors path="applicationDocs[${status.index}].documentDate" cssClass="add-margin error-msg" />
	</div>
	<div class="col-sm-3 add-margin text-center">
		<c:choose>
			<c:when test="${docs.required}">
				<input type="file" id="file${status.index}id" name="applicationDocs[${status.index}].files" class="file-ellipsis upload-file" required="required">
			</c:when>
			<c:otherwise>
				<input type="file" id="file${status.index}id" name="applicationDocs[${status.index}].files" class="file-ellipsis upload-file">
			</c:otherwise>		
		</c:choose> 
		<form:errors path="applicationDocs[${status.index}].files" cssClass="add-margin error-msg" />
		<div class="add-margin error-msg text-left" ><font size="2">
								<spring:message code="lbl.mesg.document"/>	
								</font></div>
	</div> 
	
</div>
</c:forEach> 
</c:when>
</c:choose>

<script src="<cdn:url value='/resources/js/app/documentsupload.js?rnd=${app_release_no}'/>"></script>
