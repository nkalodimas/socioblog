'use strict';

app.controller('AdminCtrl', function ($scope, $filter, $rootScope, User, Tag){
    $scope.users = [];
    User.query(function(data) {
        $scope.users = data;
    });
    //$scope.user = {id:'', email: '', username: '', password: ''};
    
    $scope.submitUser = function () {
        User.save($scope.user, function (ref) {
          $scope.users.push(ref);
          //$scope.user = {email: '', username: '', password: ''};
        });
      };
    
    $scope.deleteUser = function (userId) {
        User.remove({userId: userId}, function () {
          $scope.users = $filter('filter')($scope.users, {id: !userId});
        });
    };
    
    $scope.tags = [];

    Tag.query(function(data) {
        $scope.tags = data;
    });

    $scope.tag = {title: '', description: ''};
    
    $scope.submitTag = function () {
        Tag.save($scope.tag, function (ref) {
          $scope.tags.push(ref);
          $scope.tag = {title: '', description: ''};
          $rootScope.$broadcast("tagsChangedEvent",{});
        });
      };
    
    $scope.deleteTag = function (tagId) {
        Tag.remove({tagId: tagId}, function () {
          $scope.tags = $filter('filter')($scope.tags, {id: !tagId});
          $rootScope.$broadcast("tagsChangedEvent",{});
        });
    };
  });