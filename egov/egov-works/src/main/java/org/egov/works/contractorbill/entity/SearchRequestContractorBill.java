/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.works.contractorbill.entity;

import java.util.Date;

public class SearchRequestContractorBill {

    private Date billFromDate;
    private Date billToDate;
    private String billType;
    private String billNumber;
    private String status;
    private String workIdentificationNumber;
    private String contractorName;
    private Long department;
    private boolean spillOverFlag;
    private String workOrderNumber;

    public SearchRequestContractorBill() {
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(final String billType) {
        this.billType = billType;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(final String billNumber) {
        this.billNumber = billNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(final String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Long getDepartment() {
        return department;
    }

    public Date getBillFromDate() {
        return billFromDate;
    }

    public void setBillFromDate(final Date billFromDate) {
        this.billFromDate = billFromDate;
    }

    public Date getBillToDate() {
        return billToDate;
    }

    public void setBillToDate(final Date billToDate) {
        this.billToDate = billToDate;
    }

    public boolean isSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(final boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }
}
