var testApp = angular.module('typeaheadDirectiveSample', [ 'lovTypeahead' ]);

testApp.controller('Ctrl', function($scope) {
	$scope.country = {
		id : null,
		name : "No country selected"
	};
	$scope.dataName = "data";
	$scope.datasetIsValid = true;

	$scope.address = {
		country : null
	};

});

testApp.controller('UpdateTestCtrl', function($scope) {
	$scope.address = {
		country : {
			id : 222,
			name : "Initial Value"
		}
	};
});
