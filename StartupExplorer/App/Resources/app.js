var Alloy = require("alloy"), _ = Alloy._, Backbone = Alloy.Backbone;

var TiMapCluster = require("ti.mapcluster");

Alloy.Globals.CB = require("core");

Alloy.Globals.applicationPassword = "EB6473D0-7BC9-4BD6-A143-D55FE15C2910";

Alloy.Globals.userToken = "UT-8ea1bead-048d-4742-bf11-16713c9476e2";

Alloy.Globals.metroAreas = [];

Alloy.Globals.ddValues = false;

Alloy.Globals.fundingArray = [];

Alloy.Globals.industryArray = [];

Alloy.Globals.userLatitude = 0;

Alloy.Globals.userLongitude = 0;

Alloy.Globals.width = Ti.Platform.displayCaps.platformWidth;

Alloy.Globals.height = Ti.Platform.displayCaps.platformHeight;

Alloy.Globals.pickerWidth = .14375 * Alloy.Globals.width;

Alloy.Globals.pickerHeight = Alloy.Globals.pickerWidth * (68 / 92);

Alloy.Globals.pickerPadding = .03125 * Alloy.Globals.width;

Alloy.Globals.width2 = 2 * Alloy.Globals.width;

Alloy.Globals.widthNeg = Ti.Platform.displayCaps.platformWidth - 2 * Ti.Platform.displayCaps.platformWidth;

Alloy.Globals.height1 = Ti.Platform.displayCaps.platformHeight - 36;

Alloy.Globals.height2 = Ti.Platform.displayCaps.platformHeight - 20;

Alloy.Globals.headerHeight = .190625 * Alloy.Globals.width;

Alloy.Globals.btHeaderWidth = .10625 * Alloy.Globals.width;

Alloy.Globals.btHeaderPadding = .134375 * Alloy.Globals.width;

Alloy.Globals.skipWidth = .19375 * Alloy.Globals.width;

Alloy.Globals.skipHeight = Alloy.Globals.skipWidth * (112 / 124);

Alloy.Globals.mapWidth = .8469 * Alloy.Globals.width;

Alloy.Globals.mapHeight = Alloy.Globals.mapWidth * (367 / 542);

Alloy.Globals.mapTotalWidth = .3 * Alloy.Globals.width;

Alloy.Globals.mapTotalRadius = Alloy.Globals.mapTotalWidth / 2;

Alloy.Globals.indicatorAreaPadding = 41.41 / 100 * Alloy.Globals.width;

Alloy.Globals.indicatorWidth = .0172 * Alloy.Globals.width;

Alloy.Globals.btAreaHeight = .5 * Alloy.Globals.width;

Alloy.Globals.btDashWidth = Alloy.Globals.width;

Alloy.Globals.btDashHeight = .225 * Alloy.Globals.btDashWidth;

Alloy.Globals.footerHeight = Alloy.Globals.btDashWidth * (55 / 640);

Alloy.Globals.footerHeight2 = Alloy.Globals.footerHeight - 2 * Alloy.Globals.footerHeight;

Alloy.Globals.scrollableMapArea = Alloy.Globals.footerHeight + Alloy.Globals.btAreaHeight;

Alloy.Globals.subHeaderHeight = .215625 * Alloy.Globals.width;

Alloy.Globals.btBackPadding = .06875 * Alloy.Globals.width;

Alloy.Globals.btBackWidth = .140625 * Alloy.Globals.width;

Alloy.Globals.btBackHeight = Alloy.Globals.btBackWidth * (52 / 90);

Alloy.Globals.btTabWidth = .5 * Alloy.Globals.width;

Alloy.Globals.btTabHeight = .24375 * Alloy.Globals.btTabWidth;

Alloy.Globals.statusAreaHeight = Alloy.Globals.width * (63 / 640);

Alloy.Globals.statusAreaOffset = Alloy.Globals.width * (14 / 640);

Alloy.Globals.statusAreaTop = Alloy.Globals.subHeaderHeight + Alloy.Globals.btTabHeight - Alloy.Globals.statusAreaOffset;

Alloy.Globals.tabGroupTop = Alloy.Globals.statusAreaTop + Alloy.Globals.statusAreaHeight;

Alloy.Globals.pinWidth = .084375 * Alloy.Globals.width;

Alloy.Globals.pinHeight = Alloy.Globals.pinWidth * (62 / 54);

Alloy.Globals.rowHeight = Alloy.Globals.width * (143 / 640);

Alloy.Globals.rowRightWidth = .0718749 * Alloy.Globals.width;

Alloy.Globals.rowRightHeight = Alloy.Globals.rowRightWidth * (112 / 46);

Alloy.Globals.closeWidth = .192187 * Alloy.Globals.width;

Alloy.Globals.closeHeight = Alloy.Globals.closeWidth * (112 / 123);

Alloy.Globals.buddyLogoWidth = 40.4687 / 100 * Alloy.Globals.width;

Alloy.Globals.buddyLogoHeight = Alloy.Globals.buddyLogoWidth * (78 / 259);

