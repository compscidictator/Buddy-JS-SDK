function Controller() {
    require("alloy/controllers/BaseController").apply(this, Array.prototype.slice.call(arguments));
    this.__controllerPath = "about";
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    arguments[0] ? arguments[0]["__itemTemplate"] : null;
    var $ = this;
    var exports = {};
    $.__views.aboutView = Ti.UI.createScrollView({
        top: Alloy.Globals.height,
        left: 0,
        right: 0,
        bottom: 0,
        height: Alloy.Globals.height,
        width: Ti.UI.FILL,
        backgroundColor: "#040909",
        opacity: 0,
        layout: "vertical",
        showVerticalScrollIndicator: true,
        showHorizontalScrollIndicator: false,
        zIndex: 901,
        id: "aboutView"
    });
    $.__views.aboutView && $.addTopLevelView($.__views.aboutView);
    $.__views.btClose = Ti.UI.createButton({
        backgroundImage: "/images/bt_close.png",
        width: Alloy.Globals.closeWidth,
        height: Alloy.Globals.closeHeight,
        top: 0,
        right: 0,
        id: "btClose"
    });
    $.__views.aboutView.add($.__views.btClose);
    $.__views.buddyLogo = Ti.UI.createButton({
        backgroundImage: "/images/logo_buddy.png",
        width: Alloy.Globals.buddyLogoWidth,
        height: Alloy.Globals.buddyLogoHeight,
        id: "buddyLogo"
    });
    $.__views.aboutView.add($.__views.buddyLogo);
    $.__views.buddyHeader = Ti.UI.createLabel({
        color: "#dbd1c7",
        left: Alloy.Globals.textPadding,
        right: Alloy.Globals.textPadding,
        font: {
            fontFamily: "News702BT-Roman",
            fontSize: "16dp"
        },
        top: 12,
        textAlign: "center",
        text: "Buddy powers, optimizes and measures your mobile apps.",
        id: "buddyHeader"
    });
    $.__views.aboutView.add($.__views.buddyHeader);
    $.__views.buddyIcons = Ti.UI.createImageView({
        image: "/images/about_icons.png",
        top: 15,
        width: Alloy.Globals.iconsWidth,
        height: Alloy.Globals.iconsHeight,
        id: "buddyIcons"
    });
    $.__views.aboutView.add($.__views.buddyIcons);
    $.__views.buddyDesc = Ti.UI.createLabel({
        color: "#dbd1c7",
        left: Alloy.Globals.textPadding,
        right: Alloy.Globals.textPadding,
        font: {
            fontFamily: "TitilliumText25L-250wt",
            fontSize: "12dp"
        },
        top: 5,
        text: "Buddy is a web services platform that powers, optimizes and measures mobile apps on any OS platform, any connected device. Developers can use Buddy to add cloud connected features to their apps without having to write any server-side code, and publishers get very high fidelity analytics on the usage and engagement of their mobile experiences. Both developers and publishers have access to dashboards, displaying data relevant to their interests (developers stats or publisher analytics).\n\nBuddy is the most complete, feature-rich backend as a service (BaaS) on the market, allowing developers, brands, agencies and enterprises to focus on building amazing mobile experiences and not on the infrastructure required to power, optimize or measure those experiences.\n\n",
        id: "buddyDesc"
    });
    $.__views.aboutView.add($.__views.buddyDesc);
    $.__views.buddyDesc = Ti.UI.createLabel({
        color: "#dbd1c7",
        left: Alloy.Globals.textPadding,
        right: Alloy.Globals.textPadding,
        font: {
            fontFamily: "TitilliumText25L-250wt",
            fontSize: "12dp"
        },
        top: 5,
        text: "To update your startup's information, please email startupexplorer@buddy.com",
        id: "buddyDesc"
    });
    $.__views.aboutView.add($.__views.buddyDesc);
    $.__views.socialArea = Ti.UI.createView({
        top: 14,
        width: Alloy.Globals.socialWidthAbout,
        height: Alloy.Globals.btSocialWidth,
        id: "socialArea"
    });
    $.__views.aboutView.add($.__views.socialArea);
    $.__views.btFacebook = Ti.UI.createButton({
        width: Alloy.Globals.btSocialWidth,
        height: Alloy.Globals.btSocialWidth,
        backgroundImage: "/images/bt_facebook.png",
        left: 0,
        id: "btFacebook"
    });
    $.__views.socialArea.add($.__views.btFacebook);
    $.__views.btTwitter = Ti.UI.createButton({
        width: Alloy.Globals.btSocialWidth,
        height: Alloy.Globals.btSocialWidth,
        backgroundImage: "/images/bt_twitter.png",
        id: "btTwitter"
    });
    $.__views.socialArea.add($.__views.btTwitter);
    $.__views.btEmail = Ti.UI.createButton({
        width: Alloy.Globals.btSocialWidth,
        height: Alloy.Globals.btSocialWidth,
        backgroundImage: "/images/bt_email.png",
        right: 0,
        id: "btEmail"
    });
    $.__views.socialArea.add($.__views.btEmail);
    $.__views.rocketLogo = Ti.UI.createView({
        backgroundImage: "/images/logo_rocket.png",
        width: Alloy.Globals.rocketLogoWidth,
        height: Alloy.Globals.rocketLogoHeight,
        top: 14,
        id: "rocketLogo"
    });
    $.__views.aboutView.add($.__views.rocketLogo);
    $.__views.scrollPadding = Ti.UI.createView({
        height: 40,
        top: 16,
        id: "scrollPadding"
    });
    $.__views.aboutView.add($.__views.scrollPadding);
    exports.destroy = function() {};
    _.extend($, $.__views);
    $.btClose.addEventListener("click", function() {
        $.aboutView.animate({
            top: Alloy.Globals.height,
            curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
            opacity: 0
        });
    });
    Ti.App.addEventListener("about", function() {
        Ti.API.info("Footer Clicked");
        if ("android" == Ti.Platform.osname) {
            $.aboutView.top = 1;
            $.aboutView.animate({
                curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
                opacity: 1,
                duration: 100
            });
        } else $.aboutView.animate({
            top: 0,
            curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
            opacity: 1,
            duration: 100
        });
    });
    $.btFacebook.addEventListener("click", function() {
        Ti.Platform.openURL("https://www.facebook.com/BuddyPlatform");
    });
    $.btTwitter.addEventListener("click", function() {
        Ti.Platform.openURL("https://twitter.com/BuddyPlatform");
    });
    $.btEmail.addEventListener("click", function() {
        var emailDialog = Ti.UI.createEmailDialog();
        emailDialog.subject = "Message from Startup Explorer";
        emailDialog.toRecipients = [ "startupexplorer@buddy.com" ];
        emailDialog.open();
    });
    $.buddyLogo.addEventListener("click", function() {
        Ti.Platform.openURL("http://buddy.com");
    });
    $.rocketLogo.addEventListener("click", function() {
        Ti.Platform.openURL("http://rocketscience.is");
    });
    $.aboutView.bubbleParent = false;
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;