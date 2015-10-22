'use strict';

app.controller('UsersCtrl', function ($scope, User){
    $scope.users = {};
    User.query(function(data) {
        angular.forEach(data, function(user, key) {
            $scope.users[user.id] = user;
        });
    });
    $scope.user = {id:'', email: '', username: '', password: ''};
    
    $scope.submitUser = function () {
        User.save($scope.user, function (ref) {
          $scope.users[ref.id] = ref;
          $scope.user = {email: '', username: '', password: ''};
        });
      };
    
    $scope.deleteUser = function (userId) {
        User.delete({userId: userId}, function () {
          delete $scope.users[userId];
        });
      };
  });