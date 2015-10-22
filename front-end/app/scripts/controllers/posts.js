'use strict';

app.controller('PostsCtrl', function ($scope, $routeParams, Post){
    $scope.posts = [];
    $scope.previewChars = 125;

    var pageNum = $routeParams.pageNumber || 1;
    $scope.currentPage = pageNum;
    Post.page({pageNumber:pageNum},function(data) {
        $scope.posts = data.posts;
        $scope.totalPages = data.totalPages;
        $scope.totalPosts = data.totalPosts;
    });
    
    $scope.showNewerPosts = function (){
        return $scope.currentPage > 1;
    }
    
    $scope.showNewerPostsUrl = function (){
        return "#/posts/page/" + ($scope.currentPage - 1);
    }
    
    $scope.showOlderPosts = function (){
        return $scope.currentPage < $scope.totalPages;
    }
    
    $scope.showOlderPostsUrl = function (){
        return "#/posts/page/" + ($scope.currentPage + 1);
    }
  });