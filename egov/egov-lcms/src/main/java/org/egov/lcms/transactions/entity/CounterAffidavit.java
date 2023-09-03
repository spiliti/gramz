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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "eglc_counter_affidavit")
@SequenceGenerator(name = CounterAffidavit.SEQ_EGLC_CA, sequenceName = CounterAffidavit.SEQ_EGLC_CA, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class CounterAffidavit extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_CA = "seq_eglc_counter_affidavit";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_CA, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "legalcase", nullable = false)
    @Audited
    private LegalCase legalCase;

    @Temporal(TemporalType.DATE)
    @Column(name = "counterAffidavitduedate")
    @Audited
    private Date counterAffidavitDueDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "counterAffidavitapprovaldate")
    @Audited
    private Date counterAffidavitApprovalDate;

    @Audited
    @Column(name = "eofficecomputernumber")
    private String eOfficeComputerNumber;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public Date getCounterAffidavitDueDate() {
        return counterAffidavitDueDate;
    }

    public void setCounterAffidavitDueDate(final Date counterAffidavitDueDate) {
        this.counterAffidavitDueDate = counterAffidavitDueDate;
    }

    public Date getCounterAffidavitApprovalDate() {
        return counterAffidavitApprovalDate;
    }

    public void setCounterAffidavitApprovalDate(final Date counterAffidavitApprovalDate) {
        this.counterAffidavitApprovalDate = counterAffidavitApprovalDate;
    }

    public String geteOfficeComputerNumber() {
        return eOfficeComputerNumber;
    }

    public void seteOfficeComputerNumber(final String eOfficeComputerNumber) {
        this.eOfficeComputerNumber = eOfficeComputerNumber;
    }
}