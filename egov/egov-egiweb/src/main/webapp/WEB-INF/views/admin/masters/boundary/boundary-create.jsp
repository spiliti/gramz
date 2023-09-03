<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2016  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
				 <c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
                </c:if>
        <c:choose>
			<c:when test="${isUpdate}">
				<c:set value="" var="actionUrl" />
			</c:when>
			<c:otherwise>
				<c:set value="/egi/boundary/create" var="actionUrl" />
			</c:otherwise>
		</c:choose>
		<form:form  method="post" action="${actionUrl}" class="form-horizontal form-groups-bordered" commandName="boundary" id="boundaryCreateOrUpdate" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<c:choose>
						<c:when test="${isUpdate}">
							<strong><spring:message code="lbl.hdr.updateBoundary"/>  <c:out value="${boundary.name}"/></strong>
						</c:when>
						<c:otherwise>
							<strong><spring:message code="lbl.hdr.createBoundary"/></strong>
						</c:otherwise>
					</c:choose>
					</div>
				</div> 
				
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.hierarchyType"/></label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundaryType.hierarchyType.name}" /></strong>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundaryType"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundaryType.name}" /></strong>
							<input type="hidden" name="boundaryType" value="<c:out value="${boundaryType.id}" />" />
						</div>
					</div>
					  <div class="panel-body custom-form">
					  <c:choose>
					  	<c:when test="${not empty parentBoundary}">
							<div class="form-group">
								<label class="col-sm-3 control-label">
									<spring:message code="lbl.parent.boundary.name" />
									<span class="mandatory"></span>
								</label>
								<div class="col-sm-6 add-margin">
		                            <form:select path="parent" id="hierarchyTypeSelect" cssClass="form-control"  cssErrorClass="form-control error" required="true">
		                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
		                                <form:options items="${parentBoundary}" itemValue="id" itemLabel="name"/>
		                            </form:select>
		                            <form:errors path="parent" cssClass="error-msg"/>
	                        	</div>
	                        </div>
	                 	</c:when>
	                 	<c:otherwise>
	                 		<div class="form-group">
								<label class="col-sm-3 control-label">
									<spring:message code="lbl.parent.boundary.name" />
								</label>
								<div class="col-sm-6 add-margin">
		                            <form:select path="parent"
		                                         id="hierarchyTypeSelect" cssClass="form-control"  cssErrorClass="form-control error">
		                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
		                                <form:options items="${parentBoundary}" itemValue="id" itemLabel="name"/>
		                            </form:select>
		                            <form:errors path="parent" cssClass="error-msg"/>
	                        	</div>
	                        </div>
	                 	</c:otherwise>
	                   </c:choose>     
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.name"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:input path="name" id="name" type="text" class="form-control low-width is_valid_alphanumeric" placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="name" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.nameLocal"/>
						</label>
						<div class="col-sm-6">
							<form:input path="localName" id="name" type="text" class="form-control low-width is_valid_alphanumeric" placeholder="" autocomplete="off" />
                            <form:errors path="localName" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.number"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:input path="boundaryNum" id="name" type="text" class="form-control low-width is_valid_number" placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="boundaryNum" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.fromDate"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:input path="fromDate" id="boundaryFromDate" type="text" class="form-control low-width datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="fromDate" cssClass="add-margin error-msg"/>
						</div>
					</div>		
					<div class="form-group">
						<label class="col-sm-3 control-label">
								<spring:message code="lbl.boundary.toDate"/>
						</label>
						<div class="col-sm-6">
							<form:input path="toDate" id="boundaryToDate" type="text" class="form-control low-width" placeholder="" autocomplete="off" />
                            <form:errors path="toDate" cssClass="add-margin error-msg"/>
						</div>
					</div>		
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<a href="javascript:void(0)" class="btn btn-default" id="backBtnId" ><spring:message code="lbl.back"/></a>
					<button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
			        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
		</form:form>
			</div>
        </div>
    </div>
</div>
<script src="<cdn:url  value='/resources/js/app/boundary.js?rnd=${app_release_no}'/>"></script>
