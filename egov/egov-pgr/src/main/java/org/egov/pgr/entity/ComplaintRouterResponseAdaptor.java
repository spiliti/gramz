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

package org.egov.pgr.entity;

import java.lang.reflect.Type;
import java.util.List;

import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class ComplaintRouterResponseAdaptor implements DataTableJsonAdapter<ComplaintRouter> {

    @Override
    public JsonElement serialize(final DataTable<ComplaintRouter> complaintRouterResponse, final Type type,
                                 final JsonSerializationContext jsc) {
        final List<ComplaintRouter> complaintRouterResult = complaintRouterResponse.getData();
        final JsonArray complaintRouterResultData = new JsonArray();
        complaintRouterResult.forEach(complaintRouter -> {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("boundaryType", complaintRouter.getBoundary() != null ? complaintRouter.getBoundary().getBoundaryType().getName() : "N/A");
            jsonObject.addProperty("boundary", complaintRouter.getBoundary() != null ? complaintRouter.getBoundary()
                    .getName() : "N/A");
            jsonObject.addProperty("complaintType", complaintRouter.getComplaintType() != null ? complaintRouter
                    .getComplaintType().getName() : "N/A");
            jsonObject.addProperty("position", complaintRouter.getPosition().getName());
            jsonObject.addProperty("routerId", complaintRouter.getId());

            complaintRouterResultData.add(jsonObject);
        });
        return enhance(complaintRouterResultData, complaintRouterResponse);
    }
}
