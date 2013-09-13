function Controller() {
    function mapClick(loc) {
        Alloy.Globals.CB.Util.startLoading();
        Alloy.Globals.CB.pushController({
            controller: "tab",
            action: Alloy.Globals.CB.UI.NavAction.KeepBack,
            animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
            data: [ {
                region: loc
            } ],
            showInd: false
        });
        Ti.App.fireEvent("menuOff");
        Ti.App.fireEvent("areasOn");
    }
    require("alloy/controllers/base").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "home";
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    arguments[0] ? arguments[0]["__itemTemplate"] : null;
    var $ = this;
    var exports = {};
    $.__views.mainWindow = Ti.UI.createView({
        top: 0,
        width: Alloy.Globals.width,
        bottom: 0,
        backgroundImage: "/images/background.png",
        id: "mainWindow"
    });
    $.__views.mainWindow && $.addTopLevelView($.__views.mainWindow);
    $.__views.logo = Ti.UI.createImageView({
        image: "/images/se_logo.png",
        top: "3%",
        width: "24%",
        id: "logo"
    });
    $.__views.mainWindow.add($.__views.logo);
    $.__views.btArea = Ti.UI.createView({
        left: 0,
        right: 0,
        bottom: Alloy.Globals.footerHeight,
        height: Alloy.Globals.btAreaHeight,
        layout: "vertical",
        id: "btArea"
    });
    $.__views.mainWindow.add($.__views.btArea);
    $.__views.__alloyId6 = Ti.UI.createView({
        left: 0,
        right: 0,
        bottom: 1,
        height: "33%",
        width: Alloy.Globals.btDashWidth,
        backgroundColor: "#d65b5d",
        style: Ti.UI.iPhone.SystemButtonStyle.PLAIN,
        id: "__alloyId6"
    });
    $.__views.btArea.add($.__views.__alloyId6);
    $.__views.btNearMe = Ti.UI.createImageView({
        image: "/images/bt_nearme.png",
        id: "btNearMe"
    });
    $.__views.__alloyId6.add($.__views.btNearMe);
    $.__views.__alloyId7 = Ti.UI.createView({
        left: 0,
        right: 0,
        bottom: 1,
        height: "33%",
        width: Alloy.Globals.btDashWidth,
        backgroundColor: "#d65b5d",
        style: Ti.UI.iPhone.SystemButtonStyle.PLAIN,
        id: "__alloyId7"
    });
    $.__views.btArea.add($.__views.__alloyId7);
    $.__views.btNewest = Ti.UI.createImageView({
        image: "/images/bt_newest.png",
        id: "btNewest"
    });
    $.__views.__alloyId7.add($.__views.btNewest);
    $.__views.__alloyId8 = Ti.UI.createView({
        left: 0,
        right: 0,
        bottom: 1,
        height: "33%",
        width: Alloy.Globals.btDashWidth,
        backgroundColor: "#d65b5d",
        style: Ti.UI.iPhone.SystemButtonStyle.PLAIN,
        id: "__alloyId8"
    });
    $.__views.btArea.add($.__views.__alloyId8);
    $.__views.btAdd = Ti.UI.createImageView({
        image: "/images/bt_add.png",
        id: "btAdd"
    });
    $.__views.__alloyId8.add($.__views.btAdd);
    exports.destroy = function() {};
    _.extend($, $.__views);
    exports.baseController = "base";
    $.btAdd.addEventListener("click", function() {
        Alloy.Globals.CB.pushController({
            controller: "add",
            action: Alloy.Globals.CB.UI.NavAction.KeepBack,
            animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
            data: []
        });
        Ti.App.fireEvent("menuOff");
    });
    $.btNearMe.addEventListener("click", function() {
        Ti.App.fireEvent("near");
    });
    $.btNewest.addEventListener("click", function() {
        Ti.App.fireEvent("newest");
    });
    var areaClean = null;
    var regionData = [];
    var xhrRegion = Ti.Network.createHTTPClient({
        cache: true
    });
    xhrRegion.onload = function() {
        function dynamicSort(property) {
            var sortOrder = 1;
            if ("-" === property[0]) {
                sortOrder = -1;
                property = property.substr(1, property.length - 1);
            }
            return function(a, b) {
                var result = 1 * a[property] > 1 * b[property] ? -1 : 1 * a[property] < 1 * b[property] ? 1 : 0;
                return result * sortOrder;
            };
        }
        regionData = JSON.parse(this.responseText);
        regionData.data = regionData.data.sort(dynamicSort("startupCount"));
        var mapViewArea = Ti.UI.createScrollableView({
            id: "mapArea",
            showPagingControl: true,
            top: Alloy.Globals.btDashHeight,
            bottom: Alloy.Globals.scrollableMapArea,
            pagingControlColor: "transparent",
            color: "#000",
            views: []
        });
        var mapTotalArea = [];
        var mapTotalLabel = [];
        var mapImage = [];
        var areas = [];
        for (var i = 0; regionData.data.length > i; i++) {
            regionArea = regionData.data[i];
            areas[i] = regionArea.metroName.replace(/ /g, "%20");
            Alloy.Globals.metroAreas[i] = Ti.UI.createPickerRow({
                title: regionArea.metroName,
                value: regionArea.metroName.replace(/ /g, "%20")
            });
            mapView = Ti.UI.createView({
                id: areaClean
            });
            mapImage[i] = Ti.UI.createImageView({
                image: regionArea.iconURL,
                width: Alloy.Globals.mapWidth,
                myID: areas[i]
            });
            mapTotalArea[i] = Ti.UI.createView({
                backgroundColor: "#3cd5d0",
                height: Alloy.Globals.mapTotalWidth,
                width: Alloy.Globals.mapTotalWidth,
                opacity: ".9",
                top: "10%",
                left: "12%",
                zIndex: 999,
                borderRadius: Alloy.Globals.mapTotalRadius,
                touchEnabled: false
            });
            mapTotalLabel[i] = Ti.UI.createLabel({
                font: {
                    fontFamily: "SurfLight",
                    fontSize: "34dp"
                },
                color: "#181717",
                text: regionArea.startupCount,
                id: "sum" + i
            });
            mapTotalArea[i].add(mapTotalLabel[i]);
            mapView.add(mapTotalArea[i]);
            mapView.add(mapImage[i]);
            (function(view) {
                view.addEventListener("click", function() {
                    mapClick(this.myID);
                });
            })(mapImage[i]);
            mapViewArea.addView(mapView);
        }
        $.mainWindow.add(mapViewArea);
    };
    xhrRegion.onerror = function() {
        Titanium.API.info("Error grabbing buddy data");
        alert("Internet connection is required, please check your device settings.");
    };
    xhrRegion.open("GET", "https://webservice.buddyplatform.com/Service/v1/BuddyService.ashx?StartupData_Location_GetMetroList&BuddyApplicationName=Startup%20Explorer&BuddyApplicationPassword=" + Alloy.Globals.applicationPassword);
    xhrRegion.send();
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;