var app = angular.module('springBootFileUploaderApp', []);

app.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);

app.controller('fileUploadController', ['$scope', '$http', function($scope, $http){
    $scope.doUploadFile = function(){
       var file = $scope.uploadedFile;
       var url = "/fileUploaderApi/uploadfile";
       
       var data = new FormData();
       data.append('file', file);
    
       var config = { transformRequest: angular.identity,
    	   					transformResponse: angular.identity,
    	   					headers : { 'Content-Type': undefined }
       }
       
       $http.post(url, data, config).then( function (response) {
			$scope.uploadResult=response.data;
		}, function (response) {
			alert("File not selected or size exceeded 2MB");
		});
    };
}]);

app.controller('getFilesController', function($scope, $http) {
	$scope.search = function() {
		$http.get("/fileUploaderApi/files").then(function (response) {
			$scope.metadataList = response.data;
		}, function (response) {
			alert(response.data);
		});
	};
});
