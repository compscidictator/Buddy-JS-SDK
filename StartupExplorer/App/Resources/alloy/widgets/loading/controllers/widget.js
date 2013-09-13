function WPATH(s) {
    var index = s.lastIndexOf("/");
    var path = -1 === index ? "loading/" + s : s.substring(0, index) + "/loading/" + s.substring(index + 1);
    return path;
}

function Controller() {
    new (require("alloy/widget"))("loading");
    this.__widgetId = "loading";
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "widget";
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    arguments[0] ? arguments[0]["__itemTemplate"] : null;
    var $ = this;
    var exports = {};
    $.__views.widget = Ti.UI.createView({
        id: "widget"
    });
    $.__views.widget && $.addTopLevelView($.__views.widget);
    $.__views.backdrop = Ti.UI.createView({
        backgroundColor: "#232323",
        opacity: .75,
        id: "backdrop"
    });
    $.__views.widget.add($.__views.backdrop);
    $.__views.container = Ti.UI.createView({
        id: "container"
    });
    $.__views.widget.add($.__views.container);
    $.__views.loader = Ti.UI.createImageView({
        id: "loader"
    });
    $.__views.container.add($.__views.loader);
    $.__views.message = Ti.UI.createLabel({
        color: "#ebebeb",
        textAlign: "center",
        height: Ti.UI.SIZE,
        width: Ti.UI.SIZE,
        font: {
            fontSize: "12dp",
            fontWeight: "bold",
            fontFamily: "Quicksand-Bold"
        },
        id: "message"
    });
    $.__views.container.add($.__views.message);
    exports.destroy = function() {};
    _.extend($, $.__views);
    $.loader.images = [ WPATH("/load-cloud1.png"), WPATH("/load-cloud2.png"), WPATH("/load-cloud3.png"), WPATH("/load-cloud4.png"), WPATH("/load-cloud5.png"), WPATH("/load-cloud6.png"), WPATH("/load-cloud7.png"), WPATH("/load-cloud8.png"), WPATH("/load-cloud9.png"), WPATH("/load-cloud10.png"), WPATH("/load-cloud11.png") ];
    exports.start = function() {
        $.loader.start();
    };
    exports.stop = function() {
        $.loader.stop();
    };
    exports.setMessage = function(key) {
        $.message.text = key;
    };
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;