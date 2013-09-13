exports.baseController = "base";

var data = $.getData();
var mapType = data[0].type;
var areaTerm = data[0].area;
var fundingTerm = data[0].funding;
var industryTerm = data[0].industry;
var employeesTerm = data[0].employees * 1;
var amountTerm = data[0].amount * 1;

$.btBack.addEventListener('click', function() {
	Ti.App.fireEvent('back');
});

$.btList.addEventListener('click', function() {
	$.btList.touchEnabled = false;
	$.btMap.touchEnabled = true;
	$.tabGroup.animate({
		right : Alloy.Globals.widthNeg,
		curve : Titanium.UI.ANIMATION_CURVE_EASE_OUT,
		duration : 300
	});
	$.btList.backgroundImage = '/images/bt_list_on.png';
	$.btMap.backgroundImage = '/images/bt_map_off.png';
});

$.btMap.addEventListener('click', function() {
	$.btList.touchEnabled = true;
	$.btMap.touchEnabled = false;
	$.tabGroup.animate({
		right : 0,
		curve : Titanium.UI.ANIMATION_CURVE_EASE_IN,
		duration : 300
	});
	$.btMap.backgroundImage = '/images/bt_map_on.png';
	$.btList.backgroundImage = '/images/bt_list_off.png';
});

if (data[0].show == 'list') {
	$.btList.touchEnabled = false;
	$.btMap.touchEnabled = true;
	$.tabGroup.animate({
		right : Alloy.Globals.widthNeg,
		curve : Titanium.UI.ANIMATION_CURVE_EASE_OUT,
		duration : 300
	});
	$.btList.backgroundImage = '/images/bt_list_on.png';
	$.btMap.backgroundImage = '/images/bt_map_off.png';
};

