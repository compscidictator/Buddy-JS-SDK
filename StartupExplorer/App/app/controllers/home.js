exports.baseController = "base";

$.btAdd.addEventListener('click', function(e){
    Alloy.Globals.CB.pushController({
        controller: 'add',
		action: Alloy.Globals.CB.UI.NavAction.KeepBack,
        animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
        data: []
    });
    Ti.App.fireEvent('menuOff');	
});

$.btNearMe.addEventListener('click', function(e){
    Ti.App.fireEvent('near');	
});

$.btNewest.addEventListener('click', function(e){
	Ti.App.fireEvent('newest');
});

function mapClick(loc){
	Alloy.Globals.CB.Util.startLoading();
		    Alloy.Globals.CB.pushController({
		        controller: 'tab',
		        action: Alloy.Globals.CB.UI.NavAction.KeepBack,
		        animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
		        //pass data to next controller
		        data: [{
		            'region': loc
		        }],
		        showInd: false
		    });	
		    Ti.App.fireEvent('menuOff');
		    Ti.App.fireEvent('areasOn');
};


var areaClean = null;
var areas = [];
var regionDataUnsorted = [];
var regionData = [];
var metroAreas = [];
var xhrRegion = Ti.Network.createHTTPClient({cache:true});
xhrRegion.onload = function() {
	regionData = JSON.parse(this.responseText);
	
	//regionData.data = _.sortBy(regionData.data, dynamicSort("startupCount"));
	regionData.data = regionData.data.sort(dynamicSort("startupCount"));
	
	function dynamicSort(property) {
	    var sortOrder = 1;
	    if(property[0] === "-") {
	        sortOrder = -1;
	        property = property.substr(1, property.length - 1);
	    }
	    return function (a,b) {
	        var result = ((a[property] * 1) > (b[property] * 1)) ? -1 : ((a[property] * 1) < (b[property] * 1)) ? 1 : 0;
	        return result * sortOrder;
	    }
	}
	
	var mapViewArea = Ti.UI.createScrollableView({
		id:'mapArea',
		showPagingControl:true,
		top:Alloy.Globals.btDashHeight,
		bottom:Alloy.Globals.scrollableMapArea,
		//backgroundColor:'#fff',
		pagingControlColor:'transparent',
		color:'#000',
		views:[]
	});  		
	var mapTotalArea = [];
	var mapTotalLabel = [];
	var mapImage = [];
	var areas = [];
	for (var i = 0; i < regionData.data.length; i++){
		regionArea = regionData.data[i];
		areas[i] = regionArea.metroName.replace(/ /g,"%20");
		Alloy.Globals.metroAreas[i] = Ti.UI.createPickerRow({title:regionArea.metroName,value:regionArea.metroName.replace(/ /g,"%20")});
		

		mapView = Ti.UI.createView({
				id:areaClean
		});
		mapImage[i] = Ti.UI.createImageView({
				image:regionArea.iconURL,
				width:Alloy.Globals.mapWidth,
				myID: areas[i]
		});
		mapTotalArea[i] = Ti.UI.createView({
			backgroundColor:"#3cd5d0",
			height:Alloy.Globals.mapTotalWidth,
			width:Alloy.Globals.mapTotalWidth,
			opacity:'.9',
			top:'10%',
			left:'12%',
			zIndex:999,
			borderRadius:Alloy.Globals.mapTotalRadius,
			touchEnabled:false			
		});
		
		mapTotalLabel[i] = Ti.UI.createLabel({
			font:{fontFamily:'SurfLight', fontSize:'34dp'},
			color:'#181717',
			text:regionArea.startupCount,
			id:'sum'+i
		});	
		mapTotalArea[i].add(mapTotalLabel[i]);	
			
		mapView.add(mapTotalArea[i]);	
		mapView.add(mapImage[i]);
		
	    (function(view) {
	        view.addEventListener('click',function(e){
	            mapClick(this.myID);
	        });
	    }(mapImage[i]));  
			
		mapViewArea.addView(mapView);		
	
	}
	
     //add map to mainWindow
   
	$.mainWindow.add(mapViewArea);	

}

xhrRegion.onerror = function() {
    Titanium.API.info('Error grabbing buddy data');
    alert('Internet connection is required, please check your device settings.')
}

xhrRegion.open("GET","https://webservice.buddyplatform.com/Service/v1/BuddyService.ashx?StartupData_Location_GetMetroList&BuddyApplicationName=Startup%20Explorer&BuddyApplicationPassword="+Alloy.Globals.applicationPassword);
xhrRegion.send();