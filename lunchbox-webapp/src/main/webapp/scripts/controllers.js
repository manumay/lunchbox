var controllersModule = angular.module('lunchbox-controllers', ['lunchbox-services']);

controllersModule.controller('LoginController', function LoginController($scope, $location) {

	$scope.loginname = '';
	$scope.password = '';
	$scope.alerts = [];
	
	var err = $location.search().error;
	if (err != undefined && err == 1) {
		var errorAlert =  { type: 'error', msg: 'Warnung! Anmeldung fehlgeschlagen.' };
		$scope.alerts.push(errorAlert);
	}
	
	var logout = $location.search().logout;
	if (logout != undefined && logout == 1) {
		var logoutAlert =  { type: 'success', msg: 'Abgemeldet.' };
		$scope.alerts.push(logoutAlert);
	}

	$scope.closeAlert = function(index) {
	    $scope.alerts.splice(index, 1);
	};
	
});

controllersModule.controller('MessageBoxController', function MessageBoxController($scope, $modalInstance, title, message) {
	
	$scope.title = title;
	$scope.message = message;
	
	$scope.close = function() {
		$modalInstance.dismiss('cancel');
	}
	
});

controllersModule.controller('ConfirmationBoxController', function ConfirmationBoxController($scope, $modalInstance, title, message) {
	
	$scope.title = title;
	$scope.message = message;
	
	$scope.ok = function() {
		$modalInstance.close('ok');
	}
	
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
	
});

controllersModule.controller('LunchboxController', function LunchboxController($scope, $location, $modal, Backend) {
	
	$scope.uiLocked = false;
	$scope.profile = new Object();
	
	$scope.loadProfile = function() {
		$scope.uiLocked = true;
		Backend.getProfile(function(data) {
			$scope.profile = data;
			$scope.uiLocked = false;
		});
	}
	
	$scope.saveProfile = function() {
		$scope.uiLocked = true;
		Backend.postProfile($scope.profile, function() {
			$scope.uiLocked = false;
		});
	}
	
	$scope.isActive = function(routes) {
		for (var i = 0; i < routes.length; i++) {
			var route = routes[i];
        	if (route === $location.path()) {
        		return true;
        	}
		}
		return false;
    }
	
	$scope.logout = function() {
		window.location = '/logout';
	}
	
	$scope.error = function(error, onClosed) {
		var opts = {
			backdrop: true,
			keyboard: true,
			backdropClick: true,
			templateUrl: 'pages/message.html',
			controller: 'MessageBoxController',
			resolve: {
				title: function() { return 'Fehler'; },
				message: function() { return error.message; }
			}
		};
		
		$modal.open(opts).result.then(function(result) {
			if (onClosed != undefined) {
				onClosed();
			}
		});
	}
	
	$scope.confirm = function(title, message, onOk) {
		var opts = {
			backdrop: true,
			keyboard: true,
			backdropClick: true,
			templateUrl: 'pages/confirmation.html',
			controller: 'ConfirmationBoxController',
			resolve: {
				title: function() { return title; },
				message: function() { return message; }
			}
		};
		
		$modal.open(opts).result.then(function(result) {
			if (onOk != undefined && result == 'ok') {
				onOk();
			}
		});
	}
	
});