$.onLoad = function() {

	var tableData = [];
	var rowView = [];
	var mapData = [];
	var mapDataUnsorted = [];
	var totalStartups = null;
	var xhr = Ti.Network.createHTTPClient({
		cache : true
	});

	xhr.onload = function() {
		mapData = JSON.parse(this.responseText);

		function compare(strA, strB) {
			var icmp = strA.startupName.toLowerCase().localeCompare(strB.startupName.toLowerCase());
			return icmp == 0 ? (strA > strB ? 1 : (strA < strB ? -1 : 0
			)
			) : icmp;
		}


		mapData.data.sort(compare);

		var markers = [];
		if (mapType == 'search') {
			var newArray = [];
			Ti.API.info(amountTerm);

			Ti.API.info(employeesTerm);
			newArray = _.filter(mapData.data, function(num) {
				return num.employeeCount <= employeesTerm && num.totalFundingRaised <= amountTerm;
			});
			mapData.data = newArray;
			if (areaTerm != 'All') {
				newArray = _.where(mapData.data, {
					metroLocation : areaTerm
				});
				mapData.data = newArray;
			};
			if (fundingTerm != 'All') {
				newArray = _.where(mapData.data, {
					fundingSource : fundingTerm
				});
				mapData.data = newArray;
			};
			if (industryTerm != 'All') {
				newArray = _.where(mapData.data, {
					industry : industryTerm
				});
				mapData.data = newArray;
			};
		};

		if (mapData.data.length == 0) {
			var dialog = Ti.UI.createAlertDialog({
				message : "There are no startups that match what you're looking for. Please try another search. Thanks.",
				ok : 'OK',
				title : 'No Startups'
			})
			dialog.addEventListener('click', function(e) {
				if (e.index == 0) {
					Alloy.Globals.CB.Util.stopLoading();
					Ti.App.fireEvent('search');
				};
			});
			if (mapType == "search") {
				dialog.message = 'There are no startups that meet your search critera. Please try another search. Thanks.'
			};
			dialog.show();
		} else {

			for (var i = 0; i < mapData.data.length; i++) {
				recorded = mapData.data[i];

				rowTitle = Ti.UI.createLabel({
					font : {
						fontFamily : 'TitilliumText25L-800wt',
						fontSize : '18dp'
					},
					color : '#4b595b',
					left : Alloy.Globals.rowRightWidth,
					top : Alloy.Globals.rowRightWidth / 1.5,
					text : recorded.startupName
				});

				rowDesc = Ti.UI.createLabel({
					font : {
						fontFamily : 'TitilliumText25L-250wt',
						fontSize : '13dp'
					},
					color : '#4b595b',
					left : Alloy.Globals.rowRightWidth,
					bottom : Alloy.Globals.rowRightWidth / 1.5,
					text : recorded.city + ' ' + recorded.zipPostal + ' | ' + recorded.metroLocation
				});

				rowButton = Ti.UI.createButton({
					backgroundImage : '/images/bt_row_right.png',
					width : Alloy.Globals.rowRightWidth,
					height : Alloy.Globals.rowRightHeight,
					right : 0,
					zIndex : 2
				});

				rowView[i] = Ti.UI.createView({
					top : 0,
					left : 0,
					right : 0,
					bottom : 0,
					backgroundImage : '/images/row_item_background.png',
					myCompany : recorded,
					logoURL : recorded.logoURL
				});

				rowView[i].add(rowButton);
				rowView[i].add(rowTitle);
				rowView[i].add(rowDesc);

				row = Ti.UI.createTableViewRow({
					className : 'company', // used to improve table performance
					rowIndex : i, // custom property, useful for determining the row during events
					backgroundSelectedColor : 'none',
					height : Alloy.Globals.rowHeight,
					backgroundColor : 'transparent'

				}); ( function(view) {
						view.addEventListener('click', function(e) {

							/*Alloy.Globals.CB.pushController({
							 controller: 'detail',
							 action: Alloy.Globals.CB.UI.NavAction.KeepBack,
							 animation: Alloy.Globals.CB.UI.AnimationStyle.NavLeft,
							 //pass data to next controller
							 data: [{
							 'details': this.myCompany,
							 'region': data[0].region
							 }],
							 showInd: false
							 });		*/

							showDetail(this.myCompany);
							$.mapView.remove(mapCluster.mapview);
							Ti.App.fireEvent('showDetailMap');

						});
					}(rowView[i]));

				row.add(rowView[i]);

				tableData.push(row);

				markers[i] = {
					title : recorded.startupName,
					subtitle : recorded.streetAddress + ', ' + recorded.city,
					latitude : recorded.centerLat * 1,
					longitude : recorded.centerLong * 1,
					myCompany : recorded
				};

				totalStartups++

			};/*End iteration*/

			if (Ti.Platform.osname == 'android') {
				$.listView.bubbleParent = false;
			};

			/*if(Ti.Platform.osname == 'android'){
			 var mapCluster = MapModule.createView({
			 mapType: MapModule.NORMAL_TYPE,
			 region: {latitude:0, longitude:0,
			 latitudeDelta:10, longitudeDelta:10},
			 animate:true,
			 regionFit:true,
			 userLocation: true,
			 width: '100%',
			 height: '100%',

			 });

			 //mapCluster.mapType = MapModule.NORMAL_TYPE;
			 } else {*/
			var mapCluster = new TiMapCluster({
				mapType : Titanium.Map.STANDARD_TYPE,
				region : {
					latitude : 0,
					longitude : 0,
					latitudeDelta : 10,
					longitudeDelta : 10
				},
				animate : true,
				regionFit : true,
				userLocation : true,
				width : '100%',
				height : '100%'
			}, markers);
			//};

			var metroName = data[0].region.replace(/%20/g, " ");

			var cityLat = mapData.data[0].centerLat;
			var cityLong = mapData.data[0].centerLong;
			var zoomLat = 1;
			var zoomLong = 1;

			if (metroName == 'Seattle Area') {
				cityLat = 47.6097;
				cityLong = -122.3331;
			} else if (metroName == 'Portland Oregon') {
				cityLat = 45.5236;
				cityLong = -122.6750;
			} else if (metroName == 'New York City') {
				cityLat = 40.7142;
				cityLong = -74.0064;
			}
			;

			if (mapType == 'near') {
				cityLat = Alloy.Globals.userLatitude;
				cityLong = Alloy.Globals.userLongitude;
			} else if (mapType == 'search') {
				zoomLat = 35;
				zoomLong = 35;
			}
			;

			/*if(Ti.Platform.osname == 'android'){
			 $.mapView.add(mapCluster);
			 } else {	    */
			mapCluster.mapview.region = {
				latitude : cityLat,
				longitude : cityLong,
				latitudeDelta : zoomLat,
				longitudeDelta : zoomLong
			};

			//};
			Ti.App.addEventListener('addMapCluster', function() {
				$.mapView.add(mapCluster.mapview);
			});
			Ti.App.fireEvent('addMapCluster');

			$.mapView.addEventListener('click', function(evt) {

				// Check for all of the possible names that clicksouce
				// can report for the left button/view.

				if (evt.clicksource == 'leftButton' || evt.clicksource == 'leftPane' || evt.clicksource == 'leftView' || evt.clicksource == 'rightButton' || evt.clicksource == 'rightPane' || evt.clicksource == 'rightView' || evt.clicksource == "title" || evt.clicksource == "subtitle") {
					showDetail(evt.annotation.myCompany);
					$.mapView.remove(mapCluster.mapview);
					Ti.App.fireEvent('showDetailMap');
					//mapCluster = null;
				};

			});

			$.listView.setData(tableData);

			if (mapType == 'near') {
				metroName = "YOU"
			};
			if (mapType == 'search') {
				$.statusArea.text = totalStartups + ' RESULTS FROM YOUR SEARCH'
			} else {
				$.statusArea.text = totalStartups + ' AROUND ' + metroName.toUpperCase();
			};

			$.statusAreaButton.addEventListener('click', function() {
				$.listView.scrollToIndex(0, {
					position : Titanium.UI.iPhone.TableViewScrollPosition.MIDDLE,
					animated : true,
					duration : 300
				});
			});

			Ti.Gesture.addEventListener('shake', function() {
				$.statusAreaButton.fireEvent('click');
			});

			Alloy.Globals.CB.Util.stopLoading();
		};
	}

	xhr.onerror = function() {
		Titanium.API.info('Error grabbing buddy data');
	};

	if (mapType == 'near') {
		xhr.open("GET", "https://webservice.buddyplatform.com/Service/v1/BuddyService.ashx?StartupData_Location_Search&BuddyApplicationName=Startup%20Explorer&BuddyApplicationPassword=" + Alloy.Globals.applicationPassword + "&UserToken=" + Alloy.Globals.userToken + "&SearchDistance=5000&Latitude=" + Alloy.Globals.userLatitude + "&Longitude=" + Alloy.Globals.userLongitude + "&RecordLimit=600&SearchName=");
	} else if (mapType == "search") {
		var termClean = data[0].term.replace(/ /g, '%20');
		Ti.API.info('starting search, Term: ' + termClean);
		xhr.open("GET", "https://webservice.buddyplatform.com/Service/v1/BuddyService.ashx?StartupData_Location_Search&BuddyApplicationName=Startup%20Explorer&BuddyApplicationPassword=" + Alloy.Globals.applicationPassword + "&UserToken=" + Alloy.Globals.userToken + "&SearchDistance=100000000&Latitude=39.8106460&Longitude=-98.5569760&RecordLimit=600&SearchName=" + termClean);
	} else if (mapType == "newest") {
		xhr.open("GET", "https://webservice.buddyplatform.com/Service/v1/BuddyService.ashx?StartupData_Location_GetFromMetroArea&BuddyApplicationName=Startup%20Explorer&BuddyApplicationPassword=" + Alloy.Globals.applicationPassword + "&UserToken=" + Alloy.Globals.userToken + "&MetroName=" + data[0].region + "&RecordLimit=20");
	} else {
		xhr.open("GET", "https://webservice.buddyplatform.com/Service/v1/BuddyService.ashx?StartupData_Location_GetFromMetroArea&BuddyApplicationName=Startup%20Explorer&BuddyApplicationPassword=" + Alloy.Globals.applicationPassword + "&UserToken=" + Alloy.Globals.userToken + "&MetroName=" + data[0].region + "&RecordLimit=1000");
	}
	;
	xhr.send();

};

