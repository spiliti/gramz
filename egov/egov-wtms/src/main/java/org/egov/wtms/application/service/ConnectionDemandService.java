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
package org.egov.wtms.application.service;

import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PROPERTY_MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAXREASONCODE;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.DemandDetail;
import org.egov.wtms.application.entity.FieldInspectionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterDemandConnection;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.collection.ConnectionBillService;
import org.egov.wtms.application.service.collection.WaterConnectionBillable;
import org.egov.wtms.autonumber.BillReferenceNumberGenerator;
import org.egov.wtms.autonumber.MeterDemandNoticeNumberGenerator;
import org.egov.wtms.masters.entity.DonationDetails;
import org.egov.wtms.masters.entity.DonationHeader;
import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.entity.WaterRatesHeader;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.DonationDetailsService;
import org.egov.wtms.masters.service.DonationHeaderService;
import org.egov.wtms.masters.service.WaterRatesDetailsService;
import org.egov.wtms.masters.service.WaterRatesHeaderService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConnectionDemandService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private DonationDetailsService donationDetailsService;

    @Autowired
    private DonationHeaderService donationHeaderService;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InstallmentHibDao installmentDao;

    @Autowired
    private DemandGenericDao demandGenericDao;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterDemandConnectionService waterDemandConnectionService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private ConnectionBillService connectionBillService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    private WaterRatesDetailsService waterRatesDetailsService;

    @Autowired
    private WaterRatesHeaderService waterRatesHeaderService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public EgDemand createDemand(final WaterConnectionDetails waterConnectionDetails) {
        final Map<String, Object> feeDetails = new HashMap<>();
        DonationDetails donationDetails = null;
        final FieldInspectionDetails fieldInspectionDetails = waterConnectionDetails.getFieldInspectionDetails();
        EgDemand egDemand;
        if (fieldInspectionDetails != null) {
            feeDetails.put(WaterTaxConstants.WATERTAX_SECURITY_CHARGE, fieldInspectionDetails.getSecurityDeposit());
            feeDetails.put(WaterTaxConstants.WATERTAX_ROADCUTTING_CHARGE,
                    fieldInspectionDetails.getRoadCuttingCharges());
            feeDetails.put(WaterTaxConstants.WATERTAX_SUPERVISION_CHARGE,
                    fieldInspectionDetails.getSupervisionCharges());
            waterConnectionDetails.getFieldInspectionDetails().setEstimationCharges(fieldInspectionDetails.getSecurityDeposit()
                    + fieldInspectionDetails.getRoadCuttingCharges() + fieldInspectionDetails.getSupervisionCharges());
        }

        // (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getCode()))
        if (waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED) &&
                !WaterTaxConstants.CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            donationDetails = getDonationDetails(waterConnectionDetails);
        if (waterConnectionDetails.getConnectionType().equals(ConnectionType.METERED) &&
                waterConnectionDetails.getDonationCharges() > 0.0)
            feeDetails.put(WaterTaxConstants.WATERTAX_DONATION_CHARGE, waterConnectionDetails.getDonationCharges());

        if (donationDetails != null) {
            feeDetails.put(WaterTaxConstants.WATERTAX_DONATION_CHARGE, donationDetails.getAmount());
            waterConnectionDetails.setDonationCharges(donationDetails.getAmount());
        }

        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(WaterTaxConstants.EGMODULE_NAME), new Date(), WaterTaxConstants.YEARLY);
        // Not updating demand amount collected for new connection as per the
        // discussion.
        if (installment != null) {
            final Set<EgDemandDetails> dmdDetailSet = new HashSet<>();
            for (final String demandReason : feeDetails.keySet())
                dmdDetailSet.add(createDemandDetails((Double) feeDetails.get(demandReason), demandReason, installment));

            egDemand = new EgDemand();
            egDemand.setEgInstallmentMaster(installment);
            egDemand.getEgDemandDetails().addAll(dmdDetailSet);
            egDemand.setIsHistory("N");
            egDemand.setCreateDate(new Date());
            egDemand.setModifiedDate(new Date());
        } else
            throw new ValidationException("err.water.installment.not.found");
        return egDemand;
    }

    public DonationDetails getDonationDetails(final WaterConnectionDetails waterConnectionDetails) {
        DonationDetails donationDetails = null;
        final List<DonationHeader> donationHeaderTempList = donationHeaderService
                .findDonationDetailsByPropertyAndCategoryAndUsageandPipeSize(waterConnectionDetails.getPropertyType(),
                        waterConnectionDetails.getCategory(), waterConnectionDetails.getUsageType(),
                        waterConnectionDetails.getPipeSize().getSizeInInch(),
                        waterConnectionDetails.getPipeSize().getSizeInInch());
        for (final DonationHeader donationHeaderTemp : donationHeaderTempList) {
            donationDetails = donationDetailsService.findByDonationHeaderAndFromDateAndToDate(donationHeaderTemp,
                    new Date(), new Date());
            if (donationDetails != null)
                break;
        }
        return donationDetails;
    }

    private EgDemandDetails createDemandDetails(final Double amount, final String demandReason,
            final Installment installment) {
        final EgDemandReason demandReasonObj = getDemandReasonByCodeAndInstallment(demandReason, installment);
        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(BigDecimal.valueOf(amount));
        demandDetail.setAmtCollected(BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(demandReasonObj);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        return demandDetail;
    }

    private EgDemandDetails createDemandDetailsrForDataEntry(final BigDecimal amount, final BigDecimal collectAmount,
            final String demandReason, final String installment, final DemandDetail demandTempObj,
            final WaterConnectionDetails waterConnectionDetails) {
        Installment installObj;
        if (!waterConnectionDetails.getConnectionType().toString().equalsIgnoreCase(METERED))
            installObj = installmentDao
                    .getInsatllmentByModuleAndDescription(moduleService.getModuleByName(PROPERTY_MODULE_NAME), installment);
        else
            installObj = installmentDao
                    .getInsatllmentByModuleAndDescription(moduleService.getModuleByName(MODULE_NAME), installment);

        EgDemandDetails demandDetailBean;
        final EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository
                .findEgDemandDetailById(demandTempObj.getId());
        final EgDemandReason demandReasonObj = getDemandReasonByCodeAndInstallment(demandReason, installObj);
        if (demandDetailsObj != null && demandTempObj.getId() != null) {
            demandDetailBean = demandDetailsObj;
            if (demandDetailsObj.getAmount().compareTo(amount) != 0)
                demandDetailBean.setAmount(amount);
            if (demandDetailsObj.getAmtCollected().compareTo(collectAmount) != 0)
                demandDetailBean.setAmtCollected(collectAmount);
            demandDetailBean.setEgDemandReason(demandReasonObj);
            demandDetailBean.setModifiedDate(new Date());
        } else {
            demandDetailBean = new EgDemandDetails();
            demandDetailBean.setAmount(amount);
            demandDetailBean.setAmtCollected(collectAmount);
            demandDetailBean.setAmtRebate(BigDecimal.ZERO);
            demandDetailBean.setEgDemandReason(demandReasonObj);
            demandDetailBean.setCreateDate(new Date());
            demandDetailBean.setModifiedDate(new Date());

        }
        return demandDetailBean;
    }

    public EgDemandReason getDemandReasonByCodeAndInstallment(final String demandReason,
            final Installment installment) {
        final Query demandQuery = getCurrentSession().getNamedQuery("DEMANDREASONBY_CODE_AND_INSTALLMENTID");
        demandQuery.setParameter(0, demandReason);
        demandQuery.setParameter(1, installment.getId());
        final EgDemandReason demandReasonObj = (EgDemandReason) demandQuery.uniqueResult();
        return demandReasonObj;
    }

    public Map<String, Double> getSplitFee(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand demand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        final Map<String, Double> splitAmount = new HashMap<>();
        if (demand != null && demand.getEgDemandDetails() != null && demand.getEgDemandDetails().size() > 0)
            for (final EgDemandDetails detail : demand.getEgDemandDetails())
                if (WaterTaxConstants.WATERTAX_FIELDINSPECTION_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_FIELDINSPECTION_CHARGE,
                            detail.getAmount().doubleValue());
                else if (WaterTaxConstants.WATERTAX_DONATION_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_DONATION_CHARGE, detail.getAmount().doubleValue());
                else if (WaterTaxConstants.WATERTAX_SECURITY_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_SECURITY_CHARGE, detail.getAmount().doubleValue());
                else if (WaterTaxConstants.WATERTAX_ROADCUTTING_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_ROADCUTTING_CHARGE, detail.getAmount().doubleValue());
                else if (WaterTaxConstants.WATERTAX_SUPERVISION_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_SUPERVISION_CHARGE, detail.getAmount().doubleValue());
        return splitAmount;
    }

    public List<Object> getDmdCollAmtInstallmentWise(final EgDemand egDemand) {
        final StringBuilder queryStringBuilder = new StringBuilder();
        queryStringBuilder
                .append("select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, "
                        + "sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, "
                        + "eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id "
                        + "and dmdDet.id_demand =:dmdId and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master "
                        + "group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        return getCurrentSession().createSQLQuery(queryStringBuilder.toString()).setLong("dmdId", egDemand.getId())
                .list();
    }

    public List<Object> getDmdCollAmtInstallmentWiseUptoCurrentInstallmemt(final EgDemand egDemand,
            final WaterConnectionDetails waterConnectionDetails) {
        Installment currInstallment;
        if (waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED))
            currInstallment = getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null,
                    new Date());
        else
            currInstallment = getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.MONTHLY,
                    new Date());
        final StringBuffer strBuf = new StringBuffer(2000);
        strBuf.append(
                "select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, "
                        + "sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, "
                        + "eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id "
                        + "and dmdDet.id_demand =:dmdId and inst.start_date<=:currInstallmentDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master "
                        + "group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        final Query query = getCurrentSession().createSQLQuery(strBuf.toString())
                .setParameter("dmdId", egDemand.getId())
                .setParameter("currInstallmentDate", currInstallment.getToDate());
        return query.list();
    }

    public List<Object> getDmdCollAmtInstallmentWiseUptoCurrentFinYear(final EgDemand egDemand,
            final WaterConnectionDetails waterConnectionDetails) {
        final CFinancialYear financialyear = financialYearDAO.getFinancialYearByDate(new Date());

        final StringBuffer strBuf = new StringBuffer(2000);
        strBuf.append(
                "select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, "
                        + "sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, "
                        + "eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id "
                        + "and dmdDet.id_demand =:dmdId and inst.start_date<=:currFinEndDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master "
                        + "group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        final Query query = getCurrentSession().createSQLQuery(strBuf.toString())
                .setParameter("dmdId", egDemand.getId()).setParameter("currFinEndDate", financialyear.getEndingDate());
        return query.list();
    }

    @Transactional
    public String generateBill(final String consumerCode, final String applicationTypeCode) {
        String collectXML;
        final SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String currentInstallmentYear = null;
        final WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context
                .getBean("waterConnectionBillable");
        final WaterConnectionDetails waterConnectionDetails;
        final BillReferenceNumberGenerator billRefeNumber = beanResolver
                .getAutoNumberServiceFor(BillReferenceNumberGenerator.class);

        if (applicationTypeCode != null && (applicationTypeCode.equals(WaterTaxConstants.CHANGEOFUSE)
                || applicationTypeCode.equals(WaterTaxConstants.RECONNECTIONCONNECTION)))
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCodeAndStatus(consumerCode, ConnectionStatus.ACTIVE);
        else
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode);
        if (ConnectionStatus.INPROGRESS.equals(waterConnectionDetails.getConnectionStatus()))
            currentInstallmentYear = formatYear
                    .format(getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.YEARLY, new Date())
                            .getInstallmentYear());
        else if (ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = formatYear
                    .format(getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, new Date())
                            .getInstallmentYear());
        else if (ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && ConnectionType.METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = formatYear.format(
                    getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.MONTHLY, new Date())
                            .getInstallmentYear());
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(ApplicationThreadLocals.getUserId());

        waterConnectionBillable.setReferenceNumber(billRefeNumber.generateBillNumber(currentInstallmentYear));
        waterConnectionBillable.setBillType(getBillTypeByCode(WaterTaxConstants.BILLTYPE_AUTO));

        final String billXml = connectionBillService.getBillXML(waterConnectionBillable);
        try {
            collectXML = URLEncoder.encode(billXml, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new ApplicationRuntimeException(e.getMessage());
        }
        return collectXML;
    }

    public EgBillType getBillTypeByCode(final String typeCode) {
        return egBillDAO.getBillTypeByCode(typeCode);
    }

    public EgDemand getDemandByInstAndApplicationNumber(final Installment installment, final String consumerCode) {

        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsRepository
                .findByApplicationNumberAndInstallment(installment, consumerCode);

        return waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();

    }

    /**
     * @param waterConnectionDetails
     * @param billAmount
     * @param currentDate
     * @return Updates WaterConnectionDetails after Meter Entry Demand Calculettion and Update Previous Bill and Generates New
     * Bill
     */
    @Transactional
    public WaterConnectionDetails updateDemandForMeteredConnection(final WaterConnectionDetails waterConnectionDetails,
            final BigDecimal billAmount, final Date currentDate, final Date previousDate, final int noofmonths) {
        Installment installment;
        List<Installment> installmentList = new ArrayList<>();
        if (noofmonths > 1 && !waterConnectionDetails.getMeterConnection().get(0).isMeterDamaged())
            installmentList = installmentDao.getInstallmentsByModuleForGivenDateAndInstallmentType(
                    moduleService.getModuleByName(WaterTaxConstants.MODULE_NAME),
                    previousDate, WaterTaxConstants.MONTHLY);

        installment = getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME,
                WaterTaxConstants.MONTHLY, currentDate);

        if (installmentList.isEmpty() || !installmentList.contains(installment))
            installmentList.add(installment);

        final EgDemand demandObj = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        if (demandObj == null && waterConnectionDetails.getLegacy())
            throw new ApplicationRuntimeException("err.legacy.demand.not.present");
        if (!installmentList.isEmpty()) {
            for (final Installment installmentVal : installmentList)
                createMeteredDemandDetails(demandObj, waterConnectionDetails, billAmount, installmentVal);

            final List<EgBill> billlist = demandGenericDao.getAllBillsForDemand(demandObj, "N", "N");
            if (!billlist.isEmpty()) {
                final EgBill billObj = billlist.get(0);
                billObj.setIs_History("Y");
                billObj.setModifiedDate(new Date());
                egBillDAO.create(billObj);
            }
            generateBillForMeterAndMonthly(waterConnectionDetails.getConnection().getConsumerCode());
        }

        else
            throw new ValidationException("err.water.meteredinstallment.not.found");
        waterConnectionDetails.getWaterDemandConnection().get(0).setDemand(demandObj);
        return waterConnectionDetails;
    }

    public void createMeteredDemandDetails(final EgDemand demandObj, final WaterConnectionDetails waterConnectionDetails,
            final BigDecimal billAmount, final Installment installment) {
        Boolean isDemandDetailExist = false;
        for (final EgDemandDetails demandDetails : demandObj.getEgDemandDetails())
            if (demandDetails.getEgDemandReason().getEgInstallmentMaster().equals(installment)) {
                isDemandDetailExist = true;
                break;
            }
        if (!isDemandDetailExist) {
            final Set<EgDemandDetails> dmdDetailSet = new HashSet<>();
            dmdDetailSet.add(createDemandDetails(Double.parseDouble(billAmount.toString()),
                    WATERTAXREASONCODE, installment));
            demandObj.setBaseDemand(demandObj.getBaseDemand().add(billAmount));
            demandObj.setEgInstallmentMaster(installment);
            demandObj.getEgDemandDetails().addAll(dmdDetailSet);
            demandObj.setModifiedDate(new Date());
            final WaterDemandConnection waterDemandConnection = waterDemandConnectionService
                    .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, demandObj);
            if (demandObj.getId() != null && waterDemandConnection == null) {
                final WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
                waterdemandConnection.setDemand(demandObj);
                waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
                waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
                waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
            }

        }

    }

    /**
     * @param waterConnectionDetails
     * @param demandDeatilslist
     * @return creation or updating demand and demanddetails for data Entry Screen
     */
    @Transactional
    public WaterConnectionDetails updateDemandForNonMeteredConnectionDataEntry(
            final WaterConnectionDetails waterConnectionDetails, final String sourceChannel) {
        EgDemand demandObj;
        Installment installObj;
        final List<String> installmentList = new ArrayList<>();
        propertyTaxUtil.getInstallmentsForCurrYear(new Date()).get(CURRENTYEAR_SECOND_HALF);
        if (waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand() == null)
            demandObj = new EgDemand();
        else

            demandObj = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        final Set<EgDemandDetails> dmdDetailSet = new HashSet<>();
        for (final DemandDetail demanddetailBean : waterConnectionDetails.getDemandDetailBeanList())
            if (demanddetailBean.getActualAmount().compareTo(BigDecimal.ZERO) == 1
                    && demanddetailBean.getActualCollection().compareTo(BigDecimal.ZERO) >= 0
                    && demanddetailBean.getActualCollection().compareTo(demanddetailBean.getActualAmount()) < 1) {
                demandObj.setBaseDemand(getTotalAmountForBaseDemand(demanddetailBean, demandObj.getBaseDemand()));
                demandObj.setAmtCollected(
                        getTotalCollectedAmountForDemand(demanddetailBean, demandObj.getAmtCollected()));
                dmdDetailSet.add(createDemandDetailsrForDataEntry(demanddetailBean.getActualAmount(),
                        demanddetailBean.getActualCollection(), demanddetailBean.getReasonMaster(),
                        demanddetailBean.getInstallment(), demanddetailBean, waterConnectionDetails));

                installmentList.add(demanddetailBean.getInstallment());
            }
        demandObj.getEgDemandDetails().clear();
        demandObj.getEgDemandDetails().addAll(dmdDetailSet);
        int listlength = demandObj.getEgDemandDetails().size() - 1;
        if (!waterConnectionDetails.getConnectionType().toString().equalsIgnoreCase(METERED))
            installObj = installmentDao.getInsatllmentByModuleAndDescription(
                    moduleService.getModuleByName(PROPERTY_MODULE_NAME),
                    waterConnectionDetails.getDemandDetailBeanList().get(listlength).getInstallment());
        else {
            listlength = installmentList.size() - 1;
            installObj = installmentDao.getInsatllmentByModuleAndDescription(moduleService.getModuleByName(MODULE_NAME),
                    waterConnectionDetails.getDemandDetailBeanList().get(listlength).getInstallment());
        }
        demandObj.setEgInstallmentMaster(installObj);
        demandObj.setModifiedDate(new Date());
        if (demandObj.getIsHistory() == null)
            demandObj.setIsHistory("N");
        if (demandObj.getCreateDate() == null)
            demandObj.setCreateDate(new Date());
        if (demandObj.getId() == null) {
            final WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
            waterdemandConnection.setDemand(demandObj);
            waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
            waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
            waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
        }

        waterConnectionDetailsService.updateIndexes(waterConnectionDetails, sourceChannel);
        return waterConnectionDetails;
    }

    public BigDecimal getTotalAmountForBaseDemand(final DemandDetail demanddetailBean,
            final BigDecimal baseDemandAmount) {
        BigDecimal currentTotalAmount = BigDecimal.ZERO;
        final EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository
                .findEgDemandDetailById(demanddetailBean.getId());
        if (demanddetailBean.getId() == null)
            currentTotalAmount = baseDemandAmount.add(demanddetailBean.getActualAmount());
        else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) == -1) {
            final BigDecimal diffExtraless = demandDetailsObj.getAmount().subtract(demanddetailBean.getActualAmount());
            currentTotalAmount = baseDemandAmount.subtract(diffExtraless);
        } else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) == 1) {
            final BigDecimal diffExtra = demanddetailBean.getActualAmount().subtract(demandDetailsObj.getAmount());
            currentTotalAmount = baseDemandAmount.add(diffExtra);
        } else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) == 0)
            currentTotalAmount = baseDemandAmount;
        return currentTotalAmount;

    }

    public BigDecimal getTotalCollectedAmountForDemand(final DemandDetail demanddetailBean,
            final BigDecimal demandAmountCollected) {
        BigDecimal currentTotalAmount = BigDecimal.ZERO;
        final EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository
                .findEgDemandDetailById(demanddetailBean.getId());
        if (demanddetailBean.getId() == null)
            currentTotalAmount = demandAmountCollected.add(demanddetailBean.getActualCollection());
        else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) == -1) {
            final BigDecimal diffExtraless = demandDetailsObj.getAmtCollected()
                    .subtract(demanddetailBean.getActualCollection());
            currentTotalAmount = demandAmountCollected.subtract(diffExtraless);
        } else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) == 1) {
            final BigDecimal diffExtra = demanddetailBean.getActualCollection()
                    .subtract(demandDetailsObj.getAmtCollected());
            currentTotalAmount = demandAmountCollected.add(diffExtra);
        } else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) == 0)
            currentTotalAmount = demandAmountCollected;
        return currentTotalAmount;

    }

    /**
     * @param consumerCode
     * @return Generates Eg_bill Entry and saved with Demand and As of now we are generating Bill and its in XML format because no
     * Method to just to generate Bill and Save as of now in connectionBillService.
     */
    @Transactional
    public String generateBillForMeterAndMonthly(final String consumerCode) {

        final WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context
                .getBean("waterConnectionBillable");
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
        final MeterDemandNoticeNumberGenerator meterDemandNotice = beanResolver
                .getAutoNumberServiceFor(MeterDemandNoticeNumberGenerator.class);

        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(ApplicationThreadLocals.getUserId());
        waterConnectionBillable.setReferenceNumber(meterDemandNotice.generateMeterDemandNoticeNumber());
        waterConnectionBillable.setBillType(getBillTypeByCode(WaterTaxConstants.BILLTYPE_MANUAL));

        return connectionBillService.getBillXML(waterConnectionBillable);
    }

    public WaterConnectionDetails updateDemandForNonmeteredConnection(
            final WaterConnectionDetails waterConnectionDetails, Installment installment,
            final Boolean reconnInSameInstallment, final String workFlowAction) throws ValidationException {
        Date installemntStartDate;
        EgDemandDetails existingDemandDtlObject = null;
        int numberOfMonths;
        double totalWaterRate;
        List<Installment> installmentList = new ArrayList<>();

        if (installment == null)
            installment = getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, new Date());
        if (workFlowAction != null && workFlowAction.equals(WaterTaxConstants.WF_STATE_TAP_EXECUTION_DATE_BUTTON))
            installemntStartDate = new Date();

        else if (reconnInSameInstallment != null) {
            if (reconnInSameInstallment)
                installemntStartDate = installment.getFromDate();
            else
                installemntStartDate = waterConnectionDetails.getReconnectionApprovalDate();
        } else
            installemntStartDate = new Date();

        final CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());
        numberOfMonths = DateUtils.noOfMonthsBetween(installemntStartDate, finYear.getEndingDate());
        if (numberOfMonths >= 6)
            installmentList = waterTaxUtils.getInstallmentsForCurrYear(finYear.getStartingDate());
        else
            installmentList.add(installment);

        EgDemandDetails demandDetails;
        final WaterRatesDetails waterRatesDetails = getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
        final EgDemand currentDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        final WaterDemandConnection demandConnection = waterDemandConnectionService
                .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, currentDemand);
        for (final Installment instlment : installmentList) {
            for (final EgDemandDetails demandDtls : currentDemand.getEgDemandDetails())
                if (WATERTAXREASONCODE.equalsIgnoreCase(demandDtls.getEgDemandReason().getEgDemandReasonMaster().getCode()) &&
                        instlment.getDescription()
                                .equalsIgnoreCase(demandDtls.getEgDemandReason().getEgInstallmentMaster().getDescription()))
                    existingDemandDtlObject = demandDtls;

            final Integer noofmonths = DateUtils.noOfMonthsBetween(installemntStartDate, instlment.getToDate());
            if (existingDemandDtlObject == null) {
                if (null != waterRatesDetails) {
                    if (noofmonths > 0)
                        totalWaterRate = waterRatesDetails.getMonthlyRate() * (noofmonths + 1);
                    else
                        totalWaterRate = waterRatesDetails.getMonthlyRate();

                    demandDetails = createDemandDetails(totalWaterRate,
                            WATERTAXREASONCODE, instlment);

                    currentDemand.setBaseDemand(currentDemand.getBaseDemand().add(BigDecimal.valueOf(totalWaterRate)));
                    currentDemand.setEgInstallmentMaster(instlment);
                    currentDemand.getEgDemandDetails().add(demandDetails);
                    currentDemand.setModifiedDate(new Date());
                    if (currentDemand.getId() != null && demandConnection == null) {
                        final WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
                        waterdemandConnection.setDemand(currentDemand);
                        waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
                        waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
                        waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
                    }
                } else
                    throw new ValidationException("err.water.rate.not.found");

                installemntStartDate = new DateTime(instlment.getToDate()).plusDays(1).toDate();
            }

        }
        return waterConnectionDetails;
    }

    public WaterRatesDetails getWaterRatesDetailsForDemandUpdate(final WaterConnectionDetails waterConnectionDetails) {
        final List<WaterRatesHeader> waterRatesHeaderList = waterRatesHeaderService
                .findByConnectionTypeAndUsageTypeAndWaterSourceAndPipeSize(waterConnectionDetails.getConnectionType(),
                        waterConnectionDetails.getUsageType(), waterConnectionDetails.getWaterSource(),
                        waterConnectionDetails.getPipeSize());
        WaterRatesDetails waterRatesDetails = null;
        if (!waterRatesHeaderList.isEmpty())
            for (final WaterRatesHeader waterRatesHeadertemp : waterRatesHeaderList) {
                waterRatesDetails = waterRatesDetailsService
                        .findByWaterRatesHeaderAndFromDateAndToDate(waterRatesHeadertemp, new Date(), new Date());
                if (waterRatesDetails != null)
                    break;
            }
        return waterRatesDetails;
    }

    public Installment getCurrentInstallment(final String moduleName, final String installmentType, final Date date) {
        if (null == installmentType)
            return installmentDao.getInsatllmentByModuleForGivenDate(moduleService.getModuleByName(moduleName),
                    date);
        else
            return installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                    moduleService.getModuleByName(moduleName), date, installmentType);
    }

    public List<Object> getDmdCollAmtInstallmentWiseWithIsDmdTrue(final EgDemand egDemand) {
        final StringBuffer strBuf = new StringBuffer(2000);
        strBuf.append(
                "SELECT wcdid,dmdResId,installment,amount,amt_collected,amt_rebate,amount-amt_collected AS balance,"
                        + "instStartDate FROM (SELECT wcd.id AS wcdid,dmdRes.id AS dmdResId,dmdRes.id_installment AS installment,"
                        + "SUM(dmdDet.amount) AS amount,SUM(dmdDet.amt_collected) AS amt_collected,SUM(dmdDet.amt_rebate) AS amt_rebate,"
                        + "inst.start_date AS inststartdate FROM eg_demand_details dmdDet,eg_demand_reason dmdRes,eg_installment_master inst,"
                        + "eg_demand_reason_master dmdresmas,egwtr_connectiondetails wcd WHERE dmdDet.id_demand_reason=dmdRes.id "
                        + "AND dmdDet.id_demand =:dmdId AND dmdRes.id_installment = inst.id AND dmdresmas.id = dmdres.id_demand_reason_master "
                        + "AND dmdresmas.isdemand=TRUE AND wcd.demand = dmdDet.id_demand GROUP BY dmdRes.id, dmdRes.id_installment,"
                        + "inst.start_date,wcd.id ORDER BY inst.start_date) AS dcb");
        return getCurrentSession().createSQLQuery(strBuf.toString()).setLong("dmdId", egDemand.getId()).list();
    }

    /**
     * @param waterConnectionDetails
     * @param givenDate It Checks the Meter Entry Exist For the Entred Date Month and Returns True if It Exists and checks with
     * Demand Current Installment
     */
    public Boolean meterEntryAllReadyExistForCurrentMonth(final WaterConnectionDetails waterConnectionDetails,
            final Date givenDate) {
        Boolean currrentInstallMentExist = false;

        final Installment installment = getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME,
                WaterTaxConstants.MONTHLY, givenDate);
        if (waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand() != null
                && waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand() != null)
            if (installment != null
                    && installment.getInstallmentNumber().equals(waterTaxUtils.getCurrentDemand(waterConnectionDetails)
                            .getDemand().getEgInstallmentMaster().getInstallmentNumber()))
                currrentInstallMentExist = true;
        return currrentInstallMentExist;
    }

    public List<Object> getDmdCollAmtInstallmentWiseUptoPreviousFinYear(final EgDemand egDemand,
            final WaterConnectionDetails waterConnectionDetails) {
        final CFinancialYear financialyear = financialYearDAO.getFinancialYearByDate(new Date());

        final StringBuffer strBuf = new StringBuffer(2000);
        strBuf.append(
                "select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, "
                        + "sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, "
                        + "eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id "
                        + "and dmdDet.id_demand =:dmdId and inst.start_date<:currFinStartDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master "
                        + "group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        final Query query = getCurrentSession().createSQLQuery(strBuf.toString())
                .setParameter("dmdId", egDemand.getId())
                .setParameter("currFinStartDate", financialyear.getStartingDate());
        return query.list();
    }

    public Map<String, Installment> getInstallmentsForPreviousYear(final Date currDate) {
        final Map<String, Installment> currYearInstMap = new HashMap<>();
        final String query = "select installment from Installment installment,CFinancialYear finYear where installment.module.name = '"
                + PTMODULENAME
                + "'  and cast(installment.toDate as date) <= cast(finYear.startingDate as date) order by installment.id desc";
        final Query qry = getCurrentSession().createQuery(query);
        final List<Installment> installments = qry.list();
        currYearInstMap.put(WaterTaxConstants.PREVIOUS_SECOND_HALF, installments.get(0));
        return currYearInstMap;
    }

}