controllersModule.controller('OrderController', function OrderController($scope, Backend) {
	
	$scope.loading = true;
	$scope.offers = undefined;
	$scope.orderingLocked = false;
	
	$scope.loadOffers = function() {
		Backend.getOffers(function(data) {
			handleResponse(data, function() {
				$scope.offers = data;
				$scope.loading = false;
			});
		});
	}
	
	$scope.order = function(offer, item) {
		$scope.orderingLocked = true;

		var order = toOrder(offer, item);
		if (order.id == null) {
			Backend.putOrder(order, function(data) {
				handleResponse(data, function() {
					$scope.offers.offerOrders[offer.id] = data;
					$scope.orderingLocked = false;
				});
			});
		} else {
			Backend.postOrder(order, function(data) {
				handleResponse(data, function() {
					$scope.offers.offerOrders[offer.id] = data;
					$scope.orderingLocked = false;
				});
			});
		}
	}
	
	$scope.cancel = function(offer) {
		$scope.orderingLocked = true;

		var order = toOrder(offer);
		if (order.id != null) {
			Backend.deleteOrder(order, function(data) {
				handleResponse(data, function() {
					$scope.offers.offerOrders[offer.id] = new Object();
					$scope.orderingLocked = false;
				});
			});
		}
	}
	
	$scope.isLocked = function(offer) {
		return $scope.orderingLocked || offer.locked;
	}
	
	$scope.isSomethingOrdered = function(offer) {
		var offerOrders = $scope.offers.offerOrders;
		if (offerOrders == undefined || offerOrders == null) {
			return false;
		}
		var offerOrdersForId = offerOrders[offer.id];
		if (offerOrdersForId == undefined || offerOrdersForId == null) {
			return false;
		}
		var orderedItem = offerOrdersForId.offerItemId;
		return orderedItem != undefined && orderedItem != null && !offer.locked;
	}
	
	$scope.isOrdered = function(offer, item) {
		var offerOrders = $scope.offers.offerOrders;
		if (offerOrders == undefined || offerOrders == null) {
			return false;
		}
		var offerOrders = offerOrders[offer.id];
		if (offerOrders == undefined || offerOrders == null) {
			return false;
		}
		return offerOrders.offerItemId == item.id;
	}
	
	function handleResponse(data, onReady) {
		if (data.errorCode != undefined) {
			$scope.error(data, function() { $scope.loadOffers(); });
		}
		if (onReady != undefined) {
			onReady(data);
		}
	}
	
	function setOffers(data) {
		$scope.offers = data;
	}
	
	function toOrder(offer, item, userId) {
		var order = { 
			'id': getOrderId(offer),
			'userId': userId == undefined || userId == null ? $scope.profile.id : null,
			'offerId': offer.id,
			'offerItemId': item != undefined && item != null ? item.id : null,
			'times': 1
		};
		return order;
	}
	
	function getOrderId(offer) {
		return $scope.isSomethingOrdered(offer) ? $scope.offers.offerOrders[offer.id].id : null;
	}
	
});


controllersModule.controller('OrdersForDayReportController', function OrdersForDayController($scope, $filter, $timeout, Backend) {
	
	$scope.loading = true;
	$scope.displayDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
	$scope.ordersForDay = new Array();
	
	$scope.dateOptions = { 'year-format': "'yy'", 'starting-day': 1 };
	
	$scope.open = function() {
	    $timeout(function() {
	    	$scope.opened = true;
	    });
	};
	
	$scope.loadOrdersForDay = function() {
		var day = $scope.displayDate.getDate();
		var month = $scope.displayDate.getMonth() + 1;
		var year = $scope.displayDate.getFullYear();

		Backend.getDailyOrders(year, month, day, function(data) {
			$scope.ordersForDay = data;
			$scope.loading = false;
		});
	}
	
	$scope.displayPdf = function() {
		var day = $scope.displayDate.getDate();
		var month = $scope.displayDate.getMonth() + 1;
		var year = $scope.displayDate.getFullYear();
		window.open('/disp/reports/' + year + '/' + month + "/" + day + "/Bestellung.pdf", '_blank');
	}
	
});


controllersModule.controller('MyOrdersForMonthReportController', function MyOrdersForMonthController($scope, Backend) {
	
	$scope.loading = true;
	$scope.ordersForMonth = new Array();
	$scope.selectedMonth = getDefaultMonth();
	$scope.selectedYear = getDefaultYear();
	
	$scope.loadOrdersForMonth = function(month, year) {
		if (month == undefined || month == null) {
			month = getDefaultMonth();
		}
		if (year == undefined || year == null) {
			year = getDefaultYear();
		}
		Backend.getMonthlyOrders(year, month, function(data) {
			$scope.ordersForMonth = data;
			$scope.loading = false;
		});
	}
	
	function getDefaultMonth() {
		return (new Date().getMonth() + 1).toString();
	}
	
	function getDefaultYear() {
		return (new Date().getFullYear()).toString();
	}
	
});


