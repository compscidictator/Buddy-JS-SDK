exports.baseController = "base";

$.onLoad = function() {
	Ti.App.fireEvent('hideFooter');
};

$.btBack.addEventListener('click', function() {
	Ti.App.fireEvent('back');
});

$.name.addEventListener('return', function() {
	$.category.focus();
});

$.category.addEventListener('return', function() {
	$.employees.focus();
});

$.employees.addEventListener('return', function() {
	$.address.focus();
});

$.address.addEventListener('return', function() {
	$.address2.focus();
});

$.address2.addEventListener('return', function() {
	$.contactEmail.focus();
});

$.contactEmail.addEventListener('return', function() {
	sendForm();
});

$.btSubmit.addEventListener('click', function() {
	sendForm();
});

var apiUser = 'startupexplorer';
var apiPass = 'external5312Cartilage';

function sendForm() {
	if ($.name.value && $.category.value && $.employees.value && $.address.value && $.address2.value && $.contactEmail.value) {
		var x = $.contactEmail.value;
		var atpos = x.indexOf("@");
		var dotpos = x.lastIndexOf(".");
		if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= x.length) {
			alert("Not a valid e-mail address, please correct.");
			return false;
		} else {
			Alloy.Globals.CB.Util.startLoading();
			var toName = 'Buddy.com';
			var toEmail = 'support@buddy.com';
			var message = '<p><strong>Company Name: </strong>' + $.name.value + '<br /><strong>Category: </strong>' + $.category.value + '<br /><strong>Employees: </strong>' + $.employees.value + '<br /><strong>Address: </strong>' + $.address.value + '<br /><strong>City, State, Zip: </strong>' + $.address2.value + '<br /><strong>Contact Email: </strong>' + $.contactEmail.value + '</p>';

			var xhrSend = Ti.Network.createHTTPClient();
			xhrSend.onload = function() {
				sendUser();
			};

			xhrSend.onerror = function() {
				Titanium.API.info('Error sending email');
			};

			xhrSend.open("GET", "https://sendgrid.com/api/mail.send.json?api_user=" + apiUser + "&api_key=" + apiPass + "&to=" + toEmail + "&toname=" + toName + "&subject=New Startup Added&html=" + message + "&from=no-reply@buddy.com");
			console.log("https://sendgrid.com/api/mail.send.json?api_user=" + apiUser + "&api_key=" + apiPass + "&to=" + toEmail + "&toname=" + toName + "&subject=New Startup Added&html=" + message + "&from=no-reply@buddy.com");
			xhrSend.send();
		}
	} else {
		alert('All fields are required, please double check and try again.');
	};
};

function sendUser() {

	var toEmail = $.contactEmail.value;
	var message = '<p>We have received your application. We will contact you once you are confirmed.</p><p>Cheers,<br />The Buddy Team</p>';

	var xhrSend = Ti.Network.createHTTPClient();
	xhrSend.onload = function() {
		var dialog = Ti.UI.createAlertDialog({
			message : 'We have received your application. We will contact you once you are confirmed.',
			ok : 'OK',
			title : 'Thank You!'
		}).show();
		Alloy.Globals.CB.pushController({
			controller : 'home',
			animation : Alloy.Globals.CB.UI.AnimationStyle.NavRight,
			data : []
		});
		Ti.App.fireEvent('showFooter');
		Ti.App.fireEvent('menuOff');
		Ti.App.fireEvent('homeOn');
		Alloy.Globals.CB.Util.stopLoading();
	};

	xhrSend.onerror = function() {
		Titanium.API.info('Error sending email');
		var dialog = Ti.UI.createAlertDialog({
			message : 'There was an error sending your message. Please try again.',
			ok : 'OK',
			title : 'Message Not Sent'
		}).show();
		Alloy.Globals.CB.Util.stopLoading();
	};

	xhrSend.open("GET", "https://sendgrid.com/api/mail.send.json?api_user=" + apiUser + "&api_key=" + apiPass + "&to=" + toEmail + "&subject=Thank You!&html=" + message + "&from=no-reply@buddy.com");
	xhrSend.send();
};