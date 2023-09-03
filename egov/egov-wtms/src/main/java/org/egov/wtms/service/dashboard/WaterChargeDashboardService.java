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

package org.egov.wtms.service.dashboard;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.web.utils.WebUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.bean.dashboard.TaxPayerResponseDetails;
import org.egov.wtms.bean.dashboard.WaterChargeConnectionTypeResponse;
import org.egov.wtms.bean.dashboard.WaterChargeDashBoardRequest;
import org.egov.wtms.bean.dashboard.WaterChargeDashBoardResponse;
import org.egov.wtms.bean.dashboard.WaterTaxDefaulters;
import org.egov.wtms.service.es.WaterChargeCollectionDocService;
import org.egov.wtms.service.es.WaterChargeElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to provide APIs for CM Dashboard
 */

@Service
public class WaterChargeDashboardService {

    @Autowired
    private WaterChargeCollectionDocService waterChargeCollDocService;

    @Autowired
    private WaterChargeElasticSearchService waterChargeElasticSearchService;

    @Autowired
    private SimpleRestClient simpleRestClient;

    /**
     * Gives the total demand for Water Charges
     *
     * @param request
     * @return BigDecimal
     */
    public BigDecimal getWaterChargeTotalDemand(final HttpServletRequest request) {
        final String wtmsRestURL = format(PropertyTaxConstants.WTMS_TOTALDEMAND_RESTURL,
                WebUtils.extractRequestDomainURL(request, false));
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        return waterTaxInfo.get("currentDemand") == null ? BigDecimal.ZERO
                : new BigDecimal(Double.valueOf((Double) waterTaxInfo.get("currentDemand")));
    }

    /**
     * Gives the Collection Index details across all ULBs
     *
     * @param collectionDetailsRequest
     * @return CollectionIndexDetails
     */
    public Map<String, List<WaterChargeDashBoardResponse>> getCollectionIndexDetails(
            final WaterChargeDashBoardRequest waterChargeDashBoardRequest) {

        final Map<String, List<WaterChargeDashBoardResponse>> responsemap = new HashMap<>();
        final List<WaterChargeDashBoardResponse> collectionTotalResponseList = waterChargeCollDocService
                .getFullCollectionIndexDtls(waterChargeDashBoardRequest);

        final List<WaterChargeDashBoardResponse> collectionTrends = waterChargeCollDocService
                .getMonthwiseCollectionDetails(waterChargeDashBoardRequest);

        final List<WaterChargeDashBoardResponse> collIndexData; /*= waterChargeCollDocService
                .getResponseTableData(waterChargeDashBoardRequest);*/
        if (StringUtils.isNotBlank(waterChargeDashBoardRequest.getType()) && waterChargeDashBoardRequest.getType()
                .equalsIgnoreCase(PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE))
         collIndexData = waterChargeCollDocService
                .getResponseTableDataForBillCollector(waterChargeDashBoardRequest);
        else
            collIndexData = waterChargeCollDocService
            .getResponseTableData(waterChargeDashBoardRequest);
        
        responsemap.put("collectionWtTotal", collectionTotalResponseList);
        responsemap.put("collTrends", collectionTrends);
        responsemap.put("responseDetails", collIndexData);

        return responsemap;
    }

    public Map<String, List<WaterChargeConnectionTypeResponse>> getCollectionTypeIndexDetails(
            final WaterChargeDashBoardRequest waterChargeDashBoardRequest) {

        final Map<String, List<WaterChargeConnectionTypeResponse>> responsemap = new HashMap<>();
        final List<WaterChargeConnectionTypeResponse> collectionTotalResponseList = waterChargeCollDocService
                .getFullCollectionIndexDtlsForCOnnectionType(waterChargeDashBoardRequest);

        final List<WaterChargeConnectionTypeResponse> collectionTrends = waterChargeCollDocService
                .getMonthwiseCollectionDetailsForConnectionType(waterChargeDashBoardRequest);

        final List<WaterChargeConnectionTypeResponse> collIndexData = waterChargeCollDocService
                .getResponseDataForConnectionType(waterChargeDashBoardRequest);

       responsemap.put("collectionWtTotal", collectionTotalResponseList);
        responsemap.put("collTrends", collectionTrends);
       responsemap.put("responseDetails", collIndexData);

        return responsemap;
    }

    /**
     * Gives the receipts details across all ULBs
     *
     * @param collectionDetailsRequest
     * @return CollReceiptDetails
     */

    public Map<String, List<WaterChargeDashBoardResponse>> getReceiptDetails(
            final WaterChargeDashBoardRequest collectionDetailsRequest) {

        final Map<String, List<WaterChargeDashBoardResponse>> finalReceiptCountForwaterTaxMap = new HashMap<>();
        final List<WaterChargeDashBoardResponse> receiptDetailsList = waterChargeCollDocService
                .getTotalReceiptsCount(collectionDetailsRequest);
        final List<WaterChargeDashBoardResponse> receiptTrends = waterChargeCollDocService
                .getMonthwiseReceiptsTrend(collectionDetailsRequest);
        final List<WaterChargeDashBoardResponse> receiptTableData = waterChargeCollDocService
                .getReceiptTableData(collectionDetailsRequest);

        finalReceiptCountForwaterTaxMap.put("receiptDetailsCount", receiptDetailsList);
        finalReceiptCountForwaterTaxMap.put("receiptTrends", receiptTrends);
        finalReceiptCountForwaterTaxMap.put("receiptTableData", receiptTableData);
        return finalReceiptCountForwaterTaxMap;

    }

    /**
     * Returns Top Ten ULB's Tax Producers
     *
     * @param collectionDetailsRequest
     * @return
     */

    public TaxPayerResponseDetails getTopTenTaxProducers(final WaterChargeDashBoardRequest collectionDetailsRequest) {
        return waterChargeElasticSearchService.getTopTenTaxPerformers(collectionDetailsRequest);
    }

    /**
     * Returns Bottom Ten ULB's Tax Producers
     *
     * @param collectionDetailsRequest
     * @return
     */
    public TaxPayerResponseDetails getBottomTenTaxProducers(
            final WaterChargeDashBoardRequest waterChargeDashBoardRequest) {
        return waterChargeElasticSearchService.getBottomTenTaxPerformers(waterChargeDashBoardRequest);

    }

    public List<WaterTaxDefaulters> getTaxDefaulters(final WaterChargeDashBoardRequest waterChargeDashBoardRequest) {
        return waterChargeElasticSearchService.getTopDefaulters(waterChargeDashBoardRequest);
    }
}