/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.services.report;

import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.egf.model.IEStatementEntry;
import org.egov.egf.model.Statement;
import org.egov.egf.model.StatementEntry;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.type.CharacterType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

public abstract class ScheduleService extends PersistenceService {
    static final BigDecimal NEGATIVE = new BigDecimal("-1");
    private static final Logger LOGGER = Logger.getLogger(ScheduleService.class);
    @Autowired
    AppConfigValueService appConfigValuesService;
    int minorCodeLength;
    int majorCodeLength;
    int detailCodeLength;
    String voucherStatusToExclude;

    public ScheduleService() {
        super(null);
    }

    public ScheduleService(Class type) {
        super(type);
    }

    /* for detailed */
    Map<String, Schedules> getScheduleToGlCodeMapDetailed(final String reportType, final String coaType) {
        final Query query = getSession().createNativeQuery(new StringBuilder("SELECT coa1.glcode, s.schedule, s.schedulename, coa1.type, coa1.name")
                .append(" FROM chartofaccounts coa1, chartofaccounts coa2, chartofaccounts coa3, schedulemapping s")
                .append(" WHERE coa3.scheduleid  = s.id AND coa3.id = coa2.parentid AND coa2.id = coa1.parentid")
                .append(" AND  coa2.classification=2 AND coa1.classification=4")
                .append(" AND coa3.type IN (:coaType ) AND coa2.type IN (:coaType ) AND coa1.type IN (:coaType ) ")
                .append(" AND s.reporttype = :reportType ORDER BY coa1.glcode").toString());
        query.setParameter("coaType", coaType, StringType.INSTANCE)
                .setParameter("reportType", reportType, StringType.INSTANCE);
        final List<Object[]> results = query.list();
        final Map<String, Schedules> scheduleMap = new LinkedHashMap<String, Schedules>();
        for (final Object[] row : results) {
            if (!scheduleMap.containsKey(row[1].toString()))
                scheduleMap.put(row[1].toString(), new Schedules(row[1].toString(), row[2].toString()));
            scheduleMap.get(row[1].toString())
                    .addChartOfAccount(new ChartOfAccount(row[0].toString(), row[3].toString(), row[4].toString()));
        }
        return scheduleMap;
    }

    Map<String, Schedules> getScheduleToGlCodeMap(final String reportType, final String coaType) {
        final Query query = getSession().createNativeQuery(new StringBuilder("select distinct coa.glcode,s.schedule,s.schedulename,")
                .append("coa.type,coa.name from chartofaccounts coa, schedulemapping s where s.id=coa.scheduleid and ")
                .append("coa.classification=2 and s.reporttype = :reportType and coa.type in (:coaType )")
                .append("order by coa.glcode").toString());
        query.setParameter("coaType", coaType, StringType.INSTANCE)
                .setParameter("reportType", reportType, StringType.INSTANCE);
        final List<Object[]> results = query.list();
        final Map<String, Schedules> scheduleMap = new LinkedHashMap<String, Schedules>();
        for (final Object[] row : results) {
            if (!scheduleMap.containsKey(row[1].toString()))
                scheduleMap.put(row[1].toString(), new Schedules(row[1].toString(), row[2].toString()));
            scheduleMap.get(row[1].toString()).addChartOfAccount(
                    new ChartOfAccount(row[0].toString(), row[3].toString(), row[4].toString()));
        }
        return scheduleMap;
    }

    List<Object[]> getAllGlCodesForAllSchedule(final String reportType, final String coaType) {
        final Query query = getSession().createNativeQuery(new StringBuilder("select distinct coa.majorcode,s.schedule,s.schedulename,")
                .append("coa.type from chartofaccounts coa, schedulemapping s where s.id=coa.scheduleid and ")
                .append("coa.classification=2 and s.reporttype = :reportType and coa.type in (:coaType ) ")
                .append("group by coa.majorcode,s.schedule,s.schedulename,coa.type order by coa.majorcode").toString());
        query.setParameter("coaType", coaType, StringType.INSTANCE)
                .setParameter("reportType", reportType, StringType.INSTANCE);
        return query.list();
    }

