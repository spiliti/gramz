insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AjaxDesignationsByDepartmentWithDesignation',
'/ajaxWorkFlow-getDesignationsByObjectTypeAndDesignation',null,(select id from eg_module where name='EIS-COMMON'),1,'AjaxDesignationsByDepartmentWithDesignation',false,'eis',0,1,now(),1,now(),(select id from eg_module where name='EIS'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AjaxDesignationsByDepartmentWithDesignation'),id from eg_role where name in ('ULB Operator','Super User','Water Tax Approver','Bill Approver','Advertisement Tax Creator'
'Works Creator','Works Approver','Property Verifier','Property Approver','Sewerage Tax Approver','Sewerage Tax Creator','Council Management Creator','Council Management Approver');