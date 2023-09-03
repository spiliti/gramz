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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js?rnd=${app_release_no}' context='/egi'/>"></script>
<div class="row" id="page-content">
	<div class="col-md-12">
		<c:if test="${not empty message}">
			<div class="alert alert-success" role="alert">
				<spring:message code="${message}" />
			</div>
		</c:if>
		<form:errors></form:errors>
		<form:form method="post" class="form-horizontal form-groups-bordered"
			id="apartment" modelAttribute="apartment">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.apartment.create" />
					</div>
				</div>
				<div class="panel-body custom-form">
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.name" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="name" path="name" type="text"
								cssClass="form-control patternvalidation"						
								data-pattern="alphanumericwithspecialcharacters" autocomplete="off"
								required="required" />
							<form:errors path="name" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.code" /><span class="mandatory"></span></label>
						<div class="col-sm-6 add-margin">
							<form:input id="code" path="code" type="text"
								cssClass="form-control patternvalidation"						
								data-pattern="alphanumericwithspecialcharacters" autocomplete="off"
								required="required" />
							<form:errors path="code" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.builtUpArea" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="builtUpArea" path="builtUpArea" type="text"
								placeholder="In Square Feet" cssClass="form-control patternvalidation"
								data-pattern="decimalvalue" autocomplete="off" required="required" />
							<form:errors path="builtUpArea" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.totalProperties" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="totalProperties" path="totalProperties"
								type="text" cssClass="form-control is_valid_number"
								autocomplete="off" required="required"/>
							<form:errors path="totalProperties" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.totalFloors" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="totalFloors" path="totalFloors" type="text" placeholder="Excluding Ground Floor"
								cssClass="form-control is_valid_number" autocomplete="off" required="required" />
							<form:errors path="totalFloors" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.openSpaceArea" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="openSpaceArea" path="openSpaceArea" type="text"
								placeholder="In Square Feet" cssClass="form-control patternvalidation"
								data-pattern="decimalvalue" autocomplete="off" required="required"/>
							<form:errors path="openSpaceArea" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.liftFacility" /></label>

						<div class="col-sm-6 add-margin">
							<form:checkbox id="liftFacility" path="liftFacility" />
							<form:errors path="liftFacility" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.powerBackup" /></label>

						<div class="col-sm-6 add-margin">
							<form:checkbox id="powerBackup" path="powerBackup" />
							<form:errors path="powerBackup" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.parkingFacility" /></label>

						<div class="col-sm-6 add-margin">
							<form:checkbox id="parkingFacility" path="parkingFacility" />
							<form:errors path="parkingFacility" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.fireFightingFacility" /></label>

						<div class="col-sm-6 add-margin">
							<form:checkbox id="fireFightingFacility"
								path="fireFightingFacility" />
							<form:errors path="fireFightingFacility" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.totalResidentialProperties" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:input id="totalResidentialProperties"
								path="totalResidentialProperties" type="text"
								cssClass="form-control is_valid_number" autocomplete="off" required="required"/>
							<form:errors path="totalResidentialProperties"
								cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.totalNonResidentialProperties" /></label>

						<div class="col-sm-6 add-margin">
							<form:input id="totalNonResidentialProperties"
								path="totalNonResidentialProperties" type="text"
								cssClass="form-control is_valid_number" autocomplete="off" />
							<form:errors path="totalNonResidentialProperties"
								cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.sourceOfWater" /><span class="mandatory"></span></label>

						<div class="col-sm-6">

							<form:select path="sourceOfWater" id="sourceOfWater"
								cssClass="form-control" cssErrorClass="form-control error">
								<form:option value="Own">
													Own
												</form:option>
								<form:option value="Municipal connection">
													Municipal connection
												</form:option>
								<form:option value="Any Other">
													Any Other
												</form:option>
							</form:select>
						</div>
					</div>

					<div class="form-group">
						<label for="Field-1" class="col-sm-3 control-label"><spring:message
								code="lbl.isResidential" /><span class="mandatory"></span></label>

						<div class="col-sm-6 add-margin">
							<form:checkbox id="isResidential" path="isResidential" />
							<form:errors path="isResidential" cssClass="error-msg" />
						</div>
					</div>

				</div>

				<div id="shopdetails" class="hide">
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="lbl.shop.create" />
						</div>
					</div>
					<div class="panel-heading custom_form_panel_heading">




						<table id="shopTable" class="table table-bordered">
							<thead>
								<div>
									<th class="col-sm-1 table-div-column">Shop No.</th>
									<th class="col-sm-1 table-div-column">Floor No.</th>
									<th class="col-sm-2 table-div-column">Shop Area</th>
									<th class="col-sm-2 table-div-column">Owner Name</th>
									<th class="col-sm-2 table-div-column">Shop/Industry</th>
									<th class="col-sm-2 table-div-column">Is License Valid</th>
									<th class="col-sm-1 table-div-column">Tin No.</th>
									<th class="col-sm-2 table-div-column">License Validity</th>
									<th class="col-sm-2 table-div-column">Action</th>
								</div>
							</thead>
							<tbody>
								<c:if test="${apartment.apartmentHouseProxy.size() == 0}">
									<div id="shopDetails">
										<tr>
											<td><form:input id="shopNo"
													path="apartmentHouseProxy[0].shopNo" type="text"
													cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>
											<td><form:input id="floorNo"
													path="apartmentHouseProxy[0].floorNo" type="text"
													cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>
											<td><form:input id="shopArea"
													path="apartmentHouseProxy[0].shopArea"
													placeholder="In Square Feet" type="text"
													cssClass="form-control patternvalidation"
													data-pattern="decimalvalue" autocomplete="off" /></td>
											<td><form:input id="ownerName"
													path="apartmentHouseProxy[0].ownerName" type="text"
													cssClass="form-control patternvalidation"
													data-pattern="alphabetwithspace" autocomplete="off" /></td>
											<td><form:input id="shopOrIndustryName"
													path="apartmentHouseProxy[0].shopOrIndustryName"
													type="text" cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>
											<td><form:checkbox id="licenseStatus"
													path="apartmentHouseProxy[0].licenseStatus"
													checked="checked" /></td>
											<td><form:input id="tinNo"
													path="apartmentHouseProxy[0].tinNo" type="text"
													cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>

											<td><form:input
													path="apartmentHouseProxy[0].licenseValidity" type="text"
													class="licenseValidity form-control low-width"
													autocomplete="off" /></td>

											<td><span class="add-padding" data-toggle="tooltip"
												title="Add"><i id="add_row" class="fa fa-plus-circle"></i></span>
												<span class="add-padding" data-toggle="tooltip"
												title="Delete"><i class="delete_row fa fa-remove"></i></span>
											</td>
										</tr>
									</div>
								</c:if>
								<s:else>
									<c:forEach var="listVar" items="${apartmentHouseProxy}"
										varStatus="houseStatus">
										<tr>
											<td><form:input id="shopNo"
													path="apartmentHouseProxy[#houseStatus.index].shopNo"
													type="text" cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>
											<td><form:input id="floorNo"
													path="apartmentHouseProxy[#houseStatus.index].floorNo"
													type="text" cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>
											<td><form:input id="shopArea"
													path="apartmentHouseProxy[#houseStatus.index].shopArea"
													placeholder="In Square Feet" type="text"
													cssClass="form-control patternvalidation"
													data-pattern="decimalvalue" autocomplete="off" /></td>
											<td><form:input id="ownerName"
													path="apartmentHouseProxy[#houseStatus.index].ownerName"
													type="text" cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>
											<td><form:input id="shopOrIndustryName"
													path="apartmentHouseProxy[#houseStatus.index].shopOrIndustryName"
													type="text" cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>
											<td><form:checkbox id="licenseStatus"
													path="apartmentHouseProxy[#houseStatus.index].licenseStatus"
													checked="checked" /></td>
											<td><form:input id="tinNo"
													path="apartmentHouseProxy[#houseStatus.index].tinNo"
													type="text" cssClass="form-control patternvalidation"
													data-pattern="alphanumericwithspace" autocomplete="off" /></td>

											<td><form:input
													path="apartmentHouseProxy[#houseStatus.index].licenseValidity"
													type="text" class="licenseValidity form-control low-width"
													autocomplete="off" /></td>

											<td><span class="add-padding" data-toggle="tooltip"
												title="Add"><i id="add_row" class="fa fa-plus-circle"></i></span>
												<span class="add-padding" data-toggle="tooltip"
												title="Delete"><i class="delete_row fa fa-remove"></i></span>
											</td>
										</tr>
									</c:forEach>
								</s:else>
								</div>
							</tbody>
						</table>
					</div>

				</div>

			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" class="btn btn-primary add-margin">
						<spring:message code="lbl.button.add" />
					</button>
					<button type="reset" class="btn btn-default">
						<spring:message code="lbl.reset" />
					</button>
					<button type="button" class="btn btn-default"
						onclick="self.close()">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>
		</form:form>
	</div>
</div>
<br />
<br />
<div class="row" style="display: none;" id="table_container">
	<div class="col-md-6 col-xs-6 table-header text-left">The Search
		result is</div>
	<div class="col-sm-12">
		<table class="table table-bordered datatable" id="usages_table">
		</table>
	</div>
</div>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/app/apartment.js?rnd=${app_release_no}'/>"></script>
