/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.workflow.matrix.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkFlowMatrixDetails implements Comparable<WorkFlowMatrixDetails> {

    private Long approverNo;
    private String[] designation;
    private String state;
    private String action;
    private String status;
    private String[] buttons;
    private String department;
    private String objectType;
    private BigDecimal fromQty;
    private BigDecimal toQty;
    private Date fromDate;
    private Date toDate;
    private String additionalRule;
    private String objectTypeAlias;
    private BigDecimal fromQtyAlias;
    private BigDecimal toQtyAlias;
    private Date fromDateAlias;
    private Date toDateAlias;
    private String departmentAlias;
    private String additionalRuleAlias;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private String objectTypeDisplay;
    private Long rejectApproverNo;
    private String[] rejectDesignation;
    private String rejectState;
    private String rejectAction;
    private String rejectStatus;
    private String[] rejectButtons;

    public Long getRejectApproverNo() {
        return rejectApproverNo;
    }

    public void setRejectApproverNo(final Long rejectApproverNo) {
        this.rejectApproverNo = rejectApproverNo;
    }

    public String[] getRejectDesignation() {
        return rejectDesignation;
    }

    public void setRejectDesignation(final String[] rejectDesignation) {
        this.rejectDesignation = rejectDesignation;
    }

    public String getRejectState() {
        return rejectState;
    }

    public void setRejectState(final String rejectState) {
        this.rejectState = rejectState;
    }

    public String getRejectAction() {
        return rejectAction;
    }

    public void setRejectAction(final String rejectAction) {
        this.rejectAction = rejectAction;
    }

    public String getRejectStatus() {
        return rejectStatus;
    }

    public void setRejectStatus(final String rejectStatus) {
        this.rejectStatus = rejectStatus;
    }

    public String[] getRejectButtons() {
        return rejectButtons;
    }

    public void setRejectButtons(final String[] rejectButtons) {
        this.rejectButtons = rejectButtons;
    }

    public String getObjectTypeDisplay() {
        return objectTypeDisplay;
    }

    public void setObjectTypeDisplay(final String objectTypeDisplay) {
        this.objectTypeDisplay = objectTypeDisplay;
    }

    public String getDepartmentAlias() {
        return departmentAlias;
    }

    public void setDepartmentAlias(final String departmentAlias) {
        this.departmentAlias = departmentAlias;
        department = departmentAlias;
    }

    public String getAdditionalRuleAlias() {
        return additionalRuleAlias;
    }

    public void setAdditionalRuleAlias(final String additionalRuleAlias) {
        this.additionalRuleAlias = additionalRuleAlias;
        additionalRule = additionalRuleAlias;
    }

    public String getObjectTypeAlias() {
        return objectTypeAlias;
    }

    public void setObjectTypeAlias(final String objectTypeAlias) {
        this.objectTypeAlias = objectTypeAlias;
        objectType = objectTypeAlias;
    }

    public BigDecimal getFromQtyAlias() {
        return fromQtyAlias;
    }

    public void setFromQtyAlias(final BigDecimal fromQtyAlias) {
        this.fromQtyAlias = fromQtyAlias;
        fromQty = fromQtyAlias;
    }

    public BigDecimal getToQtyAlias() {
        return toQtyAlias;
    }

    public void setToQtyAlias(final BigDecimal toQtyAlias) {
        this.toQtyAlias = toQtyAlias;
        toQty = toQtyAlias;
    }

    public Date getFromDateAlias() {
        return fromDateAlias;
    }

    public void setFromDateAlias(final Date fromDateAlias) {
        this.fromDateAlias = fromDateAlias;
        fromDate = fromDateAlias;
    }

    public Date getToDateAlias() {
        return toDateAlias;
    }

    public void setToDateAlias(final Date toDateAlias) {
        this.toDateAlias = toDateAlias;
        toDate = toDateAlias;
    }

    private List<WorkFlowMatrixDetails> matrixdetails = new ArrayList<WorkFlowMatrixDetails>();

    public String getAdditionalRule() {
        return additionalRule;
    }

    public void setAdditionalRule(final String additionalRule) {
        this.additionalRule = additionalRule;
    }

    public List<WorkFlowMatrixDetails> getMatrixdetails() {
        return matrixdetails;
    }

    public void setMatrixdetails(final List<WorkFlowMatrixDetails> matrixdetails) {
        this.matrixdetails = matrixdetails;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    public BigDecimal getFromQty() {
        return fromQty;
    }

    public void setFromQty(final BigDecimal fromQty) {
        this.fromQty = fromQty;
    }

    public BigDecimal getToQty() {
        return toQty;
    }

    public void setToQty(final BigDecimal toQty) {
        this.toQty = toQty;
    }

    public Date getFromDate() {

        return fromDate;
    }

    public String getFromDateString() {

        return sdf.format(fromDate);
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public String getToDateString() {
        return sdf.format(toDate);
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public String[] getButtons() {
        return buttons;
    }

    public void setButtons(final String[] buttons) {
        this.buttons = buttons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public Long getApproverNo() {
        return approverNo;
    }

    public void setApproverNo(final Long approverNo) {
        this.approverNo = approverNo;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String[] getDesignation() {
        return designation;
    }

    public void setDesignation(final String[] designation) {
        this.designation = designation;
    }

    public String getDesignationString() {
        final StringBuffer str = new StringBuffer();
        if (getDesignation() != null) {
            for (final String designationname : getDesignation()) {
                str.append(designationname);
                str.append(",");
            }
            final String designationString = new String(str);
            return designationString.substring(0, designationString.length() - 1);
        } else
            return "";
    }

    public String getButtonString() {
        final StringBuffer str = new StringBuffer();
        if (getButtons() != null) {
            for (final String buttonname : getButtons()) {
                str.append(buttonname);
                str.append(",");
            }
            final String buttonsString = new String(str);
            return buttonsString.substring(0, buttonsString.length() - 1);
        } else
            return "";
    }

    public String getRejectdesignationString() {
        final StringBuffer str = new StringBuffer();
        if (getRejectDesignation() != null) {
            for (final String designationname : getRejectDesignation()) {
                str.append(designationname);
                str.append(",");
            }
            final String designationString = new String(str);
            return designationString.substring(0, designationString.length() - 1);
        } else
            return "";
    }

    public String getRejectbuttonString() {
        final StringBuffer str = new StringBuffer();
        if (getRejectButtons() != null) {
            for (final String buttonname : getRejectButtons()) {
                str.append(buttonname);
                str.append(",");
            }
            final String buttonsString = new String(str);
            return buttonsString.substring(0, buttonsString.length() - 1);
        } else
            return "";
    }

    @Override
    public int compareTo(final WorkFlowMatrixDetails o) {

        if (getApproverNo() < o.getApproverNo())
            return -1;
        else if (getApproverNo() < o.getApproverNo())
            return 1;
        else
            return 0;
    }

}
