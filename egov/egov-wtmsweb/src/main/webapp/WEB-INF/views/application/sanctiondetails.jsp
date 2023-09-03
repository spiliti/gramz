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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.sanctiondetails"/>
					</div>					
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.sanctionnumber"/><span class="mandatory"></span>
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<form:input class="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash" id="approvalNumber" path="approvalNumber" maxlength="50" required="required" />
							<form:errors path="approvalNumber" cssClass="add-margin error-msg" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.sanctiondate"/><span class="mandatory"></span>
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<form:input id="approvalDate" path="approvalDate" class="form-control datepicker today" data-date-end-date="0d" required="required" />
							<form:errors path="approvalDate" cssClass="add-margin error-msg" />
						</div>
					</div>
					<c:if test="${chairPerson != null}">
						<div class="row">
							<div class="col-md-3 col-xs-6 add-margin">
								<spring:message code="lbl.chairpersonname"/>
							</div>
							<div class="col-md-3 col-xs-6 add-margin">
								<input type="text" class="form-control" id="chairperson" name="chairperson" value="${chairPerson.name}" disabled> 
								<form:hidden path="chairPerson" id="chairPersonId" value="${chairPerson.id}"/> 
							</div>			
						</div>					
					</c:if> 
					    <div class="row">
							<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.disposal.date" /></div>
							<div class="col-xs-3 col-xs-6 add-margin add-margin view-content">
								<fmt:formatDate pattern="dd/MM/yyyy" value="${waterConnectionDetails.disposalDate}" />
							</div>					
						</div>				
				</div>
			</div>
		</div>
</div>