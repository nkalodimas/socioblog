'use strict';

app.controller('AuthCtrl', function ($scope, $rootScope, Auth, Facebook, localStorageService){
    
    $scope.fbReady = false;
    
    $scope.fbLogin = function () {
        if( $scope.fbUser.connected == true){
            $scope.login("facebook", $scope.fbUser.fbToken);
        }
        else{
            FB.getLoginStatus(function(response) {
                if (response.status == 'connected') {
                    $scope.fbUser.connected = true;
                    $scope.fbUser.fbToken = response.authResponse.accessToken;
                    $scope.login("facebook", $scope.fbUser.fbToken);
                } else {
                    FB.login(function(response) {
                        if (response.authResponse) {
                            $scope.fbUser.connected = true;
                            $scope.fbUser.fbToken = response.authResponse.accessToken;
                            $scope.login("facebook", $scope.fbUser.fbToken);
                        } else {
                          console.log("User did not provided authentication: " + response.status);
                        }
                      },{scope: 'public_profile,email,user_friends'});//TODO add read_stream
                }
              });
        }
    }

    $scope.login = function (socialId, accessToken) {
        if(socialId === "facebook"){
            
            Auth.login(socialId, accessToken)
                .then(
                    function (data) {
                        console.log("login response: " + data);
                        $rootScope.currentUser = data[1];
                        $rootScope.currentUser.token = data[0];
                        $rootScope.currentUser.socialProvider = "facebook";
                        $rootScope.userReady = true;
                        localStorageService.set('currentUser', $rootScope.currentUser);
                    },
                    function( errorMessage ) {
                        console.warn( errorMessage );
                        localStorageService.remove('currentUser');
                        $rootScope.currentUser = undefined;
                    }
                );
        }
    };
    
    $scope.fbSignUp = function () {
        if( $scope.fbUser.connected == true){
            $scope.signUp("facebook", $scope.fbUser.fbToken);
        }
        else{
            FB.getLoginStatus(function(response) {
                if (response.status == 'connected') {
                    console.log("token="+response.authResponse.accessToken)
                    $scope.fbUser.connected = true;
                    $scope.fbUser.fbToken = response.authResponse.accessToken;
                    $scope.signUp("facebook", $scope.fbUser.fbToken);
                } else {
                    console.log("inside getLoginStatus not connected");
                    FB.login(function(response) {
                        if (response.authResponse) {
                            $scope.fbUser.connected = true;
                            $scope.fbUser.fbToken = response.authResponse.accessToken;
                            $scope.signUp("facebook", $scope.fbUser.fbToken);
                        } else {
                          console.log("User did not provided authentication: " + response.status);
                        }
                      },{scope: 'public_profile,email,user_friends'});//TODO add read_stream
                }
              });
        }
    }

    $scope.signUp = function (socialId, accessToken) {
        if(socialId === "facebook"){
            
            Auth.signUp(socialId, accessToken)
                .then(
                    function (data) {
                        $rootScope.currentUser = data[1];
                        $rootScope.currentUser.token = data[0];
                        $rootScope.currentUser.socialProvider = "facebook";
                        $rootScope.userReady = true;
                        localStorageService.set('currentUser', $rootScope.currentUser);
                    },
                    function( errorMessage ) {
                        console.warn( errorMessage );
                        localStorageService.remove('currentUser');
                        $rootScope.currentUser = undefined;
                    }
                );
        }
    };
    
    $scope.logout = function () {
        $rootScope.userReady = false;
        $rootScope.currentUser = undefined;
        localStorageService.remove('currentUser');
    };
    
    $scope.$watch('fbReady',function(){
        console.log("fbready changed to:" + $scope.fbReady);
        if($scope.fbReady == true){
            $scope.fbUser = Facebook.getUser(FB);
        }
    });
    
    $scope.$on("currentUserChangedEvent", function(event, args) {
        var token = $rootScope.currentUser.token;
        $rootScope.currentUser = args.user;
        $rootScope.currentUser.token = token;
        localStorageService.set('currentUser', $rootScope.currentUser);
        
    });
    
  });