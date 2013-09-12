function addTo(to, headers) {
    if (!to || !to.length) return;
    headers.to = headers.to || [];
    _.isString(to) ? headers.to.push(to) : _.isArray(to) && (headers.to = to);
}

function addSubVal(key, val, headers) {
    if (!key || !val) return;
    headers.sub = headers.sub || {};
    _.isString(val) ? headers.sub[key] = [ val ] : _.isArray(val) && (headers.sub[key] = val);
}

function addSubObj(sub, headers) {
    if (!sub) return;
    headers.sub = headers.sub || {};
    _.extend(headers.sub, sub);
}

function setUniqueArgs(obj, headers) {
    if (!obj) return;
    headers.unique_args = obj;
}

function setCategory(category, headers) {
    if (!category) return;
    _.isString(category) && (headers.category = category);
}

function addFilterSetting(filter, setting, val, headers) {
    if (!filter || !setting || !val) return;
    headers.filters = headers.filters || {};
    headers.filters[filter] = headers.filters[filter] || {};
    headers.filters[filter].settings = headers.filters[filter].settings || {};
    headers.filters[filter].settings[setting] = val;
}

function addFilterSettings(filter, settings, headers) {
    if (!filter || !settings) return;
    Object.keys(settings).forEach(function(setting) {
        addFilterSettings(filter, setting, settings[setting], headers);
    });
}

var sendgrid = exports;

var _ = require("underscore");

var Headers;

sendgrid.Headers = Headers = function(defaults) {
    this.headers = {};
    if (defaults) {
        addTo(defaults.to, this.headers);
        addSubObj(defaults.sub, this.headers);
        setUniqueArgs(defaults.unique, this.headers);
        setCategory(defaults.category, this.headers);
        defaults.filters && Object.keys(defaults.filters).forEach(function(filter) {
            addFilterSettings(filter, defaults.filters[filter], this.headers);
        });
    }
};

Headers.prototype.toString = function() {
    return JSON.stringify(this.headers);
};

Headers.prototype.addTo = function(to) {
    addTo(to, this.headers);
};

Headers.prototype.addSubVal = function(key, val) {
    addSubVal(key, val, this.headers);
};

Headers.prototype.setUniqueArgs = function(args) {
    setUniqueArgs(args, this.headers);
};

Headers.prototype.setCategory = function(category) {
    setCategory(category, this.headers);
};

Headers.prototype.addFilterSetting = function(filter, setting, val) {
    addFilterSetting(filter, setting, val, this.headers);
};