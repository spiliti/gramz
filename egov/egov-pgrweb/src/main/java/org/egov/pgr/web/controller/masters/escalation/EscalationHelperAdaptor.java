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

package org.egov.pgr.web.controller.masters.escalation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class EscalationHelperAdaptor implements JsonSerializer<EscalationHelper> {

    @Override
    public JsonElement serialize(EscalationHelper escalationHelper, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (escalationHelper != null) {
        //    jsonObject.addProperty("id", escalationHelper.getId());
            jsonObject.addProperty("positionFrom", null != escalationHelper.getFromPosition() ? escalationHelper
                    .getFromPosition().getName() : "NA");
            jsonObject.addProperty("objectSubType", null!= escalationHelper.getComplaintType()?escalationHelper.getComplaintType().getName():"");
         //   jsonObject.addProperty("objectType", escalationHelper.getObjectType().getDescription());
            jsonObject.addProperty("positionTo", null != escalationHelper.getToPosition() ? escalationHelper
                    .getToPosition().getName() : "");

        }
        return jsonObject;
    }

}
