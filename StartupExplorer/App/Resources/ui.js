var Alloy = require("alloy"), _ = require("alloy/underscore"), util = require("util");

var UI = function() {};

UI.zoom = function(view, callback) {
    var matrix = Ti.UI.create2DMatrix({
        scale: 1.5
    });
    view.animate({
        transform: matrix,
        opacity: 0,
        duration: 250
    }, function() {
        callback && callback();
    });
};

UI.unzoom = function(view, callback) {
    var matrix = Ti.UI.create2DMatrix({
        scale: 1
    });
    view.animate({
        transform: matrix,
        opacity: 1,
        duration: 250
    }, function() {
        callback && callback();
    });
};

UI.fauxShadow = function() {
    return Ti.UI.createView({
        bottom: 0,
        height: "1dp",
        backgroundColor: "#9a9a9a"
    });
};

UI.HeaderView = function(options) {
    function option(t, idx) {
        var rightOffset = options.optionWidth * Math.abs(idx - (options.options.length - 1)) + "dp";
        var v = Ti.UI.createView({
            width: options.optionWidth + "dp",
            right: rightOffset
        });
        var l = Ti.UI.createLabel({
            text: L(t),
            color: "#fff",
            height: Ti.UI.SIZE,
            width: Ti.UI.SIZE,
            font: {
                fontFamily: "Quicksand-Bold",
                fontSize: "14dp"
            }
        });
        v.add(l);
        v.addEventListener("click", function() {
            indicator.animate({
                right: rightOffset,
                duration: 250
            }, function() {
                self.fireEvent("change", {
                    selection: t
                });
            });
        });
        return v;
    }
    var self = Ti.UI.createView(_.extend({
        backgroundColor: "transparent",
        height: "35dp"
    }, options.viewArgs || {}));
    var fauxShadow = new FauxShadow();
    self.add(fauxShadow);
    var indicator = Ti.UI.createView({
        top: 0,
        right: options.optionWidth * (options.options.length - 1) + "dp",
        bottom: "1dp",
        width: options.optionWidth + "dp",
        backgroundColor: "#ffce00"
    });
    self.add(indicator);
    if (options.hasTitle) {
        var title = Ti.UI.createLabel({
            text: options.title,
            color: "#373e47",
            left: "10dp",
            width: Ti.UI.SIZE,
            height: Ti.UI.SIZE,
            font: {
                fontFamily: "Quicksand-Bold",
                fontSize: "14dp"
            }
        });
        self.add(title);
    }
    for (var i = 0, l = options.options.length; l > i; i++) self.add(option(options.options[i], i));
    self.on = function(ev, cb) {
        self.addEventListener(ev, cb);
    };
    self.goTo = function(idx) {
        var rightOffset = options.optionWidth * Math.abs(idx - (options.options.length - 1)) + "dp";
        indicator.right = rightOffset;
    };
    return self;
};

