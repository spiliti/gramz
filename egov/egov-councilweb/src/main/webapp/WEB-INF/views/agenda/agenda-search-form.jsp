<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2016>  eGovernments Foundation
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

		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">Search Agenda</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-sm-2 control-label text-right"><spring:message
									code="lbl.committeetype" /> </label>
							<div class="col-sm-3 add-margin">
								<form:select path="committeeType" id="committeeType"
									cssClass="form-control" cssErrorClass="form-control error">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${committeeType}" itemValue="id"
										itemLabel="name" />
								</form:select>
								<form:errors path="committeeType" cssClass="error-msg" />
							</div>
							
							<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.agendaNumber" /> </label>
							<div class="col-sm-3 add-margin">
								<form:input type="text" cssClass="form-control"
									path="agendaNumber" id="agendaNumber" data-pattern="number"/>
								<form:errors path="agendaNumber" cssClass="error-msg" />
							</div>
						</div>
						<div class="form-group">
							 <label class="col-sm-2 control-label text-right"><spring:message
									code="lbl.fromdate" /> </label>
							<div class="col-sm-3 add-margin">
									<form:input path="fromDate"
										class="form-control text-left patternvalidation datepicker"
										 data-date-end-date="0d"  />
									<form:errors path="fromDate" cssClass="error-msg" />
							</div> 
							
							<label class="col-sm-2 control-label text-right"><spring:message
							code="lbl.todate" /> </label>
							<div class="col-sm-3 add-margin">
								<form:input type="text" cssClass="form-control datepicker"
									path="toDate" id="meetingDate"/>
								<form:errors path="toDate" cssClass="error-msg" />
							</div>
						</div>
							
						<input type="hidden" id="mode" name="mode" value="${mode}" />
				</div>
			</div>
		</div>
	</div>
	<div class="form-group">
		<div class="text-center">
			<button type='button' class='btn btn-primary' id="btnsearch">
				<spring:message code='lbl.search' />
			</button>
			<button type="reset" class="btn btn-danger"><spring:message code="lbl.reset"/></button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
    </div>
	