var Alloy = require("alloy"), debug = require("debug"), loading = Alloy.createWidget("loading", "widget"), indexContainer, langObj;

var Utility = function() {};

Utility.init = function(iContainer) {
    indexContainer = iContainer;
    var lang = this.loadObject("lang");
    if (null === lang) {
        lang = Alloy.CFG.defaultLanguage;
        this.saveObject("lang", lang);
    }
    langObj = require("langs/" + lang);
};

Utility.alert = function(message, title) {
    Ti.UI.createAlertDialog({
        ok: "Ok",
        message: message,
        title: void 0 === title ? this.L("alert") : title
    }).show();
};

Utility.confirm = function(message, title, callback) {
    var dialog = Ti.UI.createAlertDialog({
        buttonNames: [ this.L("yes"), this.L("no") ],
        message: message,
        cancel: 1,
        title: void 0 === title ? this.L("confirm") : title
    });
    dialog.addEventListener("click", function(e) {
        if (e.cancel === e.index || true === e.cancel) return;
        callback();
    });
    dialog.show();
};

Utility.trim = function(str) {
    str = str.replace(/^\s\s*/, ""), ws = /\s/, i = str.length;
    while (ws.test(str.charAt(--i))) ;
    return str.slice(0, i + 1);
};

Utility.ucFirst = function(str) {
    str += "";
    var f = str.charAt(0).toUpperCase();
    return f + str.substr(1);
};

Utility.switchLang = function(lang) {
    this.saveObject("lang", lang);
    langObj = require("langs/" + lang);
    Ti.API.info("switch language:==========" + lang);
};

Utility.L = function(key) {
    try {
        text = langObj[key];
        if (text) return text;
        return key;
    } catch (e) {
        alert(e);
    }
};

Utility.hasProperty = function(key) {
    return Ti.App.Properties.hasProperty(key);
};

Utility.saveObject = function(key, val) {
    Ti.App.Properties.setString(key, JSON.stringify(val));
};

Utility.loadObject = function(key) {
    var value = Ti.App.Properties.getString(key);
    try {
        return JSON.parse(value);
    } catch (e) {
        return value;
    }
};

Utility.removeObject = function(key) {
    Ti.App.Properties.removeProperty(key);
};

Utility.px = function(dip) {
    var screen_density = Ti.Platform.displayCaps.getDpi();
    var actual_pixels = dip * screen_density / (this.isAndroid ? 160 : 163);
    this.isTablet && (actual_pixels = this.isAndroid ? 2 * actual_pixels : 5 * actual_pixels);
    return actual_pixels;
};

