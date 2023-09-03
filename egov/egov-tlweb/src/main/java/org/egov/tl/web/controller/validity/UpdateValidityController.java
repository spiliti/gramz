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

package org.egov.tl.web.controller.validity;

import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.Validity;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.NatureOfBusinessService;
import org.egov.tl.service.ValidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/validity/update/{id}")
public class UpdateValidityController {

    @Autowired
    private ValidityService validityService;

    @Autowired
    private NatureOfBusinessService natureOfBusinessService;

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @ModelAttribute
    public List<NatureOfBusiness> natureOfBusinesses() {
        return natureOfBusinessService.getNatureOfBusinesses();
    }

    @ModelAttribute
    public List<LicenseCategory> licenseCategories() {
        return licenseCategoryService.getCategoriesOrderByName();
    }

    @ModelAttribute
    public Validity validity(@PathVariable Long id) {
        return validityService.findOne(id);
    }

    @RequestMapping(method = GET)
    public String showValidityUpdateForm() {
        return "validity-update";
    }

    @RequestMapping(method = POST)
    public String updateValidity(@Valid @ModelAttribute Validity validity, BindingResult bindingResult,
                                 RedirectAttributes responseAttribs) {
        if (!validity.hasValidValues())
            bindingResult.rejectValue("basedOnFinancialYear", "validity.value.notset");
        if (bindingResult.hasErrors()) {
            return "validity-update";
        }
        validityService.update(validity);
        responseAttribs.addFlashAttribute("message", "msg.validity.success");
        return "redirect:/validity/view/" + validity.getId();
    }
}
