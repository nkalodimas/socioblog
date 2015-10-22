'use strict';

app.controller('PostCreateCtrl', function ($scope, $location, $rootScope, Post){
    
    $scope.post = {title: '', body: ''};
    $scope.share = true;

    $scope.submitPost = function () {
        Post.save({},$scope.post,
                  function (ref) {
                    $scope.post = {title: '', body: ''};
                    $rootScope.$broadcast("tagsChangedEvent",{});
                    $location.path('/posts');
                  },
                  function (error){
                      $scope.error = error.toString();
                      console.error(error.toString());
                  });
      };
  });