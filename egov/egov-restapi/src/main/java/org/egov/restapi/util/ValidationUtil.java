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

package org.egov.restapi.util;

import static org.egov.restapi.constants.RestApiConstants.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.model.OwnerInformation;
import org.egov.ptis.domain.model.PayPropertyTaxDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.restapi.model.AssessmentRequest;
import org.egov.restapi.model.AssessmentsDetails;
import org.egov.restapi.model.ConstructionTypeDetails;
import org.egov.restapi.model.CreatePropertyDetails;
import org.egov.restapi.model.PropertyAddressDetails;
import org.egov.restapi.model.PropertyTransferDetails;
import org.egov.restapi.model.SurroundingBoundaryDetails;
import org.egov.restapi.model.VacantLandDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationUtil {
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    
    @Autowired
    private PropertyExternalService propertyExternalService;
    
    /**
     * Validates Property Transfer request
     * @param propertyTransferDetails
     * @return
     */
    public static ErrorDetails validatePropertyTransferRequest(PropertyTransferDetails propertyTransferDetails){
    	ErrorDetails errorDetails = null;
    	
    	String assessmentNumber = propertyTransferDetails.getAssessmentNo();
    	if(StringUtils.isBlank(assessmentNumber)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(ASSESSMENT_NO_REQ_CODE);
            errorDetails.setErrorMessage(ASSESSMENT_NO_REQ_MSG);
            return errorDetails;
    	}
    	
    	String mutationReasonCode = propertyTransferDetails.getMutationReasonCode();
    	if(StringUtils.isBlank(mutationReasonCode)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(MUTATION_REASON_CODE_REQ_CODE);
            errorDetails.setErrorMessage(MUTATION_REASON_CODE_REQ_MSG);
            return errorDetails;
    	}
    	
    	if(StringUtils.isNotBlank(mutationReasonCode) 
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_GIFT)
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_WILL)
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_RELINQUISH)
    			&& !mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_PARTITION)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(MUTATION_REASON_INVALID_CODE_REQ_CODE);
            errorDetails.setErrorMessage(MUTATION_REASON_INVALID_CODE_REQ_MSG);
            return errorDetails;
    	}
    	
    	if(mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)){
    		if(StringUtils.isBlank(propertyTransferDetails.getSaleDetails())){
    			errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(SALE_DETAILS_REQ_CODE);
                errorDetails.setErrorMessage(SALE_DETAILS_REQ_MSG);
                return errorDetails;
    		}
    	}
    	
    	if(!mutationReasonCode.equalsIgnoreCase(PropertyTaxConstants.MUTATION_REASON_CODE_SALE)){
    		if(StringUtils.isNotBlank(propertyTransferDetails.getSaleDetails())){
    			errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(OTHER_MUTATION_CODES_SALE_DETAILS_VALIDATION_CODE);
                errorDetails.setErrorMessage(OTHER_MUTATION_CODES_SALE_DETAILS_VALIDATION_MSG);
                return errorDetails;
    		}
    	}
    	
    	String deedNo = propertyTransferDetails.getDeedNo();
    	if(StringUtils.isBlank(deedNo)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(DEED_NO_REQ_CODE);
            errorDetails.setErrorMessage(DEED_NO_REQ_MSG);
            return errorDetails;
    	}
    	
    	String deedDate = propertyTransferDetails.getDeedDate();
    	if(StringUtils.isBlank(deedDate)){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(DEED_DATE_REQ_CODE);
            errorDetails.setErrorMessage(DEED_DATE_REQ_MSG);
            return errorDetails;
    	}
    	
    	List<OwnerInformation> ownerDetailsList = propertyTransferDetails.getOwnerDetails();
        if (ownerDetailsList.isEmpty()) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(OWNER_DETAILS_REQ_CODE);
            errorDetails.setErrorMessage(OWNER_DETAILS_REQ_MSG);
            return errorDetails;
        } else
            for (final OwnerInformation ownerInfo : ownerDetailsList) {
                if (ownerInfo.getMobileNumber() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(MOBILE_NO_REQ_CODE);
                    errorDetails.setErrorMessage(MOBILE_NO_REQ_MSG);
                    return errorDetails;
                }
                if (ownerInfo.getName() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(OWNER_NAME_REQ_CODE);
                    errorDetails.setErrorMessage(OWNER_NAME_REQ_MSG);
                    return errorDetails;
                }
                if (ownerInfo.getGender() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(GENDER_REQ_CODE);
                    errorDetails.setErrorMessage(GENDER_REQ_MSG);
                    return errorDetails;
                }
                if (ownerInfo.getGuardianRelation() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(GUARDIAN_RELATION_REQ_CODE);
                    errorDetails.setErrorMessage(GUARDIAN_RELATION_REQ_MSG);
                    return errorDetails;
                }
                if (ownerInfo.getGuardian() == null) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(GUARDIAN_REQ_CODE);
                    errorDetails.setErrorMessage(GUARDIAN_REQ_MSG);
                    return errorDetails;
                }
            }
    	
    	return errorDetails;
    }
    
    public ErrorDetails validateCreateRequest(final CreatePropertyDetails createPropDetails, final String mode) throws ParseException {
        ErrorDetails errorDetails = null;
        final String propertyTypeMasterCode = createPropDetails.getPropertyTypeMasterCode();
        if (StringUtils.isBlank(propertyTypeMasterCode)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(OWNERSHIP_CATEGORY_TYPE_REQ_CODE);
            errorDetails.setErrorMessage(OWNERSHIP_CATEGORY_TYPE_REQ_MSG);
            return errorDetails;
        } else if (propertyTypeMasterCode != null
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_335)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50)
                && !propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_75)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(OWNERSHIP_CATEGORY_TYPE_INVALID_CODE);
            errorDetails.setErrorMessage(OWNERSHIP_CATEGORY_TYPE_INVALID_MSG);
            return errorDetails;
        }
        final String propertyCategoryCode = createPropDetails.getCategoryCode();
        if (StringUtils.isBlank(propertyCategoryCode)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PROPERTY_CATEGORY_TYPE_REQ_CODE);
            errorDetails.setErrorMessage(PROPERTY_CATEGORY_TYPE_REQ_MSG);
            return errorDetails;
        } else if (propertyCategoryCode != null
        		&& !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_VACANT_LAND)
                && !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_STATE_GOVT)
                && !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_CENTRAL_GOVT)
                && !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_RESIDENTIAL)
                && !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL)
                && !propertyCategoryCode.equalsIgnoreCase(PropertyTaxConstants.CATEGORY_MIXED)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PROPERTY_CATEGORY_TYPE_INVALID_CODE);
            errorDetails.setErrorMessage(PROPERTY_CATEGORY_TYPE_INVALID_MSG);
            return errorDetails;
        }
        Double areaOfPlot = 0.0;
	    if(mode.equals(PropertyTaxConstants.PROPERTY_MODE_CREATE)){
	        //Owner details validations
	        final List<OwnerInformation> ownerDetailsList = createPropDetails.getOwnerDetails();
	        errorDetails = validateOwnerDetails(errorDetails, ownerDetailsList);
	        if(errorDetails != null)
	        	return errorDetails;
	    }
        
        //Assessment level validations
        final AssessmentsDetails assessmentsDetails = createPropDetails.getAssessmentDetails();
        if (assessmentsDetails == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(ASSESSMENT_DETAILS_REQ_CODE);
            errorDetails.setErrorMessage(ASSESSMENT_DETAILS_REQ_MSG);
            return errorDetails;
        } else {
            if (StringUtils.isBlank(assessmentsDetails.getMutationReasonCode())) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(REASON_FOR_CREATION_REQ_CODE);
                errorDetails.setErrorMessage(REASON_FOR_CREATION_REQ_MSG);
                return errorDetails;
            }
            if(!propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)){
            	if (StringUtils.isBlank(assessmentsDetails.getExtentOfSite())) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(EXTENT_OF_SITE_REQ_CODE);
                    errorDetails.setErrorMessage(EXTENT_OF_SITE_REQ_MSG);
                    return errorDetails;
                } else if(Double.valueOf(assessmentsDetails.getExtentOfSite()) == 0){
                	errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(AREA_GREATER_THAN_ZERO_CODE);
                    errorDetails.setErrorMessage(AREA_GREATER_THAN_ZERO_MSG);
                    return errorDetails;
                }
                areaOfPlot = Double.valueOf(assessmentsDetails.getExtentOfSite());
                if (assessmentsDetails.getIsExtentAppurtenantLand()) {
                	if (StringUtils.isBlank(assessmentsDetails.getExtentAppartenauntLand())) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(EXTENT_OF_SITE_REQ_CODE);
                        errorDetails.setErrorMessage(EXTENT_OF_SITE_REQ_MSG);
                        return errorDetails;
                    } else if(Double.valueOf(assessmentsDetails.getExtentAppartenauntLand()) == 0){
                    	errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(AREA_GREATER_THAN_ZERO_CODE);
                        errorDetails.setErrorMessage(AREA_GREATER_THAN_ZERO_MSG);
                        return errorDetails;
                    }
                }
            } 
            if (StringUtils.isBlank(assessmentsDetails.getRegdDocNo())) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(REG_DOC_NO_REQ_CODE);
                errorDetails.setErrorMessage(REG_DOC_NO_REQ_MSG);
                return errorDetails;
            } else if (StringUtils.isBlank(assessmentsDetails.getRegdDocDate())) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(REG_DOC_DATE_REQ_CODE);
                errorDetails.setErrorMessage(REG_DOC_DATE_REQ_MSG);
                return errorDetails;
            }
            if(propertyExternalService.convertStringToDate(assessmentsDetails.getRegdDocDate()).after(new Date())){
            	errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(FUTURE_DATES_NOT_ALLOWED_CODE);
                errorDetails.setErrorMessage(FUTURE_DATES_NOT_ALLOWED_MSG);
                return errorDetails;
            }
            
        	//Vacant Land validations
            if(propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)){
            	errorDetails = validateVacantLandDetails(createPropDetails, errorDetails);
            	if(errorDetails != null)
            	return errorDetails;
            }
            
        }

    	//Property Address validations
        PropertyAddressDetails propertyAddressDetails = createPropDetails.getPropertyAddressDetails();
        if(propertyAddressDetails == null){
        	errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(ADDRESS_DETAILS_REQ_CODE);
            errorDetails.setErrorMessage(ADDRESS_DETAILS_REQ_MSG);
            return errorDetails;
        } else {
        	if(StringUtils.isBlank(propertyAddressDetails.getLocalityNum())){
        		errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(LOCALITY_REQ_CODE);
                errorDetails.setErrorMessage(LOCALITY_REQ_MSG);
                return errorDetails;
        	}
        	if(StringUtils.isBlank(propertyAddressDetails.getZoneNum())){
        		errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(ZONE_NO_REQ_CODE);
                errorDetails.setErrorMessage(ZONE_NO_REQ_MSG);
                return errorDetails;
        	}
        	if(StringUtils.isBlank(propertyAddressDetails.getWardNum())){
        		errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(WARD_NO_REQ_CODE);
                errorDetails.setErrorMessage(WARD_NO_REQ_MSG);
                return errorDetails;
        	}
        	if(StringUtils.isBlank(propertyAddressDetails.getBlockNum())){
        		errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(BLOCK_NO_REQ_CODE);
                errorDetails.setErrorMessage(BLOCK_NO_REQ_MSG);
                return errorDetails;
        	}
        	if(StringUtils.isBlank(propertyAddressDetails.getElectionWardNum())){
        		errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(ELECTION_WARD_REQ_CODE);
                errorDetails.setErrorMessage(ELECTION_WARD_REQ_MSG);
                return errorDetails;
        	}
        	if(StringUtils.isBlank(propertyAddressDetails.getPinCode())){
        		errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PIN_CODE_REQ_CODE);
                errorDetails.setErrorMessage(PIN_CODE_REQ_MSG);
                return errorDetails;
        	}
        	if(!propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)){
	        	if(StringUtils.isBlank(propertyAddressDetails.getDoorNo())){
	        		errorDetails = new ErrorDetails();
	                errorDetails.setErrorCode(DOOR_NO_REQ_CODE);
	                errorDetails.setErrorMessage(DOOR_NO_REQ_MSG);
	                return errorDetails;
	        	}
        	}
        }
        if(!propertyTypeMasterCode.equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)){
        	ConstructionTypeDetails constructionTypeDetails = createPropDetails.getConstructionTypeDetails();
        	if(constructionTypeDetails == null){
        		errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(CONSTRUCTION_DETAILS_REQ_CODE);
                errorDetails.setErrorMessage(CONSTRUCTION_DETAILS_REQ_MSG);
                return errorDetails;
        	} else {
        		if(StringUtils.isBlank(constructionTypeDetails.getFloorTypeId())){
        			errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(FLOOR_TYPE_REQ_CODE);
                    errorDetails.setErrorMessage(FLOOR_TYPE_REQ_MSG);
                    return errorDetails;
        		}
        		if(StringUtils.isBlank(constructionTypeDetails.getRoofTypeId())){
        			errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(ROOF_TYPE_REQ_CODE);
                    errorDetails.setErrorMessage(ROOF_TYPE_REQ_MSG);
                    return errorDetails;
        		}
        	}
        	//Floor level validations
        	final List<FloorDetails> floorDetailsList = createPropDetails.getFloorDetails();
            if (floorDetailsList == null || floorDetailsList.isEmpty()) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(FLOOR_DETAILS_REQ_CODE);
                errorDetails.setErrorMessage(FLOOR_DETAILS_REQ_MSG);
                return errorDetails;
            } else
                for (final FloorDetails floorDetails : floorDetailsList) {
                    if (StringUtils.isBlank(floorDetails.getFloorNoCode())) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(FLOOR_NO_REQ_CODE);
                        errorDetails.setErrorMessage(FLOOR_NO_REQ_MSG);
                        return errorDetails;
                    }
                    if (StringUtils.isBlank(floorDetails.getBuildClassificationCode())) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(CLASSIFICATION_OF_BUILDING_REQ_CODE);
                        errorDetails.setErrorMessage(CLASSIFICATION_OF_BUILDING_REQ_MSG);
                        return errorDetails;
                    }
                    if (StringUtils.isBlank(floorDetails.getNatureOfUsageCode())) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(NATURE_OF_USAGES_REQ_CODE);
                        errorDetails.setErrorMessage(NATURE_OF_USAGES_REQ_MSG);
                        return errorDetails;
                    }
                    if(!floorDetails.getNatureOfUsageCode().equalsIgnoreCase(PropertyTaxConstants.PROPTYPE_RESD)){
                    	if (StringUtils.isBlank(floorDetails.getFirmName())) {
                            errorDetails = new ErrorDetails();
                            errorDetails.setErrorCode(FIRMNAME_REQ_CODE);
                            errorDetails.setErrorMessage(FIRMNAME_REQ_MSG);
                            return errorDetails;
                        }
                    }
                    if (StringUtils.isBlank(floorDetails.getOccupancyCode())) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(OCCUPANCY_REQ_CODE);
                        errorDetails.setErrorMessage(OCCUPANCY_REQ_MSG);
                        return errorDetails;
                    }
                    if (StringUtils.isBlank(floorDetails.getConstructionDate())) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(CONSTRUCTION_DATE_REQ_CODE);
                        errorDetails.setErrorMessage(CONSTRUCTION_DATE_REQ_MSG);
                        return errorDetails;
                    }
                    if (StringUtils.isBlank(floorDetails.getOccupancyDate())) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(OCCUPANCY_DATE_REQ_CODE);
                        errorDetails.setErrorMessage(OCCUPANCY_DATE_REQ_MSG);
                        return errorDetails;
                    }
                    
                    Date constructionDate = propertyExternalService.convertStringToDate(floorDetails.getConstructionDate());
                    Date occupancyDate = propertyExternalService.convertStringToDate(floorDetails.getOccupancyDate());
                    if(constructionDate.after(new Date()) || occupancyDate.after(new Date())){
                    	errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(FUTURE_DATES_NOT_ALLOWED_CODE);
                        errorDetails.setErrorMessage(FUTURE_DATES_NOT_ALLOWED_MSG);
                        return errorDetails;
                    }
                    if(occupancyDate.before(constructionDate)){
                    	errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(OCCUPANCY_DATE_BEFORE_CONSTRUCTION_DATE_CODE);
                        errorDetails.setErrorMessage(OCCUPANCY_DATE_BEFORE_CONSTRUCTION_DATE_MSG);
                        return errorDetails;
                    }
                    if(!floorDetails.getUnstructuredLand()){
                    	if (floorDetails.getPlinthLength() == null) {
                            errorDetails = new ErrorDetails();
                            errorDetails.setErrorCode(PLINTH_LENGTH_REQ_CODE);
                            errorDetails.setErrorMessage(PLINTH_LENGTH_REQ_MSG);
                            return errorDetails;
                        }
                        if(Float.valueOf(floorDetails.getPlinthLength()) == 0.0){
                        	errorDetails = new ErrorDetails();
                            errorDetails.setErrorCode(PLINTH_LENGTH_GREATER_THAN_ZERO_CODE);
                            errorDetails.setErrorMessage(PLINTH_LENGTH_GREATER_THAN_ZERO_MSG);
                            return errorDetails;
                        }
                        if (floorDetails.getPlinthBreadth() == null) {
                            errorDetails = new ErrorDetails();
                            errorDetails.setErrorCode(PLINTH_BREADTH_REQ_CODE);
                            errorDetails.setErrorMessage(PLINTH_BREADTH_REQ_MSG);
                            return errorDetails;
                        }
                        if(Float.valueOf(floorDetails.getPlinthBreadth()) == 0.0){
                        	errorDetails = new ErrorDetails();
                            errorDetails.setErrorCode(PLINTH_AREA_GREATER_THAN_ZERO_CODE);
                            errorDetails.setErrorMessage(PLINTH_AREA_GREATER_THAN_ZERO_MSG);
                            return errorDetails;
                        }
                    }
                    if (floorDetails.getPlinthArea() == null) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(PLINTH_AREA_REQ_CODE);
                        errorDetails.setErrorMessage(PLINTH_AREA_REQ_MSG);
                        return errorDetails;
                    }
                    if(Double.valueOf(floorDetails.getPlinthArea()) == 0.0){
                    	errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(PLINTH_AREA_GREATER_THAN_ZERO_CODE);
                        errorDetails.setErrorMessage(PLINTH_AREA_GREATER_THAN_ZERO_MSG);
                        return errorDetails;
                    }
                    if(Double.valueOf(floorDetails.getPlinthArea()) > areaOfPlot){
                    	errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(PLINTH_AREA_GREATER_THAN_PLOT_AREA_CODE);
                        errorDetails.setErrorMessage(PLINTH_AREA_GREATER_THAN_PLOT_AREA_MSG);
                        return errorDetails;
                    }
                }
        }
        
        return errorDetails;
    }

    /**
     * Validates Vacant Land details
     * @param createPropDetails
     * @param errorDetails
     * @return ErrorDetails
     */
	public ErrorDetails validateVacantLandDetails(final CreatePropertyDetails createPropDetails,
			ErrorDetails errorDetails) {
		final VacantLandDetails vacantLandDetails = createPropDetails.getVacantLandDetails();
		if (vacantLandDetails == null) {
		    errorDetails = new ErrorDetails();
		    errorDetails.setErrorCode(VACANT_LAND_DETAILS_REQ_CODE);
		    errorDetails.setErrorMessage(VACANT_LAND_DETAILS_REQ_MSG);
		    return errorDetails;
		} else {
		    if (StringUtils.isBlank(vacantLandDetails.getSurveyNumber())) {
		        errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(SURVEY_NO_REQ_CODE);
		        errorDetails.setErrorMessage(SURVEY_NO_REQ_MSG);
		        return errorDetails;
		    } else if (StringUtils.isBlank(vacantLandDetails.getPattaNumber())) {
		        errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(PATTA_NO_REQ_CODE);
		        errorDetails.setErrorMessage(PATTA_NO_REQ_MSG);
		        return errorDetails;
		    } else if (vacantLandDetails.getVacantLandArea() == null) {
		        errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(VACANT_LAND_AREA_REQ_CODE);
		        errorDetails.setErrorMessage(VACANT_LAND_AREA_REQ_MSG);
		        return errorDetails;
		    } else if(Float.valueOf(vacantLandDetails.getVacantLandArea()) == 0.0){
		    	errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(AREA_GREATER_THAN_ZERO_CODE);
		        errorDetails.setErrorMessage(AREA_GREATER_THAN_ZERO_MSG);
		        return errorDetails;
		    } else if (vacantLandDetails.getMarketValue() == null) {
		        errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(MARKET_AREA_VALUE_REQ_CODE);
		        errorDetails.setErrorMessage(MARKET_AREA_VALUE_REQ_MSG);
		        return errorDetails;
		    } else if(Double.valueOf(vacantLandDetails.getMarketValue()) == 0){
		    	errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(MARKET_VALUE_GREATER_THAN_ZERO_CODE);
		        errorDetails.setErrorMessage(MARKET_VALUE_GREATER_THAN_ZERO_MSG);
		        return errorDetails;
		    } else if (vacantLandDetails.getCurrentCapitalValue() == null) {
		        errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(CURRENT_CAPITAL_VALUE_REQ_CODE);
		        errorDetails.setErrorMessage(CURRENT_CAPITAL_VALUE_REQ_MSG);
		        return errorDetails;
		    } else if(Double.valueOf(vacantLandDetails.getCurrentCapitalValue()) == 0.0){
		    	errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(CURRENT_CAPITAL_VALUE_GREATER_THAN_ZERO_CODE);
		        errorDetails.setErrorMessage(CURRENT_CAPITAL_VALUE_GREATER_THAN_ZERO_MSG);
		        return errorDetails;
		    } else if (StringUtils.isBlank(vacantLandDetails.getEffectiveDate())) {
		        errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(EFFECTIVE_DATE_REQ_CODE);
		        errorDetails.setErrorMessage(EFFECTIVE_DATE_REQ_MSG);
		        return errorDetails;
		    }

		    final SurroundingBoundaryDetails surBoundaryDetails = createPropDetails.getSurroundingBoundaryDetails();
		    if (surBoundaryDetails == null) {
		        errorDetails = new ErrorDetails();
		        errorDetails.setErrorCode(SURROUNDING_BOUNDARY_DETAILS_REQ_CODE);
		        errorDetails.setErrorMessage(SURROUNDING_BOUNDARY_DETAILS_REQ_MSG);
		        return errorDetails;
		    } else {
		        if (StringUtils.isBlank(surBoundaryDetails.getNorthBoundary())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(NORTH_BOUNDARY_REQ_CODE);
		            errorDetails.setErrorMessage(NORTH_BOUNDARY_REQ_MSG);
		            return errorDetails;
		        } else if (StringUtils.isBlank(surBoundaryDetails.getSouthBoundary())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(SOUTH_BOUNDARY_REQ_CODE);
		            errorDetails.setErrorMessage(SOUTH_BOUNDARY_REQ_MSG);
		            return errorDetails;
		        } else if (StringUtils.isBlank(surBoundaryDetails.getEastBoundary())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(EAST_BOUNDARY_REQ_CODE);
		            errorDetails.setErrorMessage(EAST_BOUNDARY_REQ_MSG);
		            return errorDetails;
		        } else if (StringUtils.isBlank(surBoundaryDetails.getWestBoundary())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(WEST_BOUNDARY_REQ_CODE);
		            errorDetails.setErrorMessage(WEST_BOUNDARY_REQ_MSG);
		            return errorDetails;
		        }
		    }
		}
		return errorDetails;
	}

	/**
	 * Validates owner details 
	 * @param errorDetails
	 * @param ownerDetailsList
	 * @return ErrorDetails
	 */
	public ErrorDetails validateOwnerDetails(ErrorDetails errorDetails, final List<OwnerInformation> ownerDetailsList) {
		if (ownerDetailsList == null) {
		    errorDetails = new ErrorDetails();
		    errorDetails.setErrorCode(OWNER_DETAILS_REQ_CODE);
		    errorDetails.setErrorMessage(OWNER_DETAILS_REQ_MSG);
		    return errorDetails;
		} else {
		    for (final OwnerInformation ownerDetails : ownerDetailsList) {
		        if (StringUtils.isBlank(ownerDetails.getMobileNumber())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(MOBILE_NO_REQ_CODE);
		            errorDetails.setErrorMessage(MOBILE_NO_REQ_MSG);
		            return errorDetails;
		        } else {
		        	if(ownerDetails.getMobileNumber().trim().length() != 10){
		        		errorDetails = new ErrorDetails();
		                errorDetails.setErrorCode(MOBILENO_MAX_LENGTH_ERROR_CODE);
		                errorDetails.setErrorMessage(MOBILENO_MAX_LENGTH_ERROR_MSG);
		                return errorDetails;
		        	}
		        	Pattern pattern = Pattern.compile("\\d{10}");
		            Matcher matcher = pattern.matcher(ownerDetails.getMobileNumber());
		            if(!matcher.matches()){
		            	errorDetails = new ErrorDetails();
		                errorDetails.setErrorCode(MOBILENO_ALPHANUMERIC_ERROR_CODE);
		                errorDetails.setErrorMessage(MOBILENO_ALPHANUMERIC_ERROR_MSG);
		                return errorDetails;
		            }
		        }
		        if (StringUtils.isBlank(ownerDetails.getName())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(OWNER_NAME_REQ_CODE);
		            errorDetails.setErrorMessage(OWNER_NAME_REQ_MSG);
		            return errorDetails;
		        } else if (StringUtils.isBlank(ownerDetails.getGender())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(GENDER_REQ_CODE);
		            errorDetails.setErrorMessage(GENDER_REQ_MSG);
		            return errorDetails;
		        } else if (StringUtils.isBlank(ownerDetails.getGuardianRelation())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(GUARDIAN_RELATION_REQ_CODE);
		            errorDetails.setErrorMessage(GUARDIAN_RELATION_REQ_MSG);
		            return errorDetails;
		        } else if (StringUtils.isBlank(ownerDetails.getGuardian())) {
		            errorDetails = new ErrorDetails();
		            errorDetails.setErrorCode(GUARDIAN_REQ_CODE);
		            errorDetails.setErrorMessage(GUARDIAN_REQ_MSG);
		            return errorDetails;
		        }
		    }
		}
		return errorDetails;
		
	}
    
    public  ErrorDetails validatePaymentDetails(final PayPropertyTaxDetails payPropTaxDetails, boolean isMutationFeePayment, String propertyType) {
        ErrorDetails errorDetails = null;
        if (payPropTaxDetails.getAssessmentNo() == null || payPropTaxDetails.getAssessmentNo().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED);
        } else {
            if (payPropTaxDetails.getAssessmentNo().trim().length() > 0 && payPropTaxDetails.getAssessmentNo().trim().length() < 10) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN);
            }
            if (!basicPropertyDAO.isAssessmentNoExist(payPropTaxDetails.getAssessmentNo())) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
            }
            BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(payPropTaxDetails.getAssessmentNo());
            if(basicProperty != null){
            	Property property = basicProperty.getProperty();
            	if(property != null && property.getIsExemptedFromTax()){
            		errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_EXEMPTED_PROPERTY);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_EXEMPTED_PROPERTY);
            	}
            }
        }
        
        if(isMutationFeePayment){
        	if(!propertyExternalService.validateMutationFee(payPropTaxDetails.getAssessmentNo(), payPropTaxDetails.getPaymentAmount())){
        		errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_EXCESS_MUTATION_FEE);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_EXCESS_MUTATION_FEE);
        	}
        	PropertyMutation propertyMutation = propertyExternalService.getLatestPropertyMutationByAssesmentNo(payPropTaxDetails.getAssessmentNo());
        	if(propertyMutation == null){
        		errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_MUTATION_INVALID);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_MUTATION_INVALID);
        	}
        }
        
        if (payPropTaxDetails.getTransactionId() == null || "".equals(payPropTaxDetails.getTransactionId()) ){
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_REQUIRED);
        }
        else if(payPropTaxDetails.getTransactionId()!=null || !"".equals(payPropTaxDetails.getTransactionId())){
			BillReceiptInfo billReceiptList = propertyExternalService
					.validateTransanctionIdPresent(payPropTaxDetails.getTransactionId(), propertyType);
        if(billReceiptList!=null)
        {
             errorDetails = new ErrorDetails();
             errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_TRANSANCTIONID_VALIDATE);
             errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_TRANSANCTIONID_VALIDATE);
       
        }
        }
        if (payPropTaxDetails.getPaymentMode() == null || payPropTaxDetails.getPaymentMode().trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_REQUIRED);
        } else if (!PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CASH.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
                && !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
                &&  !PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PAYMENT_MODE_INVALID);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PAYMENT_MODE_INVALID);
        }

        if(payPropTaxDetails.getPaymentMode() != null
        		&& (
                PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_CHEQUE.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim())
                || PropertyTaxConstants.THIRD_PARTY_PAYMENT_MODE_DD.equalsIgnoreCase(payPropTaxDetails.getPaymentMode().trim()))) 
          {
            if (payPropTaxDetails.getChqddNo() == null || payPropTaxDetails.getChqddNo().trim().length() == 0) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_NO_REQUIRED);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_NO_REQUIRED);
            }else

                if (payPropTaxDetails.getChqddDate() == null ) {
                    errorDetails = new ErrorDetails();
                    errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_CHQDD_DATE_REQUIRED);
                    errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_CHQDD_DATE_REQUIRED);
                }else

                    if (payPropTaxDetails.getBankName() == null || payPropTaxDetails.getBankName().trim().length() == 0) {
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BANKNAME_REQUIRED);
                        errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BANKNAME_REQUIRED);
                    }else

                        if (payPropTaxDetails.getBranchName() == null ) {
                            errorDetails = new ErrorDetails();
                            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_BRANCHNAME_REQUIRED);
                            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_BRANCHNAME_REQUIRED);
                        }

          }
        return errorDetails;
    }

    /**
     * Validates Assessment Details request
     * @param assessmentRequest
     * @return ErrorDetails
     */
    public ErrorDetails validateAssessmentDetailsRequest(AssessmentRequest assessmentRequest){
    	ErrorDetails errorDetails = null;
    	
    	if (!basicPropertyDAO.isAssessmentNoExist(assessmentRequest.getAssessmentNo())) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
        }
    	return errorDetails;
    }
    
    public ErrorDetails validateSurveyRequest(AssessmentRequest assessmentRequest) throws ParseException{
    	ErrorDetails errorDetails = null;
    	if(StringUtils.isBlank(assessmentRequest.getTransactionType())){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(TRANSACTION_TYPE_REQUIRED_CODE);
            errorDetails.setErrorMessage(TRANSACTION_TYPE_REQUIRED_MSG);
    	} else if (StringUtils.isBlank(assessmentRequest.getFromDate())){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(FROM_DATE_REQUIRED_CODE);
            errorDetails.setErrorMessage(FROM_DATE_REQUIRED_MSG);
    	} else if (StringUtils.isBlank(assessmentRequest.getToDate())){
    		errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(TO_DATE_REQUIRED_CODE);
            errorDetails.setErrorMessage(TO_DATE_REQUIRED_MSG);
    	}
    	if(StringUtils.isNotBlank(assessmentRequest.getTransactionType()) && StringUtils.isNotBlank(assessmentRequest.getFromDate()) 
    			&& StringUtils.isNotBlank(assessmentRequest.getToDate())){
    		Long propertiesCount = propertyExternalService.getPropertiesCount(assessmentRequest.getTransactionType(), assessmentRequest.getFromDate(), assessmentRequest.getToDate());
        	if(propertiesCount>100){
        		errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PROPERTIES_LIST_EXCEED_LIMIT_CODE);
                errorDetails.setErrorMessage(PROPERTIES_LIST_EXCEED_LIMIT_MSG);
        	}
    	}
    	return errorDetails;
    }
}