controllersModule.controller('MonthlyBillingReportController', function MonthlyBillingController($scope, Backend) {
	
	$scope.loading = true;
	$scope.ordersForBilling = new Array();
	$scope.selectedMonth = getDefaultMonth();
	$scope.selectedYear = getDefaultYear();
	
	$scope.loadOrdersForBilling = function(month, year) {
		if (month == undefined || month == null) {
			month = getDefaultMonth();
		}
		if (year == undefined || year == null) {
			year = getDefaultYear();
		}
		Backend.getBillingOrders(year, month, function(data) {
			$scope.ordersForBilling = data;
			$scope.loading = false;
		});
	}
	
	$scope.sumUp = function(data) {
		var summary = new Object();
		for (var i = 0; i < data.days.length; i++) {
			var day = data.days[i];
			if (day != undefined && day != null && day.items != undefined && day.items != null) {
				for (var j = 0; j < day.items.length; j++) {
					var item = day.items[j];
					var key = item.offerItem.priceInCents.toString();
					if (summary[key] == undefined) {
						summary[key] = item.times;
					} else {
						summary[key] += item.times;
					}
				}
			}
		}
		return summary;
	}
	
	function getDefaultMonth() {
		return (new Date().getMonth() + 1).toString();
	}
	
	function getDefaultYear() {
		return (new Date().getFullYear()).toString();
	}
	
	$scope.displayPdf = function() {
		var month = $scope.selectedMonth;
		var year = $scope.selectedYear;
		
		if (month == undefined || year == undefined) {
			return;
		}
		window.open('/disp/reports/billing.' + month + '-' + year + '.xls', '_blank');
	}
	
});


controllersModule.controller('MenuSettingsController', function MenuSettingsController($scope) {
	
	$scope.uploadMenu = function() {
		$('#upload').attr('disabled', true);
		$('.uploadinfo').show();
		$('#uploadmenu').submit();
	}
	
});


controllersModule.controller('OfferSettingsController', function OfferSettingsController($scope, $timeout, $filter, $modal, Backend) {
	
	$scope.loading = true;
	$scope.offer = undefined;
	$scope.displayDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
	$scope.dateOptions = { 'year-format': "'yy'", 'starting-day': 1 };
	
	$scope.open = function() {
	    $timeout(function() {
	    	$scope.opened = true;
	    });
	};
	
	$scope.loadOffer = function() {
		var day = $scope.displayDate.getDate();
		var month = $scope.displayDate.getMonth() + 1;
		var year = $scope.displayDate.getFullYear();
		
		Backend.getOffer(year, month, day, function(data) {
			$scope.offer = data;
			$scope.loading = false;
		});
	}
	
	$scope.openOfferItemDialog = function(offerItem) {
		if (offerItem == undefined) {
			offerItem = { id: null, mealId: null, priceInCents: 395 };
		}
		var opts = {
			backdrop: true,
			keyboard: true,
			templateUrl: 'pages/settings_offeritem.html',
			controller: 'OfferItemDialogController',
			resolve: {
				offer: function() { return angular.copy($scope.offer) },
				offerItem: function() { return angular.copy(offerItem) }
			}
		};
		
		var d = $modal.open(opts);
		d.result.then(function(result) {
			if (result != undefined) {
				$scope.loadOffer();
			}
		});
	}
	
	$scope.deleteOfferItem = function(offerItem) {
		var title = 'L&ouml;schen?';
		var message = 'Das Angebot #' + offerItem.id + ' l&ouml;schen?';

		$scope.confirm(title, message, function() {
			Backend.deleteOfferItem(offerItem, function(response) {
				$scope.loadOffer();
				if (response.errorCode != undefined) {
					$scope.error(response);
				}
			});
		});
	}
});


controllersModule.controller('OfferItemDialogController', function OfferItemDialogController($scope, $modalInstance, offer, offerItem, Backend) {

	$scope.offer = offer;
	$scope.offerItem = offerItem;
	$scope.mealOptions = undefined;
	$scope.offerItemOptions = undefined;
	$scope.errorMessage = undefined;
	
	$scope.loadOptions = function() {
		$scope.loadMealOptions();
	}
	
	$scope.loadMealOptions = function() {
		Backend.getMealOptions(function(data) {
			$scope.mealOptions = data;
		});
	}
	
	$scope.isCreate = function() {
		return $scope.offerItem.id == undefined || $scope.offerItem.id == null;
	}
	
	$scope.cancel = function() {
		$modalInstance.dismiss("cancel");
	}
	
	$scope.save = function() {
		var offerItem = {
			id: $scope.offerItem.id,
			name: $scope.offerItem.name,
			offerDate: $scope.offer.date,
			mealId: $scope.offerItem.mealId,
			priceInCents: $scope.offerItem.priceInCents
		}
		if (offerItem.id == undefined) {
			Backend.putOfferItem(offerItem, function(response) {
				handleSuccess(response);
			});
		} else {
			Backend.postOfferItem(offerItem, function(response) {
				handleSuccess(response);
			});
		}
	}
	
	$scope.shouldDisplayErrorMessage = function() {
		return $scope.errorMessage != undefined;
	}
	
	handleSuccess = function(response) {
		if (response.error != undefined) {
			$scope.errorMessage = response.message;
		} else {
			$modalInstance.close($scope.offerItem);
		}
	}

});


