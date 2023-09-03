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
package org.egov.eis.service;

import org.egov.eis.repository.DesignationRepository;
import org.egov.infra.admin.master.entity.Role;
import org.egov.pims.commons.Designation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Vaibhav.K
 */

@Service
@Transactional(readOnly = true)
public class DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    @Transactional
    public void createDesignation(final Designation designation) {
        designationRepository.save(designation);
    }

    @Transactional
    public void updateDesignation(final Designation designation) {
        designationRepository.save(designation);
    }

    @Transactional
    public void deleteDesignation(final Designation designation) {
        designationRepository.delete(designation);
    }

    public Designation getDesignationByName(final String desName) {
        return designationRepository.findByNameUpperCase(desName.toUpperCase());
    }

    public Designation getDesignationById(final Long desigId) {
        return designationRepository.findOne(desigId);
    }

    public List<Designation> getAllDesignations() {
        return designationRepository.findAll();
    }

    public List<Designation> getAllDesignationsSortByNameAsc() {
        return designationRepository.findAllByOrderByNameAsc();
    }

    public List<Designation> getAllDesignationsByNameLike(final String name) {
        return designationRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name);
    }

    public List<Designation> getAllDesignationByDepartment(final Long id, final Date givenDate) {
        return designationRepository.getAllDesignationsByDepartment(id, givenDate);
    }

    public Set<Role> getRolesByDesignation(final String designationName) {
        return designationRepository.getRolesByDesignation(designationName);
    }

    public List<Designation> getDesignationsByNames(final List<String> names) {
        return designationRepository.getDesignationsByNames(names);
    }

    public List<Designation> getDesignationsByName(final String name) {
        return designationRepository.getDesignationsByName("%" + name + "%");
    }
}
