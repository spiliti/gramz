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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<c:if test="${not empty message}">
	<div class="alert alert-success" role="alert">${message}</div>
</c:if>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<strong><c:out value="${complaint.crn}"></c:out></strong>
				</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.name" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-name">
						<c:out value="${complaint.complainant.name}"></c:out>
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.phoneNumber" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin view-content"
						id="ct-mobno">
						<c:choose>
							<c:when test="${complaint.complainant.mobile !=null}">
								<c:out value="${complaint.complainant.mobile}"></c:out>
							</c:when>
							<c:otherwise><spring:message code="msg.not.applicable"/></c:otherwise>
						</c:choose>
					</div>
				</div>
				<c:if test="${not empty complaint.complainant.address}">
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.address" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content"
							id="ct-ctnumber">
							<c:out value="${complaint.complainant.address}"></c:out>
						</div>
					</div>
				</c:if>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.ctn" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin view-content"
						id="ct-ctnumber">
						<c:out value="${complaint.crn}"></c:out>
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complaintDate" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-date">
						<fmt:formatDate value="${complaint.createdDate}" var="complaintDate" pattern="dd-MM-yyyy"/>
						<c:out value="${complaintDate}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.complaintDepartment" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-dept">
						<c:out value="${complaint.department.name}"></c:out>
					</div>
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.nextescalation.date" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-nextescalation">
						<fmt:formatDate value="${complaint.escalationDate}" var="nextEscalationDate" pattern="dd-MM-yyyy HH:mm"/>
						<c:out value="${nextEscalationDate}"/>
					</div> 
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.complaintType" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-type">
						<c:out value="${complaint.complaintType.name}"></c:out>
					</div>
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.filedVia" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-filedvia">
						<c:out value="${complaint.receivingMode}"/></div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.compDetails" /></div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-details">
						<c:out value="${complaint.details }"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.photovideo" />
					</div>
					<div class="col-md-9 col-xs-12 add-margin down-file view-content"
						id="links">
						<c:choose>
							<c:when test="${!complaint.getSupportDocs().isEmpty()}">
								<c:forEach items="${complaint.getSupportDocs()}" var="file">
									<c:choose>
										<c:when test="${(file.contentType == 'image/jpg') || (file.contentType == 'image/jpeg')|| (file.contentType == 'image/gif')|| 
										(file.contentType == 'image/png')}">
										<a href="../downloadfile/${file.fileStoreId}"
												data-gallery> <img class="img-width add-margin"
												src="../downloadfile/${file.fileStoreId}" /></a>
										</c:when>
										<c:otherwise>
											<a href="../downloadfile/${file.fileStoreId}"
												data-gallery> <video class="img-width add-margin"controls="controls"
													src="../downloadfile/${file.fileStoreId}">
													<source	src="../downloadfile/${file.fileStoreId}"
														type="${file.contentType}" /></video></a>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
									<spring:message code="msg.no.attach.found"/>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.location" />
					</div>
					<div class="col-md-9 col-xs-6 add-margin view-content">
						<span class="map-tool-class btn-secondary" data-toggle="tooltip"
							data-placement="top" title="" data-original-title="Locate on map"
							onclick="jQuery('#complaint-locate').modal('show', {backdrop: 'static'});">
							<i class="fa fa-map-marker"></i></span> <span id="address_locate">
							<c:out value="${complaint.childLocation.name} - ${complaint.location.name}"></c:out></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.landmark" /> 
					</div>
					<div class="col-md-9 col-xs-6 add-margin view-content" id="ct-lanmark">
						<c:choose>
							<c:when test="${complaint.landmarkDetails != null}">
								<c:out value="${complaint.landmarkDetails}"></c:out>
							</c:when>
							<c:otherwise><spring:message code="msg.not.applicable"/></c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<div class="panel-group">
			<div class="panel panel-primary">
				<div class="panel-heading slide-history-menu">
					<div class="panel-title">
						<strong><spring:message code="lbl.hdr.complaintHistory" /></strong>
					</div>
					<div class="history-icon">
						<i class="fa fa-angle-down" id="toggle-his-icon"></i>
					</div>
				</div>
				<div class="panel-body history-slide">
					<div class="row hidden-xs visible-sm visible-md visible-lg view-content header-color">
						<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.date"/></div>
						<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.updated.by"/></div>
						<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.status"/></div>
						<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.curr.owner"/></div>
						<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.department"/></div>
						<div class="col-sm-2 col-xs-6 add-margin"><spring:message code="lbl.comments"/></div>
					</div>
					<c:choose>
						<c:when test="${!complaintHistory.isEmpty()}">
							<c:forEach items="${complaintHistory}" var="history">
							<div class="row  add-border">
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
							<div class="col-md-3 col-xs-6 add-margin"><spring:message code="msg.history.not.found"/></div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- Modal 6 (Long Modal)-->
<div class="modal fade" id="complaint-locate">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<!-- to apply shadow add class "panel-shadow" -->
							<!-- panel head -->
							<div class="panel-heading">
								<div class="panel-title" id="show_address_in_map"><spring:message code="lbl.hdr.complaintHistory" /></div>
							</div>
							<!-- panel body -->
							<div class="panel-body no-padding">
								<script src="https://maps.googleapis.com/maps/api/js"></script>
								<div id="normal" class="img-prop"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
			</div>
		</div>
	</div>
</div>

<!-- The Bootstrap Image Gallery lightbox, should be a child element of the document body -->
<div id="blueimp-gallery" class="blueimp-gallery"
	data-use-bootstrap-modal="false">
	<!-- The container for the modal slides -->
	<div class="slides"></div>
	<!-- Controls for the borderless lightbox -->
	<h3 class="title"></h3>
	<ol class="indicator"></ol>
	<!-- The modal dialog, which will be used to wrap the lightbox content -->
	<div class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" aria-hidden="true">&times;</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body next"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default pull-left prev">
						<i class="glyphicon glyphicon-chevron-left"></i><spring:message code="lbl.previous"/></button>
					<button type="button" class="btn btn-primary next"><spring:message code="lbl.next" />
					 	<i class="glyphicon glyphicon-chevron-right"></i>
					</button>
				</div>
			</div>
		</div>
	</div>
</div>

<link rel="stylesheet"	href="<cdn:url  value='/resources/global/js/image-gallery/css/blueimp-gallery.min.css' context='/egi'/>">
<script	src="<cdn:url  value='/resources/global/js/image-gallery/js/jquery.blueimp-gallery.min.js' context='/egi'/>"></script>
<script	src="<cdn:url  value='/resources/global/js/image-gallery/js/bootstrap-image-gallery.js' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/js/app/complaintview.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/app/complaintviewmap.js?rnd=${app_release_no}'/>"></script>
<script>
var lat = '${complaint.lat}';
var lng = '${complaint.lng}';
</script>