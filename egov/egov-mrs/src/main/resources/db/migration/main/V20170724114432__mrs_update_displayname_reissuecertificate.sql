update eg_action set displayname ='ReIssue Marriage Certificate' where  name='ReIssue Marriage Certificate' and application=(select id from eg_module where name='Marriage Registration');