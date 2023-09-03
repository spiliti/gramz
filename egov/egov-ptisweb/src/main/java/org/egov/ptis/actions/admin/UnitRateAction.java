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
package org.egov.ptis.actions.admin;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ROLE_PTADMINISTRATOR;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Namespace("/admin")
@ResultPath("/WEB-INF/jsp/")
@Results({ @Result(name = "new", location = "admin/unitRate-new.jsp"),
        @Result(name = "ack", location = "admin/unitRate-ack.jsp"),
        @Result(name = "search", location = "admin/unitRate-search.jsp"),
        @Result(name = "view", location = "admin/unitRate-view.jsp") })
public class UnitRateAction extends BaseFormAction {

    private Category category = new Category();
    private Long zoneId;
    private Long usageId;
    private Long structureClassId;
    private Long categoryId;
    private String ackMessage;
    private String mode = "";
    private String roleName;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    List<BoundaryCategory> bndryCatList;

    private static final String RESULT_ACK = "ack";
    private static final String RESULT_NEW = "new";
    private static final String SEARCH_FORM = "search";

    @Override
    @SkipValidation
    public Object getModel() {
        return category;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SkipValidation
    public void prepare() {

        List<Boundary> zoneList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,
                REVENUE_HIERARCHY_TYPE);
        List<PropertyUsage> usageList = getPersistenceService().findAllBy("from PropertyUsage order by usageName");
        List<StructureClassification> structureClassificationList = getPersistenceService().findAllBy(
                "from StructureClassification order by typeName");
        addDropdownData("ZoneList", zoneList);
        addDropdownData("UsageList", usageList);
        addDropdownData("StructureClassificationList", structureClassificationList);

        final Long userId = (Long) session().get(SESSIONLOGINID);
        if (userId != null)
            setRoleName(propertyTaxUtil.getRolesForUserId(userId));
    }

    @SkipValidation
    @Action(value = "/unitRate-newForm")
    public String newForm() {
        if (roleName.contains(ROLE_PTADMINISTRATOR.toUpperCase())) {
            if (mode.equals(EDIT) || mode.equals("deactivate")) {
                if (categoryId != null && categoryId != -1) {
                    category = (Category) getPersistenceService().find("from Category where id = ?", categoryId);
                    setUsageId(category.getPropUsage().getId());
                    setStructureClassId(category.getStructureClass().getId());
                }
                return RESULT_NEW;
            } else {
                mode = NEW;
                return RESULT_NEW;
            }
        } else {
            return SEARCH_FORM;
        }

    }

    @SkipValidation
    @Action(value = "/unitRate-searchForm")
    public String searchForm() {
        return SEARCH_FORM;
    }

