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

package org.egov.tl.repository;

import org.egov.tl.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {

    License findByOldLicenseNumber(String oldLicenseNumber);

    License findByOldLicenseNumberAndIdIsNot(String oldLicenseNumber, Long id);

    License findByLicenseNumber(String licenseNumber);

    License findByApplicationNumber(String applicationNumber);

    List<License> findByIsActiveTrue();

    List<License> findByNatureOfBusinessName(String natureOfBusiness);

    @Query("select l.applicationNumber from License l where upper(l.applicationNumber) like upper('%'||:applicationNumber||'%')")
    List<String> findAllApplicationNumberLike(@Param("applicationNumber") String applicationNumber);

    @Query("select l.licenseNumber from License l where upper(l.licenseNumber) like upper('%'||:licenseNumber||'%')")
    List<String> findAllLicenseNumberLike(@Param("licenseNumber") String licenseNumber);

    @Query("select l.oldLicenseNumber from License l where upper(l.oldLicenseNumber) like upper('%'||:oldLicenseNumber||'%')")
    List<String> findAllOldLicenseNumberLike(@Param("oldLicenseNumber") String oldLicenseNumber);

    @Query("select l.nameOfEstablishment from License l where upper(l.nameOfEstablishment) like upper('%'||:nameOfEstablishment||'%')")
    List<String> findAllNameOfEstablishmentLike(@Param("nameOfEstablishment") String nameOfEstablishment);

    @Query("select l.licensee.applicantName from License l where upper(l.licensee.applicantName) like upper('%'||:applicantName||'%')")
    List<String> findAllApplicantNameLike(@Param("applicantName") String applicantName);

    @Query("select l.assessmentNo from License l where upper(l.assessmentNo) like upper('%'||:assessmentNo||'%')")
    List<String> findAllAssessmentNoLike(@Param("assessmentNo") String assessmentNo);

    @Query("select l.licensee.mobilePhoneNumber from License l where l.licensee.mobilePhoneNumber like '%'||:mobilePhoneNumber||'%'")
    List<String> findAllMobilePhoneNumberLike(@Param("mobilePhoneNumber") String mobilePhoneNumber);

    @Query("select l from org.egov.tl.entity.License l where l.natureOfBusiness.name=:natureOfBusiness and l.status.name=:status and l.licenseDemand.egInstallmentMaster.fromDate < :installmentFromDate ")
    List<License> findByNatureOfBusinessNameAndStatusName(@Param("natureOfBusiness") String natureOfBusiness, @Param("status") String status, @Param("installmentFromDate") Date installmentFromDate);

}
