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
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.egov.dcb.bean.ChequePayment;
import org.egov.ptis.bean.SurveyAssessmentDetails;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.restapi.model.AssessmentRequest;
import org.egov.restapi.util.JsonConvertor;
import org.egov.restapi.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class SurveyDetailsController {

	@Autowired
    private PropertyExternalService propertyExternalService;
    @Autowired
    private ValidationUtil validationUtil;
    
    /**
     * Provides assessment details
     * @param assessmentRequest
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/property/pulseAssessmentDetails", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String getAssessmentDetails(@RequestBody String assessmentRequest)
            throws IOException, ParseException {
    	String responseJson;
    	AssessmentRequest assessmentReq = (AssessmentRequest) getObjectFromJSONRequest(assessmentRequest, AssessmentRequest.class);
        ErrorDetails errorDetails = validationUtil.validateSurveyRequest(assessmentReq);
        if (errorDetails != null) {
			responseJson = JsonConvertor.convert(errorDetails);
        } else {
        	List<SurveyAssessmentDetails> assessmentDetails = propertyExternalService.getPropertyDetailsForSurvey(assessmentReq.getTransactionType(), assessmentReq.getFromDate(), assessmentReq.getToDate());
        	responseJson = JsonConvertor.convert(assessmentDetails);
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
