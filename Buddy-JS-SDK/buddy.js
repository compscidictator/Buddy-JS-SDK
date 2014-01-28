window.Buddy = function(root) {

	root = root || "http://api-us.buddyplatform.com"

	var result = {};

	var accessToken = null;


	function supports_html5_storage() {
	  try {
	    return 'localStorage' in window && window['localStorage'] !== null;
	  } catch (e) {
	    return false;
	  }
	}
	function getUniqueId() {

		var id = getValue("buddyExplorerUniqueId");
		if (!id) {
			id = new Date().getTime(); // good enough for this
			setValue("buddyExplorerUniqueId", id);
		}
		return id;
		
		
	}

	function setValue(key, value) {
		if (supports_html5_storage()) {
			if (value) {
				window.localStorage.setItem(key, value);
			}
			else {
				clearValue(key);
			}
		}
	}

	function getValue(key) {
		if (supports_html5_storage()) {
			var val = window.localStorage.getItem(key);
			if (val != "null") {
				return val;
			}
		}
		return null;
	}
	function clearValue(key) {
		if (supports_html5_storage()) {
			 window.localStorage.removeItem(key);
		}

	}
	function setAccessToken(token) {

			if (!_nosave) {
				if (token) {
					setValue("buddyExplorerAccessToken_" + _appId, token);
				}
				else {
					clearValue("buddyExplorerAccessToken_" + _appId);
				}
			}
			accessToken = token;
		
	}

	function getAccessToken() {

		if (accessToken) {
			return accessToken;
		}
		else if (_appId) {
			 accessToken = getValue("buddyExplorerAccessToken_" + _appId);
			 return accessToken;
		}
	}

	var _appId;
	var _appKey;
	var _nosave;

	function loadCreds() {
		_appId = getValue("buddyExplorer_appid");
		_appKey = getValue("buddyExplorer_appkey");
		getAccessToken();
	}

	loadCreds();

	result.init = function(appId, appKey, nosave) {
		_appId = appId;
		_appKey = appKey;
		_nosave = nosave;
		
		result.registerDevice(appId, appKey, nosave);

	}

	result.clear = function() {
		setAccessToken(null);
		_appId = clearValue("buddyExplorer_appid");
		_appKey = clearValue("buddyExplorer_appkey");
	}

	result.registerDevice = function(appId, appKey, nosave, callback) {

		if (getAccessToken()) {
			callback && callback();
		}

		result.makeRequest("POST", "/devices", {
			appID: appId || _appId,
			appKey: appKey || _appKey,
			platform: "Browser",
			model: navigator.userAgent,
			uniqueId: getUniqueId()
		}, function(err, result){
			if (!err && !nosave) {
				setValue("buddyExplorer_appid", appId);
				setValue("buddyExplorer_appkey", appKey);
			}
			callback && callback(null, result);
		}, true)
	}

	result.loginUser = function(username, password, callback) {

		if (!getAccessToken()) {
			throw new Error("Device must be registered first")
		}

		result.makeRequest("POST", "/users/login", {
			username: username,
			password: password
		}, callback);
	}

	result.makeRequest = function(method, url, parameters, callback, noAutoToken) {

		var at = getAccessToken();
		
		if (at && !_appKey) {
			return callback(new Error("Init must be called first."))
		}
		else if (!at && !noAutoToken) {

			result.registerDevice(null, null, false, function(err, r1){
				if (!err) {
					at = getAccessToken();

					if (at) {

						result.makeRequest(method, url, parameters, callback);
						return;
					}

				}

			})
			return;
		}

		if (typeof parameters == 'function') {
			callback = parameters;
			parameters = null;
		}


		var headers = {
				"Accept" : "application/json"
			};

		if (method == "GET" && parameters != null) {
			url += "?"
			for (var k in parameters) {
				var v = parameters[k];
				if (v) {
					url += k + "=" + encodeURIComponent(v.toString()) + "&"
				}
			}
			parameters = null;
		}
		else if (parameters != null) {
			headers["Content-Type"] = "application/json";
		}

		if (at) {
			headers["Authorization"] = "Buddy " + at;
		}

	    $.ajax({
			method: method,
			url: root + url,
			headers: headers,
			processData: false,
			data: parameters ? JSON.stringify(parameters) : null,
			success:function(data) {
				var result = data.result;
				if (result && result.accessToken != null) {
					
					setAccessToken(result.accessToken);
				}
				callback && callback(null, data);
				if (!callback) {
					console.log(data);
				}
			},
			error: function(data, status, response) {
				var err = response;
				if (data.status === 0) {
					err = "NoInternetConnection";
				}
				else {
					data = JSON.parse(data.responseText);
					err = data.error;
				}
				callback && callback(new Error(data.error), data);
				if (!callback) {
					console.warn(data.error);
				}
			}
		});


	}




	return result;
	

}();