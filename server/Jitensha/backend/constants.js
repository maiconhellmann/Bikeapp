/*
 * Copyright (c) 2016, Crossover and/or its affiliates. All rights reserved.
 * CROSSOVER PROPRIETARY/CONFIDENTIAL.
 *
 *     https://www.crossover.com/
 */
// Fake data to be used throughout the server
global.user         = { email: "crossover@crossover.com", password: "crossover" };
global.token        = { accessToken: "AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA" };
global.invalidJson  = "0001A";
global.invalidCred  = "0001B";
global.invalidToken = "0001C";
global.badRequest   = 400;
global.unauthorized = 401;
global.forbidden    = 403;

global.invalidJsonFormat = function(res) {
    res.statusCode = global.badRequest;
    return res.send({ code: global.invalidJson, message: "Invalid Json format" });
};

global.invalidCredentials = function(res) {
    res.statusCode = global.unauthorized;
    return res.send({ code: global.invalidCred, message: "Invalid credentials" });
};

global.invalidAccessToken = function(res) {
    res.statusCode =  global.forbidden;
    return res.send({ code: global.invalidToken, message: "Invalid AccessToken" });
};