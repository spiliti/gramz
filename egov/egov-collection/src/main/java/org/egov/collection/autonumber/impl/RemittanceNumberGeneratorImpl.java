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
package org.egov.collection.autonumber.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.collection.autonumber.RemittanceNumberGenerator;
import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.CFinancialYear;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemittanceNumberGeneratorImpl implements RemittanceNumberGenerator {

    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    @Override
    public String generateRemittanceNumber(final CFinancialYear financialYear) {

        final String APP_NUMBER_SEQ_PREFIX = "SQ_REMITTANCE%s";
        final SimpleDateFormat sdf = new SimpleDateFormat("MM");
        final String formattedDate = sdf.format(new Date());

        final String currentYear = DateUtils.currentDateToYearFormat();
        final String sequenceName = String.format(APP_NUMBER_SEQ_PREFIX, currentYear);
        final Serializable sequenceNumber = applicationSequenceNumberGenerator.getNextSequence(sequenceName);

        final String result = String.format("%s/%06d/%s/%s", CollectionConstants.REMITTANCE_NUMBER_PREFIX,
                sequenceNumber, formattedDate, financialYear.getFinYearRange());
        return result;
    }

}
