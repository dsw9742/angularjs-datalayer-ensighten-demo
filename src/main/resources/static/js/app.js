angular.module('DigitalDataService', []) // service to retrieve digitalData from server-side
  .factory('DigitalDataHttp', ['$http', function($http){
    var DigitalData = {};
    DigitalData.get = function(pageName){
		return $http.get('/digitaldata/'+pageName);
	};
    return DigitalData;
  }]);

angular.module('app', ['ngRoute', 'DigitalDataService']) // primary application module
  .config(['$routeProvider', function($routeProvider){
	$routeProvider // route changes should always trigger a call to the server to load the full digitalData object
	  .when('/', {
		templateUrl: 'partials/home.html',
		controller: 'home-controller',
		resolve: {
		  digitalData: ['DigitalDataHttp', function(DigitalDataHttp){ // call to server for digitalData
			return DigitalDataHttp.get('home');
		  }]
		}
	  })
  }])
  .controller('app-controller', ['$scope', function($scope) { // primary application controller
	console.log('AngularJS::app-controller::app-controller loaded');
  }])
  .controller('home-controller', ['$scope', 'digitalData', function($scope, digitalData) { // controller to demo "home"-type digitalData functionality
	console.log('AngularJS::home-controller::home-controller loaded');
	
	window.digitalData = digitalData.data; // assign fresh digitalData object
	
	// make client-side updates to digitalData
	window.digitalData.page.pageInfo.destinationURL = document.location.toString();
	window.digitalData.page.pageInfo.referringURL = document.referrer.toString();
	window.digitalData.page.pageInfo.breadcrumbs = document.location.pathname.split('/');
	
	console.log('AngularJS::home-controller::digitalData loaded');
	
	// option 1
	$(document).trigger("digitalDataRefresh", {id:"0", name:"test"}); // fire custom digitalDataRefresh event using. This can be observed by tag management 
	                                                                  // system using Delegate / On Ensighten Framework or jQuery On method. This 
	                                                                  // is probably the most flexible option.
	
	// option 2
	//Bootstrapper.ensEvent.trigger("digitalDataRefresh", {id:"0", name:"test"}); // fire custom digitalDataRefresh Ensighten event using 
	                                                                              // Ensighten's proprietry event code. This can be observed by tag
	                                                                              // management system using Delegate / On Ensighten Framework or jQuery 
	                                                                              // On method. This is probably the most flexible option.
	
	// option 3
	//window.digitalDataLastUpdate = new Date(); // update digitalDataLastUpdate variable. This variable can be watched by the tag management system
	                                             // using Events:Value Changes type. This option polls.
  }]);