    @Action(value = "/unitRate-create")
    public String create() {
        Category existingCategory = (Category) getPersistenceService()
                .find("select bc.category from BoundaryCategory bc where bc.bndry.id = ? "
                        + "and bc.category.propUsage.id = ? and bc.category.structureClass.id = ? and bc.category.fromDate = ? and bc.category.isActive = true ",
                        zoneId, usageId, structureClassId, category.getFromDate());

        // If category exists for the combination of zone, usage,structure and
        // from date, update the existing category's rate
        if (existingCategory != null) {
            addActionError(getText("unit.rate.exists.for.combination"));
            mode = NEW;
            return RESULT_NEW;
        } else {
            PropertyUsage usage = (PropertyUsage) getPersistenceService().find("from PropertyUsage where id = ? ",
                    usageId);
            StructureClassification structureClass = (StructureClassification) getPersistenceService().find(
                    "from StructureClassification where id = ? ", structureClassId);
            Boundary zone = boundaryService.getBoundaryById(zoneId);
            
            category.setPropUsage(usage);
            category.setStructureClass(structureClass);
            category.setIsHistory('N');
            category.setIsActive(true);
            
            Calendar category_toDate = Calendar.getInstance();
            category_toDate.set(Calendar.DATE, 31);
            category_toDate.set(Calendar.MONTH, 3);
            category_toDate.set(Calendar.YEAR, 2099);
            category_toDate.set(Calendar.HOUR_OF_DAY, 0);
            category_toDate.set(Calendar.MINUTE, 0);
            category_toDate.set(Calendar.SECOND, 0); 
            
            category.setToDate(category_toDate.getTime());
            category.setCategoryName(usage.getUsageCode().concat("-").concat(structureClass.getConstrTypeCode())
                    .concat("-").concat(category.getCategoryAmount().toString()));

            if (zoneId != -1 && usageId != -1 && structureClassId != -1) {
                existingCategory = (Category) getPersistenceService().find(
                        "select bc.category from BoundaryCategory bc where bc.bndry.id = ? "
                                + "and bc.category.propUsage.id = ? and bc.category.structureClass.id = ? ", zoneId,
                        usageId, structureClassId);
                if (existingCategory != null) {
                    Date toDate = existingCategory.getToDate();
                    if (toDate == null || (toDate != null && toDate.after(category.getFromDate()))) {
                        Date newToDate = DateUtils.addDays(category.getFromDate(), -1);
                        existingCategory.setToDate(newToDate);
                    }
                }
            }

            BoundaryCategory boundaryCategory = new BoundaryCategory();
            boundaryCategory.setCategory(category);
            boundaryCategory.setBndry(zone);
            boundaryCategory.setFromDate(category.getFromDate());
            boundaryCategory.setToDate(category.getToDate());
            Set<BoundaryCategory> boundaryCategorySet = new HashSet<BoundaryCategory>();
            boundaryCategorySet.add(boundaryCategory);

            category.setCatBoundaries(boundaryCategorySet);
            getPersistenceService().persist(category);
        }

        setAckMessage("Unit Rate is saved successfully!");
        return RESULT_ACK;
    }

    @SkipValidation
    @Action(value = "/unitRate-search")
    public String search() {
        validateSearch();
        if (hasErrors()) {
            return SEARCH_FORM;
        } else {
            StringBuilder mainStr = new StringBuilder(400);
            mainStr.append("From BoundaryCategory bndryCat where bndryCat.bndry.id=:zone ");
            if (usageId != null && usageId != -1) {
                mainStr.append(" and bndryCat.category.propUsage.id=:usage");
            }
            if (structureClassId != null && structureClassId != -1) {
                mainStr.append(" and bndryCat.category.structureClass.id=:stucture");
            }
            mainStr.append(" and bndryCat.category.IsHistory = 'N' and bndryCat.category.isActive = true ");
            final Query query = getPersistenceService().getSession().createQuery(mainStr.toString());
            query.setLong("zone", zoneId);
            if (usageId != null && usageId != -1) {
                query.setLong("usage", usageId);
            }
            if (structureClassId != null && structureClassId != -1) {
                query.setLong("stucture", structureClassId);
            }
            bndryCatList = query.list();
            if (bndryCatList.isEmpty()) {
                addActionError(getText("no.unit.rate.exists"));
            }
            return SEARCH_FORM;
        }
    }

    @SkipValidation
    @Action(value = "/unitRate-view")
    public String view() {
        if (categoryId != null && categoryId != -1) {
            category = (Category) getPersistenceService().find("from Category where id = ?", categoryId);
        }
        setUsageId(category.getPropUsage().getId());
        setStructureClassId(category.getStructureClass().getId());
        mode = VIEW;
        return RESULT_NEW;
    }

