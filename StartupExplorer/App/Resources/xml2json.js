xml2json = {
    parser: function(xmlcode, startLevel, debug) {
        xmlcode = xmlcode.replace(/\s*\/>/g, "/>");
        xmlcode = xmlcode.replace(/dateTime\.iso8601/g, "datetime");
        xmlcode = xmlcode.replace(/<\?[^>]*>/g, "").replace(/<\![^>]*>/g, "");
        var x = this.no_fast_endings(xmlcode);
        x = this.attris_to_tags(x);
        x = escape(x);
        x = x.split("%3C").join("<").split("%3E").join(">").split("%3D").join("=").split("%22").join('"');
        x = "<JSONTAGWRAPPER>" + x + "</JSONTAGWRAPPER>";
        this.xmlobject = {};
        var y = this.xml_to_object(x, startLevel).jsontagwrapper;
        debug && (y = this.show_json_structure(y, debug));
        return y;
    },
    xml_to_object: function(xmlcode, startLevel) {
        var x = xmlcode.replace(/<\//g, "@");
        x = x.split("<");
        var y = [];
        var level = 0;
        var opentags = [];
        for (var i = 1; x.length > i; i++) {
            level++;
            if (1 == level || level > startLevel) {
                var tagname = x[i].split(">")[0];
                opentags.push(tagname);
                y.push(level + "<" + x[i].split("@")[0]);
                while (x[i].indexOf("@" + opentags[opentags.length - 1] + ">") >= 0) {
                    level--;
                    opentags.pop();
                }
            }
        }
        var oldniva = -1;
        var objname = "this.xmlobject";
        for (var i = 0; y.length > i; i++) {
            var preeval = "";
            var niva = y[i].split("<")[0];
            var tagnamn = y[i].split("<")[1].split(">")[0];
            tagnamn = tagnamn.toLowerCase();
            var rest = y[i].split(">")[1];
            if (oldniva >= niva) {
                var tabort = oldniva - niva + 1;
                for (var j = 0; tabort > j; j++) objname = objname.substring(0, objname.lastIndexOf("."));
            }
            objname += "." + tagnamn;
            var pobject = objname.substring(0, objname.lastIndexOf("."));
            "object" != eval("typeof " + pobject) && (preeval += pobject + "={value:" + pobject + "};\n");
            var objlast = objname.substring(objname.lastIndexOf(".") + 1);
            var already = false;
            for (k in eval(pobject)) k == objlast && (already = true);
            var onlywhites = true;
            for (var s = 0; rest.length > s; s += 3) "%" != rest.charAt(s) && (onlywhites = false);
            if ("" == rest || onlywhites) rest = "{}"; else if (rest / 1 != rest) {
                rest = "'" + rest.replace(/\'/g, "\\'") + "'";
                rest = rest.replace(/\*\$\*\*\*/g, "</");
                rest = rest.replace(/\*\$\*\*/g, "<");
                rest = rest.replace(/\*\*\$\*/g, ">");
            }
            "'" == rest.charAt(0) && (rest = "unescape(" + rest + ")");
            already && !eval(objname + ".sort") && (preeval += objname + "=[" + objname + "];\n");
            var before = "=";
            after = "";
            if (already) {
                before = ".push(";
                after = ")";
            }
            var toeval = preeval + objname + before + rest + after;
            eval(toeval);
            eval(objname + ".sort") && (objname += "[" + eval(objname + ".length-1") + "]");
            oldniva = niva;
        }
        return this.xmlobject;
    },
    show_json_structure: function(obj, debug, l) {
        var x = "";
        x += obj.sort ? "[\n" : "{\n";
        for (var i in obj) {
            obj.sort || (x += i + ":");
            if ("object" == typeof obj[i]) x += this.show_json_structure(obj[i], false, 1); else if ("function" == typeof obj[i]) {
                var v = obj[i] + "";
                x += v;
            } else x += "string" != typeof obj[i] ? obj[i] + ",\n" : "'" + obj[i].replace(/\'/g, "\\'").replace(/\n/g, "\\n").replace(/\t/g, "\\t").replace(/\r/g, "\\r") + "',\n";
        }
        x += obj.sort ? "],\n" : "},\n";
        if (!l) {
            x = x.substring(0, x.lastIndexOf(","));
            x = x.replace(new RegExp(",\n}", "g"), "\n}");
            x = x.replace(new RegExp(",\n]", "g"), "\n]");
            var y = x.split("\n");
            x = "";
            var lvl = 0;
            for (var i = 0; y.length > i; i++) {
                (y[i].indexOf("}") >= 0 || y[i].indexOf("]") >= 0) && lvl--;
                tabs = "";
                for (var j = 0; lvl > j; j++) tabs += "	";
                x += tabs + y[i] + "\n";
                (y[i].indexOf("{") >= 0 || y[i].indexOf("[") >= 0) && lvl++;
            }
            if ("html" == debug) {
                x = x.replace(/</g, "&lt;").replace(/>/g, "&gt;");
                x = x.replace(/\n/g, "<BR>").replace(/\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;");
            }
            "compact" == debug && (x = x.replace(/\n/g, "").replace(/\t/g, ""));
        }
        return x;
    },
    no_fast_endings: function(x) {
        x = x.split("/>");
        for (var i = 1; x.length > i; i++) {
            var t = x[i - 1].substring(x[i - 1].lastIndexOf("<") + 1).split(" ")[0];
            x[i] = "></" + t + ">" + x[i];
        }
        x = x.join("");
        return x;
    },
    attris_to_tags: function(x) {
        var d = " =\"'".split("");
        x = x.split(">");
        for (var i = 0; x.length > i; i++) {
            var temp = x[i].split("<");
            for (var r = 0; 4 > r; r++) temp[0] = temp[0].replace(new RegExp(d[r], "g"), "_jsonconvtemp" + r + "_");
            if (temp[1]) {
                temp[1] = temp[1].replace(/'/g, '"');
                temp[1] = temp[1].split('"');
                for (var j = 1; temp[1].length > j; j += 2) for (var r = 0; 4 > r; r++) temp[1][j] = temp[1][j].replace(new RegExp(d[r], "g"), "_jsonconvtemp" + r + "_");
                temp[1] = temp[1].join('"');
            }
            x[i] = temp.join("<");
        }
        x = x.join(">");
        x = x.replace(/ ([^=]*)=([^ |>]*)/g, "><$1>$2</$1");
        x = x.replace(/>"/g, ">").replace(/"</g, "<");
        for (var r = 0; 4 > r; r++) x = x.replace(new RegExp("_jsonconvtemp" + r + "_", "g"), d[r]);
        return x;
    }
};

Array.prototype.push || (Array.prototype.push = function(x) {
    this[this.length] = x;
    return true;
});

Array.prototype.pop || (Array.prototype.pop = function() {
    var response = this[this.length - 1];
    this.length--;
    return response;
});

module.exports = xml2json;