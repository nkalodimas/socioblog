'use strict';
 
app.factory('Tag', function ($resource, $rootScope, SERVER_URL) {
  return $resource(SERVER_URL + '/tags/:tagId',{tagId:'@id'},{
      save: {
          method: 'POST',
          headers : {'Spring-Token' : $rootScope.getSpringToken }
      },
      remove: {
          method: 'DELETE',
          headers : {'Spring-Token' : $rootScope.getSpringToken }
      },
      update: {
          method: 'PUT',
          headers : {'Spring-Token' : $rootScope.getSpringToken }
      }
  });
});