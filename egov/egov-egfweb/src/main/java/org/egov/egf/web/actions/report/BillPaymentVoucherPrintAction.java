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
package org.egov.egf.web.actions.report;


import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.web.actions.voucher.VoucherReport;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberToWord;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Results(value = {
		@Result(name=BillPaymentVoucherPrintAction.PRINT , location="billPaymentVoucherPrint-print.jsp"),
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=BankPaymentVoucherReport.pdf" }),
                @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "application/xls", "contentDisposition", "no-cache;filename=BankPaymentVoucherReport.xls" }),
                        @Result(name = "HTML", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "text/html" })
})

@ParentPackage("egov")
public class BillPaymentVoucherPrintAction extends BaseFormAction {
    Long chequeNumberPass;
    String chequeNumber = "";
    InstrumentHeader instrumentHeader = null;
    String cashModePartyName = "";				// Also used as a flag to check if the mode of payment is Cash
    String chequeDate = "";
    String rtgsRefNo = "";
    String rtgsDate = "";
    String paymentMode = "";
    String chequeNoComp = "";
    Long chequeNoCompL;
    String jasperpath = "/reports/templates/billPaymentVoucherReport.jasper";
    static final long serialVersionUID = 1L;
    static final String PRINT = "print";
    CVoucherHeader voucher = new CVoucherHeader();
    Paymentheader paymentHeader = new Paymentheader();
    List<Object> voucherReportList = new ArrayList<Object>();
    InputStream inputStream;
    ReportHelper reportHelper;
    Long id;
    List<Miscbilldetail> miscBillDetailList;
    Miscbilldetail ob = new Miscbilldetail();
    static final String ACCDETAILTYPEQUERY = " from Accountdetailtype where id=?";
    String bankName = "";
    String bankAccountNumber = "";
    ArrayList<Long> chequeNoList = new ArrayList<Long>();
    ArrayList<String> chequeNosList = new ArrayList<String>();
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovCommon egovCommon;

    public Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("bpvNumber", getVoucherNumber());
        paramMap.put("voucherDate", getVoucherDate());
        paramMap.put("bankName", bankName);
        paramMap.put("bankAccountNumber", bankAccountNumber);

        if (paymentHeader != null && paymentHeader.getState() != null)
            loadInboxHistoryData(paymentHeader.getStateHistory(), paramMap);

        if (miscBillDetailList != null) {
            paramMap.put("partyName", getPartyName());
            paramMap.put("billNumber", getBillNumber());
            paramMap.put("linkNo", getLinkNo());
        }

        paramMap.put("amountInWords", getAmountInWords());
        paramMap.put("chequeNumber", chequeNumber);
        paramMap.put("chequeDate", chequeDate);
        paramMap.put("rtgsRefNo", rtgsRefNo);
        paramMap.put("paymentMode", paymentMode);
        paramMap.put("rtgsDate", rtgsDate);
        paramMap.put("ulbName", ReportUtil.getCityName());
        paramMap.put("narration", getPaymentNarration());