controllersModule.controller('OrderSettingsController', function OrderSettingsController($scope, $timeout, $filter, $modal, Backend) {
	
	$scope.loading = true;
	$scope.orders = undefined;
	$scope.displayDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
	$scope.dateOptions = { 'year-format': "'yy'", 'starting-day': 1 };
	
	$scope.open = function() {
	    $timeout(function() {
	    	$scope.opened = true;
	    });
	};
	
	$scope.loadOrders = function() {
		var day = $scope.displayDate.getDate();
		var month = $scope.displayDate.getMonth() + 1;
		var year = $scope.displayDate.getFullYear();

		Backend.getOrders(year, month, day, function(data) {
			$scope.orders = data;
			$scope.loading = false;
		});
	}
	
	$scope.openOrderDialog = function(order) {
		if (order == undefined) {
			order = { id: null, userId: null, offerItemId: null, times: 1 };
		}
		var opts = {
			backdrop: true,
			keyboard: true,
			backdropClick: true,
			templateUrl: 'pages/settings_order.html',
			controller: 'OrderDialogController',
			resolve: {
				offer: function() { return $scope.orders.offer },
				item: function() { return angular.copy(order) },
				year: function() { return $scope.displayDate.getFullYear() },
				month: function() { return $scope.displayDate.getMonth() + 1 },
				day: function() { return $scope.displayDate.getDate() }
			}
		};
		
		var d = $modal.open(opts);
		d.result.then(function(result) {
			if (result != undefined) {
				$scope.loadOrders();
			}
		});
	}
	
	$scope.deleteOrder = function(order) {
		var title = 'L&ouml;schen?';
		var message = 'Die Bestellung #' + order.id + ' l&ouml;schen?';
		
		$scope.confirm(title, msg, function() {
			Backend.deleteOrder(order, function(response) {
				$scope.loadOrders();
				if (response.errorCode != undefined) {
					$scope.error(response);
				}
			});
		});
	}
});


controllersModule.controller('OrderDialogController', function OrderDialogController($scope, $modalInstance, item, offer, year, month, day, Backend) {

	$scope.order = item;
	$scope.offer = offer;
	$scope.year = year;
	$scope.month = month;
	$scope.day = day;
	$scope.userOptions = undefined;
	$scope.offerItemOptions = undefined;
	$scope.errorMessage = undefined;
	
	$scope.loadOptions = function() {
		$scope.loadOfferItemOptions()
		$scope.loadUserOptions();
	}
	
	$scope.loadUserOptions = function() {
		Backend.getUserOptions(function(data) {
			$scope.userOptions = data;
		});
	}
	
	$scope.loadOfferItemOptions = function() {
		Backend.getOfferItemOptions($scope.year, $scope.month, $scope.day, function(data) {
			$scope.offerItemOptions = data;
		});
	}
	
	$scope.isCreate = function() {
		return $scope.order.id == undefined || $scope.order.id == null;
	}
	
	$scope.cancel = function() {
		$modalInstance.dismiss("cancel");
	}
	
	$scope.save = function() {
		var order = {
			id: $scope.order.id,
			userId: $scope.order.detail.userId,
			offerId: $scope.offer.id,
			offerItemId: $scope.order.offerItem.id,
			times: $scope.order.detail.times
		}
		if (order.id == undefined) {
			Backend.putOrder(order, function(response) {
				handleSuccess(response);
			});
		} else {
			Backend.postOrder(order, function(response) {
				handleSuccess(response);
			});
		}
	}
	
	$scope.shouldDisplayErrorMessage = function() {
		return $scope.errorMessage != undefined;
	}
	
	handleSuccess = function(response) {
		if (response.error != undefined) {
			$scope.errorMessage = response.message;
		} else {
			$modalInstance.close($scope.order);
		}
	}

});


