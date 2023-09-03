/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.model.voucher;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.Designation;

import java.util.List;

public class WorkflowBean {
    private String actionName;
    private String actionState;
    private List<User> appoverUserList;
    private Long approverUserId;
    private String approverComments;
    private Integer departmentId;
    private List<Department> departmentList;
    private Integer designationId;
    private List<Designation> designationList;
    private String workFlowAction;
    private Long approverPositionId;
    private String currentState;

    public String getActionName() {
        return actionName;
    }

    public String getActionState() {
        return actionState;
    }

    public Long getApproverUserId() {
        return approverUserId;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public List<Designation> getDesignationList() {
        return designationList;
    }

    public void setActionName(final String actionName) {
        this.actionName = actionName;
    }

    public void setActionState(final String actionState) {
        this.actionState = actionState;
    }

    public List<User> getAppoverUserList() {
        return appoverUserList;
    }

    public void setAppoverUserList(final List<User> appoverUserList) {
        this.appoverUserList = appoverUserList;
    }

    public void setApproverUserId(final Long approverUserId) {
        this.approverUserId = approverUserId;
    }

    public void setDepartmentList(final List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    public void setDesignationList(final List<Designation> designationList) {
        this.designationList = designationList;
    }
    
    public String getApproverComments() {
        return approverComments;
    }

    public void setApproverComments(String approverComments) {
        this.approverComments = approverComments;
    }

    public String getWorkFlowAction() {
        return workFlowAction;
    }

    public void setWorkFlowAction(String workFlowAction) {
        this.workFlowAction = workFlowAction;
    }
    
    public Long getApproverPositionId() {
        return approverPositionId;
    }

    public void setApproverPositionId(Long approverPositionId) {
        this.approverPositionId = approverPositionId;
    }
    
    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("WorkflowBean={ ");
        str.append("actionName=").append(actionName == null ? "null" : actionName.toString());
        str.append("actionState=").append(actionState == null ? "null" : actionState.toString());
        str.append("appoverUserList=").append(appoverUserList == null ? "null" : appoverUserList.toString());
        str.append("approverUserId=").append(approverUserId == null ? "null" : approverUserId.toString());
        str.append("comments=").append(approverComments == null ? "null" : approverComments.toString());
        str.append("departmentId=").append(departmentId == null ? "null" : departmentId.toString());
        str.append("departmentList=").append(departmentList == null ? "null" : departmentList.toString());
        str.append("designationId=").append(designationId == null ? "null" : designationId.toString());
        str.append("designationList=").append(designationList == null ? "null" : designationList.toString());
        str.append("}");
        return str.toString();
    }

}
