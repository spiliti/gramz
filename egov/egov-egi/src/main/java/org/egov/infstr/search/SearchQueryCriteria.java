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

package org.egov.infstr.search;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

/**
 * Class representing a search query that uses hibernate Criteria. Can be used by search forms that use hibernate Criteria for fetching search results.
 */
public class SearchQueryCriteria implements SearchQuery {
	private final Criteria searchCriteria;
	private final Criteria countCriteria;

	public SearchQueryCriteria(final Criteria searchCriteria, final Criteria countCriteria) {
		if (searchCriteria == countCriteria) {
			throw new ApplicationRuntimeException("Search Criteria cannot be same as Count Criteria. Please pass different objects!");
		}

		this.searchCriteria = searchCriteria;

		this.countCriteria = countCriteria;
		this.countCriteria.setProjection(Projections.rowCount());
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.search.SearchQuery#getCount(org.egov.infstr.services. PersistenceService)
	 */
	@Override
	public int getCount(final PersistenceService persistenceService) {
		return ((Integer) this.countCriteria.uniqueResult()).intValue();
	}

	/*
	 * (non-Javadoc)
	 * @seeorg.egov.infstr.search.SearchQuery#getPage(org.egov.infstr.services. PersistenceService, int, int)
	 */
	@Override
	public Page getPage(final PersistenceService persistenceService, final int pageNum, final int pageSize) {
		return new Page(this.searchCriteria, pageNum, pageSize);
	}
}
