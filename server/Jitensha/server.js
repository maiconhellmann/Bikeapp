/*
 * Copyright (c) 2016, Crossover and/or its affiliates. All rights reserved.
 * CROSSOVER PROPRIETARY/CONFIDENTIAL.
 *
 *     https://www.crossover.com/
 */
var http = require('http');
var app = module.exports = require('express')();
var bodyParser = require("body-parser");
var users = require('./backend/users/users');
var payment = require('./backend/payment/payment');
var places = require('./backend/places/places');
require('./backend/constants');

var port = process.env.PORT || 8080;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

function accessTokenValidator (req, res, next) {
    var auth = req.get('Authorization');
    var accessToken = global.token.accessToken;
    if (auth !== accessToken) {
        return global.invalidAccessToken(res);
    }
    next();
};

app.all('/api/v1/rent', accessTokenValidator);
app.all('/api/v1/places', accessTokenValidator);

app.get('/api/v1', function(req, res) {
    res.json({ 'service' : 'じてんしゃ - Saitama Rent a bike' });
});

app.post('/api/v1/auth', users.auth);
app.post('/api/v1/register', users.register);
app.post('/api/v1/rent', payment.rent);
app.get('/api/v1/places', places.list);

http.createServer(app)
    .listen((port), function() {
    console.log("Listening on localhost:" + port);
});
