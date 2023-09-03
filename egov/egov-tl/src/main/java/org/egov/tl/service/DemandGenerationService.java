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
package org.egov.tl.service;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.entity.DemandGenerationLogDetail;
import org.egov.tl.entity.License;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.egov.infra.persistence.utils.PersistenceUtils.flushBatchUpdate;
import static org.egov.tl.entity.enums.ProcessStatus.COMPLETED;
import static org.egov.tl.entity.enums.ProcessStatus.INCOMPLETE;
import static org.egov.tl.entity.enums.ProcessStatus.INPROGRESS;
import static org.egov.tl.utils.Constants.PERMANENT_NATUREOFBUSINESS;
import static org.egov.tl.utils.Constants.TRADE_LICENSE;

@Service
@Transactional(readOnly = true)
public class DemandGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandGenerationService.class);
    private static final String LICENSE_NOT_ACTIVE = "License Not Active";
    private static final String SUCCESSFUL = "Successful";
    private static final String DEMAND_EXIST = "Demand exist";
    private static final String ERRORMSG = "Error occurred while generating demand for license {}";

    @Autowired
    private DemandGenerationLogService demandGenerationLogService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    @Qualifier("tradeLicenseService")
    private AbstractLicenseService licenseService;

    private int batchSize;

    @Autowired
    public DemandGenerationService(ApplicationProperties applicationProperties) {
        this.batchSize = applicationProperties.getBatchUpdateSize();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 7200)
    public DemandGenerationLog generateDemand(String installmentYearRange) {

        DemandGenerationLog demandGenerationLog = demandGenerationLogService.getDemandGenerationLogByInstallmentYear(installmentYearRange);
        if (demandGenerationLog != null)
            return demandGenerationLog;

        DemandGenerationLog previousDemandGenerationLog = demandGenerationLogService.getPreviousInstallmentDemandGenerationLog(installmentYearRange);
        if (previousDemandGenerationLog != null && previousDemandGenerationLog.getDemandGenerationStatus().equals(INCOMPLETE))
            throw new ApplicationRuntimeException("TL-008");

        CFinancialYear installmentYear = financialYearService.getFinacialYearByYearRange(installmentYearRange);
        if (!installmentYearValidForDemandGeneration(installmentYear))
            throw new ApplicationRuntimeException("TL-006");

        demandGenerationLog = demandGenerationLogService.createDemandGenerationLog(installmentYearRange);
        List<License> licenses = licenseService.getAllLicensesByNatureOfBusiness(PERMANENT_NATUREOFBUSINESS);
        return generateDemand(demandGenerationLog, installmentYear, licenses);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 7200)
    public DemandGenerationLog retryFailedDemandGeneration(String installmentYearRange) {
        DemandGenerationLog demandGenerationLog = demandGenerationLogService.getDemandGenerationLogByInstallmentYear(installmentYearRange);
        Module module = moduleService.getModuleByName(TRADE_LICENSE);
        CFinancialYear installmentYear = financialYearService.getFinacialYearByYearRange(installmentYearRange);
        Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(module, installmentYear.getStartingDate());
        int batchUpdateCount = 0;
        for (DemandGenerationLogDetail detail : demandGenerationLog.getDetails()) {
            if (detail.getStatus().equals(INCOMPLETE)) {
                try {
                    licenseService.raiseDemand(detail.getLicense(), module, installment);
                    detail.setDetail(SUCCESSFUL);
                    detail.setStatus(COMPLETED);
                } catch (RuntimeException e) {
                    LOGGER.warn(ERRORMSG, detail.getLicense().getLicenseNumber(), e);
                    demandGenerationLogService.updateDemandGenerationLogDetailOnException(demandGenerationLog, detail, e);
                }
                flushBatchUpdate(entityManager, ++batchUpdateCount, batchSize);
            }
        }
        return demandGenerationLogService.completeDemandGenerationLog(demandGenerationLog);
    }

    private boolean installmentYearValidForDemandGeneration(CFinancialYear installmentYear) {
        DateTime currentDate = new DateTime();
        DateTime startOfCalenderDate = new DateTime(installmentYear.getStartingDate()).monthOfYear().
                withMinimumValue().dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
        DateTime endOfCalenderDate = startOfCalenderDate.monthOfYear().withMaximumValue().dayOfMonth().
                withMaximumValue().millisOfDay().withMaximumValue();
        return currentDate.isAfter(startOfCalenderDate) && currentDate.isBefore(endOfCalenderDate);
    }


    private DemandGenerationLog generateDemand(DemandGenerationLog demandGenerationLog, CFinancialYear installmentYear, List<License> licenses) {
        Module module = moduleService.getModuleByName(TRADE_LICENSE);
        Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(module, installmentYear.getStartingDate());
        if (installment == null)
            throw new ApplicationRuntimeException("TL-005");
        demandGenerationLog.setDemandGenerationStatus(INPROGRESS);
        int batchUpdateCount = 0;
        for (License license : licenses) {
            DemandGenerationLogDetail demandGenerationLogDetail = demandGenerationLogService.
                    createOrGetDemandGenerationLogDetail(demandGenerationLog, license);
            try {
                if (!license.getIsActive()) {
                    demandGenerationLogDetail.setDetail(LICENSE_NOT_ACTIVE);
                } else if (!installment.equals(license.getCurrentDemand().getEgInstallmentMaster())) {
                    licenseService.raiseDemand(license, module, installment);
                    demandGenerationLogDetail.setDetail(SUCCESSFUL);
                } else {
                    demandGenerationLogDetail.setDetail(DEMAND_EXIST);
                }
                demandGenerationLogDetail.setStatus(COMPLETED);
            } catch (RuntimeException e) {
                LOGGER.warn(ERRORMSG, license.getLicenseNumber(), e);
                demandGenerationLogService.updateDemandGenerationLogDetailOnException(demandGenerationLog, demandGenerationLogDetail, e);
            }
            flushBatchUpdate(entityManager, ++batchUpdateCount, batchSize);
        }

        return demandGenerationLogService.completeDemandGenerationLog(demandGenerationLog);
    }

    @Transactional
    public boolean generateLicenseDemand(License license) {
        boolean generationSuccess = true;
        try {
            licenseService.raiseDemand(license, licenseService.getModuleName(), installmentDao.
                    getInsatllmentByModuleForGivenDate(licenseService.getModuleName(),
                            new DateTime().withMonthOfYear(4).withDayOfMonth(1).toDate()));
        } catch (ValidationException e) {
            LOGGER.warn(ERRORMSG, license.getLicenseNumber(), e);
            generationSuccess = false;
        }
        return generationSuccess;
    }

    public CFinancialYear getLatestFinancialYear() {
        return financialYearService.getFinancialYearByDate(new DateTime().withMonthOfYear(4).withDayOfMonth(1).toDate());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 7200)
    public DemandGenerationLog generateMissingDemand(String installmentYearRange) {

        DemandGenerationLog demandGenerationLog = demandGenerationLogService.getDemandGenerationLogByInstallmentYear(installmentYearRange);

        DemandGenerationLog previousDemandGenerationLog = demandGenerationLogService.getPreviousInstallmentDemandGenerationLog(installmentYearRange);
        if (previousDemandGenerationLog != null && previousDemandGenerationLog.getDemandGenerationStatus().equals(INCOMPLETE))
            throw new ApplicationRuntimeException("TL-008");

        CFinancialYear installmentYear = financialYearService.getFinacialYearByYearRange(installmentYearRange);
        if (!installmentYearValidForDemandGeneration(installmentYear))
            throw new ApplicationRuntimeException("TL-006");
        List<License> licenses = licenseService.getLicensesForDemandGeneration(PERMANENT_NATUREOFBUSINESS, installmentYear);
        List<License> demandMissingLicenses = new ArrayList<>();
        Set<License> demandLogLicenses = new HashSet<>();
        demandGenerationLog.getDetails().stream().filter(demandGenerationLogDetail -> !LICENSE_NOT_ACTIVE.equals(demandGenerationLogDetail.getDetail())).forEach(demandGenerationLogDetail -> demandLogLicenses.add(demandGenerationLogDetail.getLicense()));
        licenses.stream().forEach(license -> {
            if (!demandLogLicenses.contains(license))
                demandMissingLicenses.add(license);
        });
        return generateDemand(demandGenerationLog, installmentYear, demandMissingLicenses);

    }

}