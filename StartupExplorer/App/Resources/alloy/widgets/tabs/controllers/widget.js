function WPATH(s) {
    var index = s.lastIndexOf("/");
    var path = -1 === index ? "tabs/" + s : s.substring(0, index) + "/tabs/" + s.substring(index + 1);
    return path;
}

function Controller() {
    function doTab(name, offset, noEvent) {
        _.each([ "home", "agenda", "post", "stream", "venue" ], function(item) {
            "post" !== item && ($[item + "Icon"].image = name === item ? WPATH("/btn-" + item + "-pressed.png") : WPATH("/btn-" + item + "-default.png"));
        });
        noEvent || $.trigger("change", {
            name: name
        });
    }
    new (require("alloy/widget"))("tabs");
    this.__widgetId = "tabs";
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "widget";
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    arguments[0] ? arguments[0]["__itemTemplate"] : null;
    var $ = this;
    var exports = {};
    $.__views.root = Ti.UI.createView({
        backgroundImage: WPATH("/bg-tab.png"),
        height: "46dp",
        bottom: 0,
        id: "root"
    });
    $.__views.root && $.addTopLevelView($.__views.root);
    $.__views.home = Ti.UI.createView({
        zIndex: 10,
        width: "20%",
        id: "home"
    });
    $.__views.root.add($.__views.home);
    $.__views.homeIcon = Ti.UI.createImageView({
        image: WPATH("/btn-home-pressed.png"),
        id: "homeIcon"
    });
    $.__views.home.add($.__views.homeIcon);
    $.__views.agenda = Ti.UI.createView({
        zIndex: 10,
        width: "20%",
        id: "agenda"
    });
    $.__views.root.add($.__views.agenda);
    $.__views.agendaIcon = Ti.UI.createImageView({
        image: WPATH("/btn-agenda-default.png"),
        id: "agendaIcon"
    });
    $.__views.agenda.add($.__views.agendaIcon);
    $.__views.post = Ti.UI.createView({
        zIndex: 10,
        width: "20%",
        id: "post"
    });
    $.__views.root.add($.__views.post);
    $.__views.postIcon = Ti.UI.createButton({
        height: "46dp",
        width: "64dp",
        backgroundImage: WPATH("/btn-post-default.png"),
        backgroundSelectedImage: WPATH("/btn-post-pressed.png"),
        id: "postIcon"
    });
    $.__views.post.add($.__views.postIcon);
    $.__views.stream = Ti.UI.createView({
        zIndex: 10,
        width: "20%",
        id: "stream"
    });
    $.__views.root.add($.__views.stream);
    $.__views.streamIcon = Ti.UI.createImageView({
        image: WPATH("/btn-stream-default.png"),
        id: "streamIcon"
    });
    $.__views.stream.add($.__views.streamIcon);
    $.__views.venue = Ti.UI.createView({
        zIndex: 10,
        width: "20%",
        id: "venue"
    });
    $.__views.root.add($.__views.venue);
    $.__views.venueIcon = Ti.UI.createImageView({
        image: WPATH("/btn-venue-default.png"),
        id: "venueIcon"
    });
    $.__views.venue.add($.__views.venueIcon);
    exports.destroy = function() {};
    _.extend($, $.__views);
    Ti.Platform.displayCaps.platformWidth / 5;
    var tabPositions = {
        home: 0,
        agenda: "20%",
        post: "40%",
        stream: "60%",
        venue: "80%"
    };
    $.home.left = tabPositions.home;
    $.agenda.left = tabPositions.agenda;
    $.post.left = tabPositions.post;
    $.stream.left = tabPositions.stream;
    $.venue.left = tabPositions.venue;
    $.home.on("click", function() {
        doTab("home", tabPositions.home);
    });
    $.agenda.on("click", function() {
        doTab("agenda", tabPositions.agenda);
    });
    $.postIcon.on("click", function() {
        $.trigger("change", {
            name: "post"
        });
    });
    $.stream.on("click", function() {
        doTab("stream", tabPositions.stream);
    });
    $.venue.on("click", function() {
        doTab("venue", tabPositions.venue);
    });
    $.setTab = function(name) {
        doTab(name, tabPositions[name], true);
    };
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;