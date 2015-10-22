'use strict';
 
app.controller('MyAccountCtrl', function ($scope, $rootScope, $routeParams, User) {
    $scope.editable = false;
    console.log('inside my acc ctrl');
    User.getFull( { userId: $rootScope.currentUser.id }, function (ref) {
        $scope.user = ref;
    });
    
    $scope.editUser = function(){
        if($scope.editable == true) {
            User.update($scope.user,function(data) {
                $scope.editable = false;
                $rootScope.$broadcast("currentUserChangedEvent", {'user': data});
            });
        }
        else {
            $scope.editable = true;
        }
    };
});