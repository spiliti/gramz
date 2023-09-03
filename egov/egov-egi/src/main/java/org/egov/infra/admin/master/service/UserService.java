/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
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

package org.egov.infra.admin.master.service;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MicroserviceUtils microserviceUtils;

    @Transactional
    public User updateUser(final User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public User createUser(final User user) {
        final User savedUser = userRepository.save(user);
        microserviceUtils.createUserMicroservice(user);
        return savedUser;
    }

    public Set<Role> getRolesByUsername(final String userName) {
        return userRepository.findUserRolesByUserName(userName);
    }

    public User getUserById(final Long id) {
        return userRepository.findOne(id);
    }

    public User getUserRefById(final Long id) {
        return userRepository.getOne(id);
    }

    public User getCurrentUser() {
        return userRepository.findOne(ApplicationThreadLocals.getUserId());
    }

    public User getUserByUsername(final String userName) {
        return userRepository.findByUsername(userName);
    }

    public User getUserByEmailId(final String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    public User getUserByAadhaarNumber(final String aadhaarNumber) {
        return userRepository.findByAadhaarNumber(aadhaarNumber);
    }

    public List<User> getUserByAadhaarNumberAndType(final String aadhaarNumber, final UserType type) {
        return userRepository.findByAadhaarNumberAndType(aadhaarNumber, type);
    }

    public Optional<User> checkUserWithIdentity(final String identity) {
        return Optional.ofNullable(getUserByUsername(identity));
    }

    public List<User> findAllByMatchingUserNameForType(final String username, final UserType type) {
        return userRepository.findByUsernameContainingIgnoreCaseAndTypeAndActiveTrue(username, type);
    }

    public Set<User> getUsersByRoleName(final String roleName) {
        return userRepository.findUsersByRoleName(roleName);
    }

    public List<User> getAllEmployeeUsers() {
        return userRepository.findByTypeAndActiveTrueOrderByNameAsc(UserType.EMPLOYEE);
    }

    public List<User> getUsersByUsernameAndRolename(final String userName, final String roleName) {
        return userRepository.findUsersByUserAndRoleName(userName, roleName);
    }

    public User getUserByNameAndMobileNumberForGender(final String name, final String mobileNumber, final Gender gender) {
        return userRepository.findByNameAndMobileNumberAndGender(name, mobileNumber, gender);
    }

}