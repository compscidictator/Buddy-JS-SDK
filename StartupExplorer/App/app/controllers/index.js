$.main = Alloy.createController('main');

Alloy.Globals.CB.init({
    index: $.index,
    main: $.main.getView("content")
});

$.index.addEventListener('android:back',function(){
    Alloy.Globals.CB.pushController({
        action: Alloy.Globals.CB.UI.NavAction.Back,
        animation: Alloy.Globals.CB.UI.AnimationStyle.NavRight
    });	
});

$.index.add($.main.getView());

$.main.onLoad();

$.index.open();