        return paramMap;
    }

    private String getLinkNo() {
        if (miscBillDetailList != null && miscBillDetailList.size() > 1)
            return "MULTIPLE";
        else if (miscBillDetailList != null && miscBillDetailList.size() == 1
                && miscBillDetailList.get(0).getBillVoucherHeader() != null)
            return miscBillDetailList.get(0).getBillVoucherHeader().getVoucherNumber();
        else
            return "";
    }

    private String getAmountInWords() {
        if (miscBillDetailList != null && miscBillDetailList.size() > 1)
        {
            Float totalAmt = 0f;
            for (final Miscbilldetail misBillDet : miscBillDetailList)
                totalAmt += misBillDet.getPaidamount().floatValue();
            return NumberToWord.convertToWord(new BigDecimal(totalAmt).toString());
        }
        else if (miscBillDetailList != null && miscBillDetailList.size() == 1)
            return miscBillDetailList.get(0).getAmtInWords();
        else
            return "";
    }

    private String getBillNumber() {
        if (miscBillDetailList != null && miscBillDetailList.size() > 1)
            return "MULTIPLE";
        else if (miscBillDetailList != null && miscBillDetailList.size() == 1)
            return miscBillDetailList.get(0).getBillnumber();
        else
            return "";
    }

    private String getPartyName() {

        if (cashModePartyName != null && !cashModePartyName.equalsIgnoreCase(""))
            return cashModePartyName;
        if (miscBillDetailList != null && miscBillDetailList.size() > 1 && hasSamePartyName(miscBillDetailList))
            return miscBillDetailList.get(0).getPaidto();
        else if (miscBillDetailList != null && miscBillDetailList.size() > 1)
            return "MULTIPLE";
        else if (miscBillDetailList != null && miscBillDetailList.size() == 1)
            return miscBillDetailList.get(0).getPaidto();
        else
            return "";
    }

    boolean hasSamePartyName(final List<Miscbilldetail> billList) {
        String name = "initial";
        for (final Miscbilldetail miscbilldetail : billList)
            if ("initial".equalsIgnoreCase(name))
                name = miscbilldetail.getPaidto();
            else if (!name.equalsIgnoreCase(miscbilldetail.getPaidto()))
                return false;
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setReportHelper(final ReportHelper helper) {
        reportHelper = helper;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<Object> getVoucherReportList() {
        return voucherReportList;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String execute() {
        return print();
    }

    @Action(value = "/report/billPaymentVoucherPrint-ajaxPrint")
    public String ajaxPrint() {
        return exportHtml();
    }

    @Override
    public Object getModel() {
        return voucher;
    }

    @Action(value = "/report/billPaymentVoucherPrint-print")
    public String print() {
        return PRINT;
    }

    void populateVoucher() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);

        if (!StringUtils.isBlank(parameters.get("id")[0])) {
            chequeNosList = new ArrayList<String>();
            final Long id = Long.valueOf(parameters.get("id")[0]);
            paymentHeader = (Paymentheader) persistenceService.getSession().get(Paymentheader.class, id);
            if (paymentHeader != null && paymentHeader.getType().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
            {
                paymentMode = "rtgs";
                voucher = paymentHeader.getVoucherheader();
                if (voucher != null)
                {
                    final List<InstrumentVoucher> instrumentVoucherList = persistenceService.findAllBy(
                            "from InstrumentVoucher where voucherHeaderId.id=?", voucher.getId());
                    if (instrumentVoucherList != null && instrumentVoucherList.size() != 0)
                    {
                        final InstrumentHeader instrumentHeader = instrumentVoucherList.get(0).getInstrumentHeaderId();
                        rtgsRefNo = instrumentHeader.getTransactionNumber();
                        rtgsDate = Constants.DDMMYYYYFORMAT2.format(instrumentHeader.getTransactionDate());
                    }
                    generateVoucherReportList();
                    final Bankaccount bankAccount = paymentHeader.getBankaccount();
                    if (bankAccount != null)
                    {
                        bankName = bankAccount.getBankbranch().getBank().getName().concat("-")
                                .concat(bankAccount.getBankbranch().getBranchname());
                        bankAccountNumber = bankAccount.getAccountnumber();
                    }
                    miscBillDetailList = persistenceService.findAllBy("from Miscbilldetail where payVoucherHeader.id=?",
                            voucher.getId());
                }
                return;
            }
            if (paymentHeader != null) {
                voucher = paymentHeader.getVoucherheader();
                final List<String> excludeChequeStatusses = new ArrayList<String>();
                excludeChequeStatusses.add(FinancialConstants.INSTRUMENT_CANCELLED_STATUS);
                excludeChequeStatusses.add(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS);
                excludeChequeStatusses.add(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS);
                final List<InstrumentVoucher> instrumentVoucherList = persistenceService.findAllBy(
                        "from InstrumentVoucher where voucherHeaderId.id=?", voucher.getId());
                if (instrumentVoucherList != null && !instrumentVoucherList.isEmpty())
                    for (final InstrumentVoucher instrumentVoucher : instrumentVoucherList)
                        try {
                            if (excludeChequeStatusses.contains(instrumentVoucher.getInstrumentHeaderId().getStatusId()
                                    .getDescription()))
                                continue;
                            instrumentHeader = instrumentVoucher.getInstrumentHeaderId();
                            chequeNumber = instrumentVoucher.getInstrumentHeaderId().getInstrumentNumber();
                            chequeDate = Constants.DDMMYYYYFORMAT2.format(instrumentVoucher.getInstrumentHeaderId()
                                    .getInstrumentDate());
                            if (isInstrumentMultiVoucherMapped(instrumentVoucher.getInstrumentHeaderId().getId()))
                                chequeNosList.add(chequeNumber + "-MULTIPLE");
                            else
                                chequeNosList.add(chequeNumber);
                            chequeNumberPass = Long.parseLong(chequeNumber);
                            chequeNoList.add(chequeNumberPass);
                        } catch (final NumberFormatException ex) {
                        }
                generateVoucherReportList();
                final Bankaccount bankAccount = paymentHeader.getBankaccount();
                if (bankAccount != null) {
                    bankName = bankAccount.getBankbranch().getBank().getName().concat("-")
                            .concat(bankAccount.getBankbranch().getBranchname());
                    bankAccountNumber = bankAccount.getAccountnumber();
                }
                // For Cash mode of payment, we need to take the payto of the associated cheque.
                if (paymentHeader.getType().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_CASH))
                    if (instrumentHeader != null && instrumentHeader.getPayTo() != null)
                        cashModePartyName = instrumentHeader.getPayTo();
            }
            miscBillDetailList = persistenceService.findAllBy("from Miscbilldetail where payVoucherHeader.id=?", voucher.getId());
        }
        Collections.sort(chequeNoList);
        chequeNumber = "";
        for (final Long lval : chequeNoList)
            for (final String sval : chequeNosList) {
                if (sval.contains("MULTIPLE"))
                    chequeNoCompL = Long.parseLong(sval.substring(0, sval.lastIndexOf("-")));
                else
                    chequeNoCompL = Long.parseLong(sval);
                if (lval.equals(chequeNoCompL)) {
                    chequeNumber = chequeNumber + sval + "/";
                    break;
                }

            }
        if (chequeNumber.length() > 1)
            chequeNumber = chequeNumber.substring(0, chequeNumber.length() - 1);
    }

    private boolean isInstrumentMultiVoucherMapped(final Long instrumentHeaderId) {
        final List<InstrumentVoucher> instrumentVoucherList = persistenceService.findAllBy(
                "from InstrumentVoucher where instrumentHeaderId.id=?", instrumentHeaderId);
        boolean rep = false;
        if (instrumentVoucherList != null && instrumentVoucherList.size() != 0) {
            final Long voucherId = instrumentVoucherList.get(0).getVoucherHeaderId().getId();
            for (final InstrumentVoucher instrumentVoucher : instrumentVoucherList)
                if (voucherId != instrumentVoucher.getVoucherHeaderId().getId())
                {
                    rep = true;
                    break;
                }
        }
        return rep;
    }

    private void generateVoucherReportList() {
        if (voucher != null) {
            for (final CGeneralLedger vd : voucher.getGeneralledger())
                if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(vd.getCreditAmount().doubleValue()))==0) {
                    final VoucherReport voucherReport = new VoucherReport(persistenceService, Integer.valueOf(voucher.getId()
                            .toString()), vd, egovCommon);
                    voucherReport.setDepartment(voucher.getVouchermis().getDepartmentid());
                    voucherReportList.add(voucherReport);
                }
            for (final CGeneralLedger vd : voucher.getGeneralledger())
            	  if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(vd.getDebitAmount().doubleValue()))==0){
                    final VoucherReport voucherReport = new VoucherReport(persistenceService, Integer.valueOf(voucher.getId()
                            .toString()), vd, egovCommon);
                    voucherReport.setDepartment(voucher.getVouchermis().getDepartmentid());
                    voucherReportList.add(voucherReport);
                }
        }
    }

    String getUlbName() {
        final SQLQuery query = persistenceService.getSession().createSQLQuery("select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    private String getPaymentNarration() {
        return voucher == null || voucher.getDescription() == null ? "" : voucher.getDescription();
    }

    @Action(value = "/report/billPaymentVoucherPrint-exportPdf")
    public String exportPdf() throws JRException, IOException {
        populateVoucher();
        inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), voucherReportList);
        return "PDF";
    }

    public String exportHtml() {
        populateVoucher();
        inputStream = reportHelper.exportHtml(inputStream, jasperpath, getParamMap(), voucherReportList, "px");
        return "HTML";
    }

    @Action(value = "/report/billPaymentVoucherPrint-exportXls")
    public String exportXls() throws JRException, IOException {
        populateVoucher();
        inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), voucherReportList);
        return "XLS";
    }

    public Map<String, Object> getAccountDetails(final Integer detailtypeid, final Integer detailkeyid,
            final Map<String, Object> tempMap) throws ApplicationException {
        final Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY, detailtypeid);
        tempMap.put("detailtype", detailtype.getName());
        tempMap.put("detailtypeid", detailtype.getId());
        tempMap.put("detailkeyid", detailkeyid);
        egovCommon.setPersistenceService(persistenceService);
        final EntityType entityType = egovCommon.getEntityType(detailtype, detailkeyid);
        tempMap.put(Constants.DETAILKEY, entityType.getName());
        tempMap.put(Constants.DETAILCODE, entityType.getCode());
        return tempMap;
    }

    String getVoucherNumber() {
        return voucher == null || voucher.getVoucherNumber() == null ? "" : voucher.getVoucherNumber();
    }

    String getVoucherDate() {
        return voucher == null || voucher.getVoucherDate() == null ? "" : DateUtils.getDefaultFormattedDate(voucher
                .getVoucherDate());
    }

	void loadInboxHistoryData(final List<StateHistory> stateHistory,
			final Map<String, Object> paramMap)
			throws ApplicationRuntimeException {
		final List<String> history = new ArrayList<String>();
		final List<String> workFlowDate = new ArrayList<String>();
		if (!stateHistory.isEmpty()) {
			for (final StateHistory historyState : stateHistory)
				
				if (!"NEW".equalsIgnoreCase(historyState.getValue())) {
					history.add(historyState.getSenderName());
					workFlowDate.add(Constants.DDMMYYYYFORMAT2
							.format(historyState.getLastModifiedDate()));
					if (historyState.getValue().equalsIgnoreCase("Rejected"))
					{
						history.clear();
						workFlowDate.clear();
					}
					
				}
			
			history.add(paymentHeader.getState().getSenderName());
			workFlowDate.add(Constants.DDMMYYYYFORMAT2.format(paymentHeader
					.getState().getLastModifiedDate()));
		} else {
			history.add(paymentHeader.getState().getSenderName());
			workFlowDate.add(Constants.DDMMYYYYFORMAT2.format(paymentHeader
					.getState().getLastModifiedDate()));
		}
		for (int i = 0; i < history.size(); i++) {
			paramMap.put("workFlow_" + i, history.get(i));
			paramMap.put("workFlowDate_" + i, workFlowDate.get(i));
		}
	}

	String getVoucherDescription() {
		return voucher == null || voucher.getDescription() == null ? ""
				: voucher.getDescription();
	}

}