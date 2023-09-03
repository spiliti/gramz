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
		<form:form  method="post" class="form-horizontal form-groups-bordered" modelAttribute="boundary" id="boundaryViewForm" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="lbl.hdr.viewBoundary"/> ${boundary.localName}</strong>
					</div>
				</div> 
				
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.hierarchyType"/></label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundary.boundaryType.hierarchyType.name}" /></strong>     
							<input type="hidden" id="btnHierarchyType" value="<c:out value="${boundary.boundaryType.hierarchyType.id}" />" />            
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundaryType"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundary.boundaryType.name}" /></strong>
							<input type="hidden" id="btnBoundaryType" value="<c:out value="${boundary.boundaryType.id}" />" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.name"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundary.name}" /></strong>
							<input type="hidden" id="btnBoundary" value="<c:out value="${boundary.id}" />" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.nameLocal"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundary.localName}" default="NA"/></strong>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.parent.boundary.name"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundary.parent.name}" default="NA"/></strong>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.number"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<strong><c:out value="${boundary.boundaryNum}" /></strong>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.fromDate"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<fmt:formatDate value="${boundary.fromDate}" pattern="dd-MM-yyyy" var="fromDate"/>
							<strong><c:out value="${fromDate}" /></strong>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.boundary.toDate"/>
						</label>
						<div class="col-sm-6" style="padding-top: 7px">
							<fmt:formatDate value="${boundary.toDate}" pattern="dd-MM-yyyy" var="toDate"/>
							<strong><c:out value="${toDate}" default="NA"/></strong>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">	
					<button type="submit" id="buttonCreate" class="btn btn-primary">
                    	<spring:message code="lbl.create"/>
                    </button>
                    <button type="submit" id="buttonEdit" class="btn btn-primary">
                    	<spring:message code="lbl.edit"/>
                    </button>
			        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
		</form:form>
			</div>
        </div>
    </div>
</div>

<script src="<cdn:url  value='/resources/js/app/boundary.js?rnd=${app_release_no}'/>"></script>
