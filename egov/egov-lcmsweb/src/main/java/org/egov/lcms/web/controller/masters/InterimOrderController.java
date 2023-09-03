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
package org.egov.lcms.web.controller.masters;

import java.util.List;

import javax.validation.Valid;

import org.egov.lcms.masters.entity.InterimOrder;
import org.egov.lcms.masters.service.InterimOrderService;
import org.egov.lcms.web.adaptor.InterimOrderJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/interimorder")
public class InterimOrderController {
    private final static String INTERIMORDER_NEW = "interimorder-new";
    private final static String INTERIMORDER_RESULT = "interimorder-result";
    private final static String INTERIMORDER_EDIT = "interimorder-edit";
    private final static String INTERIMORDER_VIEW = "interimorder-view";
    private final static String INTERIMORDER_SEARCH = "interimorder-search";
    @Autowired
    private InterimOrderService interimOrderService;
    @Autowired
    private MessageSource messageSource;

    private void prepareNewForm(final Model model) {
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("interimOrder", new InterimOrder());
        return INTERIMORDER_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final InterimOrder interimOrder, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return INTERIMORDER_NEW;
        }
        interimOrderService.create(interimOrder);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.interimOrder.success", null, null));
        return "redirect:/interimorder/result/" + interimOrder.getId();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final InterimOrder interimOrder = interimOrderService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("interimOrder", interimOrder);
        return INTERIMORDER_EDIT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final InterimOrder interimOrder, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return INTERIMORDER_EDIT;
        }
        interimOrderService.update(interimOrder);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.interimOrder.update", null, null));
        return "redirect:/interimorder/result/" + interimOrder.getId();
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final InterimOrder interimOrder = interimOrderService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("interimOrder", interimOrder);
        return INTERIMORDER_VIEW;
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model) {
        final InterimOrder interimOrder = interimOrderService.findOne(id);
        model.addAttribute("interimOrder", interimOrder);
        return INTERIMORDER_RESULT;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final InterimOrder interimOrder = new InterimOrder();
        prepareNewForm(model);
        model.addAttribute("interimOrder", interimOrder);
        return INTERIMORDER_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final InterimOrder interimOrder) {
        final List<InterimOrder> searchResultList = interimOrderService.search(interimOrder);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(InterimOrder.class, new InterimOrderJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}