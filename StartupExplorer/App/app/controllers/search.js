exports.baseController = "base";

$.onLoad = function() {
    Ti.App.fireEvent('hideFooter');
};

function truncNb(Nb, ind) {
  var _nb = Nb * (Math.pow(10,ind));
  _nb = Math.floor(_nb);
  _nb = _nb / (Math.pow(10,ind));
  return _nb;
}

function int2roundKMG(val) {
  var _str = "";
  if (val >= 1e9)        { _str = truncNb((val/1e9), 1) + 'G';
  } else if (val >= 1e6) { _str = truncNb((val/1e6), 1) + 'M';
  } else if (val >= 1e3) { _str = truncNb((val/1e3), 1) + 'k';
  } else { _str = parseInt(val);
  }
  return _str;
}

var areaTerm = 'All';
var industryTerm = 'All';
var fundingTerm = 'All';
var amountTerm = '5000000';
var employeesTerm = '150';

$.fundingValue.text = '$5M';
function updateFunding(e){
    $.fundingValue.text = '$'+int2roundKMG(e.value);
    amountTerm = e.value;
};

$.employeeValue.text = '150';
function updateEmployee(e){
    $.employeeValue.text = e.value.toFixed(0);
    employeesTerm = String.format("%3.0f", e.value);
};

//creates select picker for filters
function createPicker(dataSet, type){

	var pickerContainer = Ti.UI.createView({
		top:Alloy.Globals.height2,
		height:Alloy.Globals.height2,
		width:Alloy.Globals.width,
		left:0,
		right:0,
		backgroundColor:'rgba(0,0,0,.8)',
		opacity:0,
		zIndex:1001
	});
	var picker = Ti.UI.createPicker();
	
	var defaultButton = {
		backgroundImage : '/images/picker_button_bg.png',
		width : Alloy.Globals.pickerWidth,
		height : Alloy.Globals.pickerHeight,
		top : Alloy.Globals.pickerPadding,
		bottom : Alloy.Globals.pickerPadding,
		left : Alloy.Globals.pickerPadding / 2,
		right : Alloy.Globals.pickerPadding / 2
	};

	function makeButton( title ) {
		var button = Ti.UI.createView( defaultButton );

		var label = Ti.UI.createLabel( {
			text : title,
			color : '#a4a4a0',
			height: Alloy.Globals.pickerHeight,
			font:{fontFamily:'TitilliumText25L-800wt', fontSize:'11dp'}
		} );

		button.add( label );

		return button;
	}

	var previous = makeButton( 'PREV' );
	var next = makeButton( 'NEXT' );
	var done = makeButton( 'DONE' );
	var cancel = makeButton( 'CANCEL' );	
	
	var toolbar = Ti.UI.createView( {
		backgroundColor : '#74807b',
		bottom:216,
		height:Alloy.Globals.pickerHeight+(Alloy.Globals.pickerPadding*2),
		width : Ti.UI.FILL
	});
	
	var navButtons = Ti.UI.createView( {
		layout : 'horizontal',
		height : Ti.UI.SIZE,
		width : Ti.UI.SIZE,
		left : 0
	} );

	var endButtons = Ti.UI.createView( {
		layout : 'horizontal',
		height : Ti.UI.SIZE,
		width : Ti.UI.SIZE,
		right : 0
	} );
	
	navButtons.add( previous );
	navButtons.add( next );
	endButtons.add( cancel );
	endButtons.add( done );

	toolbar.add( endButtons );	
	
	if(type=='area'){
		var none = Ti.UI.createPickerRow({title:'All'});	
		picker.add(none);	
	};
	picker.add(dataSet);
	picker.selectionIndicator = true;		
		
	pickerContainer.add(toolbar);
	
	if(Ti.Platform.osname != 'android'){
		picker.bottom = 0;
		pickerContainer.add(picker);
		toolbar.add( navButtons );
	} else {
		pickerContainer.backgroundColor = '#000';
		toolbar.bottom = 0;
		pickerContainer.height = Ti.UI.FILL;
		picker.left = 0;
		toolbar.add(picker);
		navButtons = null;
	};	
	
	$.search.add(pickerContainer);
	
	pickerContainer.animate({
		top:0,
		opacity:1,
		duration:300,
		curve: Titanium.UI.ANIMATION_CURVE_EASE_IN		
	});

	var currentIndex = 0;
	
	previous.addEventListener( 'click', function( ) {
		var previousIndex = currentIndex - 1;

		if ( previousIndex >= 0 ) {
			// move it and animate the move
			currentIndex -= 1;
			picker.setSelectedRow( 0, previousIndex, true );
		}
	} );

	next.addEventListener( 'click', function( ) {
		var nextIndex = currentIndex + 1;

		if ( nextIndex < (Alloy.Globals.metroAreas.length+1) ) {
			currentIndex += 1;
			picker.setSelectedRow( 0, nextIndex, true );
		}
	} );	
	
	cancel.addEventListener('click', function(){
		pickerContainer.animate({
			top:Alloy.Globals.height2,
			opacity:0,
			duration:300,
			curve: Titanium.UI.ANIMATION_CURVE_EASE_OUT
		}, function(){ $.search.remove(pickerContainer); picker = null; pickerContainer = null; });
	});	
	
	done.addEventListener('click', function(e){
		if(type=='area'){
		    $.sArea.children[0].text = picker.getSelectedRow(0).title;
		    areaTerm = picker.getSelectedRow(0).title
		} else if(type=='industry') {
		    $.sIndustry.children[0].text = picker.getSelectedRow(0).title;
		    industryTerm = picker.getSelectedRow(0).title;			
		} else if(type=='funding') {
		    $.sFunding.children[0].text = picker.getSelectedRow(0).title;
		    fundingTerm = picker.getSelectedRow(0).title			
		};	
		cancel.fireEvent('click');							
	});		
	
	
};//end create picker

