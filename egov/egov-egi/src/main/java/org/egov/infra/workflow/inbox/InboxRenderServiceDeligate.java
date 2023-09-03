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

package org.egov.infra.workflow.inbox;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.service.StateService;
import org.egov.infra.workflow.service.WorkflowActionService;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.egov.infstr.services.EISServeable;
import org.egov.pims.commons.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;

@Service
@Transactional(readOnly = true)
public class InboxRenderServiceDeligate<T extends StateAware> {
    private static final Logger LOG = LoggerFactory.getLogger(InboxRenderServiceDeligate.class);
    private static final String INBOX_RENDER_SERVICE_SUFFIX = "InboxRenderService";
    private static final Map<String, WorkflowTypes> WORKFLOWTYPE_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private StateService stateService;

    @Autowired
    @Qualifier("eisService")
    private EISServeable eisService;

    @Autowired
    private WorkflowTypeService workflowTypeService;

    @Autowired
    private WorkflowActionService workflowActionService;

    @ReadOnly
    public List<StateHistory> getWorkflowHistory(final Long stateId) {
        return new LinkedList<>(stateService.getStateById(stateId).getHistory());
    }

    @ReadOnly
    public List<T> getInboxItems() {
        final List<T> assignedWFItems = new ArrayList<>();
        List<Long> owners = currentUserPositionIds();
        if (!owners.isEmpty()) {
            final List<String> wfTypes = stateService.getAssignedWorkflowTypeNames(owners);
            for (final String wfType : wfTypes) {
                final Optional<InboxRenderService<T>> inboxRenderService = this.getInboxRenderService(wfType);
                if (inboxRenderService.isPresent())
                    assignedWFItems.addAll(inboxRenderService.get().getAssignedWorkflowItems(getUserId(), owners));
            }
        }
        return assignedWFItems;
    }

    @ReadOnly
    public List<T> getInboxDraftItems() {
        final List<T> draftWfItems = new ArrayList<>();
        List<Long> owners = currentUserPositionIds();
        if (!owners.isEmpty()) {
            final List<String> wfTypes = stateService.getAssignedWorkflowTypeNames(owners);
            for (final String wfType : wfTypes) {
                final Optional<InboxRenderService<T>> inboxRenderService = getInboxRenderService(wfType);
                if (inboxRenderService.isPresent())
                    draftWfItems.addAll(inboxRenderService.get().getDraftWorkflowItems(getUserId(), owners));
            }
        }
        return draftWfItems;
    }

    public WorkflowTypes getWorkflowType(final String wfType) {
        WorkflowTypes workflowType = WORKFLOWTYPE_CACHE.get(wfType);
        if (workflowType == null) {
            workflowType = workflowTypeService.getEnabledWorkflowTypeByType(wfType);
            if (workflowType != null)
                WORKFLOWTYPE_CACHE.put(wfType, workflowType);
        }
        return workflowType;
    }

    public Optional<InboxRenderService<T>> getInboxRenderService(final String wfType) {
        InboxRenderService<T> inboxRenderService = null;
        try {
            if (getWorkflowType(wfType) != null)
                inboxRenderService = applicationContext.getBean(wfType.concat(INBOX_RENDER_SERVICE_SUFFIX), InboxRenderService.class);
        } catch (final BeansException e) {
            LOG.warn("InboxRenderService bean for {} not found, have you defined {}InboxRenderService bean ?", wfType, wfType, e);
        }
        return Optional.ofNullable(inboxRenderService);
    }

    public String getNextAction(final State state) {
        String nextAction = EMPTY;
        if (state.getNextAction() != null) {
            final WorkflowAction workflowAction = workflowActionService.getWorkflowActionByNameAndType(state.getNextAction(), state.getType());
            if (workflowAction == null)
                nextAction = state.getNextAction();
            else
                nextAction = workflowAction.getDescription() == null ? state.getNextAction() : workflowAction.getDescription();
        }
        return nextAction;
    }

    private List<Long> currentUserPositionIds() {
        return this.eisService.getPositionsForUser(getUserId(), new Date()).parallelStream()
                .map(Position::getId).collect(Collectors.toList());
    }
}