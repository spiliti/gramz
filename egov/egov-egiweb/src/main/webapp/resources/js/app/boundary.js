/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

function populateBoundaryTypes(dropdown) {
	populateboundaryTypeSelect({
		hierarchyTypeId : dropdown.value
	});
}

function populateBoundaries(dropdown) {
	populateboundaries({
		boundaryTypeId : dropdown.value
	});
}

$('#buttonView').click(function() {
	var pathvars = $("#hierarchyTypeSelect").val()+","+$("#boundaryTypeSelect").val();
	$('#boundarySearchForm').attr('action', 'list-boundaries/'+pathvars);
});

$('#buttonCreate').click(function() {
	var pathvars = $("#hierarchyTypeSelect").val()+","+$("#boundaryTypeSelect").val();
	$('#boundarySearchForm').attr('action', 'create-boundary/'+pathvars);
});

$('#buttonUpdate').click(function() {
	$('#boundarySearchForm').attr('action', 'update-boundary');
	$('#boundarySearchForm').attr('method', 'get');
});

/**
 * Ajax validation to check for child boundary type for a parent  
 */
function checkForChild(){
	var id = $("#boundaryTypeSelect").val();
	if(id ==''){
		bootbox.alert('Please select the Boundary Type !');
		return false;
	}
	else{
		$.ajax({
			type: "GET",
			url: "ajax/checkchild",
			data:{'parentId' : id },
			dataType: "json",
			success: function (response) {
				if(response == true){
					bootbox.alert('Child already exists!');
					return false;
				}
				else{
					$("#boundaryTypeSearch").submit();
					return true;
				}
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}
} 

$('#boundaryTypeCreateBtn').click(function() {
	$('#boundaryTypeSuccess').attr('method', 'get');
	$('#boundaryTypeSuccess').attr('action', '/egi/boundarytype/create');
});

$('#boundaryTypeUpdateBtn').click(function() {
	var url = '/egi/boundarytype/update/'+ $('#boundaryTypeId').val();
	$('#boundaryTypeSuccess').attr('method', 'get');
	$('#boundaryTypeSuccess').attr('action', url);
});

$('#buttonCreate').click(function() {
	var pathVars = "/" + $('#btnHierarchyType').val() + "," + $('#btnBoundaryType').val();
	$('#boundaryViewForm').attr('action', '/egi/create-boundary'+pathVars);
});

$('#buttonEdit').click(function() {
	var pathVars = "/" + $('#btnHierarchyType').val() + "," + $('#btnBoundaryType').val() + "," + $('#btnBoundary').val();
	$('#boundaryViewForm').attr('method', 'get');
	$('#boundaryViewForm').attr('action', '/egi/update-boundary'+pathVars);
});

$('#backBtnId').click(function() {
	window.location = '/egi/search-boundary';
});

function checkForRootNode() {
	var hierarchyTypeId = $('#hierarchyTypeSelect').val();
	var boundaryTypeId = $('#boundaryTypeSelect').val();
	
	$.ajax({
		type: "GET",
		url: "check-is-root",
		data: { 
			'hierarchyTypeId' : hierarchyTypeId,
			'boundaryTypeId' : boundaryTypeId
		},
		dataType: "json",
		success: function (response) {
			if(response == false){
				bootbox.alert('Sorry! You can\'t create root for the Child Boundary!');
				return false;
			}
			return true;
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
}

function validateName(){
	var childName = $("#name").val();
	var parentName = $("#parent").val();
	if(childName == parentName){
		bootbox.alert('Child and parent boundary types cannot have the same name!');
		return false;
	}
	else{
		$("#boundaryTypeAddChildform").submit();
		return true;
	}
} 
