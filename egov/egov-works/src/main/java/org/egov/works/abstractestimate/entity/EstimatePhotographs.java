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
package org.egov.works.abstractestimate.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGW_ESTIMATE_PHOTOGRAPHS")
@SequenceGenerator(name = EstimatePhotographs.SEQ_EGW_ESTIMATEPHOTOGRAPHS, sequenceName = EstimatePhotographs.SEQ_EGW_ESTIMATEPHOTOGRAPHS, allocationSize = 1)
public class EstimatePhotographs extends AbstractAuditable {

    private static final long serialVersionUID = -4760202350886149567L;

    public static final String SEQ_EGW_ESTIMATEPHOTOGRAPHS = "SEQ_EGW_ESTIMATE_PHOTOGRAPHS";

    public enum WorkProgress {
        BEFORE, DURING, AFTER;

        @Override
        public String toString() {
            return StringUtils.replace(name(), "_", " ");
        }

    }

    @Id
    @GeneratedValue(generator = SEQ_EGW_ESTIMATEPHOTOGRAPHS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineestimatedetails")
    private LineEstimateDetails lineEstimateDetails;

    private double latitude;

    private double longitude;

    @SafeHtml
    @Length(max = 1024)
    private String description;

    @Temporal(value = TemporalType.DATE)
    private Date dateOfCapture;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "filestore", nullable = false)
    private FileStoreMapper fileStore;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkProgress workProgress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abstractestimate")
    private AbstractEstimate abstractestimate;

    public EstimatePhotographs() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getDateOfCapture() {
        return dateOfCapture;
    }

    public void setDateOfCapture(final Date dateOfCapture) {
        this.dateOfCapture = dateOfCapture;
    }

    public LineEstimateDetails getLineEstimateDetails() {
        return lineEstimateDetails;
    }

    public void setLineEstimateDetails(final LineEstimateDetails lineEstimateDetails) {
        this.lineEstimateDetails = lineEstimateDetails;
    }

    public FileStoreMapper getFileStore() {
        return fileStore;
    }

    public void setFileStore(final FileStoreMapper fileStore) {
        this.fileStore = fileStore;
    }

    public WorkProgress getWorkProgress() {
        return workProgress;
    }

    public void setWorkProgress(final WorkProgress workProgress) {
        this.workProgress = workProgress;
    }

    public AbstractEstimate getAbstractestimate() {
        return abstractestimate;
    }

    public void setAbstractestimate(final AbstractEstimate abstractestimate) {
        this.abstractestimate = abstractestimate;
    }
}