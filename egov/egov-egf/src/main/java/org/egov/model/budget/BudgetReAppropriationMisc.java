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
package org.egov.model.budget;

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BudgetReAppropriationMisc extends StateAware {
    private static final long serialVersionUID = 3462810824735494382L;
    private Long id;
    private String sequenceNumber;
    private String remarks;
    private Date reAppropriationDate;
    private EgwStatus status;
    private Set<BudgetReAppropriation> budgetReAppropriations = new HashSet<BudgetReAppropriation>();

    public Set<BudgetReAppropriation> getBudgetReAppropriations() {
        return budgetReAppropriations;
    }

    public void setBudgetReAppropriations(final Set<BudgetReAppropriation> budgetReAppropriations) {
        this.budgetReAppropriations = budgetReAppropriations;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(final String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Date getReAppropriationDate() {
        return reAppropriationDate;
    }

    public void setReAppropriationDate(final Date reAppropriationDate) {
        this.reAppropriationDate = reAppropriationDate;
    }

    @Override
    public String getStateDetails() {
        return sequenceNumber == null ? "" : sequenceNumber;
    }

    public List<BudgetReAppropriation> getNonApprovedReAppropriations() {
        final List<BudgetReAppropriation> reAppList = new ArrayList<BudgetReAppropriation>();
        budgetReAppropriations = budgetReAppropriations == null ? new HashSet<BudgetReAppropriation>() : budgetReAppropriations;
        for (final BudgetReAppropriation entry : budgetReAppropriations)
            if (!Constants.END.equalsIgnoreCase(entry.getState().getValue())
                    || !"APPROVED".equalsIgnoreCase(entry.getState().getValue()))
                reAppList.add(entry);
        return reAppList;
    }

    public BudgetReAppropriation getBudgetReAppropriationWithId(final Long id) {
        for (final BudgetReAppropriation reAppropriation : budgetReAppropriations)
            if (id.equals(reAppropriation.getId()))
                return reAppropriation;
        return null;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

}