controllersModule.controller('UserSettingsController', function UserSettingsController($scope, $modal, Backend) {
	
	$scope.loading = true;
	$scope.users = undefined;
	
	$scope.currentPage = 1;
	$scope.size = 10;
	$scope.sort = 'loginName';
	$scope.order = 'asc';

	$scope.loadUsers = function(page) {
		if (page == undefined || page == null) {
			page = $scope.currentPage;
		}
		Backend.getUsers(page, $scope.size, $scope.sort, $scope.order, function(data) {
			$scope.users = data;
			$scope.loading = false;
		});
	}
	
	$scope.sortBy = function(sort) {
		if (sort == $scope.sort) {
			switchSortDirection();
		} else {
			$scope.sort = sort;
			$scope.order = 'asc';
		}
		$scope.loadUsers();
	}
	
	$scope.sortedBy = function(sort, order) {
		return $scope.sort == sort && $scope.order == order;
	}
	
	switchSortDirection = function() {
		if ($scope.order == 'asc') {
			$scope.order = 'desc';
		} else {
			$scope.order = 'asc';
		}
	}
	
	$scope.openUserDialog = function(user) {
		if (user == undefined) {
			user = { id: null, loginName: '', loginSecret: null, fullName: '', mail: '', personnelNumber: '', ordererRole: true, adminRole: false, active: true };
		}
		var opts = {
			backdrop: true,
			keyboard: true,
			templateUrl: 'pages/settings_user.html',
			controller: 'UserDialogController',
			resolve: {item: function() { return angular.copy(user) }}
		};
		
		var d = $modal.open(opts);
		d.result.then(function(result) {
			if (result != undefined) {
				$scope.loadUsers();
			}
		});
	}
	
});


controllersModule.controller('UserDialogController', function UserDialogController($scope, $modalInstance, item, Backend) {

	$scope.user = item;
	$scope.errorMessage = undefined;
	
	$scope.isCreate = function() {
		return $scope.user.id == undefined || $scope.user.id == null;
	}
	
	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
	
	$scope.save = function() {
		var user = $scope.user;
		if (user.id == undefined) {
			Backend.putUser(user, function(response) {
				handleSuccess(response);
			});
		} else {
			Backend.postUser(user, function(response) {
				handleSuccess(response);
			});
		}
	}
	
	$scope.shouldDisplayErrorMessage = function() {
		return $scope.errorMessage != undefined;
	}
	
	handleSuccess = function(response) {
		if (response.error != undefined) {
			$scope.errorMessage = response.message;
		} else {
			$modalInstance.close($scope.user);
		}
	}

});


controllersModule.controller('JobSettingsController', function JobSettingsController($scope, $http, Backend) {
	
	$scope.executing = false;
	$scope.jobs = new Array();
	
	$scope.loadJobs = function() {
		Backend.getJobs(function(data) {
			$scope.jobs = data;
		});
	}
	
	$scope.triggerJob = function(name) {
		$scope.executing = true;
		$http.get('/disp/action/trigger?name=' + name).then(function() {
			$scope.loadJobs();
			$scope.executing = false;
		}, function(resp) {
			$scope.error(resp.data);
			$scope.executing = false;
		});
	}
	
});

controllersModule.controller('JournalSettingsController', function JournalSettingsController($scope, $timeout, $filter, Backend) {
	
	$scope.loading = true;
	$scope.journal = undefined;
	$scope.displayDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
	$scope.dateOptions = { 'year-format': "'yy'", 'starting-day': 1 };
	
	$scope.currentPage = 1;
	$scope.size = 10;
	$scope.sort = 'modificationDate';
	$scope.order = 'asc';
	
	$scope.open = function() {
	    $timeout(function() {
	    	$scope.opened = true;
	    });
	}
	
	$scope.sortBy = function(sort) {
		if (sort == $scope.sort) {
			switchSortDirection();
		} else {
			$scope.sort = sort;
			$scope.order = 'asc';
		}
		$scope.loadJournal();
	}
	
	$scope.sortedBy = function(sort, order) {
		return $scope.sort == sort && $scope.order == order;
	}
	
	switchSortDirection = function() {
		if ($scope.order == 'asc') {
			$scope.order = 'desc';
		} else {
			$scope.order = 'asc';
		}
	}
	
	$scope.loadJournal = function(page) {
		var day = $scope.displayDate.getDate();
		var month = $scope.displayDate.getMonth() + 1;
		var year = $scope.displayDate.getFullYear();
		
		if (page == undefined || page.length == 0) {
			page = $scope.currentPage;
		}

		Backend.getJournal(year, month, day, page, $scope.size, $scope.sort, $scope.order, function(data) {
			$scope.journal = data;
			$scope.loading = false;
		});
	}
	
});


