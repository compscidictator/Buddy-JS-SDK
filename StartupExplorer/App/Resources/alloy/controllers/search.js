function Controller() {
    function truncNb(Nb, ind) {
        var _nb = Nb * Math.pow(10, ind);
        _nb = Math.floor(_nb);
        _nb /= Math.pow(10, ind);
        return _nb;
    }
    function int2roundKMG(val) {
        var _str = "";
        _str = val >= 1e9 ? truncNb(val / 1e9, 1) + "G" : val >= 1e6 ? truncNb(val / 1e6, 1) + "M" : val >= 1e3 ? truncNb(val / 1e3, 1) + "k" : parseInt(val);
        return _str;
    }
    function updateFunding(e) {
        $.fundingValue.text = "$" + int2roundKMG(e.value);
        amountTerm = e.value;
    }
    function updateEmployee(e) {
        $.employeeValue.text = e.value.toFixed(0);
        employeesTerm = String.format("%3.0f", e.value);
    }
    function createPicker(dataSet, type) {
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
        var toolbar = Ti.UI.createView({
            backgroundColor: "#74807b",
            bottom: 216,
            height: Alloy.Globals.pickerHeight + 2 * Alloy.Globals.pickerPadding,
            width: Ti.UI.FILL
        });
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
        toolbar.add(endButtons);
        if ("area" == type) {
            var none = Ti.UI.createPickerRow({
                title: "All"
            });
            picker.add(none);
        }
        picker.add(dataSet);
        picker.selectionIndicator = true;
        pickerContainer.add(toolbar);
        if ("android" != Ti.Platform.osname) {
            picker.bottom = 0;
            pickerContainer.add(picker);
            toolbar.add(navButtons);
        } else {
            pickerContainer.backgroundColor = "#000";
            toolbar.bottom = 0;
            pickerContainer.height = Ti.UI.FILL;
            picker.left = 0;
            toolbar.add(picker);
            navButtons = null;
        }
        $.search.add(pickerContainer);
        pickerContainer.animate({
            top: 0,
            opacity: 1,
            duration: 300,
            curve: Titanium.UI.ANIMATION_CURVE_EASE_IN
        });
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
                $.search.remove(pickerContainer);
                picker = null;
                pickerContainer = null;
            });
        });
        done.addEventListener("click", function() {
            if ("area" == type) {
                $.sArea.children[0].text = picker.getSelectedRow(0).title;
                areaTerm = picker.getSelectedRow(0).title;
            } else if ("industry" == type) {
                $.sIndustry.children[0].text = picker.getSelectedRow(0).title;
                industryTerm = picker.getSelectedRow(0).title;
            } else if ("funding" == type) {
                $.sFunding.children[0].text = picker.getSelectedRow(0).title;
                fundingTerm = picker.getSelectedRow(0).title;
            }
            cancel.fireEvent("click");
        });
    }
    require("alloy/controllers/base").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "search";
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    arguments[0] ? arguments[0]["__itemTemplate"] : null;
    var $ = this;
    var exports = {};
    var __defers = {};
    $.__views.search = Ti.UI.createView({
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundImage: "/images/background.png",
        zIndex: 100,
        id: "search"
    });
    $.__views.search && $.addTopLevelView($.__views.search);
    $.__views.subHeader = Ti.UI.createView({
        left: 0,
        right: 0,
        top: 0,
        height: Alloy.Globals.subHeaderHeight,
        backgroundImage: "/images/header_background.png",
        id: "subHeader"
    });
    $.__views.search.add($.__views.subHeader);
    $.__views.btBack = Ti.UI.createButton({
        height: Alloy.Globals.btBackHeight,
        width: Alloy.Globals.btBackWidth,
        left: Alloy.Globals.btBackPadding,
        backgroundImage: "/images/bt_back.png",
        id: "btBack"
    });
    $.__views.subHeader.add($.__views.btBack);
    $.__views.searchHeader = Ti.UI.createImageView({
        height: Alloy.Globals.subTitleHeight,
        top: Alloy.Globals.subHeaderHeight,
        left: 0,
        right: 0,
        image: "/images/search_header.png",
        id: "searchHeader"
    });
    $.__views.search.add($.__views.searchHeader);
    $.__views.__alloyId12 = Ti.UI.createScrollView({
        layout: "vertical",
        top: Alloy.Globals.scrollTop,
        bottom: Alloy.Globals.btDashHeight,
        left: 0,
        right: 0,
        id: "__alloyId12"
    });
    $.__views.search.add($.__views.__alloyId12);
    $.__views.searchArea = Ti.UI.createView({
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.searchHeight,
        backgroundImage: "/images/search_background.png",
        top: Alloy.Globals.fieldTextPaddingTop2,
        id: "searchArea"
    });
    $.__views.__alloyId12.add($.__views.searchArea);
    $.__views.name = Ti.UI.createTextField({
        height: Alloy.Globals.fieldRowHeight,
        left: Alloy.Globals.searchPadding,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "16dp"
        },
        color: "#868c8c",
        returnKeyType: Ti.UI.RETURNKEY_DONE,
        id: "name",
        hintText: "Search Company Name"
    });
    $.__views.searchArea.add($.__views.name);
    $.__views.sArea = Ti.UI.createView({
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.searchBtHeight,
        backgroundImage: "/images/search_select_area.png",
        top: Alloy.Globals.fieldTextPaddingTop,
        id: "sArea"
    });
    $.__views.__alloyId12.add($.__views.sArea);
    $.__views.__alloyId13 = Ti.UI.createLabel({
        height: Alloy.Globals.searchBtHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "12dp"
        },
        color: "#5f6666",
        text: "Area",
        id: "__alloyId13"
    });
    $.__views.sArea.add($.__views.__alloyId13);
    $.__views.sIndustry = Ti.UI.createView({
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.searchBtHeight,
        backgroundImage: "/images/search_select_area.png",
        top: "1",
        id: "sIndustry"
    });
    $.__views.__alloyId12.add($.__views.sIndustry);
    $.__views.__alloyId14 = Ti.UI.createLabel({
        height: Alloy.Globals.searchBtHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "12dp"
        },
        color: "#5f6666",
        text: "Industry",
        id: "__alloyId14"
    });
    $.__views.sIndustry.add($.__views.__alloyId14);
    $.__views.sFunding = Ti.UI.createView({
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.searchBtHeight,
        backgroundImage: "/images/search_select_area.png",
        top: "1",
        id: "sFunding"
    });
    $.__views.__alloyId12.add($.__views.sFunding);
    $.__views.__alloyId15 = Ti.UI.createLabel({
        height: Alloy.Globals.searchBtHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "12dp"
        },
        color: "#5f6666",
        text: "Funding Source",
        id: "__alloyId15"
    });
    $.__views.sFunding.add($.__views.__alloyId15);
    $.__views.__alloyId16 = Ti.UI.createView({
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.searchBtHeight,
        top: Alloy.Globals.fieldTextPaddingTop2,
        layout: "composite",
        id: "__alloyId16"
    });
    $.__views.__alloyId12.add($.__views.__alloyId16);
    $.__views.__alloyId17 = Ti.UI.createLabel({
        top: 0,
        left: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "12dp"
        },
        color: "#5f6666",
        text: "FUNDING AMOUNT",
        id: "__alloyId17"
    });
    $.__views.__alloyId16.add($.__views.__alloyId17);
    $.__views.fundingValue = Ti.UI.createLabel({
        top: 0,
        right: 0,
        textAlign: "right",
        font: {
            fontFamily: "Surf-Regular",
            fontSize: "14dp"
        },
        color: "#5f6666",
        id: "fundingValue"
    });
    $.__views.__alloyId16.add($.__views.fundingValue);
    $.__views.sliderFunding = Ti.UI.createSlider({
        top: 24,
        thumbImage: "/images/slider_icon.png",
        leftTrackImage: "/images/slider_left.png",
        rightTrackImage: "/images/slider_right.png",
        id: "sliderFunding",
        min: "0",
        max: "100000000",
        width: "100%",
        value: "5000000"
    });
    $.__views.__alloyId16.add($.__views.sliderFunding);
    updateFunding ? $.__views.sliderFunding.addEventListener("change", updateFunding) : __defers["$.__views.sliderFunding!change!updateFunding"] = true;
    $.__views.__alloyId18 = Ti.UI.createView({
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.searchBtHeight,
        top: Alloy.Globals.fieldTextPaddingTop2,
        layout: "composite",
        id: "__alloyId18"
    });
    $.__views.__alloyId12.add($.__views.__alloyId18);
    $.__views.__alloyId19 = Ti.UI.createLabel({
        top: 0,
        left: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "12dp"
        },
        color: "#5f6666",
        text: "NUMBER OF EMPLOYEES",
        id: "__alloyId19"
    });
    $.__views.__alloyId18.add($.__views.__alloyId19);
    $.__views.employeeValue = Ti.UI.createLabel({
        top: 0,
        right: 0,
        textAlign: "right",
        font: {
            fontFamily: "Surf-Regular",
            fontSize: "14dp"
        },
        color: "#5f6666",
        id: "employeeValue"
    });
    $.__views.__alloyId18.add($.__views.employeeValue);
    $.__views.sliderEmployee = Ti.UI.createSlider({
        top: 24,
        thumbImage: "/images/slider_icon.png",
        leftTrackImage: "/images/slider_left.png",
        rightTrackImage: "/images/slider_right.png",
        id: "sliderEmployee",
        min: "0",
        max: "999",
        width: "100%",
        value: "150"
    });
    $.__views.__alloyId18.add($.__views.sliderEmployee);
    updateEmployee ? $.__views.sliderEmployee.addEventListener("change", updateEmployee) : __defers["$.__views.sliderEmployee!change!updateEmployee"] = true;
    $.__views.submitArea = Ti.UI.createView({
        backgroundColor: "rgba(243,238,230,.5)",
        left: 0,
        right: 0,
        bottom: 0,
        height: Alloy.Globals.btDashHeight,
        id: "submitArea"
    });
    $.__views.search.add($.__views.submitArea);
    $.__views.btSubmit = Ti.UI.createButton({
        backgroundImage: "/images/bt_add_submit.png",
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.fieldSubmitHeight,
        id: "btSubmit"
    });
    $.__views.submitArea.add($.__views.btSubmit);
    exports.destroy = function() {};
    _.extend($, $.__views);
    exports.baseController = "base";
    $.onLoad = function() {
        Ti.App.fireEvent("hideFooter");
    };
    var areaTerm = "All";
    var industryTerm = "All";
    var fundingTerm = "All";
    var amountTerm = "5000000";
    var employeesTerm = "150";
    $.fundingValue.text = "$5M";
    $.employeeValue.text = "150";
    $.sArea.addEventListener("click", function() {
        createPicker(Alloy.Globals.metroAreas, "area");
    });
    $.sIndustry.addEventListener("click", function() {
        createPicker(Alloy.Globals.industryArray, "industry");
    });
    $.sFunding.addEventListener("click", function() {
        createPicker(Alloy.Globals.fundingArray, "funding");
    });
    $.btSubmit.addEventListener("click", function() {
        Alloy.Globals.CB.Util.startLoading();
        Alloy.Globals.CB.pushController({
            controller: "tab",
            action: Alloy.Globals.CB.UI.NavAction.KeepBack,
            animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
            data: [ {
                region: "",
                type: "search",
                term: $.name.value + "%25",
                area: areaTerm,
                funding: fundingTerm,
                industry: industryTerm,
                amount: amountTerm,
                employees: employeesTerm,
                show: "list"
            } ],
            showInd: false
        });
        Ti.App.fireEvent("showFooter");
    });
    $.btBack.addEventListener("click", function() {
        Ti.App.fireEvent("back");
    });
    if (false == Alloy.Globals.ddValues) {
        var fullSet = [];
        var xhrSearch = Ti.Network.createHTTPClient({
            cache: true
        });
        xhrSearch.onload = function() {
            function compare(strA, strB) {
                var icmp = strA.toLowerCase().localeCompare(strB.toLowerCase());
                return 0 == icmp ? strA > strB ? 1 : strB > strA ? -1 : 0 : icmp;
            }
            fullSet = JSON.parse(this.responseText);
            var industryTemp = [];
            var fundingTemp = [];
            Alloy.Globals.industryArray[0] = Ti.UI.createPickerRow({
                title: "All"
            });
            Alloy.Globals.fundingArray[0] = Ti.UI.createPickerRow({
                title: "All"
            });
            for (var i = 0; fullSet.data.length > i; i++) {
                "" != fullSet.data[i].industry && (industryTemp[i] = fullSet.data[i].industry);
                "" != fullSet.data[i].fundingSource && (fundingTemp[i] = fullSet.data[i].fundingSource);
            }
            industryTemp = _.uniq(industryTemp);
            industryTemp = industryTemp.sort(compare);
            for (var i = 0; industryTemp.length > i; i++) Alloy.Globals.industryArray[i + 1] = Ti.UI.createPickerRow({
                title: industryTemp[i]
            });
            fundingTemp = _.uniq(fundingTemp);
            fundingTemp = fundingTemp.sort(compare);
            for (var i = 0; fundingTemp.length > i; i++) Alloy.Globals.fundingArray[i + 1] = Ti.UI.createPickerRow({
                title: fundingTemp[i]
            });
            Alloy.Globals.ddValues = true;
            fundingTemp = industryTemp = fullSet = xhrSearch = null;
        };
        xhrSearch.onerror = function() {
            Titanium.API.info("Error grabbing buddy data for pickers");
        };
        xhrSearch.open("GET", "https://webservice.buddyplatform.com/Service/v1/BuddyService.ashx?StartupData_Location_Search&BuddyApplicationName=Startup%20Explorer&BuddyApplicationPassword=" + Alloy.Globals.applicationPassword + "&UserToken=" + Alloy.Globals.userToken + "&SearchDistance=100000000&Latitude=39.8106460&Longitude=-98.5569760&RecordLimit=690&SearchName=");
        xhrSearch.send();
    }
    __defers["$.__views.sliderFunding!change!updateFunding"] && $.__views.sliderFunding.addEventListener("change", updateFunding);
    __defers["$.__views.sliderEmployee!change!updateEmployee"] && $.__views.sliderEmployee.addEventListener("change", updateEmployee);
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;