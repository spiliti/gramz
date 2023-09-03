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

package org.egov.infra.admin.master.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.Action;
import org.egov.infra.admin.master.repository.ActionRepository;
import org.egov.infra.web.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActionService {

    @Autowired
    private ActionRepository actionRepository;

    public Action getActionByName(final String name) {
        return actionRepository.findByName(name);
    }

    public Action getActionById(final Long id) {
        return actionRepository.findOne(id);
    }

    @Transactional
    public Action saveAction(Action action) {
        return actionRepository.save(action);
    }

    public Action getActionByUrlAndContextRoot(final String url, final String contextRoot) {
        Action action = null;
        if (url.contains("?")) {
            final String queryParams = WebUtils.extractQueryParamsFromUrl(url);
            final String urlPart = WebUtils.extractURLWithoutQueryParams(url);
            action = actionRepository.findByUrlAndContextRootAndQueryParams(urlPart, contextRoot, queryParams);
            if (action == null)
                action = findNearestMatchingAction(urlPart,
                        actionRepository.findByMatchingUrlAndContextRootAndQueryParams(urlPart, contextRoot, queryParams));
        } else {
            action = actionRepository.findByUrlAndContextRootAndQueryParamsIsNull(url, contextRoot);
            if (action == null)
                action = findNearestMatchingAction(url, actionRepository.findByMatchingUrlAndContextRoot(url, contextRoot));
        }
        return action;
    }

    private Action findNearestMatchingAction(final String url, final List<Action> actions) {
        // This is to avoid vertical escalation wrt to REST URL as much as
        // possible. TODO This required to be reworked.
        return actions.isEmpty() ? null
                : actions.parallelStream().filter(action -> StringUtils.getLevenshteinDistance(url, action.getUrl()) < 1)
                        .findFirst().orElse(actions.get(0));
    }

}
