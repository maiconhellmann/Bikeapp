/*
 * Copyright (c) 2016, Crossover and/or its affiliates. All rights reserved.
 * CROSSOVER PROPRIETARY/CONFIDENTIAL.
 *
 *     https://www.crossover.com/
 */
var fs = require('fs');
var file = './backend/places/bikes.json';

function readJsonFile() {
    return fs.readFileSync(file, 'utf8');
}

exports.list = function(req, res) {
    return res.send(JSON.parse(readJsonFile()));
};