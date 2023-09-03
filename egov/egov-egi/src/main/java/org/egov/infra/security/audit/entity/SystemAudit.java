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

package org.egov.infra.security.audit.entity;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;

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
import java.util.Date;

import static org.egov.infra.security.audit.entity.SystemAudit.SEQ_SYSTEMAUDIT;

@Entity
@Table(name = "eg_systemaudit")
@SequenceGenerator(name = SEQ_SYSTEMAUDIT, sequenceName = SEQ_SYSTEMAUDIT, allocationSize = 1)
public class SystemAudit extends AbstractPersistable<Long> {
    public static final String SEQ_SYSTEMAUDIT = "SEQ_EG_SYSTEMAUDIT";
    private static final long serialVersionUID = 3860739186574812587L;
    @Id
    @GeneratedValue(generator = SEQ_SYSTEMAUDIT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;

    private String ipAddress;

    private String userAgentInfo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loginTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date logoutTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgentInfo() {
        return userAgentInfo;
    }

    public void setUserAgentInfo(final String userAgentInfo) {
        this.userAgentInfo = userAgentInfo;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(final Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(final Date logoutTime) {
        this.logoutTime = logoutTime;
    }
}
