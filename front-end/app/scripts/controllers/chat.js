'use strict';

app.controller('ChatCtrl', function ($scope, $rootScope, SERVER_URL){

    $scope.message = "";
    $scope.messages = [];
    
    $scope.logged = false;
    var socket = $.atmosphere;
    var subSocket;
    var transport = 'websocket';
    var request = new socket.AtmosphereRequest();


    request.url = SERVER_URL+'/chat/subscribe';
    request.method = "GET";
    request.contentType = "application/json";
    request.shared = true;
    request.transport = transport;
    request.reconnectInterval = 5000;
    request.fallbackTransport = 'long-polling';

    request.onOpen = function(response) {
        console.log("--> onOpen(): ", response);
        transport = response.transport;
    };

    request.onTransportFailure = function(errorMsg, request) {
        logger.info("--> onTransportFailure(): ", errorMsg, request);
    };

    request.onMessage = function(response) {
        console.log("--> onMessage(): ", response);
        $scope.$apply(function () {
            var jsonResp = JSON.parse(response.responseBody);
            if(angular.isArray(jsonResp)) {
                angular.forEach(jsonResp, function(value){
                    if(value.time)
                    $scope.messages.unshift(value);
                });
            }
            else {
                $scope.messages.unshift(jsonResp);
            }
        });
    };

    request.onClose = function(response) {
        console.log("--> onClose():", response);
        $scope.$apply(function () {
            $scope.messages = [];
            $scope.logged = false;
        });
    };

    request.onError = function(data) {
        console.error('--> onError():', data);
    };

    subSocket = socket.subscribe(request);
    
    $scope.sendMessage = function(){
        var author = $rootScope.loggedIn() ? $rootScope.currentUser.username : "unknown";
        var msg = {};
        msg.author = author;
        msg.body = $scope.message;
        msg.time = new Date().getTime();
        subSocket.push(JSON.stringify(msg));
        $scope.message = "";
    };
});