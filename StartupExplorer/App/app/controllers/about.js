$.btClose.addEventListener('click', function(){
	$.aboutView.animate({
		top: Alloy.Globals.height,
		curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
      	opacity:0
	});
});

Ti.App.addEventListener('about', function(data) 
{ 
	Ti.API.info('Footer Clicked');
	if(Ti.Platform.osname == 'android'){
		$.aboutView.top = 1;
		$.aboutView.animate({
	      	curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
	      	opacity:1,
	      	duration:100
		});		
	} else {
		$.aboutView.animate({
			top:0,
	      	curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT,
	      	opacity:1,
	      	duration:100
		});
	};
});

$.btFacebook.addEventListener('click', function(){
	Ti.Platform.openURL("https://www.facebook.com/BuddyPlatform");
});

$.btTwitter.addEventListener('click', function(){
	Ti.Platform.openURL("https://twitter.com/BuddyPlatform");
});

$.btEmail.addEventListener('click', function(){
	var emailDialog = Ti.UI.createEmailDialog()
	emailDialog.subject = "Message from Startup Explorer";
	emailDialog.toRecipients = ['startupexplorer@buddy.com'];
	
	emailDialog.open();	
});

$.buddyLogo.addEventListener('click', function(){
	Ti.Platform.openURL("http://buddy.com");
});

$.rocketLogo.addEventListener('click', function(){
	Ti.Platform.openURL("http://rocketscience.is");
});

$.aboutView.bubbleParent = false;