update EG_ACTION set  ordernumber =1 where name='ApplicationProcessTimeMaster' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =2 where name='CategoryMaster' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =3 where name='ChairPersonDetailsScreen' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =4 where name='DonationMasterDetailsScreen' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =5 where name='PipeSizeMaster' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =6 where name='DocumentNamesMaster' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =7 where name='Meter Cost Master' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =8 where name='WaterRatesDetailsMaster' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =9 where name='Property Category' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =10 where name='Property Usage' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =11 where name='Property Pipe Size' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =12 where name='UsageTypeMaster' and contextroot = 'wtms';
update EG_ACTION set  ordernumber =13 where name='Water Source Type' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/applicationProcessTime/list',enabled = true , displayname ='View Application Process Time Master' , ordernumber =14  where name='ApplicationProcessTimeList' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/categoryMaster/list' ,enabled = true , displayname ='View Category Type Master' ,ordernumber =15  where name='CategoryMasterList' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/documentNamesMaster/list' ,enabled = true , displayname ='View Document Names Master', ordernumber =16 where name='DocumentNamesList' and contextroot = 'wtms';
update EG_ACTION set enabled = true , displayname ='View Donation Master' ,ordernumber =17 where name ='viewDonationMaster' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/meterCostMaster/list',enabled = true , displayname ='View Meter Cost Master' , ordernumber =18 where name='MeterCostMasterList' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/pipesizeMaster/list' ,enabled = true , displayname ='View PipeSize Master' , ordernumber =19 where name='PipeSizeList' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/propertyCategoryMaster/list',enabled = true , displayname ='View Property Category' , ordernumber =20 where name='PropertyCategoryList' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/propertyPipeSizeMaster/list',enabled = true , displayname ='View Property PipeSize' , ordernumber =21 where name='PropertyPipeSizeList' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/propertyUsageMaster/list',enabled = true , displayname ='View Property Usage',ordernumber =22 where name='PropertyUsageList' and contextroot = 'wtms';
update EG_ACTION set url = '/masters/usageTypeMaster/list' ,enabled = true , displayname ='View Usage Type Master' ,ordernumber =23 where name ='viewUsageTypeMaster' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/waterRatesMaster/list/',enabled = true , displayname ='View Water Rates Master',ordernumber =24 where name='viewWaterRatesMaster' and contextroot = 'wtms';
update EG_ACTION set  url = '/masters/waterSourceTypeMaster/list',enabled = true , displayname ='View Water Source Master', ordernumber =25 where name='WaterSourceTypeList' and contextroot = 'wtms';