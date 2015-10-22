'use strict';
 
app.controller('UserViewCtrl', function ($scope, $routeParams, User) {
    User.getFull( { userId: $routeParams.userId }, function (ref) {
          $scope.user = ref;
        });
});