var express = require('express');
var port = process.env.PORT || 3000;
var app = express();
app.use(express.static(__dirname + '/dist'));
app.use('/bower_components',  express.static(__dirname + '/app/bower_components'));
app.listen(port);