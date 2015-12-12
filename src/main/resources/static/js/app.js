angular.module('DataService', []) // service to retrieve data + digitalData from server-side
  .factory('DataHttp', ['$http', function($http){
    var Data = {};
    Data.get = function(pageName){
		return $http.get('/data/'+pageName);
	};
    return Data;
  }]);

angular.module('app', ['ngRoute', 'DataService']) // primary application module
  .config(['$routeProvider', function($routeProvider){
	$routeProvider // route changes should always trigger a call to the server to load the full digitalData object
	  .when('/', {
		templateUrl: 'partials/home.html',
		controller: 'home-controller',
		resolve: {
		  response: ['DataHttp', function(DataHttp){ // call to server for digitalData
			return DataHttp.get('home');
		  }]
		}
	  })
	  .when('/products', {
		templateUrl: 'partials/products/list.html',
		controller: 'products-controller',
		resolve: {
		  response: ['DataHttp', '$route', function(DataHttp, $route){
			return DataHttp.get('products');
		  }]
		}
	  })
	  .when('/products/category/:category', {
		templateUrl: 'partials/products/listByCategory.html',
		controller: 'products-by-category-controller',
		resolve: {
		  response: ['DataHttp', '$route', function(DataHttp, $route){
			return DataHttp.get('products/category/'+$route.current.params.category);
		  }]
		}
	  })
	  .when('/products/:id', {
		templateUrl: 'partials/products/view.html',
		controller: 'product-controller',
		resolve: {
		  response: ['DataHttp', '$route', function(DataHttp, $route){
			return DataHttp.get('products/'+$route.current.params.id);
		  }]
		}
	  })
  }])
  .controller('app-controller', ['$scope', function($scope) { // primary application controller
	console.log('AngularJS::app-controller::app-controller loaded');
  }])
  .controller('home-controller', ['$rootScope', '$scope', 'response', function($rootScope, $scope, response) { // controller to demo "home"-type digitalData functionality
	console.log('AngularJS::home-controller::home-controller loaded');
	
	$rootScope.isAuthenticated = response.data.isAuthenticated;
	$rootScope.cartId = response.data.cartId;
	$rootScope.cartSize = response.data.cartSize;
	
	window.digitalData = JSON.parse(response.data.digitalData); // parse and assign fresh digitalData object
	
	// make client-side updates to digitalData
	window.digitalData.page.pageInfo.destinationURL = document.location.toString();
	window.digitalData.page.pageInfo.referringURL = document.referrer.toString();
	window.digitalData.page.pageInfo.breadcrumbs = document.location.pathname.split('/');
	
	console.log('AngularJS::home-controller::digitalData loaded, assigned, and updated');
	
	// option 1
	$(document).trigger("digitalDataRefresh", {id:"0", name:"digitalDataRefresh"}); // fire custom digitalDataRefresh event using. This can be observed by tag management 
	                                                                  // system using Delegate / On Ensighten Framework or jQuery On method. This 
	                                                                  // is probably the most flexible option.
	
	// option 2
	//Bootstrapper.ensEvent.trigger("digitalDataRefresh", {id:"0", name:"digitalDataRefresh"}); // fire custom digitalDataRefresh Ensighten event using 
	                                                                              // Ensighten's proprietry event code. This can be observed by tag
	                                                                              // management system using Delegate / On Ensighten Framework or jQuery 
	                                                                              // On method. This is probably the most flexible option.
	
	// option 3
	//window.digitalDataLastUpdate = new Date(); // update digitalDataLastUpdate variable. This variable can be watched by the tag management system
	                                             // using Events:Value Changes type. This option polls.
  }])
  .controller('products-controller', ['$rootScope', '$scope', 'response', function($rootScope, $scope, response) {
	console.log('AngularJS::products-controller::products-controller loaded');
	  
	$rootScope.isAuthenticated = response.data.isAuthenticated;
	$rootScope.cartId = response.data.cartId;
	$rootScope.cartSize = response.data.cartSize;
		
	window.digitalData = JSON.parse(response.data.digitalData);
		
	window.digitalData.page.pageInfo.destinationURL = document.location.toString();
	window.digitalData.page.pageInfo.referringURL = document.referrer.toString();
	window.digitalData.page.pageInfo.breadcrumbs = document.location.pathname.split('/');
		
	console.log('AngularJS::products-controller::digitalData loaded, assigned, and updated');
		
	$(document).trigger("digitalDataRefresh", {id:"0", name:"digitalDataRefresh"});
	
	$scope.products = response.data.products;
  }])
  .controller('products-by-category-controller', ['$rootScope', '$scope', 'response', function($rootScope, $scope, response) {
	console.log('AngularJS::products-by-category-controller::products-by-category-controller loaded');
	  
	$rootScope.isAuthenticated = response.data.isAuthenticated;
	$rootScope.cartId = response.data.cartId;
	$rootScope.cartSize = response.data.cartSize;
		
	window.digitalData = JSON.parse(response.data.digitalData);
		
	window.digitalData.page.pageInfo.destinationURL = document.location.toString();
	window.digitalData.page.pageInfo.referringURL = document.referrer.toString();
	window.digitalData.page.pageInfo.breadcrumbs = document.location.pathname.split('/');
		
	console.log('AngularJS::products-by-category-controller::digitalData loaded, assigned, and updated');
		
	$(document).trigger("digitalDataRefresh", {id:"0", name:"digitalDataRefresh"});
	
	$scope.products = response.data.products;
  }])
  .controller('product-controller', ['$rootScope', '$scope', 'response', function($rootScope, $scope, response) {
	console.log('AngularJS::product-controller::product-controller loaded');
	  
	$rootScope.isAuthenticated = response.data.isAuthenticated;
	$rootScope.cartId = response.data.cartId;
	$rootScope.cartSize = response.data.cartSize;
		
	window.digitalData = JSON.parse(response.data.digitalData);
		
	window.digitalData.page.pageInfo.destinationURL = document.location.toString();
	window.digitalData.page.pageInfo.referringURL = document.referrer.toString();
	window.digitalData.page.pageInfo.breadcrumbs = document.location.pathname.split('/');
		
	console.log('AngularJS::product-controller::digitalData loaded, assigned, and updated');
		
	$(document).trigger("digitalDataRefresh", {id:"0", name:"digitalDataRefresh"});
	
	$scope.product = response.data.product;
  }]);