$.sArea.addEventListener('click', function(){
	
	createPicker(Alloy.Globals.metroAreas, 'area');

});

$.sIndustry.addEventListener('click', function(){
	
	createPicker(Alloy.Globals.industryArray, 'industry');	

});

$.sFunding.addEventListener('click', function(){
	
	createPicker(Alloy.Globals.fundingArray, 'funding');	
	
});


$.btSubmit.addEventListener('click', function(){
	Alloy.Globals.CB.Util.startLoading();
				Alloy.Globals.CB.pushController({
			        controller: 'tab',
			        action: Alloy.Globals.CB.UI.NavAction.KeepBack,
			        animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
			        //pass data to next controller
			        data: [{
			            'region': '',
			            'type': 'search',
			            'term': $.name.value+'%25',
			            'area': areaTerm,
			            'funding': fundingTerm,
			            'industry': industryTerm,
			            'amount': amountTerm,
			            'employees': employeesTerm,
			            'show': 'list'
			        }],
			        showInd: false
				});	
				Ti.App.fireEvent('showFooter');	
});

$.btBack.addEventListener('click', function(){
    Ti.App.fireEvent('back');
});

if(Alloy.Globals.ddValues == false) {
	var fullSet = [];
	var xhrSearch = Ti.Network.createHTTPClient({cache:true});
	xhrSearch.onload = function() {
		fullSet = JSON.parse(this.responseText);
		var industryTemp = [];
		var fundingTemp = [];
		Alloy.Globals.industryArray[0] = Ti.UI.createPickerRow({title:'All'});
		Alloy.Globals.fundingArray[0] = Ti.UI.createPickerRow({title:'All'});		
		for (var i = 0; i < fullSet.data.length; i++){
			if(fullSet.data[i].industry != '') {
				industryTemp[i] = fullSet.data[i].industry;
			};
			if(fullSet.data[i].fundingSource != '') {
				fundingTemp[i] = fullSet.data[i].fundingSource;
			};		
		};
		
		function compare(strA, strB) {
		   var icmp = strA.toLowerCase().localeCompare(strB.toLowerCase());
		   return icmp == 0
		          ? (strA > strB
		            ? 1
		            : (
		              strA < strB ? -1 : 0
		              )
		            )
		          : icmp;
		}		
		
		industryTemp = _.uniq(industryTemp);
		industryTemp = industryTemp.sort( compare );
		
		for (var i = 0; i < industryTemp.length; i++){
				Alloy.Globals.industryArray[i+1] = Ti.UI.createPickerRow({title:industryTemp[i]});
		};		
		
		fundingTemp = _.uniq(fundingTemp);
		fundingTemp = fundingTemp.sort( compare );
		for (var i = 0; i < fundingTemp.length; i++){
				Alloy.Globals.fundingArray[i+1] = Ti.UI.createPickerRow({title:fundingTemp[i]});
		};			
		
		Alloy.Globals.ddValues = true;
		fundingTemp = industryTemp = fullSet = xhrSearch = null;
	};
	
	xhrSearch.onerror = function() {
	    Titanium.API.info('Error grabbing buddy data for pickers');
	};
	
	xhrSearch.open("GET","https://webservice.buddyplatform.com/Service/v1/BuddyService.ashx?StartupData_Location_Search&BuddyApplicationName=Startup%20Explorer&BuddyApplicationPassword="+Alloy.Globals.applicationPassword+"&UserToken="+Alloy.Globals.userToken+"&SearchDistance=100000000&Latitude=39.8106460&Longitude=-98.5569760&RecordLimit=690&SearchName=");		
	xhrSearch.send();
};
