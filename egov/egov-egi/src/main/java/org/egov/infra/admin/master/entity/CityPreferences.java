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

package org.egov.infra.admin.master.entity;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static org.egov.infra.admin.master.entity.CityPreferences.SEQ_CITY_PREF;

@Entity
@Table(name = "eg_citypreferences")
@SequenceGenerator(name = SEQ_CITY_PREF, sequenceName = SEQ_CITY_PREF, allocationSize = 1)
public class CityPreferences extends AbstractAuditable {

    public static final String SEQ_CITY_PREF = "SEQ_EG_CITYPREFERENCES";
    private static final long serialVersionUID = -7160795726709889116L;
    @Id
    @GeneratedValue(generator = SEQ_CITY_PREF, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "municipalityLogo")
    @NotNull
    @Fetch(FetchMode.JOIN)
    private FileStoreMapper municipalityLogo;

    @NotNull
    @SafeHtml
    @Length(max = 50)
    private String municipalityName;

    @SafeHtml
    @Length(max = 20)
    private String municipalityContactNo;

    @SafeHtml
    @Length(max = 200)
    private String municipalityAddress;

    @SafeHtml
    @Length(max = 50)
    private String municipalityContactEmail;

    @SafeHtml
    @Length(max = 100)
    private String municipalityGisLocation;

    @SafeHtml
    @Length(max = 20)
    private String municipalityCallCenterNo;

    @SafeHtml
    @Length(max = 100)
    private String municipalityFacebookLink;

    @SafeHtml
    @Length(max = 100)
    private String municipalityTwitterLink;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public FileStoreMapper getMunicipalityLogo() {
        return municipalityLogo;
    }

    public void setMunicipalityLogo(final FileStoreMapper municipalityLogo) {
        this.municipalityLogo = municipalityLogo;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(final String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getMunicipalityContactNo() {
        return municipalityContactNo;
    }

    public void setMunicipalityContactNo(final String municipalityContactNo) {
        this.municipalityContactNo = municipalityContactNo;
    }

    public String getMunicipalityAddress() {
        return municipalityAddress;
    }

    public void setMunicipalityAddress(final String municipalityAddress) {
        this.municipalityAddress = municipalityAddress;
    }

    public String getMunicipalityContactEmail() {
        return municipalityContactEmail;
    }

    public void setMunicipalityContactEmail(final String municipalityContactEmail) {
        this.municipalityContactEmail = municipalityContactEmail;
    }

    public String getMunicipalityGisLocation() {
        return municipalityGisLocation;
    }

    public void setMunicipalityGisLocation(final String municipalityGisLocation) {
        this.municipalityGisLocation = municipalityGisLocation;
    }

    public String getMunicipalityCallCenterNo() {
        return municipalityCallCenterNo;
    }

    public void setMunicipalityCallCenterNo(final String municipalityCallCenterNo) {
        this.municipalityCallCenterNo = municipalityCallCenterNo;
    }

    public String getMunicipalityFacebookLink() {
        return municipalityFacebookLink;
    }

    public void setMunicipalityFacebookLink(final String municipalityFacebookLink) {
        this.municipalityFacebookLink = municipalityFacebookLink;
    }

    public String getMunicipalityTwitterLink() {
        return municipalityTwitterLink;
    }

    public void setMunicipalityTwitterLink(final String municipalityTwitterLink) {
        this.municipalityTwitterLink = municipalityTwitterLink;
    }

    public boolean logoExist() {
        return municipalityLogo != null;
    }
}
