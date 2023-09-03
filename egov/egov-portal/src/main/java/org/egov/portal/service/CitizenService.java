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
package org.egov.portal.service;

import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.token.service.TokenService;
import org.egov.portal.entity.Citizen;
import org.egov.portal.repository.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;
import static org.egov.infra.messaging.MessagePriority.HIGH;
import static org.egov.infra.utils.ApplicationConstant.CITIZEN_ROLE_NAME;
import static org.egov.infra.utils.ApplicationConstant.CITY_LOGIN_URL;

@Service
@Transactional(readOnly = true)
public class CitizenService {

    private static final String CITIZEN_REG_SERVICE = "Citizen Registration";

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public void create(Citizen citizen) {
        citizen.addRole(roleService.getRoleByName(CITIZEN_ROLE_NAME));
        citizen.updateNextPwdExpiryDate(applicationProperties.userPasswordExpiryInDays());
        citizen.setPassword(passwordEncoder.encode(citizen.getPassword()));
        citizen.setActive(true);
        citizenRepository.saveAndFlush(citizen);
        messagingService.sendSMS(citizen.getMobileNumber(), getMessage("citizen.reg.sms"));
        messagingService.sendEmail(citizen.getEmailId(), getMessage("citizen.reg.mail.subject"),
                getMessage("citizen.reg.mail.body", citizen.getName(),
                        format(CITY_LOGIN_URL, getDomainURL()), getMunicipalityName()));
    }

    @Transactional
    public void update(Citizen citizen) {
        citizenRepository.save(citizen);
    }

    public Citizen getCitizenByEmailId(String emailId) {
        return citizenRepository.findByEmailId(emailId);
    }

    public Citizen getCitizenByUserName(String userName) {
        return citizenRepository.findByUsername(userName);
    }

    @Transactional
    public boolean isValidOTP(String otp, String mobileNumber) {
        return tokenService.redeemToken(otp, mobileNumber, CITIZEN_REG_SERVICE);
    }

    @Transactional
    public boolean sendOTPMessage(String mobileNumber) {
        String otp = randomNumeric(5);
        tokenService.generate(otp, mobileNumber, CITIZEN_REG_SERVICE);
        messagingService.sendSMS(mobileNumber, getMessage("citizen.reg.otp.sms", otp), HIGH);
        return TRUE;
    }

    private String getMessage(String msgKey, Object... arg) {
        return messageSource.getMessage(msgKey, arg, Locale.getDefault());
    }
}