//For Detail View

function truncNb(Nb, ind) {
	var _nb = Nb * (Math.pow(10, ind));
	_nb = Math.floor(_nb);
	_nb = _nb / (Math.pow(10, ind));
	return _nb;
}

function int2roundKMG(val) {
	var _str = "";
	if (val >= 1e9) {
		_str = truncNb((val / 1e9), 1) + 'G';
	} else if (val >= 1e6) {
		_str = truncNb((val / 1e6), 1) + 'M';
	} else if (val >= 1e3) {
		_str = truncNb((val / 1e3), 1) + 'k';
	} else {
		_str = parseInt(val);
	}
	return _str;
}

function showDetail(details) {
	var name = details.startupName;
	var street = details.streetAddress;
	var city = details.city;
	var state = details.state;
	var zip = details.zipPostal;
	var phone = details.phoneNumber;
	var region = details.metroLocation;
	var url = details.homePageURL;
	var industry = details.industry;
	var employees = details.employeeCount;
	var funding = details.totalFundingRaised;
	var source = details.fundingSource;
	var twitter = details.twitterURL;
	var facebook = details.facebookURL;
	var linkedin = details.linkedinURL;
	var crunchbase = details.crunchBaseUrl;
	var latitude = details.centerLat;
	var longitude = details.centerLong;
	var logo = details.logoURL;

	$.name.text = name;

	if (industry) {
		var industryLabel = Ti.UI.createLabel({
			color : '#d15059',
			font : {
				fontFamily : 'TitilliumText25L-800wt',
				fontSize : '12dp'
			},
			left : Alloy.Globals.detailHeadingPadding,
			text : industry,
			top : Alloy.Globals.detailIndustryWidth
		});
		$.desc.add(industryLabel);
	};

	if (employees || funding || source) {
		var layout = 0;
		if (employees) {
			layout++;
		};
		if (funding && funding != 0) {
			layout++;
		};
		if (source) {
			layout++;
		};

		var barHeader = Ti.UI.createView({
			layout : 'horizontal',
			bottom : 0,
			left : Alloy.Globals.detailHeadingPadding,
			right : Alloy.Globals.detailHeadingPadding,
			height : Alloy.Globals.barHeadingHeight
		});
		var barInfo = Ti.UI.createView({
			layout : 'horizontal',
			top : 0,
			left : Alloy.Globals.detailHeadingPadding,
			right : Alloy.Globals.detailHeadingPadding,
			height : Alloy.Globals.barInfoHeight
		});

		if (employees) {
			if (employees == 0) {
				employees = 1;
			};
			var employeeHeader = Ti.UI.createLabel({
				backgroundColor : '#a5d9d5',
				text : 'EMPLOYEES',
				color : '#4b595b',
				width : 0,
				height : Alloy.Globals.barHeadingHeight,
				font : {
					fontFamily : 'Surf-Regular',
					fontSize : '12dp'
				},
				textAlign : 'center'
			});
			var employeeInfo = Ti.UI.createLabel({
				backgroundColor : '#57c4c3',
				text : employees,
				color : '#4b595b',
				width : 0,
				height : Alloy.Globals.barHeadingHeight,
				font : {
					fontFamily : 'SurfLight',
					fontSize : '20dp'
				},
				textAlign : 'center'
			});
			if (layout == 1) {
				employeeHeader.width = '100%';
				employeeInfo.width = '100%';
			};
			if (layout == 2) {
				employeeHeader.width = '49.5%';
				employeeInfo.width = '49.5%';
			};
			if (layout == 3) {
				employeeHeader.width = '32.66%';
				employeeInfo.width = '32.66%';
			};
			barHeader.add(employeeHeader);
			barInfo.add(employeeInfo);
		};
		if (funding && funding != 0) {
			var fundingHeader = Ti.UI.createLabel({
				backgroundColor : '#a5d9d5',
				text : 'AMOUNT',
				color : '#4b595b',
				width : 0,
				height : Alloy.Globals.barHeadingHeight,
				font : {
					fontFamily : 'Surf-Regular',
					fontSize : '12dp'
				},
				textAlign : 'center'
			});
			var fundingInfo = Ti.UI.createLabel({
				backgroundColor : '#57c4c3',
				text : int2roundKMG(funding),
				color : '#4b595b',
				width : 0,
				height : Alloy.Globals.barHeadingHeight,
				font : {
					fontFamily : 'SurfLight',
					fontSize : '20dp'
				},
				textAlign : 'center'
			});
			if (layout == 1) {
				fundingHeader.width = '100%';
				fundingInfo.width = '100%';
			};
			if (layout == 2) {
				fundingHeader.width = '49.5%';
				fundingInfo.width = '49.5%';
				fundingInfo.left = '1%';
				fundingHeader.left = '1%';
			};
			if (layout == 3) {
				fundingHeader.width = '32.66%';
				fundingInfo.width = '32.66%';
				fundingInfo.left = '1%';
				fundingHeader.left = '1%';
			};
			barHeader.add(fundingHeader);
			barInfo.add(fundingInfo);
		};
		if (source) {
			var sourceHeader = Ti.UI.createLabel({
				backgroundColor : '#a5d9d5',
				text : 'FUNDING',
				color : '#4b595b',
				width : 0,
				height : Alloy.Globals.barHeadingHeight,
				font : {
					fontFamily : 'Surf-Regular',
					fontSize : '12dp'
				},
				textAlign : 'center'
			});
			var sourceInfo = Ti.UI.createLabel({
				backgroundColor : '#57c4c3',
				text : source,
				color : '#4b595b',
				width : 0,
				height : Alloy.Globals.barHeadingHeight,
				font : {
					fontFamily : 'SurfLight',
					fontSize : '20dp'
				},
				textAlign : 'center'
			});
			if (layout == 1) {
				sourceHeader.width = '100%';
				sourceInfo.width = '100%';
			};
			if (layout == 2) {
				sourceHeader.width = '49.5%';
				sourceInfo.width = '49.5%';
				sourceInfo.left = '1%';
				sourceHeader.left = '1%';
			};
			if (layout == 3) {
				sourceHeader.width = '32.66%';
				sourceInfo.width = '32.66%';
				sourceInfo.left = '1%';
				sourceHeader.left = '1%';
			};
			barHeader.add(sourceHeader);
			barInfo.add(sourceInfo);
		};

		$.desc.add(barHeader);
		$.mapArea.add(barInfo);
		$.desc.height = Alloy.Globals.detailHeadingHeight;
	} else {
		$.desc.height = Alloy.Globals.detailHeadingHeight - Alloy.Globals.barHeadingHeight - 12;
	};

	var pinLabel = Ti.UI.createLabel({
		text : ' ',
		color : '#303e41',
		font : {
			fontSize : '14dp',
			fontFamily : "TitilliumText25L-800wt"
		},
		height : Alloy.Globals.pinDetailHeight,
		width : Alloy.Globals.pinDetailWidth,
		backgroundImage : "/images/detail_map_icon.png"
	});

	var pinView = Ti.UI.createImageView({
		image : pinLabel.toImage(), //setting label as a blob
		width : 'auto',
		height : 'auto'
	});

	var detailMap = Ti.UI.createView({
		width : Alloy.Globals.detailMapWidth,
		height : Alloy.Globals.detailMapHeight,
		top : 2
	});

	var detailMapObeject = Ti.Map.createView({
		width : Alloy.Globals.detailMapWidth,
		height : Alloy.Globals.detailMapHeight,
		top : 0,
		mapType : Titanium.Map.STANDARD_TYPE,
		region : {
			latitude : latitude,
			longitude : longitude,
			latitudeDelta : 0.0009,
			longitudeDelta : 0.0009
		},
		animate : true,
		regionFit : true,
		userLocation : false,
		touchEnabled : false
	});

	var plotPoints = Titanium.Map.createAnnotation({
		latitude : latitude,
		longitude : longitude,
		title : '',
		image : pinView.toBlob()
	});

	$.scrollView.add(detailMap);

	Ti.App.addEventListener('showDetailMap', function() {
		detailMapObeject.addAnnotation(plotPoints);
		detailMap.add(detailMapObeject);
	});

	var address = Ti.UI.createLabel({
		height : Alloy.Globals.detailAddressHeight,
		width : Alloy.Globals.detailMapWidth,
		top : 0,
		font : {
			fontFamily : 'TitilliumText25L-250wt',
			fontSize : '12dp'
		},
		color : '#676f71',
		backgroundColor : '#fff',
		textAlign : 'center',
		text : street.trim() + ', ' + city.trim() + ', ' + state.trim() + ' ' + zip.trim()
	});

	$.scrollView.add(address);

	var directionsButton = Ti.UI.createView({
		top : '1%',
		width : Alloy.Globals.detailMapWidth,
		backgroundColor : '#303e41',
		height : Alloy.Globals.detailBtHeight
	});
	var directionsLabel = Ti.UI.createLabel({
		color : '#dbd1c7',
		font : {
			fontFamily : 'SurfLight',
			fontSize : '16dp'
		},
		height : Alloy.Globals.detailBtHeight,
		text : 'directions'
	});
	if (Ti.Platform.osname != 'android') {
		var dirSize = directionsLabel.toImage();
		var directionsImage = Ti.UI.createImageView({
			image : '/images/detail_directions_icon.png',
			width : Alloy.Globals.dirImageWidth,
			height : Alloy.Globals.detailImageWidth,
			left : (directionsButton.width / 2) - dirSize.width
		});

		directionsButton.add(directionsImage);
	};
	directionsButton.add(directionsLabel);
	$.scrollView.add(directionsButton);

	directionsButton.addEventListener('click', function() {

		Ti.Platform.openURL('http://maps.google.com/maps?saddr=' + Alloy.Globals.userLatitude + ',' + Alloy.Globals.userLongitude + '&daddr=' + latitude + ',' + longitude);

	});

	if (phone && phone != 0 && phone != 'noe') {
		var phoneButton = Ti.UI.createView({
			top : '.5%',
			width : Alloy.Globals.detailMapWidth,
			backgroundColor : '#303e41',
			height : Alloy.Globals.detailBtHeight
		});
		var phoneOriginal = phone;
		phone = phone.replace(/\D/g, "");
		phone = phone.value = "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6);
		var phoneLabel = Ti.UI.createLabel({
			color : '#dbd1c7',
			font : {
				fontFamily : 'SurfLight',
				fontSize : '16dp'
			},
			height : Alloy.Globals.detailBtHeight,
			text : phone
		});
		if (Ti.Platform.osname != 'android') {
			var labelSize = phoneLabel.toImage();
			var phoneImage = Ti.UI.createImageView({
				image : '/images/detail_phone_icon.png',
				width : Alloy.Globals.detailImageWidth,
				height : Alloy.Globals.detailImageWidth,
				left : (phoneButton.width / 2) - labelSize.width + 3 + Alloy.Globals.detailImageWidth
			});

			phoneButton.add(phoneImage);
		};
		phoneButton.add(phoneLabel);
		$.scrollView.add(phoneButton);

		var phoneCleaned = phone.replace(/"/g, "").replace(/'/g, "").replace(/\(|\)/g, "").replace('-', "").replace(' ', '');
		phoneButton.addEventListener('click', function() {
			//alert('phone clicked: '+phoneCleaned);
			Ti.Platform.openURL('tel:' + phoneCleaned);
		});

	};

	if (url || crunchbase) {
		var layout = 0;
		if (url) {
			layout++;
		};
		if (crunchbase) {
			layout++;
		};
		var buttonArea = Ti.UI.createView({
			top : '.5%',
			width : Alloy.Globals.detailMapWidth,
			height : Alloy.Globals.detailBtHeight,
			layout : 'horizontal'
		});
		if (url) {
			var webButton = Ti.UI.createView({
				backgroundColor : '#303e41',
				height : Alloy.Globals.detailBtHeight,
				width : Alloy.Globals.detailMapWidth
			});
			url = url.replace('http://', "");
			if (layout == 1) {
				webButton.width = Alloy.Globals.detailMapWidth;
			};
			if (layout == 2) {
				webButton.width = (Alloy.Globals.detailMapWidth / 2) - .5;
			};
			var webLabel = Ti.UI.createLabel({
				color : '#dbd1c7',
				font : {
					fontFamily : 'SurfLight',
					fontSize : '16dp'
				},
				height : Alloy.Globals.detailBtHeight,
				text : 'website'
			});
			if (Ti.Platform.osname != 'android') {
				var webSize = webLabel.toImage();
				var webImage = Ti.UI.createImageView({
					image : '/images/detail_web_icon.png',
					width : Alloy.Globals.detailImageWidth,
					height : Alloy.Globals.detailImageWidth,
					left : (webButton.width / 2) - webSize.width
				});

				webButton.add(webImage);
			};
			webButton.add(webLabel);
			buttonArea.add(webButton);

			webButton.addEventListener('click', function() {
				Ti.Platform.openURL('http://' + url);
			});

		};
		if (crunchbase) {
			var crunchButton = Ti.UI.createView({
				backgroundColor : '#303e41',
				height : Alloy.Globals.detailBtHeight,
				width : Alloy.Globals.detailMapWidth
			});
			var crunchLabel = Ti.UI.createLabel({
				color : '#dbd1c7',
				font : {
					fontFamily : 'SurfLight',
					fontSize : '16dp'
				},
				height : Alloy.Globals.detailBtHeight,
				text : 'crunchbase'
			});
			if (layout == 1) {
				crunchButton.width = Alloy.Globals.detailMapWidth;
			};
			if (layout == 2) {
				crunchButton.width = (Alloy.Globals.detailMapWidth / 2) - .5;
				crunchButton.left = '1';
			};
			if (Ti.Platform.osname != 'android') {
				var crunchSize = crunchLabel.toImage();
				var crunchImage = Ti.UI.createImageView({
					image : '/images/detail_crunch_icon.png',
					width : Alloy.Globals.detailImageWidth,
					height : Alloy.Globals.detailImageWidth,
					left : (crunchButton.width / 2) - crunchSize.width + Alloy.Globals.detailImageWidth
				});

				crunchButton.add(crunchImage);
			};
			crunchButton.add(crunchLabel);
			buttonArea.add(crunchButton);

			crunchButton.addEventListener('click', function() {
				Ti.Platform.openURL(crunchbase);
			});
		};
		$.scrollView.add(buttonArea);
	};

	var scrollPadding = Ti.UI.createView({
		width : Alloy.Globals.detailMapWidth,
		height : Alloy.Globals.detailBtHeight
	});

	if (twitter || facebook || linkedin) {
		var layout = 0;
		if (linkedin) {
			layout++;
		};
		if (facebook) {
			layout++;
		};
		if (twitter) {
			layout++;
		};

		var socialArea = Ti.UI.createView({
			top : '.5%',
			layout : 'horizontal',
			left : Alloy.Globals.detailHeadingPadding,
			right : Alloy.Globals.detailHeadingPadding,
			height : Alloy.Globals.detailBtHeight
		});

		if (linkedin) {
			var linkedInButton = Ti.UI.createView({
				backgroundColor : '#303e41',
				height : Alloy.Globals.detailBtHeight,
				width : '100%'
			});
			var linkedInImage = Ti.UI.createImageView({
				height : Alloy.Globals.socialHeight,
				width : Alloy.Globals.socialWidth,
				image : '/images/detail_linkedin_icon.png'
			});
			linkedInButton.add(linkedInImage);
			if (layout == 1) {
				linkedInButton.width = '100%';
			};
			if (layout == 2) {
				linkedInButton.width = '49.5%';
			};
			if (layout == 3) {
				linkedInButton.width = '32.66%';
			};
			socialArea.add(linkedInButton);
			linkedInButton.addEventListener('click', function() {
				Ti.Platform.openURL(linkedin);
			});
		};
		if (facebook) {
			var facebookButton = Ti.UI.createView({
				backgroundColor : '#303e41',
				height : Alloy.Globals.detailBtHeight,
				width : '100%'
			});
			var facebookImage = Ti.UI.createImageView({
				height : Alloy.Globals.socialHeight,
				width : Alloy.Globals.socialWidth,
				image : '/images/detail_facebook_icon.png'
			});
			facebookButton.add(facebookImage);
			if (layout == 1) {
				facebookButton.width = '100%';
			};
			if (layout == 2) {
				facebookButton.width = '49.5%';
				if (linkedin) {
					facebookButton.left = '1%';
				};
			};
			if (layout == 3) {
				facebookButton.width = '32.66%';
				facebookButton.left = '1%';
			};
			facebookButton.addEventListener('click', function() {
				Ti.Platform.openURL(facebook);
			});
			socialArea.add(facebookButton);
		};
		if (twitter) {
			var twitterButton = Ti.UI.createView({
				backgroundColor : '#303e41',
				height : Alloy.Globals.detailBtHeight,
				width : '100%'
			});
			var twitterImage = Ti.UI.createImageView({
				height : Alloy.Globals.socialHeight,
				width : Alloy.Globals.socialWidth,
				image : '/images/detail_twitter_icon.png'
			});
			twitterButton.add(twitterImage);
			if (layout == 1) {
				twitterButton.width = '100%';
			};
			if (layout == 2) {
				twitterButton.width = '49.5%';
				twitterButton.left = '1%';
			};
			if (layout == 3) {
				twitterButton.width = '32.66%';
				twitterButton.left = '1%';
			};
			twitterButton.addEventListener('click', function() {
				Ti.Platform.openURL(twitter);
			});
			socialArea.add(twitterButton);
		};
		$.scrollView.add(socialArea);
	};

	var updateButton = Ti.UI.createView({
		backgroundColor : '#303e41',
		top : '.5%',
		width : Alloy.Globals.detailMapWidth,
		height : Alloy.Globals.detailMapHeight,
		height : Alloy.Globals.detailBtHeight
	});

	var label = Ti.UI.createLabel({
		text : "Updates?",
		color : '#a4a4a0',
		height : Alloy.Globals.pickerHeight,
		font : {
			fontFamily : 'TitilliumText25L-800wt',
			fontSize : '11dp'
		}
	});

	updateButton.addEventListener('click', function() {
		var emailDialog = Ti.UI.createEmailDialog();
		emailDialog.subject = "Message from Startup Explorer";
		emailDialog.toRecipients = ['startupexplorer@buddy.com'];
		
		emailDialog.open();	
	});
	updateButton.add(label);
	
	$.scrollView.add(updateButton);

	$.scrollView.add(scrollPadding);

	$.detailHeader.height = Alloy.Globals.detailHeadingPadding2;

	$.containerLM.animate({
		left : -Alloy.Globals.width,
		duration : 300,
		curve : Titanium.UI.ANIMATION_CURVE_EASE_OUT
	});

	$.btBackDetail.addEventListener('click', function() {
		$.containerLM.animate({
			left : 0,
			duration : 300,
			curve : Titanium.UI.ANIMATION_CURVE_EASE_OUT
		}, function() {
			if (industry) {
				$.desc.remove(industryLabel);
			};
			if (employees || funding || source) {
				$.desc.remove(barHeader);
				$.mapArea.remove(barInfo);
			}

			$.scrollView.remove(address);
			$.scrollView.remove(directionsButton);
			if (phone && phone != 0 && phone != 'noe') {
				$.scrollView.remove(phoneButton);
			};
			if (url || crunchbase) {
				$.scrollView.remove(buttonArea);
			};
			if (twitter || facebook || linkedin) {
				$.scrollView.remove(socialArea);
			};
			$.scrollView.remove(detailMap);
			//$.scrollView.remove(detailMapObject);
			$.scrollView.remove(scrollPadding);
			Ti.App.fireEvent('addMapCluster');
		});
	});

};
