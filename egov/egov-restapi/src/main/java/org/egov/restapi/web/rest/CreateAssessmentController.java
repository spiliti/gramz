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
package org.egov.restapi.web.rest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.dcb.bean.ChequePayment;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.model.NewPropertyDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.restapi.model.AmenitiesDetails;
import org.egov.restapi.model.AssessmentsDetails;
import org.egov.restapi.model.ConstructionTypeDetails;
import org.egov.restapi.model.CorrespondenceAddressDetails;
import org.egov.restapi.model.CreatePropertyDetails;
import org.egov.restapi.model.PropertyAddressDetails;
import org.egov.restapi.model.SurroundingBoundaryDetails;
import org.egov.restapi.model.VacantLandDetails;
import org.egov.restapi.util.JsonConvertor;
import org.egov.restapi.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.egov.ptis.constants.PropertyTaxConstants;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class CreateAssessmentController {

	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
    private PropertyExternalService propertyExternalService;
	
	/**
     * This method is used to create property.
     * 
     * @param createPropertyDetails - Property details request
     * @return
     * @throws IOException
     * @throws ParseException
     */
	@RequestMapping(value = "/property/createProperty", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public String createProperty(@RequestBody String createPropertyDetails)
			throws IOException, ParseException {
		String responseJson;
		ApplicationThreadLocals.setUserId(2L);
		CreatePropertyDetails createPropDetails = (CreatePropertyDetails) getObjectFromJSONRequest(
                createPropertyDetails, CreatePropertyDetails.class);
		
		ErrorDetails errorDetails = validationUtil.validateCreateRequest(createPropDetails, PropertyTaxConstants.PROPERTY_MODE_CREATE);
		if (errorDetails != null) {
			responseJson = JsonConvertor.convert(errorDetails);
        } else {
		
        AssessmentsDetails assessmentsDetails = createPropDetails.getAssessmentDetails();
        PropertyAddressDetails propAddressDetails = createPropDetails.getPropertyAddressDetails();
        Boolean isCorrAddrDiff = propAddressDetails.getIsCorrAddrDiff();
        CorrespondenceAddressDetails corrAddressDetails = propAddressDetails.getCorrAddressDetails();
        String corrAddr1 = StringUtils.EMPTY;
        String corrAddr2 = StringUtils.EMPTY;
        String corrPinCode = StringUtils.EMPTY;
        if(corrAddressDetails != null){
        	corrAddr1 = corrAddressDetails.getCorrAddr1();
            corrAddr2 = corrAddressDetails.getCorrAddr2();
            corrPinCode = corrAddressDetails.getCorrPinCode();
        }

        List<Document> documents = null;
        NewPropertyDetails newPropertyDetails;
        if(createPropDetails.getPropertyTypeMasterCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND)){
        	VacantLandDetails vacantLandDetails = createPropDetails.getVacantLandDetails();
            SurroundingBoundaryDetails surroundingBoundaryDetails = createPropDetails.getSurroundingBoundaryDetails();

            newPropertyDetails = propertyExternalService.createNewProperty(createPropDetails.getPropertyTypeMasterCode(),
            		createPropDetails.getCategoryCode(), null, createPropDetails.getApartmentCmplxCode(), createPropDetails.getOwnerDetails(), assessmentsDetails.getMutationReasonCode(), null,
            		false, null, assessmentsDetails.getRegdDocNo(), assessmentsDetails.getRegdDocDate(), propAddressDetails.getLocalityNum(), propAddressDetails.getBlockNum(),
            		propAddressDetails.getZoneNum(), propAddressDetails.getStreetNum(), propAddressDetails.getElectionWardNum(), propAddressDetails.getDoorNo(), propAddressDetails.getEnumerationBlockCode(),
            		propAddressDetails.getPinCode(), isCorrAddrDiff, corrAddr1, corrAddr2, corrPinCode, false, false, false, false,
            		false, false, false, null, null, null, null, Collections.emptyList(),
            		vacantLandDetails.getSurveyNumber(), vacantLandDetails.getPattaNumber(), vacantLandDetails.getVacantLandArea(), vacantLandDetails.getMarketValue(),
            		vacantLandDetails.getCurrentCapitalValue(), vacantLandDetails.getEffectiveDate(), surroundingBoundaryDetails.getNorthBoundary(), 
            		surroundingBoundaryDetails.getSouthBoundary(), surroundingBoundaryDetails.getEastBoundary(),
            		surroundingBoundaryDetails.getWestBoundary(), assessmentsDetails.getParentPropertyAssessmentNo(), documents);
        } else {
            AmenitiesDetails amenitiesDetails = createPropDetails.getAmenitiesDetails();
            ConstructionTypeDetails constructionTypeDetails = createPropDetails.getConstructionTypeDetails();
            List<FloorDetails> floorDetailsList = createPropDetails.getFloorDetails();
            
            newPropertyDetails = propertyExternalService.createNewProperty(createPropDetails.getPropertyTypeMasterCode(),
            		createPropDetails.getCategoryCode(), null, createPropDetails.getApartmentCmplxCode(), createPropDetails.getOwnerDetails(), 
            		assessmentsDetails.getMutationReasonCode(), assessmentsDetails.getExtentOfSite(), assessmentsDetails.getIsExtentAppurtenantLand(),
            		assessmentsDetails.getOccupancyCertificationNo(), assessmentsDetails.getRegdDocNo(),assessmentsDetails.getRegdDocDate(), 
            		propAddressDetails.getLocalityNum(), propAddressDetails.getBlockNum(), propAddressDetails.getZoneNum(), 
            		propAddressDetails.getStreetNum(), propAddressDetails.getElectionWardNum(), propAddressDetails.getDoorNo(), 
            		propAddressDetails.getEnumerationBlockCode(), propAddressDetails.getPinCode(), isCorrAddrDiff, corrAddr1, corrAddr2, corrPinCode,
            		amenitiesDetails.hasLift(), amenitiesDetails.hasToilet(), amenitiesDetails.hasWaterTap(), amenitiesDetails.hasElectricity(),
            		amenitiesDetails.hasAttachedBathroom(), amenitiesDetails.hasWaterHarvesting(), amenitiesDetails.hasCableConnection(), 
            		constructionTypeDetails.getFloorTypeId(), constructionTypeDetails.getRoofTypeId(), constructionTypeDetails.getWallTypeId(),
            		constructionTypeDetails.getWoodTypeId(), floorDetailsList, null, null, null, null,
            		null, floorDetailsList.get(0).getOccupancyDate(), null, null, null, null, assessmentsDetails.getParentPropertyAssessmentNo(), documents);
        }
        responseJson = JsonConvertor.convert(newPropertyDetails);
        }
        return responseJson;
	}
	
	/**
     * This method is used to get POJO object from JSON request.
     * 
     * @param jsonString - request JSON string
     * @return
     * @throws IOException
     */
    private Object getObjectFromJSONRequest(String jsonString, Class cls)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        mapper.setDateFormat(ChequePayment.CHEQUE_DATE_FORMAT);
        return mapper.readValue(jsonString, cls);
    }
    
}
