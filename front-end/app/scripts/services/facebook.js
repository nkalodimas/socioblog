'use strict';

app.service('Facebook', function($q, $rootScope) {
       
      return {
            getUser: function(FB) {
                var user = {};
                user.connected = null;
                  FB.getLoginStatus(function(response) {
                    if (response.status == 'connected') {
                        user.connected = true;
                        user.fbToken = response.authResponse.accessToken;
                    } else {
                        user.connected = false;
                    }
                  });
                return user;
            }
          };
        });