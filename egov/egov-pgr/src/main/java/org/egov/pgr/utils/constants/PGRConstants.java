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

package org.egov.pgr.utils.constants;

public final class PGRConstants {
    public static final String MODULE_NAME = "PGR";
    public static final String EG_OBJECT_TYPE_COMPLAINT = "Complaint";
    public static final String GO_ROLE_NAME = "Grievance Officer";
    public static final String GRO_ROLE_NAME = "Grievance Routing Officer";
    public static final String PGR_INDEX_NAME = "complaint";
    public static final String PGR_INDEX_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DASHBOARD_GROUPING_WARDWISE = "ward";
    public static final String DASHBOARD_GROUPING_DEPARTMENTWISE = "department";
    public static final String DASHBOARD_GROUPING_REGION = "region";
    public static final String DASHBOARD_GROUPING_ULBGRADE = "ulbGrade";
    public static final String DASHBOARD_GROUPING_DISTRICT = "district";
    public static final String DASHBOARD_GROUPING_CITY = "ulb";
    public static final String DASHBOARD_GROUPING_ALL_ULB = "allulb";
    public static final String DASHBOARD_GROUPING_ALL_WARDS = "allwards";
    public static final String DASHBOARD_GROUPING_ALL_LOCALITIES = "alllocalities";
    public static final String DASHBOARD_GROUPING_ALL_FUNCTIONARY = "allfunctionary";
    public static final String DELIMITER_COLON = "::";
    public static final String NOASSIGNMENT = "NO ASSIGNMENT";

    public static final String DATE = "date";
    public static final String COMMENT = "comments";
    public static final String UPDATEDBY = "updatedBy";
    public static final String UPDATEDUSERTYPE = "updatedUserType";
    public static final String USERTYPE = "usertype";
    public static final String USER = "user";
    public static final String STATUS = "status";
    public static final String DEPT = "department";

    //Dashboard
    public static final String DISPOSALPERC = "disposalPerc";
    public static final String WARDNAME = "wardName";
    public static final String WARDID = "wardId";
    public static final String COUNT = "count";
    public static final String COLOR = "color";
    //Reports
    public static final String BYBOUNDARY = "ByBoundary";
    public static final String FROMDATE = "fromDates";
    public static final String TODATE = "toDates";
    public static final String USER_SELECT_QRY = "  SELECT   emp.name||'~'|| pos.name    as name, ";
    public static final String COMPLAINTTYPE_SELECT_QRY = " SELECT ctype.name as name, ";
    public static final String DEPT_SELECT_QRY = " SELECT dept.name as name, ";

    public static final String CITY_CODE = "cityCode";
    public static final String WARD_NUMBER = "wardNo";
    public static final String WARD_NAME = "wardName";
    public static final String CITY_NAME = "cityName";
    public static final String DISTRICT_NAME = "cityDistrictName";
    public static final String COMPLAINT_ALL = "ALL";
    public static final String COMPLAINT_PENDING = "PENDING";
    public static final String COMPLAINT_COMPLETED = "COMPLETED";
    public static final String COMPLAINT_REJECTED = "REJECTED";
    public static final String COMPLAINTS_FILED = "FILED";
    public static final String COMPLAINTS_RESOLVED = "RESOLVED";
    public static final String COMPLAINTS_UNRESOLVED = "UNRESOLVED";
    public static final String COMPLAINT_REGISTERED = "REGISTERED";
    public static final String COMPLAINT_CLOSED = "CLOSED";
    public static final String COMPLAINT_WITHDRAWN = "WITHDRAWN";
    public static final String COMPLAINT_REOPENED = "REOPENED";

    public static final String[] PENDING_STATUS = {COMPLAINT_REGISTERED, "FORWARDED", "PROCESSING", "NOTCOMPLETED", COMPLAINT_REOPENED};
    public static final String[] COMPLETED_STATUS = {COMPLAINT_COMPLETED, COMPLAINT_WITHDRAWN, COMPLAINT_CLOSED};
    public static final String[] REJECTED_STATUS = {COMPLAINT_REJECTED};
    public static final String[] RESOLVED_STATUS = {COMPLAINT_COMPLETED, COMPLAINT_WITHDRAWN, COMPLAINT_CLOSED, COMPLAINT_REJECTED};
    public static final String DEFAULT_RECEIVING_MODE = "WEBSITE";
    public static final String SYSTEMUSER = "SYSTEM";
    public static final String ESCALATEDSTATUS = "ESCALATED";
    public static final String COMPLAINT_ATTRIB = "complaint";
    public static final String CITIZEN_RATING_ATTRIB = "citizenRating";
    public static final String APPROVAL_COMMENT_ATTRIB = "approvalComent";
    public static final String APPROVAL_POSITION_ATTRIB = "approvalPosition";
    public static final String LOCATION_ATTRIB = "location";

    private PGRConstants() {
        //Only invariables
    }
}