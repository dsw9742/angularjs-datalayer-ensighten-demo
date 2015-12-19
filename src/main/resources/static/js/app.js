// uncomment these lines to set this function live
//window.postpageload = function(event, data) {
//  console.log('event: %o, data: $o', event, data);
//}

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
		
	  /***************************
	   * OPTION 1: DIRECT EVENTS *
	   **************************/
	  /*
	   * STEP 1: DEVELOPERS TRIGGER EVENT WITHIN APP CODE
	   */ 
	  /* STEP 1, OPTION A: fire custom digitalDataRefresh event using jQuery. */
	  $(document).trigger("digitalDataRefresh"); // app code, uncomment to set live
	  /* This can be observed by tag management system using the jQuery .on() method. This is 
	     probably the most flexible option since 
	     jQuery's .on() method is more extensive than Ensighten Events or the Ensighten Framework 
	     Delegation / On Bootstrapper.on() method. Consider using this if option if your TMS users 
	     are comfortable wrapping tags with jQuery code and you can use the jQuery library.
	       
	     Pros:
	     - simple for developers to implement
	     - lightweight
	     - simple design makes troubleshooting straight-forward
	     - can pass data payload directly to tags via event
	     - no hard-coded dependencies on vendor-specific code 
	     Cons:
	     - cannot use Apps within Ensighten Manage
	     - may or may not be able to use jQuery */
 
	  /* STEP 1, OPTION B: fire custom digitalDataRefresh Ensighten event using Ensighten proprietary code. */  
	  //Bootstrapper.ensEvent.trigger("digitalDataRefresh"); // app code, uncomment to set live
	  /* This can be observed by tag management system by using Ensighten Events:Named Events 
	     feature. Once Events are
         created that correspond with the event names implemented by the development team, TMS users 
         can use those Events as Conditions when deploying tag code instead of requiring them to 
         manually wrap their tags with code.
	                                                           
         Pros:
         - simple for developers to implement
         - lightweight
         - simple design makes troubleshooting straight-forward
	     - retains ability to use Apps within Ensighten Manage
         Cons:
	     - hard-coded dependency on vendor-specific code
	     - possible to miss hearing app events if they trigger before Ensighten Manage completely loads
         - cannot pass data payload directly to tags via event, so event data must be stored elsewhere (e.g. digitalData object) */
	  
	  /* 
	   * STEP 2: IMPLEMENT TAG DEPLOYMENT(S)
	   */
	  /* if you use STEP 1, OPTION A above: */
	     
	  /* "tags" deployment info:
	       app: Custom JS
	       tag code: wrap the tag code in $(document).on("digitalDataRefresh" [, selector] [, data], handler) method
	                 example:
	                 $(document).on("digitalDataRefresh", function(event, data) {
	                   console.log('Ensighten Manage::jQuery::digitalDataRefresh event observed.');
	                   console.log('Ensighten Manage::jQuery::digitalDataRefresh event %o',event);
	                   console.log('Ensighten Manage::jQuery::digitalDataRefresh data %o',data);
	                 });
	       condition: (synchronous) Global
	       dependencies: none
	       execution time: Immediate
	    
	  /* if you use STEP 1, OPTION B above: */
	  
	  /* event info:
	       name: "digitalDataRefresh"
	       conditions: as close to (synchronous) Global as possible
	       type: Named Event
	       
	     "tags" deployment info:
	       app: any supported manage App
	       tag code: no extra wrapping, etc. required
	                 example:
	                 console.log('Ensighten Manage::Named Event::digitalDataRefresh event observed.');
	       condition: named event (from above)
	       dependencies: none
	       execution time: Immediate */
	  
	  /*************************************
	   * OPTION 2: GLOBAL WRAPPER FUNCTION *
	   ************************************/
	  /*
	   * STEP 1: DEVELOPERS CALL A "DUMMY" GLOBAL FUNCTION FROM APP CODE
	   */ 
	  // window.postpageload(event, data);  // app code, uncomment to set live. Reference top of this app.js file. 
	  
	  /*
	   * STEP 2: USE A TAG MANAGEMENT SYSTEM DEPLOYMENT TO OVERWRITE THE "DUMMY" GLOBAL FUNCTION WITH "REAL" CODE
	   */
	  /* STEP 2, OPTION A: combo event switchboard w/ tags. */ 
	  /* "event switchboard and tags" deployment info:
	       app: Custom JS
	       tag code: overwrite dummy global function with real code
	                 example:
	                 window.postpageload(event, data) {
	                   switch (event.eventInfo.eventName) {
	                     case 'digitalDataRefresh':
	                       console.log('this could be tag 1 code and this data is available to it: event %o, data %o', event, data);
	                       console.log('this could be tag 2 code and this data is available to it: event %o, data %o', event, data);
	                       break;
	                   }
	                 }
	       condition: (synchronous) Global
	       dependencies: none
	       execution time: Immediate
	    
	     Pros:
	     - simple for developers to implement
	     - simple design makes troubleshooting straight-forward
	     - can pass data payload directly to tags via event
	     - no hard-coded dependencies on vendor-specific code 
	     Cons:
	     - cannot use Apps within Ensighten Manage
	     - maintaining all tag code in one deployment can lead to code clutter */ 
	  
	  /* STEP 2, OPTION B: separate event switchboard deployment, fires Ensighten named events, and tag deployments. */
	  /* "event switchboard" deployment info:
	     app: Custom JS
	       tag code: overwrite dummy global function with real code that fires events
	                 example:
	                 window.postpageload(event, data) {
	                   switch (event.eventInfo.eventName) {
	                     case 'digitalDataRefresh':
	                       Bootstrapper.ensEvent.trigger("digitalDataRefresh");
	                       break;
                         case 'digitalDataEvent':
	                       Bootstrapper.ensEvent.trigger("digitalDataEvent");
	                       break;
	                   }
	                 }
	       condition: (synchronous) Global
	       dependencies: none
	       execution time: Immediate
	    
	     events info: (using same example events from "event switchboard" deployment info, above)
	       name: "digitalDataRefresh"
	       conditions: as close to (synchronous) Global as possible
	       type: Named Event
	    
	       name: "digitalDataEvent"
	       conditions: as close to (synchronous) Global as possible
	       type: Named Event
	    
	     "tag" deployments info:
	       app: any supported manage App
	       tag code: no extra wrapping, etc. required
	                 example:
	                 console.log('this could be tag 1 code');
	       condition: named event (as appropriate, from event info, above)
	       dependencies: "event switchboard" deployment
	       execution time: Immediate
	    
	     Pros:
	     - simple for developers to implement
	     - no hard-coded dependencies on vendor-specific code 
	     - retains ability to use Apps within Ensighten Manage
	     Cons:
	     - complex design makes troubleshooting more difficult
	     - possible to miss hearing app events if they trigger before Ensighten Manage completely loads
         - cannot pass data payload directly to tags via event, so event data must be stored elsewhere (e.g. digitalData object) */ 
	  
	  /*********************
	   * OPTION 3: POLLING * 
	   ********************/ 
	  /*
	   * STEP 1: DEVELOPERS UPDATE GLOBAL VARIABLE
	   */
	  //window.eventUpdate = new Date(); // app code, uncomment to set live
	  
	  /*
	   * STEP 2: POLL GLOBAL VARIABLE FOR CHANGES
	   */
	  /* STEP 2, OPTION A: utilize Ensighten Manage Events: Value Changes feature. */
      /* data definition info:
           extractor: function() { return window.eventUpdate; }
           conditions: (synchronous) Global
           spaces: all
           persistence: Instance
           trigger: Custom
                    leave custom trigger code empty (this fires the data definition as quickly as possible)
          
         event info:
           name: "digitalDataRefresh"
	       conditions: as close to (synchronous) Global as possible
	       type: Value Changes
	       settings:
	         element to monitor: 
	         fire when first set: yes
	         fire every update: yes
         
         "tag" deployments info:
           app: any supported manage App
	       tag code: no extra wrapping, etc. required
	                 example:
	                 console.log('this could be tag 1 code');
	       condition: value changes event (as appropriate, from event info, above)
	       dependencies: none
	       execution time: Immediate
         
         Pros:
         - simple for developers to implement
	     - no hard-coded dependencies on vendor-specific code 
	     - retains ability to use Apps within Ensighten Manage
         Cons:
         - complex design makes troubleshooting more difficult
         - data definition must exist in every space in account
         - additional overhead from polling impacts app performance
         - possible to miss hearing events due to polling
         - possible to miss hearing app events if they trigger before Ensighten Manage completely loads
         - cannot pass data payload directly to tags via event, so event data must be stored elsewhere (e.g. digitalData object) */
	  
	  /* STEP 2, OPTION B: utilize Ensighten Framework Property Watcher Helper. */
	  /* Not going to go into detail here, but more info is available at 
	     https://success.ensighten.com/hc/en-us/articles/201720700-Helpers-Property-Watcher (account required).
	     
	     Basic idea is to create a "property watcher" deployment that wraps the window.eventUpdate property. When
	     the property updates, either 1) trigger Ensighten Named Events (which have to be built in the UI) and wire
	     up Ensighten Apps to those Events as Conditions, or 2) build the tag code directly into the deployment.  
	     
	     Pros:
         - simple for developers to implement
	     - no hard-coded dependencies on vendor-specific code 
	     - it *can* (1) retain ability to use Apps within Ensighten Manage
         Cons:
         - it *might* (2) prevent use of Apps within Ensighten Manage
         - complex design makes troubleshooting more difficult
         - data definition must exist in every space in account
         - additional overhead from polling impacts app performance
         - possible to miss hearing events due to polling
         - possible to miss hearing app events if they trigger before Ensighten Manage completely loads
         - cannot pass data payload directly to tags via event, so event data must be stored elsewhere (e.g. digitalData object) */
	  
	  /*******************************************************
	   * OPTION 4: OVERWRITE Array.prototype.push() FUNCTION *
	   ******************************************************/
      /*
       * STEP 1: DEPLOYMENT TO OVERWRITE Array.prototype.push() FUNCTION
       */
	  /* Array.prototype.push() overwrite deployment info:
	       app: Custom JS
	       tag code: overwrite Array.prototype.push() function with same code + fires Ensighten events
	                 example:
	                 Array.prototype.push = function() {
                       for(var i=0, l=arguments.length; i<l; i++) {
                         Bootstrapper.ensEvent.trigger("arrayPush"); 
                         this[this.length] = arguments[i];
                       }
                       return this.length;
                     };
	       condition: (synchronous) Global
	       dependencies: none
	       execution time: Immediate
	  
	  /*
	   * STEP 2: a) COMBO EVENT SWITCHBOARD W/ TAGS OR b) SEPARATE SWITCHBOARD/EVENTS/TAG DEPLOYMENTS
	   */
	  /*
	    See the above examples that demonstrate how a) or b) can be implemented.
	   
	    Pros:
	     - simple for developers to implement (once Array.prototype.push() has been overwritten)
		 - no hard-coded dependencies on vendor-specific code 
		 - it *can* (b) retain ability to use Apps within Ensighten Manage
	    Cons:
	     - requires overwriting the Array.prototype.push() browser JavaScript engine function
	     - it *might* (a) prevent use of Apps within Ensighten Manage
	     - complex design makes troubleshooting more difficult
	     - possible to miss hearing app events if they trigger before Ensighten Manage completely loads
	     - cannot pass data payload directly to tags via event, so event data must be stored elsewhere (e.g. digitalData object) */
	  
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
		  
		  // create event (we are following W3C CEDDL spec in this example, https://www.w3.org/community/custexpdata/)
		  var event = { 
		    eventInfo: {
			  eventName:"digitalDataEvent", 
			  eventAction:"addToCart",
			  // eventPoints: , // as a design choice, we don't use this property ...
			  // type: , // ... or this
			  timeStamp: new Date(),
			  // cause: , // ... or this
			  // effect: , // ... or this
			},
			category: {
		      primaryCategory:"cart",	  
		    },
			attributes: { // custom attributes
		      data:cart, // as a design choice, we elect to pass server-side payload to a property called "data"
		    },
		  };
		  
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
		  
		  $(document).trigger("digitalDataEvent"); // fire custom digitalDataEvent event using jQuery (option 1).
		                                           // since we're using jQuery we could also bypass the entire step
		                                           // above where we stash the event in the digitalData.event[]
		                                           // array and pass the event object as the second parameter in
		                                           // this call instead, i.e. 
		                                           // $(document).trigger("digitalDataEvent", event);
		  
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
