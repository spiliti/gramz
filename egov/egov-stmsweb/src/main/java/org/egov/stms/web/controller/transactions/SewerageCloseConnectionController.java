/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.stms.web.controller.transactions;

import static org.egov.stms.utils.constants.SewerageTaxConstants.COMMON_ERROR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MESSAGE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WF_STATE_REJECTED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CLOSESEWERAGECONNECTION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.CHANGEINCLOSETS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULETYPE;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.service.SewerageApplicationTypeService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnection;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.web.controller.utils.SewerageApplicationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/transactions")
public class SewerageCloseConnectionController extends GenericWorkFlowController {

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private SewerageApplicationTypeService sewerageApplicationTypeService;
    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;
    @Autowired
    private SewerageConnectionService sewerageConnectionService;
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SewerageApplicationValidator sewerageApplicationValidator;

    @RequestMapping(value = "/closeConnection/{shscNumber}", method = RequestMethod.GET)
    public String view(@ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails, final Model model,
                       @PathVariable final String shscNumber, final HttpServletRequest request) {
        final SewerageConnection sewerageConnection = sewerageConnectionService.findByShscNumber(shscNumber);
        final SewerageApplicationDetails sewerageApplicationDetailsFromDB = sewerageApplicationDetailsService
                .findByShscNumberAndIsActive(shscNumber);
        sewerageApplicationDetails.setConnection(sewerageConnection);

        if (StringUtils.isNotBlank(shscNumber)) {
            final SewerageApplicationDetails applicationDetails = sewerageApplicationDetailsService
                    .isApplicationInProgress(shscNumber);
            if (applicationDetails != null && CLOSESEWERAGECONNECTION
                    .equalsIgnoreCase(applicationDetails.getApplicationType().getCode())) {
                model.addAttribute(MESSAGE, "msg.validate.closeconnection.application.inprogress");
                return COMMON_ERROR;
            } else if (applicationDetails != null
                    && CHANGEINCLOSETS.equalsIgnoreCase(applicationDetails.getApplicationType().getCode())) {
                model.addAttribute(MESSAGE, "msg.validate.changenoofclosets.application.inprogress");
                return COMMON_ERROR;
            }
        }

        if (sewerageApplicationDetailsFromDB != null) {
            BigDecimal taxDue = sewerageApplicationDetailsService.getPendingTaxAmount(sewerageApplicationDetailsFromDB);
            if (taxDue.compareTo(BigDecimal.ZERO) > 0) {
                model.addAttribute(MESSAGE, "msg.validate.demandamountdue");
                return COMMON_ERROR;
            }
        }
        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(shscNumber, request);
        if (propertyOwnerDetails != null)
            model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);

