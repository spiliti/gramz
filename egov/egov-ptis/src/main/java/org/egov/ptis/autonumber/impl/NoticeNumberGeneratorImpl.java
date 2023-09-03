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
package org.egov.ptis.autonumber.impl;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.ptis.autonumber.NoticeNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeNumberGeneratorImpl implements NoticeNumberGenerator {

    private static final String SEQ_EGPT_NOTICE_NUMBER = "SEQ_EGPT_NOTICE_NUMBER";

    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    @Override
    public String generateNoticeNumber(final String noticeType) {
        String noticeNumber = "";
        if (StringUtils.isNotBlank(noticeType)) {
            String noticeTypeCode = "";
            if (PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE.equalsIgnoreCase(noticeType))
                noticeTypeCode = "SN";
            else if (PropertyTaxConstants.NOTICE_TYPE_MUTATION_CERTIFICATE.equalsIgnoreCase(noticeType))
                noticeTypeCode = "MC";
            else if (PropertyTaxConstants.NOTICE_TYPE_ESD.equalsIgnoreCase(noticeType))
                noticeTypeCode = "ESD";
            else if (PropertyTaxConstants.NOTICE_TYPE_RPPROCEEDINGS.equalsIgnoreCase(noticeType))
                noticeTypeCode = "RP";
            else if (PropertyTaxConstants.NOTICE_TYPE_GRPPROCEEDINGS.equalsIgnoreCase(noticeType))
                noticeTypeCode = "GRP";
            else if (PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE.equalsIgnoreCase(noticeType))
                noticeTypeCode = "HN";
            else if (PropertyTaxConstants.NOTICE_TYPE_VRPROCEEDINGS.equalsIgnoreCase(noticeType))
                noticeTypeCode = "VR";
            else if (PropertyTaxConstants.NOTICE_TYPE_INVENTORY.equalsIgnoreCase(noticeType))
                noticeTypeCode = "IN";
            else if (PropertyTaxConstants.NOTICE_TYPE_DISTRESS.equalsIgnoreCase(noticeType))
                noticeTypeCode = "DN";
            else if (PropertyTaxConstants.NOTICE_TYPE_EXEMPTION.equalsIgnoreCase(noticeType))
                noticeTypeCode = "EX";
            else if (PropertyTaxConstants.VALUATION_CERTIFICATE.equalsIgnoreCase(noticeType))
                noticeTypeCode = "VC";
            else if (PropertyTaxConstants.NOTICE_TYPE_OC.equalsIgnoreCase(noticeType))
                noticeTypeCode = "OC";
            else if (PropertyTaxConstants.NOTICE_TYPE_OCCUPIER.equalsIgnoreCase(noticeType))
                noticeTypeCode = "ON";
            final String sequenceName = SEQ_EGPT_NOTICE_NUMBER;
            final Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);
            noticeNumber = String.format("%s/%s%06d", noticeTypeCode, ApplicationThreadLocals.getCityCode(), nextSequence);
        }
        return noticeNumber;
    }
}