controllersModule.controller('MealSettingsController', function MealSettingsController($scope, $modal, Backend) {
	
	$scope.loading = true;
	$scope.search = undefined;
	$scope.meals = undefined;

	$scope.currentPage = 1;
	$scope.size = 10;
	$scope.sort = 'headline';
	$scope.order = 'asc';
	
	$scope.filter = function() {
		$scope.currentPage = 1;
		$scope.loadMeals();
	}
	
	$scope.loadMeals = function(page) {
		if (page == undefined || page == null) {
			page = $scope.currentPage;
		}
		Backend.getMeals(page, $scope.size, $scope.sort, $scope.order, $scope.search, function(data) {
			$scope.meals = data;
			$scope.loading = false;
		});
	}
	
	$scope.sortBy = function(sort) {
		if (sort == $scope.sort) {
			switchSortDirection();
		} else {
			$scope.sort = sort;
			$scope.order = 'asc';
		}
		$scope.loadMeals();
	}
	
	$scope.sortedBy = function(sort, order) {
		return $scope.sort == sort && $scope.order == order;
	}
	
	switchSortDirection = function() {
		if ($scope.order == 'asc') {
			$scope.order = 'desc';
		} else {
			$scope.order = 'asc';
		}
	}
	
	$scope.openMealDialog = function(meal) {
		if (meal == undefined) {
			meal = { id: null, headline: null, description: null, ingredients: null };
		}
		var opts = {
			backdrop: true,
			keyboard: true,
			backdropClick: true,
			templateUrl: 'pages/settings_meal.html',
			controller: 'MealDialogController',
			resolve: {
				meal: function() { return angular.copy(meal) }
			}
		};
		
		var d = $modal.open(opts);
		d.result.then(function(result) {
			if (result != undefined) {
				$scope.searchText = undefined;
			}
		});
	}
	
	$scope.deleteMeal = function(meal) {
		var title = 'L&ouml;schen?';
		var message = 'Die Mahlzeit ' + meal.headline + ' l&ouml;schen?';
		
		$scope.confirm(title, msg, function() {
			Backend.deleteMeal(meal, function(response) {
				$scope.loadMeals();
				if (response.errorCode != undefined) {
					$scope.error(response);
				}
			});
		});
	}
	
});


controllersModule.controller('MealDialogController', function MealDialogController($scope, $modalInstance, meal, Backend) {

	$scope.meal = meal;
	$scope.errorMessage = undefined;
	
	$scope.isCreate = function() {
		return $scope.meal.id == undefined || $scope.meal.id == null;
	}
	
	$scope.cancel = function() {
		$modalInstance.dismiss("cancel");
	}
	
	$scope.save = function() {
		var meal = $scope.meal;
		if (meal.id == undefined) {
			Backend.putMeal(meal, function(response) {
				handleSuccess(response);
			});
		} else {
			Backend.postMeal(meal, function(response) {
				handleSuccess(response);
			});
		}
	}
	
	$scope.shouldDisplayErrorMessage = function() {
		return $scope.errorMessage != undefined;
	}
	
	handleSuccess = function(response) {
		if (response.error != undefined) {
			$scope.errorMessage = response.message;
		} else {
			$modalInstance.close($scope.meal);
		}
	}

});


controllersModule.controller('UserStatisticsController', function UserStatisticsController($scope, Backend) {
	
	$scope.loading = true;
	$scope.stats = new Object();
	$scope.stats.menuDistribution = [];
	$scope.stats.salesByMonth = [];
	
	$scope.loadStats = function() {
		Backend.getUserStatistics(function(data) {
			$scope.stats = data;
			$scope.loading = false;
		});
	}
	
});


controllersModule.controller('SystemStatisticsController', function SystemStatisticsController($scope, Backend) {
	
	$scope.loading = true;
	$scope.stats = new Object();
	$scope.stats.menuDistribution = [];
	$scope.stats.salesByMonth = [];
	
	$scope.loadStats = function() {
		Backend.getSystemStatistics(function(data) {
			$scope.stats = data;
			$scope.loading = false;
		});
	}
	
});
