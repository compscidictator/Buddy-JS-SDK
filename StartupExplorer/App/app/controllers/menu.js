$.btHome.addEventListener('click', function(){
	Ti.App.fireEvent('home');
});

$.btAreas.addEventListener('click', function(){
	Ti.App.fireEvent('areas');
});

$.btNear.addEventListener('click', function(){
	Ti.App.fireEvent('near');
});

$.btSearch.addEventListener('click', function(){
	Ti.App.fireEvent('search');	
});

Ti.App.addEventListener('menuOff', function(data) 
{ 
	$.btHome.backgroundImage = '/images/bt_home_off.png';
	$.btAreas.backgroundImage = '/images/bt_areas_off.png';
	$.btSearch.backgroundImage = '/images/bt_search_off.png';	
	$.btNear.backgroundImage = '/images/bt_near_off.png';		
});
Ti.App.addEventListener('homeOn', function(data) 
{ 
	$.btHome.backgroundImage = '/images/bt_home_on.png';		
});
Ti.App.addEventListener('areasOn', function(data) 
{ 
	$.btAreas.backgroundImage = '/images/bt_areas_on.png';		
});
Ti.App.addEventListener('searchOn', function(data) 
{ 
	$.btSearch.backgroundImage = '/images/bt_search_on.png';		
});
Ti.App.addEventListener('nearOn', function(data) 
{ 
	$.btNear.backgroundImage = '/images/bt_near_on.png';		
});