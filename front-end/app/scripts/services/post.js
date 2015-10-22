'use strict';
 
app.factory('Post', function ($resource, $rootScope, SERVER_URL) {
  return $resource(SERVER_URL + '/posts/:postId',{postId:'@id'},{
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
      },
      page: {
          method: 'GET',
          url: SERVER_URL + '/posts/page/:pageNumber',
          params: {pageNumber: '@pageNumber'},
      },
      search: {
          method: 'GET',
          url: SERVER_URL + '/posts/search/:searchTerm/:pageNumber',
          params: {searchTerm: '@searchTerm',
                   pageNumber: '@pageNumber'},
      },
      byUser: {
          method: 'GET',
          url: SERVER_URL + '/posts/user/:userId/:pageNumber',
          params: {userId: '@userId',
                   pageNumber: '@pageNumber'},
      },
      byTag: {
          method: 'GET',
          url: SERVER_URL + '/posts/:tagTitle/:pageNumber',
          params: {tagTitle: '@tagTitle',
                   pageNumber: '@pageNumber'},
      },
      addComment: {
          method: 'POST',
          url: SERVER_URL + '/posts/:postId/comment',
          params: {postId: '@postId'},
          headers : {'Spring-Token' : $rootScope.getSpringToken },
      },
      addTags: {
          method: 'POST',
          url: SERVER_URL + '/posts/:postId/tags',
          params: {postId: '@postId'},
          headers : {'Spring-Token' : $rootScope.getSpringToken }
      },
      share: {
          method: 'POST',
          url: SERVER_URL + '/posts/:postId/share',
          params: {postId: '@postId'},
          headers : {'Spring-Token' : $rootScope.getSpringToken }
      }
  });
});