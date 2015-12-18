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
	  $(document).trigger("digitalDataRefresh"); // fire custom digitalDataRefresh event using jQuery. This can be observed by tag management 
		                                         // system using the jQuery .on() method. This is probably the most flexible option since 
	                                             // jQuery's .on() method is more extensive than Ensighten Events or the Ensighten Framework 
	                                             // Delegation / On Bootstrapper.on() method. Consider using this if option if your TMS users 
	                                             // are comfortable wrapping tags with jQuery code and you can use the jQuery library.
	                                             //
	                                             // Pros:
	                                             //
	                                             // Cons:
	                                             // - can't use Apps within Ensighten
	                                             // - may or may not be able to use jQuery
		
	  // option 2
	  Bootstrapper.ensEvent.trigger("digitalDataRefresh"); // fire custom digitalDataRefresh Ensighten event using Ensighten's proprietary Bootstrapper.ensEvent.trigger() method. 
	                                                       // This can be observed by tag management system by using Ensighten's Events:Named Events feature. Once Events are
	                                                       // created that correspond with the event names implemented by the development team, TMS users can use those Events as
	                                                       // Conditions when deploying tag code instead of requiring them to manually wrap their tags with code. Supposedly this
	                                                       // option also allows tag code to be wrapped by Boostrapper.on() method (Ensighten Framework Delegation / On) but I
	                                                       // personally cannot get this work to with AngularJS, possibly because Delegation / On relies on CSS selectors, and 
	                                                       // there is no CSS to select in this particular case.
	                                                       //
                                                           // Pros:
                                                           //
                                                           // Cons:
                                                           //
	  
	  // option 2a
	  // wrapper function
	  // global postpageload-handling function(event, data) {
	  //   console.log(out);
	  // }
	  //
	  // use TMS deployment to overwrite postpageload-handling(event, data) {
	  //   // put whatever tags/deployment code you want here (including Ensighten Bootstrapper.ensEvent.trigger() or other vendor-specific code)
	  // }
	  
	  // option 2b
	  // overwrite push method for array.push() using prototype to add events, etc.
	  //
	  // path 1 - enable Manage App use
	  //          trigger Named Event, then Data Def / App pulls from most recent digitalData.event[]
	  // 
	  // path 2 - all custom JS, no Manage App use
	  //          trigger event, then Data Def / App pulls from most recent digitalData.event[]
	  // 
	  
	  // option 3
	  //window.digitalDataLastUpdate = new Date(); // update digitalDataLastUpdate variable. This variable can be watched by the tag management system using Ensighten's Events:
	                                               // Value Changes feature. This feature polls so there are performance implications to consider, but it does also allow TMS users
	                                               // to use Events as Conditions when deploying tag code instead of requiring them to manually wrap their tags with code.
                                                   //
                                                   // Pros:
                                                   //
                                                   // Cons:
                                                   //
	  
	  // option 4
	  //Ensighten Framework Property Watcher Helper // more info is available at https://success.ensighten.com/hc/en-us/articles/201720700-Helpers-Property-Watcher. This option 
	                                                // is not built out as it is very similar to option 3 but Bootstrapper.propertyWatcher is attached to window.digitalDataLastUpdate,
	                                                // configured, and polls for changes. Either the tag code can be deployed directly in response to the change as part of the 
	                                                // Bootstrapper.propertyWatcher configuration, or a custom event can be triggered and observed by jQuery or Ensighten.
	                                                // 
	                                                // Pros:
	                                                //
	                                                // Cons:
	                                                // 
	  
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
		  window.digitalData.event.push(event); // update digitalData.event array. We stash the event object in the 
		                                        // digitalData.event array so that 1) we have page-level event 
		                                        // persistence in case calculations need to be performed amongst a
		                                        // (limited) set of events and 2) we are not reliant on the event
		                                        // to send the data (those this may be desired).
		                                        //
		                                        // Note we are not updating any of 
		                                        // the digitalData.cart subobject here as a design choice, though we 
		                                        // could. The digitalData.cart subobject will, however, reflect the 
		                                        // addition of this product the next time the entire digitalData
		                                        // object is requested and returned from the server-side.
		  $(document).trigger("digitalDataEvent"); // fire custom digitalDataEvent event using jQuery
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
