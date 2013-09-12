var Alloy = require("alloy"), net = require("net"), util = require("util"), debug = require("debug"), xml2json = require("xml2json");

var WP = function() {};

var genStructTag = function(tags) {
    var xmldata = "<struct>";
    for (var s in tags) if (s) {
        var struct = tags[s];
        xmldata += "<member><name>" + struct.name + "</name>";
        xmldata += "array" == struct.type ? "<value>" + genArrayTag(struct.value) + "</value>" : "<value><" + struct.type + ">" + struct.value + "</" + struct.type + "></value>";
        xmldata += "</member>";
    }
    xmldata += "</struct>";
    return xmldata;
};

var genArrayTag = function(tags) {
    var xmldata = "<array><data>";
    for (var s in tags) if (s) {
        var arr = tags[s];
        xmldata += "struct" == arr.type ? genStructTag(arr.value) : "<value><" + arr.type + ">" + arr.value + "</" + arr.type + "></value>";
    }
    xmldata += "</array></data>";
    return xmldata;
};

WP.xmlRPC = function(args) {
    args.url || (args.url = Alloy.CFG.webService + Alloy.CFG.xmlrpcService);
    var xmldata = "<methodCall>";
    xmldata += "<methodName>" + args.method + "</methodName>";
    xmldata += "<params>";
    for (var k in args.params) if (k) {
        var p = args.params[k];
        xmldata += "<param>";
        switch (p.type) {
          case "struct":
            xmldata += genStructTag(p.value);
            break;

          case "array":
            xmldata += genArrayTag(p.value);
            break;

          default:
            xmldata += "<" + p.type + ">" + p.value + "</" + p.type + ">";
        }
        xmldata += "</param>";
    }
    xmldata += "</params></methodCall>";
    debug.echo(xmldata, 113, "wp.js");
    var requestObj = {
        url: args.url,
        data: xmldata,
        type: "POST",
        notToJSON: true,
        onerror: function(d) {
            debug.dump(d, 121, "wp.js error");
        },
        onload: function(result) {
            debug.dump(result, 125, "wp.js");
            var returnObj = xml2json.parser(result, args.startLevel);
            util.actInd.hide();
            args.callback && args.callback(returnObj);
        }
    };
    util.actInd.show();
    net.request(requestObj);
};

module.exports = WP;