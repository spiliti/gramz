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

package org.egov.works.web.adaptor;

import java.lang.reflect.Type;

import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.service.TrackMilestoneService;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.workorderestimate.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class ViewEstimatePhotographJsonAdaptor implements JsonSerializer<LineEstimateDetails> {
	
	@Autowired
	private WorkOrderEstimateService workOrderEstimateService;
	
	@Autowired
	private TrackMilestoneService trackMilestoneService; 
	
    @Override
    public JsonElement serialize(final LineEstimateDetails lineEstimateDetails, final Type type,
            final JsonSerializationContext jsc) {
    	
    	final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateByEstimateNumber(lineEstimateDetails.getEstimateNumber());
    	
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimateDetails != null) {
            if (lineEstimateDetails.getProjectCode() != null)
                jsonObject.addProperty("winCode", lineEstimateDetails.getProjectCode().getCode());
            else
                jsonObject.addProperty("winCode", "NA");
            if (lineEstimateDetails.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", lineEstimateDetails.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (lineEstimateDetails.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", lineEstimateDetails.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if(workOrderEstimate != null){
            	jsonObject.addProperty("workOrderNumber", workOrderEstimate.getWorkOrder().getWorkOrderNumber());
            	jsonObject.addProperty("contractorName", workOrderEstimate.getWorkOrder().getContractor().getName());
            	
            } else{
            	jsonObject.addProperty("workOrderNumber", "NA");
            	jsonObject.addProperty("contractorName", "NA");
            	jsonObject.addProperty("workCompletion", "NA");
            }
            if(workOrderEstimate != null){
            	final TrackMilestone trackMilestone = trackMilestoneService.getTrackMilestoneTotalPercentage(workOrderEstimate.getId());
            	if(trackMilestone != null)
            		jsonObject.addProperty("workCompletion", trackMilestone.getTotalPercentage());
            	else
            		jsonObject.addProperty("workCompletion", "NA");
            		
            }
        		
            	
            jsonObject.addProperty("lineEstimateDetailsId", lineEstimateDetails.getId());
        }
        return jsonObject;
    }
}