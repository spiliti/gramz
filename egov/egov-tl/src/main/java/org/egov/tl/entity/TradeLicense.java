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

package org.egov.tl.entity;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.egov.tl.utils.Constants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "egtl_trade_license")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class TradeLicense extends License {

    private static final long serialVersionUID = 986289058758315223L;
    @Transient
    private List<Integer> financialyear = new ArrayList<>();
    @Transient
    private List<Integer> legacyInstallmentwiseFees = new ArrayList<>();
    @Transient
    private List<Boolean> legacyFeePayStatus = new ArrayList<>();
    @Transient
    private MultipartFile[] files;

    @Override
    public String getStateDetails() {
        final StringBuilder details = new StringBuilder();
        if (isNotBlank(getLicenseNumber()))
            details.append("Trade License Number").append(getLicenseNumber()).append(" and ");
        details.append("App No. ").append(applicationNumber).append(" dated ").append(toDefaultDateFormat(applicationDate));
        if (isNotBlank(getState().getComments()))
            details.append("<br/> Remarks : ").append(getState().getComments());
        return details.toString();
    }

    @Override
    public String myLinkId() {
        if (Constants.CLOSURE_NATUREOFTASK.equals(getState().getNatureOfTask()))
            return "/tl/viewtradelicense/viewTradeLicense-closure.action?model.id=" + id;
        else
            return "/tl/newtradelicense/newTradeLicense-showForApproval.action?model.id=" + id;
    }

    public List<Integer> getFinancialyear() {
        return financialyear;
    }

    public void setFinancialyear(final List<Integer> financialyear) {
        this.financialyear = financialyear;
    }

    public List<Integer> getLegacyInstallmentwiseFees() {
        return legacyInstallmentwiseFees;
    }

    public void setLegacyInstallmentwiseFees(final List<Integer> legacyInstallmentwiseFees) {
        this.legacyInstallmentwiseFees = legacyInstallmentwiseFees;
    }

    public List<Boolean> getLegacyFeePayStatus() {
        return legacyFeePayStatus;
    }

    public void setLegacyFeePayStatus(final List<Boolean> legacyFeePayStatus) {
        this.legacyFeePayStatus = legacyFeePayStatus;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

}