    @SkipValidation
    @Action(value = "/unitRate-update")
    public String update() {
        Category catFromDb = null;
        Category existingCategory = (Category) getPersistenceService()
                .find("select bc.category from BoundaryCategory bc where bc.bndry.id = ? "
                        + "and bc.category.propUsage.id = ? and bc.category.structureClass.id = ? and bc.category.fromDate = ? and bc.category.categoryAmount = ? ",
                        zoneId, usageId, structureClassId, category.getFromDate(), category.getCategoryAmount());
        if (existingCategory != null) {
            addActionError(getText("unit.rate.exists.for.combination"));
            mode = EDIT; 
            return RESULT_NEW;
        } else {
            if (category != null && category.getId() != null && category.getId() != -1) {
                catFromDb = (Category) getPersistenceService().find("from Category where id = ?", category.getId());
            }
            Category categoryObj = new Category();
            categoryObj.setCategoryAmount(category.getCategoryAmount());
            categoryObj.setFromDate(category.getFromDate());
            PropertyUsage usage = (PropertyUsage) getPersistenceService().find("from PropertyUsage where id = ? ",
                    usageId);
            StructureClassification structureClass = (StructureClassification) getPersistenceService().find(
                    "from StructureClassification where id = ? ", structureClassId);
            Boundary zone = boundaryService.getBoundaryById(zoneId);
            categoryObj.setPropUsage(usage);
            categoryObj.setStructureClass(structureClass);
            categoryObj.setIsHistory('N');
            categoryObj.setToDate(catFromDb.getToDate());
            categoryObj.setIsActive(true);
            categoryObj.setCategoryName(usage.getUsageCode().concat("-").concat(structureClass.getConstrTypeCode())
                    .concat("-").concat(categoryObj.getCategoryAmount().toString()));

            BoundaryCategory boundaryCategory = new BoundaryCategory();
            boundaryCategory.setCategory(categoryObj);
            boundaryCategory.setBndry(zone);
            boundaryCategory.setFromDate(categoryObj.getFromDate());
            boundaryCategory.setToDate(categoryObj.getToDate());
            
            
            if (catFromDb != null) {
                catFromDb.setIsHistory('Y');
                Date toDate = catFromDb.getToDate();
                if (toDate == null || (toDate != null && toDate.after(catFromDb.getFromDate()))) {
                    Date newToDate = DateUtils.addDays(category.getFromDate(), -1);
                    catFromDb.setToDate(newToDate);
                }
            }

            Set<BoundaryCategory> boundaryCategorySet = new HashSet<BoundaryCategory>();
            boundaryCategorySet.add(boundaryCategory);

            categoryObj.setCatBoundaries(boundaryCategorySet);
            getPersistenceService().persist(categoryObj);
            getPersistenceService().update(catFromDb);
            setAckMessage("Unit Rate is updated successfully!");
            return RESULT_ACK;
        }
    }

    @SkipValidation
    @Action(value = "/unitRate-deactivate")
    public String deactivate() {
        if (categoryId != null && categoryId != -1) {
            category = (Category) getPersistenceService().find("from Category where id = ?", categoryId);
            category.setIsActive(false);
            setAckMessage("Unit Rate deactivated successfully!");
            getPersistenceService().update(category);
        }
        return RESULT_ACK;
    }

    @Override
    public void validate() {
        if (zoneId == null || zoneId == -1) {
            addActionError(getText("unit.rate.zone.required"));
        }
        if (usageId == null || usageId == -1) {
            addActionError(getText("unit.rate.usage.required"));
        }
        if (structureClassId == null || structureClassId == -1) {
            addActionError(getText("unit.rate.structure.classification.required"));
        }
        if (category.getCategoryAmount() == null) {
            addActionError(getText("unit.rate.category.amount.required"));
        }
        if (category.getFromDate() == null) {
            addActionError(getText("unit.rate.fromDate.required"));
        }
    }

    public void validateSearch() {
        if (zoneId == null || zoneId == -1) {
            addActionError(getText("unit.rate.zone.required"));
        }
    }

    public Long getUsageId() {
        return usageId;
    }

    public void setUsageId(Long usageId) {
        this.usageId = usageId;
    }

    public Long getStructureClassId() {
        return structureClassId;
    }

    public void setStructureClassId(Long structureClassId) {
        this.structureClassId = structureClassId;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<BoundaryCategory> getBndryCatList() {
        return bndryCatList;
    }

    public void setBndryCatList(List<BoundaryCategory> bndryCatList) {
        this.bndryCatList = bndryCatList;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

}
