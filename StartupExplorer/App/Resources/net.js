var Alloy = require("alloy"), util = require("util"), debug = require("debug"), webService = Alloy.CFG.webService;

var Net = function() {};

Net.setWebService = function(ws) {
    webService = ws;
};

Net.request = function(opts) {
    var xhr = Ti.Network.createHTTPClient(), url = webService;
    if (!Ti.Network.online) {
        util.alert(util.L("networkError"), util.L("error"));
        return;
    }
    opts.url && (url = opts.url);
    if (!url) {
        util.alert(util.L("noWebService"), util.L("error"));
        return;
    }
    xhr.onerror = function(e) {
        Alloy.Globals.CB.Util.actInd.actIndWin.actInd.isHide && Alloy.Globals.CB.Util.actInd.hide();
        opts.onerror ? opts.onerror(e) : Ti.API.error(e);
        xhr = null;
    };
    xhr.onload = function() {
        try {
            if (opts.isDownload) opts.onload && opts.onload(xhr.status, this.responseData); else if (opts.returnXML) opts.onload(this.responseXML); else {
                var data = this.responseText;
                if (data) {
                    opts.notToJSON || (data = JSON.parse(data));
                    if (!opts.onload) return data;
                    opts.onload(data);
                }
            }
        } catch (e) {
            xhr.onerror(e);
        }
    };
    xhr.ondatastream = function(data) {
        try {
            opts.ondatastream && opts.ondatastream(data);
        } catch (e) {
            xhr.onerror(e);
        }
    };
    opts.method && (url += webService + opts.method);
    debug.echo(url, 121, "net");
    opts.type ? xhr.open(opts.type, url) : xhr.open("GET", url);
    xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
    opts.data ? xhr.send(opts.data) : xhr.send(null);
};

Net.downloadRemoteFile = function(fileInfo) {
    var requestObj = {
        url: fileInfo.url,
        isDownload: true,
        onerror: function(d) {
            debug.dump(d, 120, "net.js error");
        },
        ondatastream: function(data) {
            if (data && fileInfo.updateProgress) {
                var value = parseInt(100 * data.progress, 10);
                fileInfo.updateProgress(value);
            }
        },
        onload: function(status, data) {
            debug.echo("status: " + status, 131, "net.js");
            var directory = Ti.Filesystem.getFile(util.getAppDataDirectory() + Alloy.CFG.downloadFolder);
            directory.exists() || directory.createDirectory();
            var filename = directory.resolve() + Ti.Filesystem.separator + fileInfo.filename;
            if (200 == status) {
                if (data) {
                    var file = Ti.Filesystem.getFile(filename);
                    file.write(data);
                    fileInfo.complete(filename);
                    file = null;
                }
            } else debug.echo("File not found!  : " + filename, 150, "net.js");
            directory = null;
        }
    };
    Net.request(requestObj);
};

Net.downloadBatchFiles = function(files, callback, downloadedFiles) {
    var total = files.length;
    void 0 === downloadedFiles && (downloadedFiles = []);
    if (total > 0) {
        util.progressBar.setCounter(1);
        Net.downloadRemoteFile({
            url: files[total - 1].url,
            filename: files[total - 1].name,
            updateProgress: function(progressValue) {
                util.progressBar.setValue(progressValue);
            },
            complete: function(filename) {
                downloadedFiles.push(filename);
                files.pop();
                Net.downloadBatchFiles(files, callback, downloadedFiles);
            }
        });
    } else callback && callback(downloadedFiles);
};

module.exports = Net;