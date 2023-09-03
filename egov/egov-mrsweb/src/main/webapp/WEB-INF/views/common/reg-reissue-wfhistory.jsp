<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading slide-history-menu">
		<div class="panel-title">
			<spring:message  code="lbl.apphistory"/>
		</div>
		<div class="history-icon">
			<i class="fa fa-angle-up fa-2x" id="toggle-his-icon"></i>
		</div>
	</div>
	<div class="panel-body history-slide display-hide">
		<div class="row add-margin hidden-xs visible-sm visible-md visible-lg header-color">
			<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.date"/></div>
			<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.updatedby"/></div>
			<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.status" /></div>
			<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.currentowner"/></div>
			<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.department" /></div>
			<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.comments" /></div>
		</div>
		<c:choose>
				<c:when test="${!applicationHistory.isEmpty()}">
					<c:forEach items="${applicationHistory}" var="history">
					<div class="row add-margin">
						<div class="col-sm-2 col-xs-12 add-margin">
							<fmt:formatDate value="${history.date}" var="historyDate"
								pattern="dd-MM-yyyy HH:mm a E" />
							<c:out value="${historyDate}" />
						</div>
						<div class="col-sm-2 col-xs-12 add-margin">
							<c:out value="${history.updatedBy}" />
						</div>
						<div class="col-sm-2 col-xs-12 add-margin">
							<c:out value="${history.status}" />
						</div>
						<div class="col-sm-2 col-xs-12 add-margin">
							<c:out value="${history.user}" />
						</div>
						<div class="col-sm-2 col-xs-12 add-margin">
							<c:out value="${history.department}" />
						</div>
						<div class="col-sm-2 col-xs-12 add-margin">
							<c:out value="${history.comments}" />&nbsp;
						</div>
					</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.nohistorydetails.code"/></div>
				</c:otherwise>
			</c:choose>
	</div>
</div>