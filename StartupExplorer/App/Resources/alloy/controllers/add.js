function Controller() {
    function sendForm() {
        if ($.name.value && $.category.value && $.employees.value && $.address.value && $.address2.value && $.contactEmail.value) {
            var x = $.contactEmail.value;
            var atpos = x.indexOf("@");
            var dotpos = x.lastIndexOf(".");
            if (1 > atpos || atpos + 2 > dotpos || dotpos + 2 >= x.length) {
                alert("Not a valid e-mail address, please correct.");
                return false;
            }
            Alloy.Globals.CB.Util.startLoading();
            var toName = "Buddy.com";
            var toEmail = "startupexplorer@buddy.com";
            var message = "<p><strong>Company Name: </strong>" + $.name.value + "<br /><strong>Category: </strong>" + $.category.value + "<br /><strong>Employees: </strong>" + $.employees.value + "<br /><strong>Address: </strong>" + $.address.value + "<br /><strong>City, State, Zip: </strong>" + $.address2.value + "<br /><strong>Contact Email: </strong>" + $.contactEmail.value + "</p>";
            var xhrSend = Ti.Network.createHTTPClient();
            xhrSend.onload = function() {
                sendUser();
            };
            xhrSend.onerror = function() {
                Titanium.API.info("Error sending email");
            };
            xhrSend.open("GET", "https://sendgrid.com/api/mail.send.json?api_user=" + apiUser + "&api_key=" + apiPass + "&to=" + toEmail + "&toname=" + toName + "&subject=New Startup Added&html=" + message + "&from=no-reply@buddy.com");
            xhrSend.send();
        } else alert("All fields are required, please double check and try again.");
    }
    function sendUser() {
        var toEmail = $.contactEmail.value;
        var message = "<p>We have received your application. We will contact you once you are confirmed.</p><p>Cheers,<br />The Buddy Team</p>";
        var xhrSend = Ti.Network.createHTTPClient();
        xhrSend.onload = function() {
            Ti.UI.createAlertDialog({
                message: "We have received your application. We will contact you once you are confirmed.",
                ok: "OK",
                title: "Thank You!"
            }).show();
            Alloy.Globals.CB.pushController({
                controller: "home",
                animation: Alloy.Globals.CB.UI.AnimationStyle.NavRight,
                data: []
            });
            Ti.App.fireEvent("showFooter");
            Ti.App.fireEvent("menuOff");
            Ti.App.fireEvent("homeOn");
            Alloy.Globals.CB.Util.stopLoading();
        };
        xhrSend.onerror = function() {
            Titanium.API.info("Error sending email");
            Ti.UI.createAlertDialog({
                message: "There was an error sending your message. Please try again.",
                ok: "OK",
                title: "Message Not Sent"
            }).show();
            Alloy.Globals.CB.Util.stopLoading();
        };
        xhrSend.open("GET", "https://sendgrid.com/api/mail.send.json?api_user=" + apiUser + "&api_key=" + apiPass + "&to=" + toEmail + "&subject=Thank You!&html=" + message + "&from=no-reply@buddy.com");
        xhrSend.send();
    }
    require("alloy/controllers/base").apply(this, Array.prototype.slice.call(arguments));
    arguments[0] ? arguments[0]["__parentSymbol"] : null;
    arguments[0] ? arguments[0]["$model"] : null;
    var $ = this;
    var exports = {};
    $.__views.addStartup = Ti.UI.createView({
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundImage: "/images/background.png",
        zIndex: 100,
        id: "addStartup"
    });
    $.__views.addStartup && $.addTopLevelView($.__views.addStartup);
    $.__views.subHeader = Ti.UI.createView({
        left: 0,
        right: 0,
        top: 0,
        height: Alloy.Globals.subHeaderHeight,
        backgroundImage: "/images/header_background.png",
        id: "subHeader"
    });
    $.__views.addStartup.add($.__views.subHeader);
    $.__views.btBack = Ti.UI.createButton({
        height: Alloy.Globals.btBackHeight,
        width: Alloy.Globals.btBackWidth,
        left: Alloy.Globals.btBackPadding,
        backgroundImage: "/images/bt_back.png",
        id: "btBack"
    });
    $.__views.subHeader.add($.__views.btBack);
    $.__views.addHeader = Ti.UI.createImageView({
        height: Alloy.Globals.subTitleHeight,
        top: Alloy.Globals.subHeaderHeight,
        width: Alloy.Globals.width,
        image: "/images/add_header.png",
        id: "addHeader"
    });
    $.__views.addStartup.add($.__views.addHeader);
    $.__views.scrollView = Ti.UI.createScrollView({
        layout: "vertical",
        top: Alloy.Globals.scrollTop,
        bottom: Alloy.Globals.btDashHeight,
        contentHeight: "auto",
        id: "scrollView"
    });
    $.__views.addStartup.add($.__views.scrollView);
    $.__views.fieldText = Ti.UI.createLabel({
        font: {
            fontFamily: "TitilliumText25L-250wt",
            fontSize: "14dp"
        },
        color: "#4b595b",
        left: Alloy.Globals.fieldTextPadding,
        right: Alloy.Globals.fieldTextPadding,
        top: Alloy.Globals.fieldTextPaddingTop,
        text: "To join, complete the form below. We will contact you about additional info needed.",
        id: "fieldText"
    });
    $.__views.scrollView.add($.__views.fieldText);
    $.__views.__alloyId0 = Ti.UI.createView({
        backgroundColor: "rgba(238,237,236,.75)",
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.fieldRowHeight,
        top: Alloy.Globals.fieldTextPaddingTop,
        id: "__alloyId0"
    });
    $.__views.scrollView.add($.__views.__alloyId0);
    $.__views.name = Ti.UI.createTextField({
        height: Alloy.Globals.fieldRowHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "14dp"
        },
        color: "#303e41",
        returnKeyType: Ti.UI.RETURNKEY_NEXT,
        id: "name",
        hintText: "Startup Name"
    });
    $.__views.__alloyId0.add($.__views.name);
    $.__views.__alloyId1 = Ti.UI.createView({
        backgroundColor: "rgba(238,237,236,.75)",
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.fieldRowHeight,
        top: Alloy.Globals.fieldTextPaddingTop,
        id: "__alloyId1"
    });
    $.__views.scrollView.add($.__views.__alloyId1);
    $.__views.category = Ti.UI.createTextField({
        height: Alloy.Globals.fieldRowHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "14dp"
        },
        color: "#303e41",
        returnKeyType: Ti.UI.RETURNKEY_NEXT,
        id: "category",
        hintText: "Startup Category"
    });
    $.__views.__alloyId1.add($.__views.category);
    $.__views.__alloyId2 = Ti.UI.createView({
        backgroundColor: "rgba(238,237,236,.75)",
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.fieldRowHeight,
        top: Alloy.Globals.fieldTextPaddingTop,
        id: "__alloyId2"
    });
    $.__views.scrollView.add($.__views.__alloyId2);
    $.__views.employees = Ti.UI.createTextField({
        height: Alloy.Globals.fieldRowHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "14dp"
        },
        color: "#303e41",
        returnKeyType: Ti.UI.RETURNKEY_NEXT,
        id: "employees",
        hintText: "Number of Employees"
    });
    $.__views.__alloyId2.add($.__views.employees);
    $.__views.__alloyId3 = Ti.UI.createView({
        backgroundColor: "rgba(238,237,236,.75)",
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.fieldRowHeight,
        top: Alloy.Globals.fieldTextPaddingTop,
        id: "__alloyId3"
    });
    $.__views.scrollView.add($.__views.__alloyId3);
    $.__views.address = Ti.UI.createTextField({
        height: Alloy.Globals.fieldRowHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "14dp"
        },
        color: "#303e41",
        returnKeyType: Ti.UI.RETURNKEY_NEXT,
        id: "address",
        hintText: "What is your business address?"
    });
    $.__views.__alloyId3.add($.__views.address);
    $.__views.__alloyId4 = Ti.UI.createView({
        backgroundColor: "rgba(238,237,236,.75)",
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.fieldRowHeight,
        top: Alloy.Globals.fieldTextPaddingTop,
        id: "__alloyId4"
    });
    $.__views.scrollView.add($.__views.__alloyId4);
    $.__views.address2 = Ti.UI.createTextField({
        height: Alloy.Globals.fieldRowHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "14dp"
        },
        color: "#303e41",
        returnKeyType: Ti.UI.RETURNKEY_NEXT,
        id: "address2",
        hintText: "Please tell us your city, state, and zip"
    });
    $.__views.__alloyId4.add($.__views.address2);
    $.__views.__alloyId5 = Ti.UI.createView({
        backgroundColor: "rgba(238,237,236,.75)",
        width: Alloy.Globals.fieldRowWidth,
        height: Alloy.Globals.fieldRowHeight,
        top: Alloy.Globals.fieldTextPaddingTop,
        id: "__alloyId5"
    });
    $.__views.scrollView.add($.__views.__alloyId5);
    $.__views.contactEmail = Ti.UI.createTextField({
        height: Alloy.Globals.fieldRowHeight,
        left: Alloy.Globals.fieldTextBox,
        right: Alloy.Globals.fieldTextBox,
        font: {
            fontFamily: "TitilliumText25L-800wt",
            fontSize: "14dp"
        },
        color: "#303e41",
        returnKeyType: Ti.UI.RETURNKEY_DONE,
        autocapitalization: Titanium.UI.TEXT_AUTOCAPITALIZATION_NONE,
        autocorrect: false,
        keyboardType: Ti.UI.KEYBOARD_EMAIL,
        id: "contactEmail",
        hintText: "Contact email address"
    });
    $.__views.__alloyId5.add($.__views.contactEmail);
    $.__views.submitArea = Ti.UI.createView({
        backgroundColor: "rgba(243,238,230,.5)",
        left: 0,
        right: 0,
        bottom: 0,
        height: Alloy.Globals.btDashHeight,
        id: "submitArea"
    });
    $.__views.addStartup.add($.__views.submitArea);
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
    $.btBack.addEventListener("click", function() {
        Ti.App.fireEvent("back");
    });
    $.name.addEventListener("return", function() {
        $.category.focus();
    });
    $.category.addEventListener("return", function() {
        $.employees.focus();
    });
    $.employees.addEventListener("return", function() {
        $.address.focus();
    });
    $.address.addEventListener("return", function() {
        $.address2.focus();
    });
    $.address2.addEventListener("return", function() {
        $.contactEmail.focus();
    });
    $.contactEmail.addEventListener("return", function() {
        sendForm();
    });
    $.btSubmit.addEventListener("click", function() {
        sendForm();
    });
    var apiUser = "startupexplorer";
    var apiPass = "external5312Cartilage";
    _.extend($, exports);
}

var Alloy = require("alloy"), Backbone = Alloy.Backbone, _ = Alloy._;

module.exports = Controller;