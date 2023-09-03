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

package org.egov.egf.workflow.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.inbox.DefaultInboxRenderServiceImpl;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetDetail;

/**
 * @author eGov
 */
public class BudgetDetailRenderService extends DefaultInboxRenderServiceImpl<BudgetDetail> {

    public BudgetDetailRenderService(final PersistenceService<BudgetDetail, Long> stateAwarePersistenceService) {
        super(stateAwarePersistenceService);
    }

    private List<BudgetDetail> getGroupedWorkflowItems(final List<BudgetDetail> allItems) {
        final List<BudgetDetail> budgetDetailGroup = new ArrayList<BudgetDetail>(0);
        final HashMap<String, Integer> assignedItems = new HashMap<String, Integer>(0);
        for (final StateAware nextItem : allItems)
            if (nextItem instanceof BudgetDetail) {
                final BudgetDetail nextDetail = (BudgetDetail) nextItem;
                final String groupingCriteria = "";

                if (assignedItems.get(groupingCriteria) == null) {
                    budgetDetailGroup.add(nextDetail);
                    assignedItems.put(groupingCriteria, 1);

                }
            }
        return budgetDetailGroup;
    }

    @Override
    public List<BudgetDetail> getDraftWorkflowItems(final Long userId, final List<Long> owner) {
        return getGroupedWorkflowItems(super.getDraftWorkflowItems(userId, owner));
    }

    @Override
    public List<BudgetDetail> getAssignedWorkflowItems(final Long userId, final List<Long> owner) {
        return getGroupedWorkflowItems(super.getAssignedWorkflowItems(userId, owner));
    }
}