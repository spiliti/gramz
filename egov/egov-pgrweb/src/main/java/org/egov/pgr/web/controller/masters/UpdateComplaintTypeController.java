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

package org.egov.pgr.web.controller.masters;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/complainttype/update/{code}")
public class UpdateComplaintTypeController {
    private static final String COMPLAINTTYPE_UPDATE_SUCCESS = "/complaintType-success";

    private final DepartmentService departmentService;
    private final ComplaintTypeService complaintTypeService;
    private @Autowired ComplaintTypeCategoryService complaintTypeCategoryService;

    @Autowired
    public UpdateComplaintTypeController(final DepartmentService departmentService, final ComplaintTypeService complaintTypeService) {
        this.departmentService = departmentService;
        this.complaintTypeService = complaintTypeService;
    }

    @ModelAttribute("categories")
    public List<ComplaintTypeCategory> categories() {
        return complaintTypeCategoryService.findAll();
    }

    @ModelAttribute("departments")
    public List<Department> departments() {
        return departmentService.getAllDepartments();
    }

    @ModelAttribute
    public ComplaintType complaintTypeModel(@PathVariable final String code, final Model model) {
        model.addAttribute("mode", "update");
        return complaintTypeService.findByCode(code);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String complaintTypeFormForUpdate() {
        return "complaint-type";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateComplaintType(@Valid @ModelAttribute ComplaintType complaintType, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "complaint-type";
        complaintType = complaintTypeService.updateComplaintType(complaintType);
        redirectAttrs.addFlashAttribute("complaintType", complaintType);
        redirectAttrs.addFlashAttribute("message", "msg.comp.type.update.success");
        return "redirect:" + complaintType.getCode() + COMPLAINTTYPE_UPDATE_SUCCESS;
    }

    @RequestMapping(COMPLAINTTYPE_UPDATE_SUCCESS)
    public ModelAndView successView(@PathVariable final String code, @ModelAttribute final ComplaintType complaintType) {
        return new ModelAndView("complaintType/complaintType-success", "complaintType",
                complaintTypeService.findByCode(code));
    }
}
