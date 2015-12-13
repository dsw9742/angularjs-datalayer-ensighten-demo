angular.module('DataService', []) // service to retrieve data from server-side and work with it client-side
  .factory('Data', ['$http', '$rootScope', function($http, $rootScope){
    var Data = {};
    Data.get = function(pageName){ // factory function to retrieve data from server-side
	  return $http.get('/data/'+pageName);
	};
	Data.addToCart = function(cartId, productId){ // factory function to add product to shopping cart
	  return $http.put('/data/carts/'+cartId+'/addToCart/'+productId);
	};
	Data.removeFromCart = function(cartId, productId){ // factory function to remove product from shopping cart
	  return $http.put('/data/carts/'+cartId+'/removeFromCart/'+productId);
	};
	Data.complete = function(formData){  // factory function to purchase products in shopping cart
	  return $http.post('/data/orders/complete/', formData);
	}
	Data.setRootScopeVars = function(response){ // factory function to set some $rootScope variables
	  $rootScope.isAuthenticated = response.data.isAuthenticated;
	  $rootScope.cartId = response.data.cartId;
	  $rootScope.cartSize = response.data.cartSize;
	};
	Data.setDigitalData = function(response, controllerName){ // factory function to set digitalData object and notify tag management system
	  window.digitalData = JSON.parse(response.data.digitalData); // parse and assign fresh digitalData object
		
	  // make client-side updates to digitalData
	  window.digitalData.page.pageInfo.destinationURL = document.location.toString();
	  window.digitalData.page.pageInfo.referringURL = document.referrer.toString();
	  window.digitalData.page.pageInfo.breadcrumbs = document.location.pathname.split('/');
		
	  console.log('AngularJS::%s::digitalData loaded, assigned, and updated',controllerName);
		
	  // option 1
	  $(document).trigger("digitalDataRefresh", {id:"0", name:"digitalDataRefresh"}); // fire custom digitalDataRefresh event using jQuery. This can be observed by tag management 
		                                                                  			  // system using Delegate / On Ensighten Framework or jQuery On method. This 
		                                                                  			  // is probably the most flexible option.
		
	  // option 2
	  //Bootstrapper.ensEvent.trigger("digitalDataRefresh", {id:"0", name:"digitalDataRefresh"}); // fire custom digitalDataRefresh Ensighten event using 
		                                                                              			  // Ensighten's proprietary event code. This can be observed by tag
		                                                                              			  // management system using Delegate / On Ensighten Framework or jQuery 
		                                                                              			  // On method. This is probably the most flexible option.
		
	  // option 3
	  //window.digitalDataLastUpdate = new Date(); // update digitalDataLastUpdate variable. This variable can be watched by the tag management system
		                                           // using Events:Value Changes type. This option polls.
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
		  response: ['Data', function(Data){ // call to server for digitalData
			return Data.get('home');
		  }]
		}
	  })
	  .when('/products', {
		templateUrl: 'partials/products/list.html',
		controller: 'products-controller',
		resolve: {
		  response: ['Data', '$route', function(Data, $route){
			return Data.get('products');
		  }]
		}
	  })
	  .when('/products/category/:category', {
		templateUrl: 'partials/products/listByCategory.html',
		controller: 'products-by-category-controller',
		resolve: {
		  response: ['Data', '$route', function(Data, $route){
			return Data.get('products/category/'+$route.current.params.category);
		  }]
		}
	  })
	  .when('/products/:id', {
		templateUrl: 'partials/products/view.html',
		controller: 'product-controller',
		resolve: {
		  response: ['Data', '$route', function(Data, $route){
			return Data.get('products/'+$route.current.params.id);
		  }]
		}
	  })
	  .when('/carts/:id', {
		templateUrl: 'partials/carts/view.html',
		controller: 'cart-controller',
		resolve: {
		  response: ['Data', '$route', function(Data, $route){
			return Data.get('carts/'+$route.current.params.id);
		  }]
		}
	  })
	  .when('/orders/checkout', {
		templateUrl: 'partials/orders/checkout.html',
		controller: 'order-checkout-controller',
		resolve: {
		  response: ['Data', function(Data){
			return Data.get('orders/checkout');
		  }]
		}
	  })
	  .when('/orders/complete', {
		templateUrl: 'partials/orders/complete.html',
		controller: 'order-complete-controller',
		resolve: {
		  response: ['$rootScope', function($rootScope){
			return $rootScope.orderData;
		  }]
		}
	  })
  }])
  .controller('app-controller', ['$scope', function($scope) { // primary application controller
	console.log('AngularJS::app-controller::app-controller loaded');
  }])
  .controller('home-controller', ['$scope', 'response', 'Data', function($scope, response, Data) { // controller to demo "home"-type digitalData functionality
	console.log('AngularJS::home-controller::controller loading');
	
	Data.setRootScopeVars(response);
	Data.setDigitalData(response, 'home-controller');

	console.log('AngularJS::home-controller::controller loaded');
  }])
  .controller('products-controller', ['$scope', 'response', 'Data', function($scope, response, Data) {
	console.log('AngularJS::products-controller::controller loading');

	Data.setRootScopeVars(response);
	Data.setDigitalData(response, 'products-controller');

	console.log('AngularJS::products-controller::controller loaded');
  }])
  .controller('products-by-category-controller', ['$scope', 'response', 'Data', function($scope, response, Data) {
	console.log('AngularJS::products-by-category-controller::controller loading');
	  
	Data.setRootScopeVars(response);
	Data.setDigitalData(response, 'products-by-category-controller');
	$scope.products = response.data.products;
	
	console.log('AngularJS::products-by-category-controller::controller loaded');
  }])
  .controller('product-controller', ['$rootScope', '$scope', 'response', 'Data', function($rootScope, $scope, response, Data) {
	console.log('AngularJS::product-controller::controller loading');
	  
	Data.setRootScopeVars(response);
	Data.setDigitalData(response, 'product-controller');
	$scope.product = response.data.product;
	$scope.addToCart = function() {
	  Data.addToCart($rootScope.cartId, $scope.product.id)
	    .success(function(cart) {
		  $rootScope.cartSize = $rootScope.cartSize+1; // update UI
		  var event = {id:"1", name:"digitalDataEvent", type:"addToCart", data:cart}; // create event
		  window.digitalData.event.push(event); // update digitalData.event array. Note we are not updating any of 
		                                        // the digitalData.cart subobject here as a design choice, though we 
		                                        // could. The digitalData.cart subobject will, however, reflect the 
		                                        // addition of this product the next time the entire digitalData
		                                        // object is requested and returned from the server-side.
		  $(document).trigger("digitalDataEvent", event); // fire custom digitalDataEvent event using jQuery
	    });
	};
	
	console.log('AngularJS::product-controller::controller loaded');
  }])
  .controller('cart-controller', ['$scope', 'response', 'Data', function($scope, response, Data) {
	console.log('AngularJS::cart-controller::controller loading');
	  
	Data.setRootScopeVars(response);
	Data.setDigitalData(response, 'cart-controller');
	$scope.cart = response.data.cart;
	
	console.log('AngularJS::cart-controller::controller loaded');
  }])
  .controller('order-checkout-controller', ['$rootScope', '$scope', 'response', 'Data', '$location', function($rootScope, $scope, response, Data, $location) {
	console.log('AngularJS::order-checkout-controller::controller loading');
		  
	Data.setRootScopeVars(response);
	Data.setDigitalData(response, 'order-checkout-controller');
	$scope.formData = {};
	$scope.submit = function() {
	  Data.complete($scope.formData)
	    .success(function(data) {
	      $rootScope.orderData = data;
	      $location.path('/orders/complete');
	    });
	};
		
	console.log('AngularJS::order-checkout-controller::controller loaded');
  }])
  .controller('order-complete-controller', ['$rootScope', '$scope', 'response', 'Data', function($rootScope, $scope, response, Data) {
	console.log('AngularJS::order-complete-controller::controller loading');
	Data.setRootScopeVars({data:response});
	Data.setDigitalData({data:response}, 'order-complete-controller');
	$scope.order = response.order;
	$scope.$on('$locationChangeStart', function() {
	  delete $rootScope.orderData;
	});
	console.log('AngularJS::order-complete-controller::controller loaded');
  }]);
