'use strict';

app.factory('Auth',
  function ($http, $q, SERVER_URL, $rootScope, localStorageService) {
    var Auth = {
      signUp: function (socialId, accessToken) {
        var request = $http({
                        method: "post",
                        url: SERVER_URL + '/users/signup',
                        data: {
                            'access-Token': accessToken,
                            'socialId': socialId
                        }
                    });
 
        return( request.then( handleSuccess, handleError ) );
      },
      login: function (socialId, accessToken) {
        var request = $http({
                        method: "post",
                        url: SERVER_URL + '/users/login',
                        data: {
                            'access-Token': accessToken,
                            'socialId': socialId
                        }
                    });
 
        return( request.then( handleSuccess, handleError ) );
      },
      isLoggedIn: function () {
        var request = $http({
                        method: "post",
                        url: SERVER_URL + '/users/status',
                        headers : {'Spring-Token' : $rootScope.getSpringToken }
                    });
 
        return( request.then( handleSuccess, handleError ) );
      },
    };

    $rootScope.loggedIn = function () {
      return ( ($rootScope.currentUser !== undefined) && ($rootScope.userReady == true) ) ;
    };

    $rootScope.isAdmin = function () {
      return $rootScope.loggedIn(); 
    };

    $rootScope.getSpringToken = function () {
        if($rootScope.currentUser !== undefined)
            return $rootScope.currentUser.token;
        else
            return '';
    }

    return Auth;
      
    function handleError( response ) {

        if (
            ! angular.isObject( response.data ) ||
            ! response.data.message
            ) {

            return( $q.reject( "An unknown error occurred." ) );

        }

        // Otherwise, use expected error message.
        return( $q.reject( response.data.message ) );

    }


    // I transform the successful response, unwrapping the application data
    // from the API response payload.
    function handleSuccess( response ) {

        return( response.data );

    }
 
  });