var serviceModule = angular.module('lunchbox-services', ['ngResource']);
	
serviceModule.factory('Backend', function($resource) {
	return new backendFactory($resource);
});

function backendFactory($resource) {
	
	this.deleteMeal = function(meal, onReady, onFailure) {
		var res = $resource('/disp/json/meal/' + meal.id, {}, { delete: { method: 'DELETE' } });
		res.delete(function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.deleteOfferItem = function(offerItem, onReady, onFailure) {
		var res = $resource('/disp/json/offeritem/' + offerItem.id, {} , { delete: { method: 'DELETE' } });
		res.delete(function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.deleteOrder = function(order, onReady, onFailure) {
		var res = $resource('/disp/json/order/' + order.id, {} , { delete: { method: 'DELETE' } });
		res.delete(function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.deleteUser = function(user, onReady, onFailure) {
		var res = $resource('/disp/json/user/' + user.id, {}, { delete: { method: 'POST' } });
		res.delete(function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getBillingOrders = function(year, month, onReady, onFailure) {
		var res = $resource('/disp/json/reports/' + year + '/' + month + '/billing', {}, { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getDailyOrders = function(year, month, day, onReady, onFailure) {
		var res = $resource('/disp/json/reports/' + year + '/' + month + '/' + day,{} , { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getJobs = function(onReady, onFailure) {
		var res = $resource('/disp/json/jobs', {}, { get: { method: 'GET', isArray:true } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getJournal = function(year, month, day, page, size, sort, order, onReady, onFailure) {
		var url = '/disp/json/journal/' + year + '/' + month + '/' + day + '?page=' + enc(page) 
			+ '&size=' + enc(size) + '&sort=' + enc(sort) + '&order=' + enc(order);
		var res = $resource(url, {}, { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getMeals = function(page, size, sort, order, search, onReady, onFailure) {
		var url = '/disp/json/meals?page=' + enc(page) + '&size=' + enc(size) 
				+ '&sort=' + enc(sort) + '&order=' + enc(order);
		if (search != undefined && search.length > 0) {
			url += '&search=' + enc(search);
		}
		var res = $resource(url, {}, { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getMealOptions = function(onReady, onFailure) {
		var res = $resource('/disp/json/options/meals', {}, { get: { method: 'GET', isArray: true } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getMonthlyOrders = function(year, month, onReady, onFailure) {
		var res = $resource('/disp/json/reports/' + year + '/' + month + '/mine', {}, { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getOffers = function(onReady, onFailure) {
		var res = $resource('/disp/json/offers', {}, { get: { method: 'GET', isArray: false } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getOffer = function(year, month, day, onReady, onFailure) {
		var res = $resource('/disp/json/offeritems/' + year + '/' + month + '/' + day, {}, { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getOfferItemOptions = function(year, month, day, onReady, onFailure) {
		var res = $resource('/disp/json/options/offeritems/' + year + '/' + month + '/' + day, {}, { get: { method: 'GET', isArray: true } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady)
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getOrders = function(year, month, day, onReady, onFailure) {
		var res = $resource('/disp/json/orders/' + year + '/' + month + '/' + day,{} , { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getProfile = function(onReady, onFailure) {
		var res = $resource('/disp/json/profile', {}, { get: { method: 'GET', isArray: false } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getSystemStatistics = function(onReady, onFailure) {
		var res = $resource('/disp/json/statistics/system', {}, { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getUserOptions = function(onReady, onFailure) {
		var res = $resource('/disp/json/options/users', {}, { get: { method: 'GET', isArray: true } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getUsers = function(page, size, sort, order, onReady, onFailure) {
		var res = $resource('/disp/json/users?page=' + enc(page) + '&size=' + enc(size) + '&sort=' + enc(sort) + '&order=' + enc(order),
				{},	{ get: { method: 'GET', isArray: false } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.getUserStatistics = function(onReady, onFailure) {
		var res = $resource('/disp/json/statistics/me', {}, { get: { method: 'GET' } });
		res.get({}, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.postMeal = function(meal, onReady, onFailure) {
		var res = $resource('/disp/json/meal/' + meal.id,{} , { post: { method: 'POST' } });
		res.post(meal, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.postOfferItem = function(offerItem, onReady, onFailure) {
		var res = $resource('/disp/json/offeritem/' + offerItem.id,{} , { post: { method: 'POST' } });
		res.post(offerItem, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.postOrder = function(order, onReady, onFailure) {
		var res = $resource('/disp/json/order/' + order.id,{} , { post: { method: 'POST' } });
		res.post(order, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.postProfile = function(profile, onReady, onFailure) {
		var res = $resource('/disp/json/profile',{} , { post: { method: 'POST' } });
		res.post(profile, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.postUser = function(user, onReady, onFailure) {
		var res = $resource('/disp/json/user/' + user.id, {}, { post: { method: 'POST' } });
		res.post(user, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.putMeal = function(meal, onReady, onFailure) {
		var res = $resource('/disp/json/meal',{} , { put: { method: 'PUT' } });
		res.put(meal, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.putOfferItem = function(offerItem, onReady, onFailure) {
		var res = $resource('/disp/json/offeritem',{} , { put: { method: 'PUT' } });
		res.put(offerItem, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.putOrder = function(order, onReady, onFailure) {
		var res = $resource('/disp/json/order',{} , { put: { method: 'PUT' } });
		res.put(order, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	this.putUser = function(user, onReady, onFailure) {
		var res = $resource('/disp/json/user',{} , { put: { method: 'PUT' } });
		res.put(user, function(resp) {
			handleResponse(resp, onReady);
		}, function(resp) {
			handleServerError(resp, onFailure);
		});
	}
	
	function handleResponse(resp, onReady) {
		if (resp != undefined) {
			var errorCode = resp.errorCode;
			if (errorCode != undefined) {
				if (errorCode == 401) {
					window.location = '/login.html';
				}
			}
		}
		if (onReady != undefined) {
			onReady(resp);
		}
	}
	
	function handleServerError(resp, onFailure) {
		if (onFailure != undefined) {
			onFailure(resp);
		}
	}
	
	function enc(b) {
		return encodeURIComponent(b);
	}

}