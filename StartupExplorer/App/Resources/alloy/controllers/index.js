function Controller() {
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    var $ = this;
    var exports = {};
    $.__views.index = Ti.UI.createWindow({
        navBarHidden: true,
        exitOnClose: true,
        orientationModes: [ Titanium.UI.PORTRAIT ],
        backgroundColor: "#000",
        id: "index"
    });
    $.__views.index && $.addTopLevelView($.__views.index);
    exports.destroy = function() {};
    _.extend($, $.__views);
    $.main = Alloy.createController("main");
    Alloy.Globals.CB.init({
        index: $.index,
        main: $.main.getView("content")
    });
    $.index.addEventListener("android:back", function() {
        Alloy.Globals.CB.pushController({
            action: Alloy.Globals.CB.UI.NavAction.Back,
            animation: Alloy.Globals.CB.UI.AnimationStyle.NavRight
        });
    });
    $.index.add($.main.getView());
    $.main.onLoad();
    $.index.open();
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;