var Alloy = require("alloy"), _ = require("alloy/underscore")._;

var _mainContent, _currentController, _previousController;

var CB = function() {};

CB.Version = Ti.App.version;

CB.UI = require("ui");

CB.Util = require("util");

CB.Net = require("net");

CB.WP = require("wp");

CB.Debug = require("debug");

CB.init = function(mainContent) {
    CB.Util.init(mainContent.index);
    _mainContent = mainContent.main;
    _currentController = Alloy.createController(Alloy.CFG.firstController);
};

CB.getCurrentController = function() {
    void 0 === _currentController && (_currentController = Alloy.createController(Alloy.CFG.firstController));
    return _currentController;
};

CB.pushController = function(args) {
    args.showInd && CB.Util.actInd.show();
    args.duration || (args.duration = Alloy.CFG.animationDuration);
    var oldController = _currentController;
    var oldView = oldController.getView();
    oldView.name || (oldView.name = Alloy.CFG.firstController);
    (void 0 === args.controller || null === args.controller) && (args.controller = oldView.name);
    if (args.action && args.action == CB.UI.NavAction.Back) {
        args.controller = _previousController;
        Alloy.Globals.CB.Debug.echo("_previousController:==" + _previousController, 81, "core.js");
    }
    _currentController = Alloy.createController(args.controller, args.data || {});
    _currentController.onLoad();
    var currentView = _currentController.getView();
    currentView.name = args.controller;
    _mainContent.width = Ti.Platform.displayCaps.platformWidth;
    currentView.width = Ti.Platform.displayCaps.platformWidth;
    switch (args.animation) {
      case CB.UI.AnimationStyle.FadeIn:
        currentView.left = 0;
        currentView.opacity = 0;
        _mainContent.add(currentView);
        currentView.animate({
            opacity: 1,
            duration: args.duration
        }, function() {
            finishedPush(args, currentView, oldView, oldController);
        });
        break;

      case CB.UI.AnimationStyle.FadeOut:
        currentView.left = 0;
        currentView.opacity = 0;
        _mainContent.add(currentView);
        oldView.opacity = 1;
        oldView.animate({
            opacity: 0,
            duration: args.duration
        }, function() {
            currentView.animate({
                opacity: 1,
                duration: args.duration
            }, function() {
                finishedPush(args, currentView, oldView, oldController);
            });
        });
        break;

      case CB.UI.AnimationStyle.NavLeft:
        currentView.left = Ti.Platform.displayCaps.platformWidth;
        _mainContent.add(currentView);
        _mainContent.children[0].left = 0;
        _mainContent.children[0].width = Ti.Platform.displayCaps.platformWidth;
        _mainContent.width = 2 * Ti.Platform.displayCaps.platformWidth;
        _mainContent.left = 0;
        _mainContent.animate({
            left: -currentView.left,
            duration: args.duration
        }, function() {
            currentView.left = 0;
            _mainContent.left = 0;
            _mainContent.width = Ti.Platform.displayCaps.platformWidth;
            finishedPush(args, currentView, oldView, oldController);
        });
        break;

      case CB.UI.AnimationStyle.NavRight:
        _mainContent.width = 2 * Ti.Platform.displayCaps.platformWidth;
        _mainContent.left = Ti.Platform.displayCaps.platformWidth;
        _mainContent.children[0].left = Ti.Platform.displayCaps.platformWidth;
        _mainContent.children.length > 1 && (_mainContent.children[1].left = Ti.Platform.displayCaps.platformWidth);
        currentView.left = 0;
        _mainContent.add(currentView);
        _mainContent.animate({
            left: 0,
            duration: args.duration
        }, function() {
            _mainContent.left = 0;
            _mainContent.width = Ti.Platform.displayCaps.platformWidth;
            finishedPush(args, currentView, oldView, oldController);
        });
        break;

      case CB.UI.AnimationStyle.SlideLeft:
        currentView.left = Ti.Platform.displayCaps.platformWidth;
        _mainContent.add(currentView);
        currentView.animate({
            left: 0,
            duration: args.duration
        }, function() {
            finishedPush(args, currentView, oldView, oldController);
        });
        break;

      case CB.UI.AnimationStyle.SlideRight:
        oldView.left = 0;
        oldView.zIndex = 100;
        oldView.animate({
            left: Ti.Platform.displayCaps.platformWidth,
            duration: args.duration
        }, function() {
            finishedPush(args, currentView, oldView, oldController);
        });
        break;

      case CB.UI.AnimationStyle.SlideUp:
        currentView.top = 2 * Ti.Platform.displayCaps.platformHeight;
        _mainContent.add(currentView);
        currentView.animate({
            top: 0,
            duration: args.duration
        }, function() {
            finishedPush(args, currentView, oldView, oldController);
        });
        break;

      case CB.UI.AnimationStyle.SlideDown:
        oldView.top = 0;
        oldView.zIndex = 100;
        oldView.animate({
            top: 2 * Ti.Platform.displayCaps.platformHeight,
            duration: args.duration
        }, function() {
            finishedPush(args, currentView, oldView, oldController);
        });
        break;

      case CB.UI.AnimationStyle.None:
        finishedPush(args, currentView, oldView, oldController);
        break;

      default:
        finishedPush(args, currentView, oldView, oldController);
    }
};

var finishedPush = function(args, currentView, oldView, oldController) {
    if (Alloy.CFG.hasCustomTabs && !args.noTabs) {
        var tab = args.container;
        args.currTab && (tab = args.currTab);
        Alloy.Globals.Tabs.setTab(tab);
    }
    args.action && args.action == CB.UI.NavAction.KeepBack && (_previousController = oldView.name);
    oldController.onClose();
    _mainContent.remove(oldView);
    args.callback && args.callback(_currentController);
    oldController = null;
    args.showInd && CB.Util.actInd.hide();
};

module.exports = CB;