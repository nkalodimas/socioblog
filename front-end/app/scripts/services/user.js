'use strict';
 
app.factory('User', function ($resource, $rootScope, SERVER_URL) {
  return $resource(SERVER_URL + '/users/:userId',{userId:'@id'},{
      save: {
          method: 'POST',
          headers : {'Spring-Token' : $rootScope.getSpringToken }
      },
      remove: {
          method: 'DELETE',
          headers : {'Spring-Token' : $rootScope.getSpringToken }
      },
      getFull: {
          method: 'GET',
          url: SERVER_URL + '/users/full/:userId',
          params: {userId: '@userId',},
      },
      update: {
          method: 'PUT',
          headers : {'Spring-Token' : $rootScope.getSpringToken }
      }
  });
});