        final SewerageApplicationType applicationType = sewerageApplicationTypeService
                .findByCode(CLOSESEWERAGECONNECTION);
        sewerageApplicationDetails.setApplicationType(applicationType);
        sewerageApplicationDetails.setApplicationDate(new Date());
        sewerageApplicationDetails.setConnectionDetail(sewerageApplicationDetailsFromDB.getConnectionDetail());
        model.addAttribute("ptAssessmentNo", sewerageApplicationDetailsFromDB.getConnectionDetail()
                .getPropertyIdentifier());
        model.addAttribute("shscNumber", shscNumber);
        model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
        sewerageApplicationDetails.getWorkflowContainer().setAdditionalRule(sewerageApplicationDetails.getApplicationType().getCode());
        prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
        model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
        model.addAttribute("typeOfConnection", CLOSESEWERAGECONNECTION);
        return "closeSewerageConnection";
    }

    @RequestMapping(value = "/closeConnection/{shscNumber}", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final SewerageApplicationDetails sewerageApplicationDetails,
                         @PathVariable final String shscNumber,
                         final BindingResult resultBinder, final HttpServletRequest request, final Model model,
                         @RequestParam("files") final MultipartFile[] files) {

        final SewerageApplicationDetails sewerageApplicationDetailsFromDB = sewerageApplicationDetailsService
                .findByShscNumberAndIsActive(shscNumber);
        if (sewerageApplicationDetailsFromDB != null
                && StringUtils.isNotEmpty(sewerageApplicationDetailsFromDB.getConnectionDetail().toString()))
            sewerageApplicationDetails.setConnectionDetail(sewerageApplicationDetailsFromDB.getConnectionDetail());

        sewerageApplicationValidator.validateClosureApplication(sewerageApplicationDetails, resultBinder, request);

        if (resultBinder.hasErrors()) {
            sewerageApplicationDetails.setApplicationDate(new Date());
            prepareWorkflow(model, sewerageApplicationDetails, sewerageApplicationDetails.getWorkflowContainer());
            final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(shscNumber, request);
            if (propertyOwnerDetails != null)
                model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
            model.addAttribute("additionalRule", sewerageApplicationDetails.getApplicationType().getCode());
            model.addAttribute("approvalPosOnValidate", request.getParameter("approvalPosition"));
            model.addAttribute("stateType", sewerageApplicationDetails.getClass().getSimpleName());
            return "closeSewerageConnection";
        }

        sewerageApplicationDetails.setStatus(sewerageTaxUtils.getStatusByCodeAndModuleType(
                APPLICATION_STATUS_CREATED, MODULETYPE));

        Long approvalPosition = sewerageApplicationDetails.getWorkflowContainer().getApproverPositionId();
        final SewerageConnection sewerageConnection = sewerageConnectionService.findByShscNumber(shscNumber);
        sewerageConnection.setStatus(SewerageConnectionStatus.INPROGRESS);
        sewerageApplicationDetails.setConnection(sewerageConnection);
        sewerageConnection.addApplicantDetails(sewerageApplicationDetails);

        final SewerageApplicationDetails newSewerageApplicationDetails = sewerageApplicationDetailsService
                .createNewSewerageConnection(sewerageApplicationDetails, files, request);

        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());

        Assignment assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);
        List<Assignment> asignList = new ArrayList<>();
        if (assignObj != null) {
            asignList.add(assignObj);
        } else if (assignObj == null && approvalPosition > 0)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());

        final String nextDesign = !asignList.isEmpty() ? asignList.get(0).getDesignation().getName() : "";

        final String pathVars = newSewerageApplicationDetails.getApplicationNumber() + ","
                + sewerageTaxUtils.getApproverName(approvalPosition) + ","
                + (currentUserAssignment != null ? currentUserAssignment.getDesignation().getName() : "") + ","
                + (nextDesign != null ? nextDesign : "");

        return "redirect:/transactions/closeConnection-success?pathVars=" + pathVars;
    }

    @RequestMapping(value = "/closeConnection-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute SewerageApplicationDetails sewerageApplicationDetails,
                                    final HttpServletRequest request, final Model model, final ModelMap modelMap) {
        final String[] keyNameArray = request.getParameter("pathVars").split(",");
        String applicationNumber = "";
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1)
                applicationNumber = keyNameArray[0];
            else if (keyNameArray.length == 3) {
                applicationNumber = keyNameArray[0];
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                applicationNumber = keyNameArray[0];
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        if (applicationNumber != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);

        if (WF_STATE_REJECTED.equalsIgnoreCase(sewerageApplicationDetails.getCurrentState().getValue())) {
            Position initiatorPos = sewerageApplicationDetails.getCurrentState().getInitiatorPosition();
            List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(
                    initiatorPos.getId(), new Date());
            Optional<Assignment> assignment = assignmentList.stream().findAny();
            approverName = assignment.isPresent() ? assignment.get().getEmployee().getName() : "";
            nextDesign = initiatorPos.getDeptDesig().getDesignation().getName();
        }
        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);
        model.addAttribute("cityName", ApplicationThreadLocals.getCityName());
        return new ModelAndView("closeConnection-success", "sewerageApplicationDetails", sewerageApplicationDetails);
    }
}