    List<Object[]> amountPerFundQueryForAllSchedules(final String filterQuery, final Map<String, Object> queryParams, final Date toDate, final Date fromDate,
                                                     final String reportType) {
        final String voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        final Query query = getSession().createNativeQuery(new StringBuilder("select sum(debitamount)-sum(creditamount),v.fundid,substr(c.glcode,1,")
                .append(minorCodeLength).append("),")
                .append("c.name from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where  ")
                .append(" v.id=g.voucherheaderid and c.id=g.glcodeid and v.id=mis.voucherheaderid and v.status not in(")
                .append(voucherStatusToExclude).append(")  AND v.voucherdate <= :voucherToDate and v.voucherdate >= :voucherFromDate and substr(c.glcode,1,")
                .append(minorCodeLength).append(") in (select distinct coa2.glcode from chartofaccounts coa2, ")
                .append("schedulemapping s where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = :reportType) ").append(filterQuery)
                .append(" group by v.fundid,substr(c.glcode,1,").append(minorCodeLength).append("),c.name order by substr(c.glcode,1,")
                .append(minorCodeLength).append(")").toString());
        query.setParameter("voucherToDate",toDate, DateType.INSTANCE)
                .setParameter("voucherFromDate", fromDate, DateType.INSTANCE)
                .setParameter("reportType", reportType, StringType.INSTANCE);
        queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        return query.list();
    }

    /* For view all schedules Detail */
    List<Object[]> amountPerFundQueryForAllSchedulesDetailed(final String filterQuery, final Map<String, Object> queryParams, final Date toDate, final Date fromDate,
                                                             final String reportType) {
        final String voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        final Query query = getSession().createNativeQuery(new StringBuilder("select sum(debitamount)-sum(creditamount),v.fundid,substr(c.glcode,1,")
                .append(detailCodeLength).append("),").append("c.name from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where  ")
                .append(" v.id=g.voucherheaderid and c.id=g.glcodeid and v.id=mis.voucherheaderid and v.status not in(")
                .append(voucherStatusToExclude).append(")  AND v.voucherdate <= :voucherToDate and v.voucherdate >= :voucherFromDate and substr(c.glcode,1,")
                .append(detailCodeLength).append(") not in (select DISTINCT coa4.glcode from chartofaccounts coa4 where coa4.parentid in (SELECT coa3.id")
                .append(" FROM chartofaccounts coa3 WHERE coa3.parentid IN(select coa2.id from chartofaccounts coa2, ")
                .append("schedulemapping s where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = :reportType ))) ").append(filterQuery)
                .append(" group by v.fundid,substr(c.glcode,1,").append(detailCodeLength).append("),c.name order by substr(c.glcode,1,")
                .append(detailCodeLength).append(")").toString());
        query.setParameter("voucherToDate", toDate, DateType.INSTANCE)
                .setParameter("voucherFromDate", fromDate, DateType.INSTANCE)
                .setParameter("reportType", reportType, StringType.INSTANCE);
        queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        return query.list();
    }

