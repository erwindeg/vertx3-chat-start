'use strict';

/**
 * @ngdoc function
 * @name resourcesApp.controller:MainCtrl
 * @description # MainCtrl Controller of the resourcesApp
 */
angular.module('resourcesApp').controller('MainCtrl',
		function($scope, $resource) {
			$scope.messages = $resource('/api/history').query();

			$scope.sendMessage = function() {
				$scope.message.date = Date.now();
				$scope.messages.push($scope.message);
			};
		});
