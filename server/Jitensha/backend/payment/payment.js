/*
 * Copyright (c) 2016, Crossover and/or its affiliates. All rights reserved.
 * CROSSOVER PROPRIETARY/CONFIDENTIAL.
 *
 *     https://www.crossover.com/
 */
var isvalid = require('isvalid');
var schema = { 'number':     { type: String, required: true },
               'name':       { type: String, required: true },
               'expiration': { type: String, required: true },
               'code':       { type: String, required: true }
             };

exports.rent = function(req, res) {
    isvalid(req.body, schema, function(err, data) {
        if (err) {
            return global.invalidJsonFormat(res);
        } else {
            return res.send({ message: "Rent operation has been completed" });
        }
    });
};