'use strict';
 
app.controller('PostViewCtrl', function ($scope, $routeParams, $rootScope, $location, Post, Tag) {
    
    $scope.editable = false;
    $scope.addingTags = false;
    $scope.selectedValues = [];
    
    Post.get( { postId: $routeParams.postId }, function (ref) {
          $scope.post = ref;
          $scope.shared = false;
        });

    $scope.comment = {body: ''};

    $scope.submitComment = function(){
        Post.addComment({postId:$scope.post.id}, $scope.comment, function(data) {
            $scope.post.comments.push(data);
            $scope.comment = {body: ''};
        });
    }
    
    $scope.savePost = function(){
        $scope.editable = false;
        Post.update($scope.post, function(data) {
        });
    }
    
    $scope.editPost = function(){
        $scope.editable = true;
    }
    
    $scope.addTags = function(){
        if($scope.addingTags == false) {
            Tag.query({}, function(data) {
                $scope.tags = data;
                angular.forEach($scope.post.tags,function(postsTag) {
                    angular.forEach($scope.tags, function(tag,index) {
                        if(tag.id == postsTag.id) {
                            $scope.selectedValues.push($scope.tags[index]);
                        }
                    });
                });
                $scope.addingTags = true;
            });
        }
        else {
            var tagIds = [];
            angular.forEach($scope.selectedValues,function(tag) {
                tagIds.push(tag.id);
            });
            Post.addTags({postId:$scope.post.id}, tagIds, function(data) {
                $scope.addingTags = false;
                $scope.post.tags = $scope.selectedValues;
                $rootScope.$broadcast("tagsChangedEvent",{});
            });
        }
    }

    $scope.deletePost = function () {
        Post.remove({postId: $scope.post.id}, function () {
            $rootScope.$broadcast("tagsChangedEvent",{});
            $location.path('/');
        });
    };
    
    $scope.sharePost = function (socialId) {
        Post.share({postId: $scope.post.id},socialId, function () {
          $scope.shared = true;
        });
    };
});