    public String getAppConfigValueFor(final String module, final String key) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(module, key).get(0).getValue();
    }

    public String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT1.format(date);
    }

    void addRowToStatement(final Statement statement, final Object[] row, final String glCode) {
        final StatementEntry entry = new StatementEntry();
        entry.setGlCode(glCode);
        entry.setAccountName(row[3].toString());
        statement.add(entry);
    }

    public List<Fund> getFunds() {
        final Criteria voucherHeaderCriteria = getSession().createCriteria(CVoucherHeader.class);
        final List fundIdList = voucherHeaderCriteria.setProjection(Projections.distinct(Projections.property("fundId.id")))
                .list();
        if (!fundIdList.isEmpty())
            return getSession().createCriteria(Fund.class).add(Restrictions.in("id", fundIdList)).list();
        return new ArrayList<Fund>();
    }

    protected List<Object[]> getAllGlCodesForSubSchedule(final String majorCode, final Character type, final String reportType) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Getting schedule for " + majorCode);
        final Query query = getSession().createNativeQuery(new StringBuilder("select distinct coa.glcode,coa.name,s.schedule,s.schedulename from chartofaccounts coa, ")
                .append("schedulemapping s where s.id=coa.scheduleid and coa.classification=2 and s.reporttype = :reportType and coa.majorcode=:majorCode")
                .append(" and coa.type=:type order by coa.glcode").toString());
        query.setParameter("reportType", reportType, StringType.INSTANCE)
                .setParameter("majorCode", majorCode, StringType.INSTANCE)
                .setParameter("type", type, CharacterType.INSTANCE);
        return query.list();
    }

    protected List<Object[]> getAllGlCodesForSchedule(final List<String> reportType) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Getting schedule for ");
        final Query query = getSession().createNativeQuery(new StringBuilder("SELECT coa1.glcode, s.schedule, s.schedulename, coa1.type, coa1.name")
                .append(" FROM chartofaccounts coa1, chartofaccounts coa2, chartofaccounts coa3, schedulemapping s")
                .append(" WHERE coa3.scheduleid  = s.id AND coa3.id = coa2.parentid AND coa2.id = coa1.parentid")
                .append(" AND coa2.classification=2 AND coa1.classification=4")
                .append(" AND coa3.type   IN (:reportType) AND coa2.type IN (:reportType) AND coa1.type IN (:reportType) AND s.reporttype = 'IE' ORDER BY coa1.glcode").toString());
        query.setParameterList("reportType", reportType, StringType.INSTANCE);
        return query.list();
    }

    protected List<Object[]> getAllDetailGlCodesForSubSchedule(final String majorCode, final Character type,
                                                               final String reportType) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Getting detail codes for " + majorCode + "reporttype" + reportType);
        final Query query = getSession().createNativeQuery(new StringBuilder("select distinct coad.glcode,coad.name from chartofaccounts coa,chartofaccounts coad,")
                .append(" schedulemapping s where    s.id=coa.scheduleid  AND coa.classification=2 AND s.reporttype=:reportType and coad.majorcode=:majorCode")
                .append(" and coa.type=:type and coa.glcode=SUBSTR(coad.glcode,1,")
                .append(minorCodeLength).append(") and coad.classification=4 order by coad.glcode").toString());
        query.setParameter("reportType", reportType, StringType.INSTANCE)
                .setParameter("majorCode", majorCode, StringType.INSTANCE)
                .setParameter("type", type, CharacterType.INSTANCE);
        return query.list();
    }

    protected List<Object[]> getSchedule(final String majorCode, final Character type, final String reportType) {
        final Query query = getSession().createNativeQuery(new StringBuilder("select distinct coa.glcode,coa.name,s.schedule,s.schedulename from chartofaccounts coa, ")
                .append("schedulemapping s where s.id=coa.scheduleid and coa.classification=2 and s.reporttype = :reportType and coa.majorcode=:majorCode")
                .append(" and coa.type=:type order by coa.glcode").toString());
        query.setParameter("reportType", reportType, StringType.INSTANCE)
                .setParameter("majorCode", majorCode, StringType.INSTANCE)
                .setParameter("type", type, CharacterType.INSTANCE);
        return query.list();
    }

    protected List<Object[]> getAllLedgerTransaction(final String majorcode, final Date toDate, final Date fromDate,
                                                     final List<Integer> fundId,
                                                     final String filterQuery, final Map<String, Object> queryParams) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Getting ledger transactions details where >>>> EndDate=" + toDate + "from Date=" + fromDate);
        final String voucherStatusToExclude = getAppConfigValueFor("EGF", "statusexcludeReport");
        if (!majorcode.equals("")) {
        }

        final Query query = getSession()
                .createNativeQuery(new StringBuilder("select g.glcode,coa.name,sum(g.debitamount)-sum(g.creditamount),v.fundid,coa.type,coa.majorcode")
                        .append(" from generalledger g,chartofaccounts coa, voucherheader v,vouchermis mis")
                        .append(" where v.id=mis.voucherheaderid and g.voucherheaderid=v.id and g.glcodeid=coa.id and v.voucherdate BETWEEN :voucherFromDate and :voucherToDate")
                        .append(" and v.status not in (").append(voucherStatusToExclude).append(") and v.id=g.voucherheaderid and v.fundid in :fundId")
                        .append(filterQuery).append(" and g.glcodeid=coa.id  ")
                        .append("GROUP by g.glcode,coa.name,v.fundid ,coa.type ,coa.majorcode order by g.glcode,coa.name,coa.type").toString());
        query.setParameter("voucherFromDate", fromDate, DateType.INSTANCE)
                .setParameter("voucherToDate", toDate, DateType.INSTANCE)
                .setParameterList("fundId", fundId, IntegerType.INSTANCE);
        queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        return query.list();
    }

    List<Object[]> getRowsForGlcode(final List<Object[]> resultMap, final String glCode) {
        final List<Object[]> rows = new ArrayList<Object[]>();
        for (final Object[] row : resultMap)
            if (row[2].toString().equalsIgnoreCase(glCode))
                rows.add(row);
        return rows;
    }

    protected void addRowForSchedule(final Statement statement, final List<Object[]> allGlCodes) {
        if (!allGlCodes.isEmpty())
            statement.add(new StatementEntry("Schedule " + allGlCodes.get(0)[2].toString() + ":",
                    allGlCodes.get(0)[3].toString(), "", null, null, true));
    }

    protected void addRowForIESchedule(final Statement statement, final List<Object[]> allGlCodes) {
        if (!allGlCodes.isEmpty())
            statement.addIE(new IEStatementEntry("Schedule " + allGlCodes.get(0)[2].toString() + ":", allGlCodes.get(0)[3]
                    .toString(), "", true));
    }

    boolean contains(final List<Object[]> result, final String glCode) {
        for (final Object[] row : result)
            if (row[2].toString().equalsIgnoreCase(glCode))
                return true;
        return false;
    }

    void computeAndAddTotals(final Statement statement) {
        BigDecimal currentTotal = BigDecimal.ZERO;
        BigDecimal previousTotal = BigDecimal.ZERO;
        for (int index = 0; index < statement.size(); index++) {
            if (statement.get(index).getCurrentYearTotal() != null)
                currentTotal = currentTotal.add(statement.get(index).getCurrentYearTotal());
            if (statement.get(index).getPreviousYearTotal() != null)
                previousTotal = previousTotal.add(statement.get(index).getPreviousYearTotal());
        }
        statement.add(new StatementEntry(null, "Total", "", previousTotal, currentTotal, true));
    }

    /* for detailed */
    void computeAndAddTotalsForSchedules(final Statement statement) {
        BigDecimal currentTotal = BigDecimal.ZERO;
        BigDecimal previousTotal = BigDecimal.ZERO;
        Map<String, BigDecimal> fundTotals = new HashMap<String, BigDecimal>();
        for (final StatementEntry entry : statement.getEntries())
            if (entry.getAccountName().equals("Schedule Total")) {
                entry.setCurrentYearTotal(currentTotal);
                entry.setPreviousYearTotal(previousTotal);
                entry.setFundWiseAmount(fundTotals);
                currentTotal = BigDecimal.ZERO;
                previousTotal = BigDecimal.ZERO;
                fundTotals = new HashMap<String, BigDecimal>();
            } else {
                if (entry.getCurrentYearTotal() != null)
                    currentTotal = currentTotal.add(entry.getCurrentYearTotal());
                if (entry.getPreviousYearTotal() != null)
                    previousTotal = previousTotal.add(entry.getPreviousYearTotal());

                for (final Entry<String, BigDecimal> row : entry.getFundWiseAmount().entrySet())
                    fundTotals.put(row.getKey(),
                            fundTotals.get(row.getKey()) != null ? fundTotals.get(row.getKey()).add(zeroOrValue(row.getValue()))
                                    : zeroOrValue(row.getValue()));
            }
    }

    private BigDecimal zeroOrValue(final BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    List<Object[]> currentYearAmountQuery(final String filterQuery, final Map<String, Object> queryParams, final Date toDate, final Date fromDate,
                                          final String majorCode, final String reportType) {
        final Query query = getSession().createNativeQuery(new StringBuilder("select sum(debitamount)-sum(creditamount),v.fundid,c.glcode ")
                .append("from generalledger g,chartofaccounts c,voucherheader v,vouchermis mis  where ")
                .append(" v.id=g.voucherheaderid and c.id=g.glcodeid and v.status not in(").append(voucherStatusToExclude)
                .append(")  AND v.voucherdate <= :voucherToDate and v.id=mis.voucherheaderid and v.voucherdate >=:voucherFromDate")
                .append(" and c.glcode in (select distinct coad.glcode from chartofaccounts coa2, schedulemapping s ")
                .append(",chartofaccounts coad where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = :reportType and coa2.glcode=SUBSTR(coad.glcode,1,")
                .append(minorCodeLength).append(") and coad.classification=4 and coad.majorcode=:majorCode) and c.majorcode=:majorCode and c.classification=4 ")
                .append(filterQuery).append(" group by v.fundid,c.glcode order by c.glcode").toString());
        query.setParameter("voucherToDate", toDate)
                .setParameter("voucherFromDate", fromDate)
                .setParameter("reportType", reportType, StringType.INSTANCE)
                .setParameter("majorCode", majorCode, StringType.INSTANCE);
        queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        return query.list();
    }

}

