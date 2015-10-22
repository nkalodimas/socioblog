'use strict';

app.controller('UserPostsCtrl', function ($scope, $routeParams, Tag, Post){
    
        $scope.posts = [];
        $scope.previewChars = 5;

        var pageNum = $routeParams.pageNumber || 1;

        $scope.currentPage = pageNum;

        $scope.userId = $routeParams.userId || "";

        Post.byUser({userId:$scope.userId, pageNumber:pageNum}, function(data) {
            $scope.posts = data.posts;
            $scope.totalPages = data.totalPages;
            $scope.totalPosts = data.totalPosts;
            $scope.user = data.user;
        });

        $scope.showNewerPosts = function (){
            return $scope.currentPage > 1;
        };

        $scope.showOlderPosts = function (){
            return $scope.currentPage < $scope.totalPages;
        };

        $scope.showNewerPostsUrl = function (){
            return "#/users/" + $scope.userId + "/posts/" + ($scope.currentPage - 1);
        };

        $scope.showOlderPostsUrl = function (){
            return "#/users/" + $scope.userId + "/posts/" + (parseInt($scope.currentPage, 10) + 1);
        };
  });