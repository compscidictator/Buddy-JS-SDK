function Controller() {
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    var $ = this;
    var exports = {};
    $.__views.menuArea = Ti.UI.createView({
        height: Alloy.Globals.headerHeight,
        top: 0,
        left: 0,
        right: 0,
        backgroundImage: "/images/menu_background.png",
        zIndex: 10,
        id: "menuArea"
    });
    $.__views.menuArea && $.addTopLevelView($.__views.menuArea);
    $.__views.menuButtons = Ti.UI.createView({
        left: Alloy.Globals.btHeaderPadding,
        top: "20.49%",
        layout: "horizontal",
        id: "menuButtons"
    });
    $.__views.menuArea.add($.__views.menuButtons);
    $.__views.btHome = Ti.UI.createButton({
        width: Alloy.Globals.btHeaderWidth,
        height: Alloy.Globals.btHeaderWidth,
        right: Alloy.Globals.btHeaderWidth,
        backgroundImage: "/images/bt_home_on.png",
        id: "btHome"
    });
    $.__views.menuButtons.add($.__views.btHome);
    $.__views.btAreas = Ti.UI.createButton({
        width: Alloy.Globals.btHeaderWidth,
        height: Alloy.Globals.btHeaderWidth,
        right: Alloy.Globals.btHeaderWidth,
        backgroundImage: "/images/bt_areas_off.png",
        id: "btAreas"
    });
    $.__views.menuButtons.add($.__views.btAreas);
    $.__views.btSearch = Ti.UI.createButton({
        width: Alloy.Globals.btHeaderWidth,
        height: Alloy.Globals.btHeaderWidth,
        right: Alloy.Globals.btHeaderWidth,
        backgroundImage: "/images/bt_search_off.png",
        id: "btSearch"
    });
    $.__views.menuButtons.add($.__views.btSearch);
    $.__views.btNear = Ti.UI.createButton({
        width: Alloy.Globals.btHeaderWidth,
        height: Alloy.Globals.btHeaderWidth,
        right: Alloy.Globals.btHeaderWidth,
        backgroundImage: "/images/bt_near_off.png",
        id: "btNear"
    });
    $.__views.menuButtons.add($.__views.btNear);
    exports.destroy = function() {};
    _.extend($, $.__views);
    $.btHome.addEventListener("click", function() {
        Ti.App.fireEvent("home");
    });
    $.btAreas.addEventListener("click", function() {
        Ti.App.fireEvent("areas");
    });
    $.btNear.addEventListener("click", function() {
        Ti.App.fireEvent("near");
    });
    $.btSearch.addEventListener("click", function() {
        Ti.App.fireEvent("search");
    });
    Ti.App.addEventListener("menuOff", function() {
        $.btHome.backgroundImage = "/images/bt_home_off.png";
        $.btAreas.backgroundImage = "/images/bt_areas_off.png";
        $.btSearch.backgroundImage = "/images/bt_search_off.png";
        $.btNear.backgroundImage = "/images/bt_near_off.png";
    });
    Ti.App.addEventListener("homeOn", function() {
        $.btHome.backgroundImage = "/images/bt_home_on.png";
    });
    Ti.App.addEventListener("areasOn", function() {
        $.btAreas.backgroundImage = "/images/bt_areas_on.png";
    });
    Ti.App.addEventListener("searchOn", function() {
        $.btSearch.backgroundImage = "/images/bt_search_on.png";
    });
    Ti.App.addEventListener("nearOn", function() {
        $.btNear.backgroundImage = "/images/bt_near_on.png";
    });
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;