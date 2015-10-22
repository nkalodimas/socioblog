'use strict';
/* global app:true */

var app = angular.module('frontEndApp', [
    'ngRoute',
    'ngResource',
    'ui.bootstrap',
    'LocalStorageModule'
]);
app.constant('SERVER_URL', '');

app.config(['localStorageServiceProvider', function(localStorageServiceProvider){
  localStorageServiceProvider.setPrefix('ls');
}]);

app.config(function($routeProvider){
    $routeProvider
        .when('/',{
            templateUrl: 'views/recentposts.html',
            controller: 'PostsCtrl'
        })
        .when('/users',{
            templateUrl: 'views/users.html',
            controller: 'UsersCtrl'
        })
        .when('/users/:userId', {
            templateUrl: 'views/showuser.html',
            controller: 'UserViewCtrl'
        })
        .when('/myaccount', {
            templateUrl: 'views/myaccount.html',
            controller: 'MyAccountCtrl'
        })
        .when('/posts',{
            templateUrl: 'views/recentposts.html',
            controller: 'PostsCtrl'
        })
        .when('/posts/page/:pageNumber',{
            templateUrl: 'views/recentposts.html',
            controller: 'PostsCtrl'
        })
        .when('/posts/new',{
            templateUrl: 'views/createpost.html',
            controller: 'PostCreateCtrl'
        })
        .when('/posts/:postId', {
            templateUrl: 'views/showpost.html',
            controller: 'PostViewCtrl'
        })
        .when('/posts/search/:searchTerm/:pageNumber', {
            templateUrl: 'views/searchresults.html',
            controller: 'SearchCtrl'
        })
        .when('/posts/:tagTitle/:pageNumber',{
            templateUrl: 'views/categoryposts.html',
            controller: 'TagsCtrl'
        })//TODO
        .when('/users/:userId/posts/:pageNumber',{
            templateUrl: 'views/userposts.html',
            controller: 'UserPostsCtrl' 
        })
        .when('/admin',{
            templateUrl: 'views/admin.html',
            controller: 'AdminCtrl'
        })
        .when('/atmo', {
            templateUrl: 'views/atmo.html',
        })
        .otherwise({
            redirectTo: '/'
        });
});

    app.run(function($rootScope, localStorageService, Auth) {
        console.log("inside run");
        $rootScope.userReady = false;
        var localUser = localStorageService.get('currentUser');
        if(localUser){
            $rootScope.currentUser = localUser;
            Auth.isLoggedIn()
                .then(
                    function (data) {
                        if(data == "loggedIn") {
                            $rootScope.userReady = true;
                        }else { //was loggedIn but spring-token expired
                            Auth.login($rootScope.currentUser.socialId, $rootScope.currentUser.accessToken)
                                .then(
                                    function (data) {
                                        $rootScope.currentUser = data[1];
                                        $rootScope.currentUser.token = data[0];
                                        $rootScope.userReady = true;
                                        localStorageService.set('currentUser', $scope.currentUser);
                                    },
                                    function( errorMessage ) {
                                        console.warn( errorMessage );
                                        localStorageService.remove('currentUser');
                                        $rootScope.currentUser = undefined;
                                    }
                                );
                        }
                    },
                    function( errorMessage ) {
                        console.warn( errorMessage );
                        localStorageService.remove('currentUser');
                        $rootScope.currentUser = undefined;
                    }
                );
        }
    });