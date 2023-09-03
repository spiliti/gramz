INSERT INTO egmrs_registrationunit (id,name, code,address,ismainregistrationunit, zone,isactive,version,createdby,createddate,lastmodifiedby,lastmodifieddate) VALUES (nextval('SEQ_EGMRS_REGISTRATIONUNIT'), 'Main office', 'MAINOFFICE', 'Main office', false, (select  ID  from eg_boundary  where boundarytype in ( select id from eg_boundary_type  where hierarchytype  in (select id from eg_hierarchy_type where name='ADMINISTRATION')
and name='City') limit 1), true, 0, 1, current_timestamp, 1, current_timestamp);