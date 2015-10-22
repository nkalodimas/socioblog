'use strict';

app.controller('TagsCtrl', function ($scope, $routeParams, Tag, Post){

    if($routeParams.tagTitle) {
    
        $scope.posts = [];
        $scope.previewChars = 5;

        var pageNum = $routeParams.pageNumber || 1;

        $scope.currentPage = pageNum;

        $scope.tagTitle = $routeParams.tagTitle || "";

        Post.byTag({tagTitle:$scope.tagTitle, pageNumber:pageNum}, function(data) {
            $scope.posts = data.posts;
            $scope.totalPages = data.totalPages;
            $scope.totalPosts = data.totalPosts;
            $scope.tag = data.tag;
        });

        $scope.showNewerPosts = function (){
            return $scope.currentPage > 1;
        };

        $scope.showOlderPosts = function (){
            return $scope.currentPage < $scope.totalPages;
        };

        $scope.showNewerPostsUrl = function (){
            return "#/posts/" + $scope.tag + "/" + ($scope.currentPage - 1);
        };

        $scope.showOlderPostsUrl = function (){
            return "#/posts/" + $scope.tag + "/" + ($scope.currentPage + 1);
        };
    }
    else {
        $scope.tags = [];
        Tag.query(function(data) {
            $scope.tags = data;
        });
        
        $scope.$on("tagsChangedEvent", function(event, args) {
            Tag.query(function(data) {
                $scope.tags = data;
            });
        });
    }

  });