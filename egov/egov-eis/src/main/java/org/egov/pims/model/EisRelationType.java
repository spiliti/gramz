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
package org.egov.pims.model;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;

// Generated Aug 8, 2008 9:23:49 AM by Hibernate Tools 3.2.0.b9




/**
 * EgeisNomineeType generated by hbm2java
 */
@Unique(fields={"nomineeType"},id="id",tableName="EGEIS_RELATION_TYPE",columnName={"RELATION_TYPE"},message="Relation Type is unique")
public class EisRelationType implements java.io.Serializable
{

	private Long id;
	@Required(message="Relation Type should not be empty")
	private String nomineeType;

	private boolean fullBenefitEligible;

	private String gender;

	private Long eligibleAge;
	
	private String eligStatusIfMarried;
	
	private String eligStatusIfEmployed;
	
	private String narration;
	
	public EisRelationType() {}
	public String getEligStatusIfMarried() {
		return eligStatusIfMarried;
	}
	public void setEligStatusIfMarried(String eligStatusIfMarried) {
		this.eligStatusIfMarried = eligStatusIfMarried;
	}
	public String getEligStatusIfEmployed() {
		return eligStatusIfEmployed;
	}
	public void setEligStatusIfEmployed(String eligStatusIfEmployed) {
		this.eligStatusIfEmployed = eligStatusIfEmployed;
	}
	
	public EisRelationType(boolean fullBenefitElegible, String gender)
	{
		this.fullBenefitEligible = fullBenefitElegible;
		this.gender = gender;
	}

	

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getNomineeType()
	{
		return this.nomineeType;
	}

	public void setNomineeType(String nomineeType)
	{
		this.nomineeType = nomineeType;
	}

	public boolean isFullBenefitEligible()
	{
		return this.fullBenefitEligible;
	}

	public void setFullBenefitEligible(boolean fullBenefitElegible)
	{
		this.fullBenefitEligible = fullBenefitElegible;
	}

	public String getGender()
	{
		return this.gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public Long getEligibleAge() {
		return eligibleAge;
	}
	public void setEligibleAge(Long eligibleAge) {
		this.eligibleAge = eligibleAge;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	
}
