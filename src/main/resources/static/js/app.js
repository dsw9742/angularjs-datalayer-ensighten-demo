angular.module('app', ['ngRoute']) // primary application module
  .config(['$routeProvider', function($routeProvider){
	$routeProvider // route changes should always trigger a call to the server to load the full digitalData object
	  .when('/', {
		templateUrl: 'partials/home.html',
		controller: 'home-controller',
		//resolve: {
		//  digitalData: ['DigitalDataHttp', function(DigitalDataHttp){ // call to server for digitalData
		//	return DigitalDataHttp.get('home');
		//  }]
		//}
	  })
  }])
  .controller('app-controller', ['$scope', function($scope) { // primary application controller
	console.log('AngularJS::app-controller::app-controller loaded');
  }])
  .controller('home-controller', ['$scope', function($scope) { // controller to demo "home"-type digitalData functionality
	console.log('AngularJS::home-controller::home-controller loaded');
	// digitalData.data.page.pageInfo.destinationURL = "http://localhost:8080/#/home"; // if desired, any client-side updates to the digitalData object can be made HERE 
	                                                                                   // (commented out because we don't actually want to do this in this case)
	//window.digitalData = digitalData.data; // assign fresh digitalData object
	//window.digitalDataLastUpdate = new Date(); // update digitalDataLastUpdate. This variable is watched by the tag management system.
  }]);