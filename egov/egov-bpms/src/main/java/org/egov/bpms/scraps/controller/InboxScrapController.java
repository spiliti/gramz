/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *
 *       Copyright (C) 2016  eGovernments Foundation
 *
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.bpms.scraps.controller;

import com.google.gson.GsonBuilder;
import org.egov.bpms.service.inbox.ActivitiInboxService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.support.ui.Inbox;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.infra.utils.StringUtils.escapeSpecialChars;
import static org.egov.infra.workflow.entity.StateAware.byCreatedDate;

@Controller
@RequestMapping("/bpms/inbox")
public class InboxScrapController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a");

    @Autowired
    private InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ActivitiInboxService activitiInboxService;

    @RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String showInbox() {
       return  createInboxData(inboxRenderServiceDeligate.getInboxItems(securityUtils.getCurrentUser().getId()));
    }

    @RequestMapping(value = "/draft", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String showInboxDraft() {
        return createInboxData(inboxRenderServiceDeligate.getInboxDraftItems(securityUtils.getCurrentUser().getId()));
    }

    @RequestMapping(value = "/history", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String showInboxHistory(@RequestParam final Long stateId) {
        return createInboxHistoryData(inboxRenderServiceDeligate.getWorkflowHistory(stateId));
    }

    private String createInboxData(final List<StateAware> inboxStates) {
        final List<Inbox> inboxItems = new LinkedList<>();
        inboxStates.sort(byCreatedDate());
        for (final StateAware stateAware : inboxStates) {
            final State state = stateAware.getCurrentState();
            final WorkflowTypes workflowTypes = inboxRenderServiceDeligate.getWorkflowType(stateAware.getStateType());
            final Inbox inboxItem = new Inbox();
            inboxItem.setId(workflowTypes.isGrouped() ? EMPTY : state.getId() + "#" + workflowTypes.getId());
            inboxItem.setDate(DATE_FORMATTER.print(new DateTime(state.getCreatedDate())));
            inboxItem.setSender(state.getSenderName());
            inboxItem.setTask(isBlank(state.getNatureOfTask()) ? workflowTypes.getDisplayName() : state.getNatureOfTask());
            final String nextAction = inboxRenderServiceDeligate.getNextAction(state);
            inboxItem.setStatus(state.getValue() + (isBlank(nextAction) ? EMPTY : " - " + nextAction));
            inboxItem.setDetails(isBlank(stateAware.getStateDetails()) ? EMPTY : stateAware.getStateDetails());
            inboxItem.setLink(workflowTypes.getLink().replace(":ID", stateAware.myLinkId()));
            inboxItems.add(inboxItem);
        }
        List<Inbox> activitiInboxItems= activitiInboxService.search(securityUtils.getCurrentUser().getUsername());
        inboxItems.addAll(activitiInboxItems);
        return "{ \"data\":" + new GsonBuilder().create().toJson(inboxItems) + "}";
    }

    private String createInboxHistoryData(final List<StateHistory> stateHistories) {
        final List<Inbox> inboxHistoryItems = new LinkedList<>();
        for (final StateHistory stateHistory : stateHistories) {
            final WorkflowTypes workflowTypes = inboxRenderServiceDeligate.getWorkflowType(stateHistory.getState().getType());
            final Inbox inboxHistoryItem = new Inbox();
            inboxHistoryItem.setId(stateHistory.getState().getId().toString());
            inboxHistoryItem.setDate(DATE_FORMATTER.print(new DateTime(stateHistory.getLastModifiedDate())));
            inboxHistoryItem.setSender(stateHistory.getSenderName());
            inboxHistoryItem.setTask(isBlank(stateHistory.getNatureOfTask()) ? workflowTypes.getDisplayName() : stateHistory.getNatureOfTask());
            inboxHistoryItem
                    .setStatus(stateHistory.getValue() + (isBlank(stateHistory.getNextAction()) ? EMPTY : "-" + stateHistory.getNextAction()));
            inboxHistoryItem.setDetails(isBlank(stateHistory.getComments()) ? EMPTY : escapeSpecialChars(stateHistory.getComments()));
            inboxHistoryItem.setLink(EMPTY);
            inboxHistoryItems.add(inboxHistoryItem);
        }

        return "{ \"data\":" + new GsonBuilder().disableHtmlEscaping().create().toJson(inboxHistoryItems) + "}";
    }
}
