function TiMapCluster(_mapOptions, _markers) {
    this.mapview = Titanium.Map.createView(_mapOptions);
    this.markers = _markers || [];
    this.clustered = [];
    this.mapview.TiMapCluster = this;
    this.mapview.addEventListener("regionChanged", this._autoUpdate);
}

TiMapCluster.prototype.setMarkers = function(_markers) {
    this.markers = _markers;
    this.mapview.TiMapCluster = this;
    this._update();
};

TiMapCluster.prototype.addMarker = function(_marker) {
    this.markers.push(_marker);
    this.mapview.TiMapCluster = this;
    this._update();
};

TiMapCluster.prototype.resize = function(_w, _h) {
    this.mapview.width = _w;
    this.mapview.height = _h;
};

TiMapCluster.prototype.setPosition = function(_x, _y) {
    this.mapview.left = _x;
    this.mapview.top = _y;
};

TiMapCluster.prototype._pixelDistance = function(_lat1, _lon1, _lat2, _lon2) {
    var region = this.mapview.actualRegion || this.mapview.region;
    var latPx = this.mapview.size.height * Math.abs(_lat1 - _lat2) / region.latitudeDelta;
    var lonPx = this.mapview.size.width * Math.abs(_lon1 - _lon2) / region.longitudeDelta;
    var distance = Math.sqrt(Math.pow(latPx, 2) + Math.pow(lonPx, 2));
    return distance;
};

TiMapCluster.prototype._cluster = function() {
    var clustered = [];
    var markers = [];
    for (var i in this.markers) markers.push(this.markers[i]);
    while (markers.length > 0) {
        var marker = markers.pop();
        var cluster = [];
        for (var key = markers.length; key > -1; key--) {
            var target = markers[key];
            if ("undefined" != typeof target) {
                var pixels = this._pixelDistance(marker.latitude, marker.longitude, target.latitude, target.longitude);
                if (20 > pixels) {
                    markers.splice(key, 1);
                    cluster.push(target);
                }
            }
        }
        cluster.push(marker);
        var size = 2 > cluster.length ? cluster.length : 2;
        var title = "";
        var subtitle = "";
        var recorded = [];
        if (1 == cluster.length) {
            title = cluster[0].title;
            subtitle = cluster[0].subtitle;
            recorded = cluster[0].myCompany;
        } else title = "";
        var midPoint = this._midPoint(cluster);
        var annotation = {
            latitude: midPoint.latitude,
            longitude: midPoint.longitude,
            title: title,
            animate: false,
            subtitle: subtitle,
            cluster: cluster,
            image: "pins/" + size + ".png",
            myCompany: recorded
        };
        if ("android" == Ti.Platform.osname) annotation.image = "pins/android_" + size + ".png"; else {
            annotation.leftButton = "/images/map_share_icon.png";
            annotation.rightButton = Titanium.UI.iPhone.SystemButton.DISCLOSURE;
        }
        clustered.push(annotation);
    }
    this.clustered = clustered;
    return clustered;
};

TiMapCluster.prototype._update = function() {
    var clustered = this._cluster();
    this.mapview.removeAllAnnotations();
    for (var i = 0; clustered.length > i; i++) this.mapview.addAnnotation(Titanium.Map.createAnnotation(clustered[i]));
    Alloy.Globals.CB.Util.stopLoading();
};

var oldLat = "";

TiMapCluster.prototype._autoUpdate = function(e) {
    Ti.API.info(e.latitudeDelta);
    var currentDelta = Math.floor(e.latitudeDelta);
    .01 > e.latitudeDelta ? currentDelta = e.latitudeDelta.toFixed(3) : .1 > e.latitudeDelta ? currentDelta = e.latitudeDelta.toFixed(2) : 1 > e.latitudeDelta && (currentDelta = e.latitudeDelta.toFixed(1));
    Ti.API.info(currentDelta);
    if (oldLat != currentDelta) {
        Alloy.Globals.CB.Util.startLoading();
        var self = this.TiMapCluster;
        this.actualRegion = {
            latitude: e.latitude,
            longitude: e.longitude,
            latitudeDelta: e.latitudeDelta,
            longitudeDelta: e.longitudeDelta
        };
        self._update();
        Ti.API.info("update ran");
        oldLat = currentDelta;
    }
};

TiMapCluster.prototype._midPoint = function(_points) {
    var sumLat = 0;
    var sumLon = 0;
    for (var i in _points) {
        sumLat += _points[i].latitude;
        sumLon += _points[i].longitude;
    }
    return {
        latitude: sumLat / _points.length,
        longitude: sumLon / _points.length
    };
};

module.exports = TiMapCluster;