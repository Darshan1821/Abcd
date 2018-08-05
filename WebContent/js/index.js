var dayCareApp = angular.module('dayCareApp', [ 'ngRoute',
		'angularUtils.directives.dirPagination' ]);

dayCareApp.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'home.html',
	}).when('/home', {
		templateUrl : 'home.html',
		controller : 'BabySitterController'
	}).when('/babySitterRegistration', {
		templateUrl : 'babySitterRegistration.html',
		controller : 'BabySitterController'
	}).when('/toddlerRegistration', {
		templateUrl : 'toddlerRegistration.html',
		controller : 'ToddlerController'
	}).when('/viewAllToddlers', {
		templateUrl : 'viewAllToddlers.html',
		controller : 'ViewController'
	}).when('/updateToddler/:id', {
		templateUrl : 'updateToddler.html',
		controller : 'UpdateController'
	}).when('/success/:message', {
		templateUrl : 'success.html',
		controller : 'SuccessController'
	}).when('/error/:message', {
		templateUrl : 'error.html',
		controller : 'ErrorController'
	}).when('/dberror/:message', {
		templateUrl : 'db-error.html',
		controller : 'DBErrorController'
	}).otherwise({
		redirectTo : "/home"
	})
})

dayCareApp.controller('BabySitterController',
		function($scope, $http, $location) {

			$scope.bsname = "";
			$scope.bsage = "";
			$scope.bsexp = "";
			$scope.bstype = "";

			$scope.registerBabySitter = function() {
				var babySitter = new Object();
				babySitter.bsName = $scope.bsname;
				babySitter.bsAge = $scope.bsage;
				babySitter.bseEperience = $scope.bsexp;
				babySitter.bsType = $scope.bstype;

				$http({
					method : 'post',
					url : 'Controller?action=registerBabySitter',
					dataType : 'JSON',
					data : JSON.stringify(babySitter),
					contentType : 'application/json',
					mimeType : 'application/json',
				}).then(function(result) {
					if (result.status == 200) {
						$location.path("/success/" + result.data);
					}
				}, function(error) {
					if (error.status == 400) {
						$location.path("/error/" + error.data);
					}else if(error.status == 503){
						$location.path("/dberror/" + error.data);
					}
				})
			}
		})

dayCareApp.controller('ToddlerController', function($scope, $http, $location) {
	$scope.babySitters = [];

	$http({
		method : 'GET',
		url : 'Controller?action=getBabySitters'
	}).then(function(result) {
		$scope.babySitters = result.data;
	}, function(error) {
		if (error.status == 400) {
			$location.path("/error/" + error.data);
		}else if(error.status == 503){
			$location.path("/dberror/" + error.data);
		}
	})

	$scope.registerToddler = function() {
		var toddler = new Object();
		toddler.toodlerName = $scope.tdname;
		toddler.toddlerAge = $scope.tdage;

		var babySitter = new Object();
		babySitter.bsId = $scope.bsname;

		toddler.babySitter = babySitter;

		$http({
			method : 'post',
			url : 'Controller?action=registerToddler',
			dataType : 'JSON',
			data : JSON.stringify(toddler),
			contentType : 'application/json',
			mimeType : 'application/json',
		}).then(function(result) {
			if (result.status == 200) {
				$location.path("/success/" + result.data);
			}
		}, function(error) {
			if (error.status == 400) {
				$location.path("/error/" + error.data);
			}else if(error.status == 503){
				$location.path("/dberror/" + error.data);
			}
		})
	}
})

dayCareApp.controller('UpdateController', function($scope, $http, $location,
		$routeParams) {
	$scope.babySitters = [];

	$scope.tdid = "";
	$scope.tdname = "";
	$scope.tdage = "";
	$scope.bsname = "";

	$http({
		method : 'GET',
		url : 'Controller?action=getToddler&tdId=' + $routeParams.id
	}).then(function(result) {
		$scope.tdid = result.data.toddlerId;
		$scope.tdname = result.data.toodlerName;
		$scope.tdage = result.data.toddlerAge;
	}, function(error) {
		if (error.status == 400) {
			$location.path("/error/" + error.data);
		}else if(error.status == 503){
			$location.path("/dberror/" + error.data);
		}
	})

	$http({
		method : 'GET',
		url : 'Controller?action=getBabySitters'
	}).then(function(result) {
		$scope.babySitters = result.data;
	}, function(error) {
		if (error.status == 400) {
			$location.path("/error/" + error.data);
		}else if(error.status == 503){
			$location.path("/dberror/" + error.data);
		}
	})

	$scope.updateToddler = function() {
		var toddler = new Object();
		toddler.toddlerId = $scope.tdid;
		toddler.toodlerName = $scope.tdname;

		var babySitter = new Object();
		babySitter.bsId = $scope.bsname;

		toddler.babySitter = babySitter;

		$http({
			method : 'post',
			url : 'Controller?action=updateToddler',
			dataType : 'JSON',
			data : JSON.stringify(toddler),
			contentType : 'application/json',
			mimeType : 'application/json',
		}).then(function(result) {
			if (result.status == 200) {
				$location.path("/success/" + result.data);
			}
		}, function(error) {
			if (error.status == 400) {
				$location.path("/error/" + error.data);
			}else if(error.status == 503){
				$location.path("/dberror/" + error.data);
			}
		})
	}
})

dayCareApp.controller('ViewController', function($scope, $http, $location) {

	$scope.toddlers = [];

	$scope.sort = function(keyname) {
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}

	$http({
		method : 'GET',
		url : 'Controller?action=getAllToddlers'
	}).then(function(result) {
		$scope.toddlers = result.data;
		console.log($scope.toddlers);
	}, function(error) {
		if (error.status == 400) {
			$location.path("/error/" + error.data);
		}else if(error.status == 503){
			$location.path("/dberror/" + error.data);
		}
	})
})

dayCareApp.controller('SuccessController', function($scope, $timeout,
		$routeParams, $location) {
	$scope.message = $routeParams.message;

	$timeout(function() {
		$scope.timeOutMessage = "Redirecting to home page...";
	}, 3000);

	$timeout(function() {
		$location.path("/home");
	}, 6000);
})

dayCareApp.controller('ErrorController', function($scope, $timeout,
		$routeParams, $location) {
	$scope.message = $routeParams.message;

	$timeout(function() {
		$scope.timeOutMessage = "Redirecting to home page...";
	}, 3000);

	$timeout(function() {
		$location.path("/home");
	}, 6000);
})

dayCareApp.controller('DBErrorController', function($scope, $timeout,
		$routeParams, $location) {
	$scope.message = $routeParams.message;
})