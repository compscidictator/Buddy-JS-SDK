var Alloy = require("alloy");

var Debug = function() {};

Debug.echo = function(s, line, page, type) {
    if (Alloy.CFG.isDebug) {
        var debugType = Alloy.CFG.msgType;
        var msgTitle = "[CB Debug Message]";
        null !== page && void 0 !== page && (msgTitle = "[CB Debug Message in " + page + " ]");
        null !== line && void 0 !== line && (msgTitle += " Line " + line + " : ");
        null !== type && void 0 !== type && (debugType = type);
        Ti.API.warn(msgTitle);
        Ti.API[debugType](s);
    }
};

Debug.dump = function(o, line, page, type) {
    if (Alloy.CFG.isDebug) {
        var debugType = Alloy.CFG.msgType;
        var msgTitle = "[CB Debug Dump Object]";
        null !== page && void 0 !== page && (msgTitle = "[CB Debug Dump Object in " + page + " ]");
        false !== line && (msgTitle += " Line " + line);
        null !== type && void 0 !== type && (debugType = type);
        Ti.API.warn(msgTitle);
        o ? Ti.API[debugType](JSON.stringify(o)) : Ti.API[debugType](o);
    }
};

module.exports = Debug;