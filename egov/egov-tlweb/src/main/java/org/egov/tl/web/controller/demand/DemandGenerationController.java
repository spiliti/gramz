/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
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
package org.egov.tl.web.controller.demand;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.tl.entity.DemandGenerationLog;
import org.egov.tl.entity.DemandGenerationLogDetail;
import org.egov.tl.entity.License;
import org.egov.tl.entity.enums.ProcessStatus;
import org.egov.tl.service.DemandGenerationService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.web.response.adaptor.DemandGenerationResponseAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.tl.utils.Constants.DMD_GENERATION_DATA;
import static org.egov.tl.utils.Constants.DMD_GEN_INSTALLMENT;
import static org.egov.tl.utils.Constants.DMD_GEN_RETRY;
import static org.egov.tl.utils.Constants.MESSAGE;

@Controller
@RequestMapping("/demand")
public class DemandGenerationController {

    private static final String DEMAND_GENERATION_STATUS_MSG = "msg.demand.generation.%s";
    private static final String GENERATE_DEMAND_RESPONSE_URL = "redirect:/demand/generate";

    @Autowired
    private DemandGenerationService demandGenerationService;

    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    private TradeLicenseService tradeLicenseService;

    @ModelAttribute("financialYearList")
    public List<CFinancialYear> financialYearList() {
        return financialYearService.getAllFinancialYears();
    }

    @RequestMapping(value = "generate", method = RequestMethod.GET)
    public String newForm() {
        return "demand-generate";
    }

    @RequestMapping(value = "generate", method = RequestMethod.POST)
    public String generateDemand(@RequestParam String installmentYear, RedirectAttributes responseAttribs) {
        DemandGenerationLog bulkDemandGenerationLog = demandGenerationService.generateDemand(installmentYear);
        prepareReponseData(bulkDemandGenerationLog, responseAttribs);
        return GENERATE_DEMAND_RESPONSE_URL;
    }

    @RequestMapping(value = "regenerate", method = RequestMethod.POST)
    public String regenerateDemand(@RequestParam String installmentYear, RedirectAttributes responseAttribs) {
        DemandGenerationLog bulkDemandGenerationLog = demandGenerationService.retryFailedDemandGeneration(installmentYear);
        prepareReponseData(bulkDemandGenerationLog, responseAttribs);
        return GENERATE_DEMAND_RESPONSE_URL;
    }

    @RequestMapping(value = "generatemissing", method = RequestMethod.POST)
    public String generateMissingDemand(@RequestParam String installmentYear, RedirectAttributes responseAttribs) {
        DemandGenerationLog bulkDemandGenerationLog = demandGenerationService.generateMissingDemand(installmentYear);
        prepareReponseData(bulkDemandGenerationLog, responseAttribs);
        return GENERATE_DEMAND_RESPONSE_URL;
    }

    @RequestMapping(value = "licensedemandgenerate", method = RequestMethod.GET)
    public String generateDemandForLicense(@RequestParam Long licenseId, Model model) {
        model.addAttribute("licenseNumber", tradeLicenseService.getLicenseById(licenseId).getLicenseNumber());
        model.addAttribute("financialYear", demandGenerationService.getLatestFinancialYear().getFinYearRange());
        return "demandgenerate-result";
    }

    @RequestMapping(value = "licensedemandgenerate", method = RequestMethod.POST)
    public String generateDemandForLicense(@RequestParam String licenseNumber, RedirectAttributes redirectAttrs) {
        License license = tradeLicenseService.getLicenseByLicenseNumber(licenseNumber);
        boolean generationStatus = demandGenerationService.generateLicenseDemand(license);
        redirectAttrs.addFlashAttribute(MESSAGE, generationStatus ?
                "msg.demand.generation.completed" : "msg.demand.generation.incomplete");
        redirectAttrs.addAttribute("licenseId", license.getId());
        return "redirect:/demand/licensedemandgenerate";
    }

    private void prepareReponseData(DemandGenerationLog demandGenerationLog, RedirectAttributes responseAttribs) {
        responseAttribs.addFlashAttribute(DMD_GEN_INSTALLMENT, demandGenerationLog.getInstallmentYear());
        responseAttribs.addFlashAttribute(DMD_GEN_RETRY, demandGenerationLog.getDemandGenerationStatus().equals(ProcessStatus.INCOMPLETE));
        responseAttribs.addFlashAttribute(DMD_GENERATION_DATA, toJSON(demandGenerationLog.getDetails(),
                DemandGenerationLogDetail.class, DemandGenerationResponseAdaptor.class));
        responseAttribs.addFlashAttribute(MESSAGE, String.format(DEMAND_GENERATION_STATUS_MSG, demandGenerationLog.getDemandGenerationStatus()));
    }
}