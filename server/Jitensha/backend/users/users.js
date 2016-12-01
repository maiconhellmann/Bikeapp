/*
 * Copyright (c) 2016, Crossover and/or its affiliates. All rights reserved.
 * CROSSOVER PROPRIETARY/CONFIDENTIAL.
 *
 *     https://www.crossover.com/
 */
var isvalid = require('isvalid');
var schema = { 'email':    { type: String, required: true },
               'password': { type: String, required: true } };

exports.auth = function(req, res) {
    isvalid(req.body, schema, function(err, data) {
        if (err) {
            return global.invalidJsonFormat(res);
        } else {
            var body = req.body;
            if ((body.email !== global.user.email) ||
                (body.password !== global.user.password)) {
                return global.invalidCredentials(res);
            } else {
                return res.send(global.token);
            }
        }
    });
};

exports.register = function(req, res) {
	isvalid(req.body, schema, function(err, data) {
		if (err) {
			return global.invalidJsonFormat(res);
		}
		return res.send(global.token);	
	});
}
