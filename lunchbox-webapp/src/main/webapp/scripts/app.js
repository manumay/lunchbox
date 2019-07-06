var appModule = angular.module('lunchbox', ['ui.bootstrap', 'lunchbox-controllers', 'lunchbox-services', 'lunchbox-directives']);

appModule.config(['$routeProvider', function($routeProvider) {
	$routeProvider.
		when('/order', { title: 'Bestellung', templateUrl: '/pages/order.html', controller: 'OrderController' } ).
		when('/profile', { title: 'Benutzerprofil', templateUrl: '/pages/profile.html' } ).
		when('/statistics/user', { title: 'Berichte | Benutzerstatistik', templateUrl: '/pages/statistics_user.html', controller: 'UserStatisticsController' } ).
		when('/statistics/system', { title: 'Berichte | Systemstatistik', templateUrl: '/pages/statistics_system.html', controller: 'SystemStatisticsController' } ).
		when('/reports/day', { title: 'Berichte | Bestellungen f&uuml;r Tag', templateUrl: '/pages/reports_day.html', controller: 'OrdersForDayReportController' } ).
		when('/reports/month', { title: 'Berichte | Meine Bestellungen f&uuml;r Monat', templateUrl: '/pages/reports_month.html', controller: 'MyOrdersForMonthReportController' } ).
		when('/reports/billing', { title: 'Berichte | Monatsabrechnung', templateUrl: '/pages/reports_billing.html', controller: 'MonthlyBillingReportController' } ).
		when('/settings/jobs', { title: 'Verwaltung | Hintergrundjobs', templateUrl: '/pages/settings_jobs.html', controller: 'JobSettingsController' } ).
		when('/settings/journal', { title: 'Verwaltung | Logbuch', templateUrl: '/pages/settings_journal.html', controller: 'JournalSettingsController' } ).
		when('/settings/meals', { title: 'Verwaltung | Mahlzeiten', templateUrl: '/pages/settings_meals.html', controller: 'MealSettingsController' } ).
		when('/settings/menu', { title: 'Verwaltung | Speisekarte', templateUrl: '/pages/settings_menu.html', controller: 'MenuSettingsController' } ).
		when('/settings/offers', { title: 'Verwaltung | Angebote', templateUrl: '/pages/settings_offers.html', controller: 'OfferSettingsController' } ).
		when('/settings/orders', { title: 'Verwaltung | Bestellungen', templateUrl: '/pages/settings_orders.html', controller: 'OrderSettingsController' } ).
		when('/settings/users', { title: 'Verwaltung | Benutzer', templateUrl: '/pages/settings_users.html', controller: 'UserSettingsController' } );
}]);

appModule.run(['$location', '$rootScope', function($location, $rootScope) {
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
    	if (current != '/logout') {
    		$rootScope.title = current.$route.title;
    	}
    });
}]);

appModule.config(['$locationProvider', function($locationProvider) {
	$locationProvider.html5Mode(true);
}]);