Utility.cleanSpecialChars = function(str) {
    if (null === str) return "";
    if ("string" == typeof str) return str.replace(/&quot;/g, '"').replace(/\&amp\;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&#039;/g, "'");
    return "";
};

Utility.actInd = {
    actIndWin: void 0,
    init: function(win) {
        win.actInd = Titanium.UI.createActivityIndicator({
            height: Ti.UI.FILL,
            width: Ti.UI.FILL,
            bottom: "15%",
            zIndex: 9999
        });
        win.actInd.isHide = true;
        win.actIndContainer = Ti.UI.createView({
            height: Ti.UI.FILL,
            width: Ti.UI.FILL
        });
        win.add(win.actIndContainer);
        win.actIndContainer.mainBg = Ti.UI.createView({
            height: Ti.UI.FILL,
            width: Ti.UI.FILL,
            backgroundColor: "#333",
            opacity: .8
        });
        win.actIndContainer.add(win.actIndContainer.mainBg);
        win.actIndContainer.actIndBg = Ti.UI.createView({
            backgroundColor: "#000",
            center: {
                x: Ti.Platform.displayCaps.platformWidth / 2,
                y: Ti.Platform.displayCaps.platformHeight / 2
            },
            width: 150,
            height: 100,
            borderRadius: 10
        });
        win.actIndContainer.add(win.actIndContainer.actIndBg);
        win.actIndContainer.actIndBg.loading = Ti.UI.createLabel({
            text: Utility.L("loading"),
            color: "#fff",
            left: "30%",
            bottom: "10%"
        });
        win.actIndContainer.actIndBg.add(win.actIndContainer.actIndBg.loading);
        win.actIndContainer.hide();
        if (Utility.isAndroid) {
            Alloy.Globals.CB.Debug.echo("=======android====`", 261, "util.js");
            win.actIndContainer.zIndex = -9999;
            win.actInd.style = Titanium.UI.ActivityIndicatorStyle.PLAIN;
        } else {
            Alloy.Globals.CB.Debug.echo("======iphone=====`", 265, "util.js");
            win.actInd.style = Titanium.UI.iPhone.ActivityIndicatorStyle.BIG;
        }
        win.actInd.show();
        win.actIndContainer.actIndBg.add(win.actInd);
        Utility.actInd.actIndWin = win;
    },
    show: function(message) {
        message && (Utility.actInd.actIndWin.actIndContainer.actIndBg.loading.text = message);
        if (Utility.actInd.actIndWin && Utility.actInd.actIndWin.actInd.isHide) {
            Utility.actInd.actIndWin.actInd.isHide = false;
            Utility.actInd.actIndWin.actIndContainer.show();
            Utility.isAndroid && (Utility.actInd.actIndWin.actIndContainer.zIndex = 9999);
        }
    },
    setMessage: function(message) {
        Utility.actInd.actIndWin.actIndContainer.actIndBg.loading.text = message;
    },
    hide: function() {
        if (Utility.actInd.actIndWin && !Utility.actInd.actIndWin.actInd.isHide) {
            Utility.actInd.actIndWin.actInd.isHide = true;
            Utility.actInd.actIndWin.actIndContainer.hide();
            Utility.isAndroid && (Utility.actInd.actIndWin.actIndContainer.zIndex = -9999);
        }
    }
};

Utility.progressBar = {
    pbar: void 0,
    counter: 0,
    total: 1,
    show: function(args) {
        void 0 === args && (args = {});
        Utility.progressBar.message = args.msg || Alloy.Globals.CB.Util.L("progressMsg");
        Utility.progressBar.total = args.total || 1;
        var msg = Utility.format(Utility.progressBar.message, [ 1, Utility.progressBar.total ]);
        if (Utility.isAndroid && 3 > parseFloat(Ti.version)) Utility.progressBar.pbar = Ti.UI.createActivityIndicator({
            location: Titanium.UI.ActivityIndicator.DIALOG,
            type: Titanium.UI.ActivityIndicator.DETERMINANT,
            message: msg,
            min: 0,
            max: args.max || 100,
            value: 1,
            zIndex: 100
        }); else {
            Utility.progressBar.pbar = Titanium.UI.createProgressBar({
                width: args.width || "80%",
                min: 0,
                max: args.max || 100,
                value: 1,
                height: args.height || 60,
                message: msg,
                top: args.top || "50%",
                color: args.color || "#fff",
                zIndex: 100
            });
            args.style && (Utility.progressBar.pbar.style = args.style);
            Utility.progressBar.pbar.font = args.font ? args.font : {
                fontSize: 14,
                fontWeight: "bold"
            };
        }
        indexContainer.pview = Ti.UI.createView({
            width: Ti.UI.FILL,
            height: Ti.UI.FILL,
            opacity: .75,
            backgroundColor: "#232323"
        });
        indexContainer.pview.add(Utility.progressBar.pbar);
        indexContainer.add(indexContainer.pview);
        Utility.progressBar.pbar.show();
    },
    setCounter: function(counter) {
        Utility.progressBar.counter += counter;
        var msg = Utility.format(Utility.L("progressMsg"), [ Utility.progressBar.counter, Utility.progressBar.total ]);
        Utility.progressBar.pbar.message = msg;
    },
    setValue: function(val) {
        Utility.progressBar.pbar.value = val;
    },
    hide: function() {
        Utility.progressBar.pbar.hide();
        indexContainer.remove(indexContainer.pview);
        indexContainer.pview = null;
    }
};

Utility.cleanDownloadFolder = function() {
    var directory = Ti.Filesystem.getFile(this.getAppDataDirectory(), Alloy.CFG.downloadFolder);
    directory.exists() && directory.deleteDirectory(true);
};

Utility.getAppDataDirectory = function() {
    if (Utility.isAndroid) return Ti.Filesystem.isExternalStoragePresent() ? Ti.Filesystem.externalStorageDirectory : Ti.Filesystem.tempDirectory;
    return Ti.Filesystem.applicationDataDirectory;
};

Utility.startLoading = function(message) {
    indexContainer.add(loading.getView());
    message && loading.setMessage(message);
    loading.start();
};

Utility.stopLoading = function() {
    debug.echo("stop loading==========", 413, "util");
    loading.stop();
    indexContainer.remove(loading.getView());
};

Utility.format = function(formatted, args) {
    for (var i = 0; args.length > i; i++) {
        var regexp = new RegExp("\\{" + i + "\\}", "gi");
        formatted = formatted.replace(regexp, args[i]);
    }
    return formatted;
};

Utility.trim = function(str) {
    return str.replace(/^\s+|\s+$/g, "");
};

Utility.ltrim = function(str) {
    return str.replace(/^\s+/, "");
};

Utility.rtrim = function(str) {
    return str.replace(/\s+$/, "");
};

Utility.lpad = function(str, padString, length) {
    while (length > str.length) str = padString + str;
    return str;
};

Utility.rpad = function(str, padString, length) {
    while (length > str.length) str += padString;
    return str;
};

Utility.reverse = function(str) {
    return str.split("").reverse().join("");
};

Utility.isTablet = Math.min(Ti.Platform.displayCaps.platformHeight, Ti.Platform.displayCaps.platformWidth) > 600;

Utility.isAndroid = "android" == Ti.Platform.osname;

Utility.osname = Ti.Platform.osname;

module.exports = Utility;