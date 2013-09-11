// The contents of this file will be executed before any of
// your view controllers are ever executed, including the index.
// You have access to all functionality on the `Alloy` namespace.
//
// This is a great place to do any initialization for your app
// or create any global variables/functions that you'd like to
// make available throughout your app. You can easily make things
// accessible globally by attaching them to the `Alloy.Globals`
// object. For example:
//
// Alloy.Globals.someGlobalFunction = function(){};

var TiMapCluster = require('ti.mapcluster');
Alloy.Globals.CB = require('core');

Alloy.Globals.applicationPassword = 'EB6473D0-7BC9-4BD6-A143-D55FE15C2910';
Alloy.Globals.userToken = 'UT-8ea1bead-048d-4742-bf11-16713c9476e2';

Alloy.Globals.metroAreas = [];
Alloy.Globals.ddValues = false;
Alloy.Globals.fundingArray = [];
Alloy.Globals.industryArray = [];

Alloy.Globals.userLatitude = 0;
Alloy.Globals.userLongitude = 0;
	
Alloy.Globals.width = Ti.Platform.displayCaps.platformWidth;
Alloy.Globals.height = Ti.Platform.displayCaps.platformHeight;

Alloy.Globals.pickerWidth = (14.374999999999998 / 100) * Alloy.Globals.width;
Alloy.Globals.pickerHeight = Alloy.Globals.pickerWidth * (68 / 92);
Alloy.Globals.pickerPadding = (3.125 / 100) * Alloy.Globals.width;

Alloy.Globals.width2 = Alloy.Globals.width*2;

Alloy.Globals.widthNeg = Ti.Platform.displayCaps.platformWidth - (Ti.Platform.displayCaps.platformWidth*2);

Alloy.Globals.height1 = Ti.Platform.displayCaps.platformHeight - 36;
Alloy.Globals.height2 = Ti.Platform.displayCaps.platformHeight - 20;

Alloy.Globals.headerHeight = Alloy.Globals.width * (122 / 640);
Alloy.Globals.btHeaderWidth = (10.625 / 100) * Alloy.Globals.width;
Alloy.Globals.btHeaderPadding = (13.4375 / 100) * Alloy.Globals.width;

Alloy.Globals.skipWidth = (19.375 / 100) * Alloy.Globals.width;
Alloy.Globals.skipHeight = Alloy.Globals.skipWidth * (112 / 124);	

Alloy.Globals.mapWidth = (84.69 / 100) * Alloy.Globals.width;
Alloy.Globals.mapHeight = Alloy.Globals.mapWidth * (367 / 542);	

Alloy.Globals.mapTotalWidth = (30 / 100) * Alloy.Globals.width;
Alloy.Globals.mapTotalRadius = Alloy.Globals.mapTotalWidth / 2;

Alloy.Globals.indicatorAreaPadding = (41.41 / 100) * Alloy.Globals.width;
Alloy.Globals.indicatorWidth = (1.72 / 100) * Alloy.Globals.width;

Alloy.Globals.btAreaHeight = Alloy.Globals.width * (320 / 640);
Alloy.Globals.btDashWidth = Alloy.Globals.width;
Alloy.Globals.btDashHeight = Alloy.Globals.btDashWidth * (144 / 640);

Alloy.Globals.footerHeight = Alloy.Globals.btDashWidth * (55 / 640);
Alloy.Globals.footerHeight2 = Alloy.Globals.footerHeight - (Alloy.Globals.footerHeight*2);

Alloy.Globals.scrollableMapArea = Alloy.Globals.footerHeight + Alloy.Globals.btAreaHeight;

//List&Map View variables

Alloy.Globals.subHeaderHeight = Alloy.Globals.width * (138 / 640);

Alloy.Globals.btBackPadding = (6.875 / 100) * Alloy.Globals.width;
Alloy.Globals.btBackWidth = (14.0625 / 100) * Alloy.Globals.width;
Alloy.Globals.btBackHeight = Alloy.Globals.btBackWidth * (52 / 90);

Alloy.Globals.btTabWidth = (50 / 100) * Alloy.Globals.width;
Alloy.Globals.btTabHeight = Alloy.Globals.btTabWidth * (78 / 320);

Alloy.Globals.statusAreaHeight = Alloy.Globals.width * (63 / 640);
Alloy.Globals.statusAreaOffset = Alloy.Globals.width * (14 / 640);
Alloy.Globals.statusAreaTop = Alloy.Globals.subHeaderHeight + Alloy.Globals.btTabHeight - Alloy.Globals.statusAreaOffset;

Alloy.Globals.tabGroupTop = Alloy.Globals.statusAreaTop + Alloy.Globals.statusAreaHeight;

Alloy.Globals.pinWidth = (8.4375 / 100) * Alloy.Globals.width;
Alloy.Globals.pinHeight = Alloy.Globals.pinWidth * (62 / 54);

Alloy.Globals.rowHeight = Alloy.Globals.width * (143 / 640);

Alloy.Globals.rowRightWidth = (7.18749 / 100) * Alloy.Globals.width;
Alloy.Globals.rowRightHeight = Alloy.Globals.rowRightWidth * (112 / 46);

//About View
Alloy.Globals.closeWidth = (19.2187 / 100) * Alloy.Globals.width;
Alloy.Globals.closeHeight = Alloy.Globals.closeWidth * (112 / 123);

