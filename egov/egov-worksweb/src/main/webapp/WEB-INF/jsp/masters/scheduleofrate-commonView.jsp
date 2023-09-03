<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading"></div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<s:text name="master.sor.category" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<s:property value="%{scheduleCategory.code}" />
					</div>

					<div class="col-xs-3 add-margin">
						<s:text name="master.sor.code" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<s:property value="%{code}" />
					</div>

					<div class="col-xs-3 add-margin">
						<s:text name="master.sor.description" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<s:property value="%{description}" />
					</div>
					
					<div class="col-xs-3 add-margin">
						<s:text name="master.sor.uom" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<s:property value="%{uom.uom}" />
					</div>
				</div>
			</div>		
							
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<s:text name="master.sor.rateDetails" />
				</div>
			</div>
			<div class="panel-body">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th><s:text name="column.title.SLNo" /></th>
							<th><s:text name="master.sor.rate" /></th>
							<th><s:text name="master.sor.startDate" /></th>
							<th><s:text name="master.sor.endDate" /></th>
						</tr>
					</thead>
					<tbody>
					<s:iterator id="rateIterator" value="model.sorRates" status="rate_row_status">
						<tr>
							<td><s:property value="#rate_row_status.count" /></td>
							<td><s:property value="%{rate}" /></td>
							<td><s:date name="validity.startDate" format="dd/MM/yyyy" /></td>
							<td><s:date name="validity.endDate" format="dd/MM/yyyy" /></td>
						</tr>
					</s:iterator>
					</tbody>
				</table>
			</div>
		</div>

		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<s:text name="sor.marketrateDetails" />
				</div>
			</div>
			<div class="panel-body">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th><s:text name="column.title.SLNo" /></th>
							<th><s:text name="master.sor.marketRate" /></th>
							<th><s:text name="master.sor.startDate" /></th>
							<th><s:text name="master.sor.endDate" /></th>
						</tr>
					</thead>
					<tbody>
					<s:iterator id="marketRateIterator" value="model.marketRates" status="marketrate_row_status">
						<tr>
							<td><s:property value="#marketrate_row_status.count" /></td>
							<td><s:property value="%{marketRate}" /></td>
							<td><s:date name="validity.startDate" format="dd/MM/yyyy" /></td>
							<td><s:date name="validity.endDate" format="dd/MM/yyyy" /></td>
						</tr>
					</s:iterator>
					</tbody>
				</table>
			</div>
		</div>
</div>
