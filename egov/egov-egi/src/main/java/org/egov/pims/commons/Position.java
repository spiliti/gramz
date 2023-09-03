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

package org.egov.pims.commons;

import org.egov.infra.persistence.entity.AbstractAuditable;

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

import static org.egov.pims.commons.Position.SEQ_POSITION;

@Entity
@Table(name = "eg_position")
@SequenceGenerator(name = SEQ_POSITION, sequenceName = SEQ_POSITION, allocationSize = 1)
public class Position extends AbstractAuditable {
    public static final String SEQ_POSITION = "SEQ_EG_POSITION";
    private static final long serialVersionUID = -7237503685614187960L;
    @Id
    @GeneratedValue(generator = SEQ_POSITION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "deptDesig")
    private DeptDesig deptDesig;
    private boolean isPostOutsourced;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isPostOutsourced() {
        return isPostOutsourced;
    }

    public void setPostOutsourced(final boolean isPostOutsourced) {
        this.isPostOutsourced = isPostOutsourced;
    }

    public boolean getIsPostOutsourced() {
        return isPostOutsourced;
    }

    public void setIsPostOutsourced(final boolean isPostOutsourced) {
        this.isPostOutsourced = isPostOutsourced;
    }

    public DeptDesig getDeptDesig() {
        return deptDesig;
    }

    public void setDeptDesig(final DeptDesig deptDesig) {
        this.deptDesig = deptDesig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return this.getName().equals(position.getName());
    }

    @Override
    public int hashCode() {
        return 31 * this.getName().hashCode();
    }

}
