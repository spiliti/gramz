/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.masters.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egswtax_feesdetail_master")
@SequenceGenerator(name = FeesDetailMaster.SEQ_FEESDETAILMASTER, sequenceName = FeesDetailMaster.SEQ_FEESDETAILMASTER, allocationSize = 1)
public class FeesDetailMaster extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = -8684164217549324091L;

    public static final String SEQ_FEESDETAILMASTER = "SEQ_EGSWTAX_FEESDETAIL_MASTER";

    @Id
    @GeneratedValue(generator = SEQ_FEESDETAILMASTER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(max = 64)
    private String description;

    @NotNull
    @SafeHtml
    @Length(max = 12)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "fees", nullable = false)
    private FeesMaster fees;

    private Boolean isMandatory;
    private Boolean isFixedRate;
    private BigDecimal amount;
    
    public Boolean getIsFixedRate() {
        return isFixedRate;
    }

    public void setFixedRate(Boolean isFixedRate) {
        this.isFixedRate = isFixedRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private boolean isActive;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public FeesMaster getFees() {
        return fees;
    }

    public void setFees(final FeesMaster fees) {
        this.fees = fees;
    }

    public Boolean isMandatory() {
        return isMandatory;
    }
    public Boolean getIsMandatory() {
        return isMandatory;
    } 

    public void setMandatory(final boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

}