class ChartOfAccount {
    private static final Logger LOGGER = Logger.getLogger(ChartOfAccount.class);
    public final String glCode;
    public final String type;
    public final String name;

    public ChartOfAccount(final String glCode, final String type, final String name) {
        this.glCode = glCode;
        this.type = type;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return 31 + (glCode == null ? 0 : glCode.hashCode());
    }

    @Override
    public boolean equals(final Object obj) {
        try {
            final ChartOfAccount other = (ChartOfAccount) obj;
            return glCode.equals(other.glCode);
        } catch (final Exception e) {
            LOGGER.error("Failed :" + e.getMessage(), e);
            return false;
        }
    }
}

class Schedules {
    public final String scheduleNumber;
    public final String scheduleName;

    public final Set<ChartOfAccount> chartOfAccount = new LinkedHashSet<ChartOfAccount>();

    public Schedules(final String scheduleNumber, final String scheduleName) {
        this.scheduleNumber = scheduleNumber;
        this.scheduleName = scheduleName;
    }

    public boolean contains(final String glCode) {
        return chartOfAccount.contains(new ChartOfAccount(glCode, null, null));
    }

    public String getCoaName(final String glCode) {
        for (final ChartOfAccount coa : chartOfAccount)
            if (glCode.equalsIgnoreCase(coa.glCode))
                return coa.name;
        return "";
    }

    public void addChartOfAccount(final ChartOfAccount s) {
        chartOfAccount.add(s);
    }
}