Alloy.Globals.buddyLogoWidth = (40.4687 / 100) * Alloy.Globals.width;
Alloy.Globals.buddyLogoHeight = Alloy.Globals.buddyLogoWidth * (78 / 259);

Alloy.Globals.iconsWidth = (50.625 / 100) * Alloy.Globals.width;
Alloy.Globals.iconsHeight = Alloy.Globals.iconsWidth * (248 / 324);

Alloy.Globals.textPadding = (6.875 / 100) * Alloy.Globals.width;

Alloy.Globals.socialWidthAbout = (46.875 / 100) * Alloy.Globals.width;
Alloy.Globals.btSocialWidth = (11.25 / 100) * Alloy.Globals.width;

Alloy.Globals.rocketLogoWidth = (83.125 / 100) * Alloy.Globals.width;
Alloy.Globals.rocketLogoHeight = Alloy.Globals.rocketLogoWidth * (286 / 532);

//Add View
Alloy.Globals.subTitleHeight = Alloy.Globals.width * (65 / 640);

Alloy.Globals.fieldRowWidth = (85.9375 / 100) * Alloy.Globals.width;
Alloy.Globals.fieldRowHeight = Alloy.Globals.fieldRowWidth * (88 / 550);
Alloy.Globals.fieldSubmitHeight = Alloy.Globals.fieldRowWidth * (66 / 550);

Alloy.Globals.fieldTextPadding = (9.0625 / 100) * Alloy.Globals.width;
Alloy.Globals.fieldTextPaddingTop = Alloy.Globals.fieldTextPadding / 3;
Alloy.Globals.fieldTextPaddingTop2 = Alloy.Globals.fieldTextPadding / 2;
Alloy.Globals.fieldTextPaddingTop3 = Alloy.Globals.fieldTextPadding / 1.5;
Alloy.Globals.fieldTextPaddingTop4 = Alloy.Globals.fieldTextPadding / 4;
Alloy.Globals.fieldTextBox = (2.34375 / 100) * Alloy.Globals.width;

//Search View
Alloy.Globals.searchPadding = (11.875 / 100) * Alloy.Globals.width;
Alloy.Globals.scrollTop = Alloy.Globals.subTitleHeight + Alloy.Globals.subHeaderHeight;

Alloy.Globals.searchHeight = Alloy.Globals.fieldRowWidth * (87 / 550);
Alloy.Globals.searchBtHeight = Alloy.Globals.fieldRowWidth * (92 / 550);

//Detail View
Alloy.Globals.detailHeadingHeight = Alloy.Globals.width * (182 / 640);
Alloy.Globals.detailHeadingPadding = (6.875 / 100) * Alloy.Globals.width;

Alloy.Globals.detailHeadingPadding2 = Alloy.Globals.detailHeadingPadding/2;

Alloy.Globals.logoHeight = Alloy.Globals.detailHeadingHeight - Alloy.Globals.detailHeadingPadding;
Alloy.Globals.logoWidth = Alloy.Globals.width - (Alloy.Globals.detailHeadingPadding*2);

Alloy.Globals.detailDescTop = Alloy.Globals.subHeaderHeight;
Alloy.Globals.detailScrollTop = Alloy.Globals.detailDescTop;

Alloy.Globals.barHeadingHeight = Alloy.Globals.width * (62 / 640);
Alloy.Globals.barInfoHeight = Alloy.Globals.width * (73 / 640);

Alloy.Globals.detailMapWidth = (85.9375 / 100) * Alloy.Globals.width;
Alloy.Globals.detailMapHeight = Alloy.Globals.detailMapWidth * (168 / 550);
Alloy.Globals.detailAddressHeight = (8.90625 / 100) * Alloy.Globals.width;

Alloy.Globals.pinDetailWidth = (15 / 100) * Alloy.Globals.width;
Alloy.Globals.pinDetailHeight = Alloy.Globals.pinDetailWidth * (108 / 96);

Alloy.Globals.detailBtHeight = (10.3125 / 100) * Alloy.Globals.width;

Alloy.Globals.detailIndustryWidth = (10.3125 / 100) * Alloy.Globals.width;

Alloy.Globals.detailImageWidth = (4.375 / 100) * Alloy.Globals.width;
Alloy.Globals.dirImageWidth = (5.46875 / 100) * Alloy.Globals.width;


Alloy.Globals.socialWidth = (6.5625 / 100) * Alloy.Globals.width;
Alloy.Globals.socialHeight = Alloy.Globals.socialWidth * (34 / 42);


Ti.API.info('Ti.Platform.displayCaps.density: ' + Ti.Platform.displayCaps.density);
Ti.API.info('Ti.Platform.displayCaps.dpi: ' + Ti.Platform.displayCaps.dpi);
Ti.API.info('Ti.Platform.displayCaps.platformHeight: ' + Alloy.Globals.height);
Ti.API.info('Ti.Platform.displayCaps.platformWidth: ' + Alloy.Globals.width);
Ti.API.info('Ti.Platform.osname: ' + Ti.Platform.osname);
if(Ti.Platform.osname === 'android'){
  Ti.API.info('Ti.Platform.displayCaps.xdpi: ' + Ti.Platform.displayCaps.xdpi);
  Ti.API.info('Ti.Platform.displayCaps.ydpi: ' + Ti.Platform.displayCaps.ydpi);
  Ti.API.info('Ti.Platform.displayCaps.logicalDensityFactor: ' + Ti.Platform.displayCaps.logicalDensityFactor);
}