/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 *
 * Copyright (C) <2015> eGovernments Foundation
 *
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 *
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 *
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 *
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 *
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 *
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.transactions.repository;

import java.util.Date;
import java.util.List;

import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SewerageApplicationDetailsRepository extends JpaRepository<SewerageApplicationDetails, Long> {

    SewerageApplicationDetails findByApplicationNumber(String applicationNumber);

    SewerageApplicationDetails findByApplicationNumberAndConnection_Status(String applicationNumber,
            SewerageConnectionStatus connectionStatus);

    List<SewerageApplicationDetails> findAllByApplicationDateOrderByApplicationNumberAsc(Date applicationDate);

    List<SewerageApplicationDetails> findAllByApplicationDateAndConnection_StatusOrderByApplicationNumberAsc(
            Date applicationDate, SewerageConnectionStatus connectionStatus);

    List<SewerageApplicationDetails> findAllByApplicationTypeOrderByApplicationNumberAsc(SewerageApplicationType applicationType);

    List<SewerageApplicationDetails> findAllByApplicationTypeAndConnection_StatusOrderByApplicationNumberAsc(
            SewerageApplicationType applicationType, SewerageConnectionStatus connectionStatus);

    SewerageApplicationDetails findByConnection_ShscNumberAndConnection_Status(String shscNumber,
            SewerageConnectionStatus connectionStatus);

    List<SewerageApplicationDetails> findByConnection_ShscNumber(String shscNumber);

    SewerageApplicationDetails findByConnection(SewerageConnection sewerageConnection);

    SewerageApplicationDetails findByConnectionAndConnection_Status(SewerageConnection sewerageConnection,
            SewerageConnectionStatus connectionStatus);

    @Query("select scd from SewerageApplicationDetails scd where scd.connection.status not in ('INACTIVE') and scd.connectionDetail.propertyIdentifier =:propertyIdentifier order by scd.id desc")
    List<SewerageApplicationDetails> getSewerageConnectionDetailsByPropertyID(
            @Param("propertyIdentifier") String propertyIdentifier); 
    
    @Query("select scd from SewerageApplicationDetails scd where scd.isActive='t' and scd.connection.shscNumber =:shscNumber")
    SewerageApplicationDetails getActiveSewerageApplicationByShscNumber(@Param("shscNumber") String shscNumber); 
    
    @Query("select scd from SewerageApplicationDetails scd where scd.connection.shscNumber =:shscNumber and scd.isActive='f' and upper(scd.status.code) not in ('CANCELLED','SANCTIONED')")
    SewerageApplicationDetails getSewerageApplicationInWorkFlow(@Param("shscNumber") String shscNumber);

}