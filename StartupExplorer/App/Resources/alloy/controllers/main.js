function Controller() {
    function createPicker(type) {
        function makeButton(title) {
            var button = Ti.UI.createView(defaultButton);
            var label = Ti.UI.createLabel({
                text: title,
                color: "#a4a4a0",
                height: Alloy.Globals.pickerHeight,
                font: {
                    fontFamily: "TitilliumText25L-800wt",
                    fontSize: "11dp"
                }
            });
            button.add(label);
            return button;
        }
        function closePicker() {
            if ("Select a city..." != picker.getSelectedRow(0).title) {
                var areaClean = picker.getSelectedRow(0).title.replace(/ /g, "%20");
                if ("newest" == type) {
                    Alloy.Globals.CB.Util.startLoading();
                    pickerContainer.animate({
                        top: Alloy.Globals.height2,
                        opacity: 0,
                        duration: 300,
                        curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT
                    }, function() {
                        Alloy.Globals.CB.pushController({
                            controller: "tab",
                            action: Alloy.Globals.CB.UI.NavAction.KeepBack,
                            animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
                            data: [ {
                                region: areaClean,
                                type: "newest"
                            } ],
                            showInd: true
                        });
                        $.container.remove(pickerContainer);
                        pickerContainer = null;
                    });
                } else $.content.animate({
                    top: 0,
                    curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
                    duration: 100,
                    opacity: 1
                }, function() {
                    $.content.touchEnabled = true;
                    pickerContainer.animate({
                        top: Alloy.Globals.height2,
                        opacity: 0,
                        duration: 300,
                        curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT
                    }, function() {
                        Alloy.Globals.CB.Util.startLoading();
                        Alloy.Globals.CB.pushController({
                            controller: "tab",
                            action: Alloy.Globals.CB.UI.NavAction.KeepBack,
                            animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
                            data: [ {
                                region: areaClean
                            } ],
                            showInd: false
                        });
                        $.container.remove(pickerContainer);
                        pickerContainer = null;
                    });
                });
            }
        }
        var pickerContainer = Ti.UI.createView({
            top: Alloy.Globals.height2,
            height: Alloy.Globals.height2,
            width: Alloy.Globals.width,
            left: 0,
            right: 0,
            backgroundColor: "rgba(0,0,0,.8)",
            opacity: 0,
            zIndex: 1001
        });
        var picker = Ti.UI.createPicker();
        var none = Ti.UI.createPickerRow({
            title: "Select a city..."
        });
        picker.add(none);
        picker.add(Alloy.Globals.metroAreas);
        picker.selectionIndicator = true;
        var toolbar = Ti.UI.createView({
            backgroundColor: "#74807b",
            bottom: 216,
            height: Alloy.Globals.pickerHeight + 2 * Alloy.Globals.pickerPadding,
            width: Ti.UI.FILL
        });
        if ("android" != Ti.Platform.osname) {
            var defaultButton = {
                backgroundImage: "/images/picker_button_bg.png",
                width: Alloy.Globals.pickerWidth,
                height: Alloy.Globals.pickerHeight,
                top: Alloy.Globals.pickerPadding,
                bottom: Alloy.Globals.pickerPadding,
                left: Alloy.Globals.pickerPadding / 2,
                right: Alloy.Globals.pickerPadding / 2
            };
            var previous = makeButton("PREV");
            var next = makeButton("NEXT");
            var done = makeButton("DONE");
            var cancel = makeButton("CANCEL");
            var navButtons = Ti.UI.createView({
                layout: "horizontal",
                height: Ti.UI.SIZE,
                width: Ti.UI.SIZE,
                left: 0
            });
            var endButtons = Ti.UI.createView({
                layout: "horizontal",
                height: Ti.UI.SIZE,
                width: Ti.UI.SIZE,
                right: 0
            });
            navButtons.add(previous);
            navButtons.add(next);
            endButtons.add(cancel);
            endButtons.add(done);
            picker.bottom = 0;
            pickerContainer.add(picker);
            toolbar.add(navButtons);
            toolbar.add(endButtons);
            var currentIndex = 0;
            previous.addEventListener("click", function() {
                var previousIndex = currentIndex - 1;
                if (previousIndex >= 0) {
                    currentIndex -= 1;
                    picker.setSelectedRow(0, previousIndex, true);
                }
            });
            next.addEventListener("click", function() {
                var nextIndex = currentIndex + 1;
                if (Alloy.Globals.metroAreas.length + 1 > nextIndex) {
                    currentIndex += 1;
                    picker.setSelectedRow(0, nextIndex, true);
                }
            });
            cancel.addEventListener("click", function() {
                pickerContainer.animate({
                    top: Alloy.Globals.height2,
                    opacity: 0,
                    duration: 300,
                    curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT
                }, function() {
                    $.container.remove(pickerContainer);
                    pickerContainer = null;
                });
            });
            done.addEventListener("click", function() {
                closePicker();
            });
        } else {
            var overlay = Ti.UI.createView({
                top: toolbar.height,
                bottom: 0,
                left: 0,
                right: 0,
                backgroundColor: "#000",
                opacity: .8
            });
            pickerContainer.add(overlay);
            pickerContainer.backgroundColor = "transparent";
            toolbar.top = 0;
            pickerContainer.height = Ti.UI.FILL;
            toolbar.add(picker);
            picker.addEventListener("change", function() {
                closePicker();
            });
        }
        pickerContainer.add(toolbar);
        $.container.add(pickerContainer);
        pickerContainer.animate({
            top: 0,
            opacity: 1,
            duration: 300,
            curve: Titanium.UI.ANIMATION_CURVE_EASE_IN
        });
    }
    function locationSetting(e) {
        if (e.error) {
            Ti.API.error("geo - position" + e.error);
            return;
        }
        Alloy.Globals.userLatitude = e.coords.latitude;
        Alloy.Globals.userLongitude = e.coords.longitude;
    }
    function footerClick() {
        Ti.App.fireEvent("about");
    }
    require("alloy/controllers/base").apply(this, Array.prototype.slice.call(arguments));
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    var $ = this;
    var exports = {};
    var __defers = {};
    $.__views.main = Ti.UI.createView({
        id: "main"
    });
    $.__views.main && $.addTopLevelView($.__views.main);
    $.__views.container = Ti.UI.createView({
        backgroundColor: "#fff",
        opacity: 0,
        height: "100%",
        id: "container"
    });
    $.__views.main.add($.__views.container);
    $.__views.__alloyId10 = Alloy.createController("menu", {
        id: "__alloyId10",
        __parentSymbol: $.__views.container
    });
    $.__views.__alloyId10.setParent($.__views.container);
    $.__views.content = Ti.UI.createView({
        top: "0",
        left: 0,
        right: 0,
        height: Alloy.Globals.height2,
        zIndex: 100,
        backgroundImage: "/images/background.png",
        touchEnabled: true,
        opacity: 1,
        id: "content"
    });
    $.__views.container.add($.__views.content);
    $.__views.footer = Ti.UI.createImageView({
        image: "/images/buddy_footer.png",
        left: 0,
        right: 0,
        bottom: 0,
        height: Alloy.Globals.footerHeight,
        width: Alloy.Globals.width,
        zIndex: 101,
        id: "footer"
    });
    $.__views.container.add($.__views.footer);
    footerClick ? $.__views.footer.addEventListener("click", footerClick) : __defers["$.__views.footer!click!footerClick"] = true;
    $.__views.__alloyId11 = Alloy.createController("about", {
        id: "__alloyId11",
        __parentSymbol: $.__views.container
    });
    $.__views.__alloyId11.setParent($.__views.container);
    exports.destroy = function() {};
    _.extend($, $.__views);
    exports.baseController = "base";
    $.onLoad = function() {
        var firstController = Alloy.Globals.CB.getCurrentController();
        $.content.add(firstController.getView());
        $.container.animate({
            opacity: 1,
            duration: 250
        }, function() {
            firstController.onLoad();
        });
    };
    var showTour = Titanium.Filesystem.getFile(Titanium.Filesystem.applicationDataDirectory, "showTour.txt");
    if (showTour.exists()) Ti.API.info("Tour taken"); else {
        var tour = Ti.UI.createView({
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            zIndex: 102,
            opacity: 0,
            backgroundColor: "#000"
        });
        var tourImage = Ti.UI.createImageView({
            image: "/images/tour_background.png",
            top: 0,
            width: Alloy.Globals.width,
            zIndex: 1
        });
        var tourItems = Ti.UI.createView({
            layout: "vertical",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: "transparent",
            zIndex: 103
        });
        var tourLabel = Ti.UI.createLabel({
            font: {
                fontFamily: "SurfLight",
                fontSize: "32dp"
            },
            color: "#fff",
            top: "56%",
            zIndex: 100,
            text: "MENU WHERE?"
        });
        var tourText = Ti.UI.createLabel({
            font: {
                fontFamily: "TitilliumText25L-250wt",
                fontSize: "18dp"
            },
            color: "#fff",
            width: "80%",
            textAlign: "center",
            zIndex: 100,
            opacity: .5,
            text: "It’s there, just hidden. Like a four leaf clover on a field of grass. You get it! From the top edge, swipe down to show the menu."
        });
        var btSkip = Ti.UI.createButton({
            backgroundImage: "/images/bt_skip.png",
            width: Alloy.Globals.skipWidth,
            height: Alloy.Globals.skipHeight,
            top: 10,
            zIndex: 100
        });
        tourItems.add(tourLabel);
        tourItems.add(tourText);
        tourItems.add(btSkip);
        tour.add(tourImage);
        tour.add(tourItems);
        $.container.add(tour);
        btSkip.addEventListener("click", function() {
            $.container.top = 0;
            $.container.opacity = 1;
            tour.animate({
                opacity: 0,
                curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
                duration: 300
            }, function() {
                tour.visible = false;
                tour = null;
            });
            var showTour = Titanium.Filesystem.getFile(Titanium.Filesystem.applicationDataDirectory, "showTour.txt");
            showTour.write("Toured");
            var contents = showTour.read();
            Ti.API.info("Show Tour = " + contents);
        });
        tour.animate({
            opacity: 1,
            duration: 300,
            curve: Titanium.UI.ANIMATION_CURVE_EASE_IN
        });
    }
    $.container.addEventListener("swipe", function(e) {
        if ("down" == e.direction && 0 == $.content.top) {
            $.content.animate({
                top: Alloy.Globals.headerHeight,
                curve: Titanium.UI.ANIMATION_CURVE_EASE_IN,
                opacity: .6,
                duration: 100
            });
            $.content.touchEnabled = false;
        } else if ("up" == e.direction) {
            $.content.animate({
                top: 0,
                curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
                opacity: 1,
                duration: 100
            });
            $.content.touchEnabled = true;
        }
    });
    Ti.App.addEventListener("showMap", function() {
        $.content.animate({
            opacity: 1,
            duration: 0
        });
    });
    Ti.App.addEventListener("back", function() {
        Alloy.Globals.CB.pushController({
            controller: "home",
            animation: Alloy.Globals.CB.UI.AnimationStyle.NavRight,
            data: []
        });
        Ti.App.fireEvent("showFooter");
        Ti.App.fireEvent("menuOff");
        Ti.App.fireEvent("homeOn");
    });
    Ti.App.addEventListener("home", function() {
        $.content.animate({
            top: 0,
            curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
            opacity: 1
        }, function() {
            Alloy.Globals.CB.pushController({
                controller: "home",
                animation: Alloy.Globals.CB.UI.AnimationStyle.NavRight,
                data: []
            });
        });
        $.content.touchEnabled = true;
        Ti.App.fireEvent("showFooter");
        Ti.App.fireEvent("menuOff");
        Ti.App.fireEvent("homeOn");
    });
    Ti.App.addEventListener("areas", function() {
        createPicker("areas");
        Ti.App.fireEvent("showFooter");
        Ti.App.fireEvent("menuOff");
        Ti.App.fireEvent("areasOn");
    });
    Ti.App.addEventListener("near", function() {
        $.content.animate({
            top: 0,
            curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
            duration: 100,
            opacity: 1
        }, function() {
            $.content.touchEnabled = true;
            Alloy.Globals.CB.Util.startLoading();
            Alloy.Globals.CB.pushController({
                controller: "tab",
                action: Alloy.Globals.CB.UI.NavAction.KeepBack,
                animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
                data: [ {
                    region: "",
                    type: "near"
                } ],
                showInd: false
            });
        });
        Ti.App.fireEvent("showFooter");
        Ti.App.fireEvent("menuOff");
        Ti.App.fireEvent("nearOn");
    });
    Ti.App.addEventListener("search", function() {
        $.content.animate({
            top: 0,
            curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
            duration: 300,
            opacity: 1
        }, function() {
            Alloy.Globals.CB.pushController({
                controller: "search",
                animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
                data: []
            });
        });
        $.content.touchEnabled = true;
        Ti.App.fireEvent("hideFooter");
        Ti.App.fireEvent("menuOff");
        Ti.App.fireEvent("searchOn");
    });
    Ti.App.addEventListener("newest", function() {
        createPicker("newest");
        Ti.App.fireEvent("showFooter");
        Ti.App.fireEvent("menuOff");
        Ti.App.fireEvent("areasOn");
    });
    Ti.App.addEventListener("hideFooter", function() {
        $.footer.animate({
            bottom: -Alloy.Globals.footerHeight,
            duration: 400
        });
    });
    Ti.App.addEventListener("showFooter", function() {
        $.footer.animate({
            bottom: 0,
            duration: 400
        });
    });
    Ti.Geolocation.purpose = "For your location on the map";
    Ti.Geolocation.addEventListener("location", locationSetting);
    __defers["$.__views.footer!click!footerClick"] && $.__views.footer.addEventListener("click", footerClick);
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;