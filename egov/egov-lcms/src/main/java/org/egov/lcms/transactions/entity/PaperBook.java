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
package org.egov.lcms.transactions.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "EGLC_PAPERBOOK")
@SequenceGenerator(name = PaperBook.SEQ_EGLC_PAPERBOOK, sequenceName = PaperBook.SEQ_EGLC_PAPERBOOK, allocationSize = 1)
public class PaperBook extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_PAPERBOOK = "SEQ_EGLC_PAPERBOOK";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_PAPERBOOK, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "legalcase", nullable = false)
    private LegalCase legalCase;
    private Date lastDateToDepositAmt;
    private Double depositedAmount;
    private String concernedOfficerName;
    private String remarks;
    private boolean isPaperBookRequired;

    public Date getLastDateToDepositAmt() {
        return lastDateToDepositAmt;
    }

    public void setLastDateToDepositAmt(final Date lastDateToDepositAmt) {
        this.lastDateToDepositAmt = lastDateToDepositAmt;
    }

    public Double getDepositedAmount() {
        return depositedAmount;
    }

    public void setDepositedAmount(final Double depositedAmount) {
        this.depositedAmount = depositedAmount;
    }

    public String getConcernedOfficerName() {
        return concernedOfficerName;
    }

    public void setConcernedOfficerName(final String concernedOfficerName) {
        this.concernedOfficerName = concernedOfficerName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public boolean getIsPaperBookRequired() {
        return isPaperBookRequired;
    }

    public void setIsPaperBookRequired(final boolean isPaperBookRequired) {
        this.isPaperBookRequired = isPaperBookRequired;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public void setPaperBookRequired(final boolean isPaperBookRequired) {
        this.isPaperBookRequired = isPaperBookRequired;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

}