Alloy.Globals.iconsWidth = .50625 * Alloy.Globals.width;

Alloy.Globals.iconsHeight = Alloy.Globals.iconsWidth * (248 / 324);

Alloy.Globals.textPadding = .06875 * Alloy.Globals.width;

Alloy.Globals.socialWidthAbout = .46875 * Alloy.Globals.width;

Alloy.Globals.btSocialWidth = .1125 * Alloy.Globals.width;

Alloy.Globals.rocketLogoWidth = .83125 * Alloy.Globals.width;

Alloy.Globals.rocketLogoHeight = Alloy.Globals.rocketLogoWidth * (286 / 532);

Alloy.Globals.subTitleHeight = Alloy.Globals.width * (65 / 640);

Alloy.Globals.fieldRowWidth = .859375 * Alloy.Globals.width;

Alloy.Globals.fieldRowHeight = .16 * Alloy.Globals.fieldRowWidth;

Alloy.Globals.fieldSubmitHeight = .12 * Alloy.Globals.fieldRowWidth;

Alloy.Globals.fieldTextPadding = .090625 * Alloy.Globals.width;

Alloy.Globals.fieldTextPaddingTop = Alloy.Globals.fieldTextPadding / 3;

Alloy.Globals.fieldTextPaddingTop2 = Alloy.Globals.fieldTextPadding / 2;

Alloy.Globals.fieldTextPaddingTop3 = Alloy.Globals.fieldTextPadding / 1.5;

Alloy.Globals.fieldTextPaddingTop4 = Alloy.Globals.fieldTextPadding / 4;

Alloy.Globals.fieldTextBox = .0234375 * Alloy.Globals.width;

Alloy.Globals.searchPadding = .11875 * Alloy.Globals.width;

Alloy.Globals.scrollTop = Alloy.Globals.subTitleHeight + Alloy.Globals.subHeaderHeight;

Alloy.Globals.searchHeight = Alloy.Globals.fieldRowWidth * (87 / 550);

Alloy.Globals.searchBtHeight = Alloy.Globals.fieldRowWidth * (92 / 550);

Alloy.Globals.detailHeadingHeight = .284375 * Alloy.Globals.width;

Alloy.Globals.detailHeadingPadding = .06875 * Alloy.Globals.width;

Alloy.Globals.detailHeadingPadding2 = Alloy.Globals.detailHeadingPadding / 2;

Alloy.Globals.logoHeight = Alloy.Globals.detailHeadingHeight - Alloy.Globals.detailHeadingPadding;

Alloy.Globals.logoWidth = Alloy.Globals.width - 2 * Alloy.Globals.detailHeadingPadding;

Alloy.Globals.detailDescTop = Alloy.Globals.subHeaderHeight;

Alloy.Globals.detailScrollTop = Alloy.Globals.detailDescTop;

Alloy.Globals.barHeadingHeight = Alloy.Globals.width * (62 / 640);

Alloy.Globals.barInfoHeight = Alloy.Globals.width * (73 / 640);

Alloy.Globals.detailMapWidth = .859375 * Alloy.Globals.width;

Alloy.Globals.detailMapHeight = Alloy.Globals.detailMapWidth * (168 / 550);

Alloy.Globals.detailAddressHeight = .0890625 * Alloy.Globals.width;

Alloy.Globals.pinDetailWidth = .15 * Alloy.Globals.width;

Alloy.Globals.pinDetailHeight = 1.125 * Alloy.Globals.pinDetailWidth;

Alloy.Globals.detailBtHeight = .103125 * Alloy.Globals.width;

Alloy.Globals.detailIndustryWidth = .103125 * Alloy.Globals.width;

Alloy.Globals.detailImageWidth = .04375 * Alloy.Globals.width;

Alloy.Globals.dirImageWidth = .0546875 * Alloy.Globals.width;

Alloy.Globals.socialWidth = .065625 * Alloy.Globals.width;

Alloy.Globals.socialHeight = Alloy.Globals.socialWidth * (34 / 42);

Ti.API.info("Ti.Platform.displayCaps.density: " + Ti.Platform.displayCaps.density);

Ti.API.info("Ti.Platform.displayCaps.dpi: " + Ti.Platform.displayCaps.dpi);

Ti.API.info("Ti.Platform.displayCaps.platformHeight: " + Alloy.Globals.height);

Ti.API.info("Ti.Platform.displayCaps.platformWidth: " + Alloy.Globals.width);

Ti.API.info("Ti.Platform.osname: " + Ti.Platform.osname);

if ("android" === Ti.Platform.osname) {
    Ti.API.info("Ti.Platform.displayCaps.xdpi: " + Ti.Platform.displayCaps.xdpi);
    Ti.API.info("Ti.Platform.displayCaps.ydpi: " + Ti.Platform.displayCaps.ydpi);
    Ti.API.info("Ti.Platform.displayCaps.logicalDensityFactor: " + Ti.Platform.displayCaps.logicalDensityFactor);
}

Alloy.createController("index");