UI.createPopupWin = function(args) {
    args.winHeight || (args.winHeight = .9 * Ti.Platform.displayCaps.platformHeight);
    args.winWidth || (args.winWidth = .9 * Ti.Platform.displayCaps.platformWidth);
    args.view.popBgView = Titanium.UI.createView({
        backgroundColor: "#000",
        height: args.view.height,
        width: args.view.width,
        opacity: .8
    });
    args.view.add(args.view.popBgView);
    args.view.popWin = Titanium.UI.createWindow({});
    args.view.popWin.popView = Titanium.UI.createView({
        borderColor: "#999",
        height: args.winHeight,
        width: args.winWidth,
        backgroundColor: "#fff"
    });
    args.borderRadius && (args.view.popWin.popView.borderRadius = args.borderRadius);
    args.borderWidth && (args.view.popWin.popView.borderWidth = args.borderWidth);
    if (util.isAndroid) {
        args.view.popWin.add(args.view.popWin.popView);
        args.view.popWin.open({
            animate: true
        });
        args.view.popWin.popView.addEventListener("postlayout", function() {
            createCloseButton(args.view);
        });
    } else {
        var t = Titanium.UI.create2DMatrix();
        t = t.scale(0);
        args.view.popWin.transform = t;
        var t1 = Titanium.UI.create2DMatrix();
        t1 = t1.scale(1.1);
        var a = Titanium.UI.createAnimation();
        a.transform = t1;
        a.duration = 300;
        a.addEventListener("complete", function() {
            var t2 = Titanium.UI.create2DMatrix();
            t2 = t2.scale(1);
            args.view.popWin.animate({
                transform: t2,
                duration: 300
            });
            createCloseButton(args.view);
        });
        args.view.popWin.add(args.view.popWin.popView);
        args.view.popWin.open(a);
    }
    createCloseButton = function(view) {
        view.popWin.popCloseBtn = Ti.UI.createButton({
            top: "3%",
            right: "5%",
            width: 30,
            height: 30,
            zIndex: 100,
            backgroundImage: "btn-close.png"
        });
        view.popWin.popView.removeEventListener("postlayout", function() {});
        view.popWin.popCloseBtn.addEventListener("click", function() {
            closePopupWin(view);
        });
        view.popWin.add(view.popWin.popCloseBtn);
    };
    closePopupWin = function(view) {
        view.remove(view.popBgView);
        if (util.isAndroid) {
            view.popWin.popCloseBtn.hide();
            view.popWin.close({
                animate: true
            });
        } else {
            var t3 = Titanium.UI.create2DMatrix();
            t3 = t3.scale(0);
            view.popWin.close({
                transform: t3,
                duration: 300
            });
        }
    };
    return args.view.popWin.popView;
};

UI.createDropDownList = function(ddlArgs) {
    var html = "<html><head><meta name='viewport' content='user-scalable=0, initial-scale=1, maximum-scale=1, minimum-scale=1'></head><body style='background-color:transparent ;margin:0;padding:0'>";
    html += "<select id='{0}' style='width:100%; height:100%;font-size: {1}px; '>";
    for (var itemIndex in ddlArgs.items) html += util.format("<option value='{0}' {1}><span style='font-size:8px'>{2}</span></option>", [ ddlArgs.items[itemIndex].value, ddlArgs.items[itemIndex].selected, ddlArgs.items[itemIndex].text ]);
    html += "</select>";
    html += "<script type='text/javascript'>";
    html += "document.getElementById('{0}').onchange = function(){ Titanium.App.fireEvent('app:set{0}',{value:this.value}); };";
    html += "</script>";
    html += "</body></html>";
    html = util.format(html, [ ddlArgs.id, void 0 === ddlArgs.innerFontSize ? "12" : ddlArgs.innerFontSize ]);
    void 0 === ddlArgs.top && util.isAndroid && (ddlArgs.top = "10dp");
    void 0 === ddlArgs.height && (ddlArgs.height = .055 * Ti.Platform.displayCaps.platformHeight);
    var ddlObj = Ti.UI.createWebView({
        top: ddlArgs.top,
        left: ddlArgs.left,
        width: ddlArgs.width,
        height: .055 * Ti.Platform.displayCaps.platformHeight,
        scalesPageToFit: true,
        html: html
    });
    Ti.App.addEventListener("app:set" + ddlArgs.id, function(e) {
        ddlArgs.callback(e);
    });
    ddlObj.close = function() {
        Ti.App.removeEventListener("app:set" + ddlArgs.id, function(e) {
            ddlArgs.callback(e);
        });
    };
    return ddlObj;
};

UI.AnimationStyle = {
    None: 0,
    FadeIn: 1,
    FadeOut: 2,
    NavLeft: 3,
    NavRight: 4,
    SlideLeft: 5,
    SlideRight: 6,
    SlideUp: 7,
    SlideDown: 8
};

UI.NavAction = {
    None: 0,
    KeepBack: 1,
    Back: 2
};

module.exports = UI;