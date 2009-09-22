var TM = {};

var writer = function () {
    this.buffer = "";
}
writer.prototype.write = function(text) {
    this.buffer += text;
};
writer.prototype.toString = function() {
    return this.buffer;
};

function _render(component, output) {
    component.render(output, {});
    if (component.childNames) {
        var subWriter = new writer();
        for (var x in component.childNames()) {
            var id = component.childNames()[x];
            _render(component.get(id), subWriter);
        }
        component.renderEnd(output, subWriter.toString());
    }
};

function render(component) {
    var thisWriter = new writer();
    _render(component, thisWriter);
    return thisWriter.toString();
};

(function () {
    var attr = function (params) {
        for (var property in params) if (params.hasOwnProperty(property) && this.paramsWhiteList.hasOwnProperty(property)) {
            switch (property) {
                case "class":
                    this.Class[params["class"]] = 1;
                break;
                default:
                    this.params[property] = params[property];
            }
        }
        var res = [];
        for (property in this.Class) if (this.Class.hasOwnProperty(property)) {
            res.push(property);
        }
        this.params["class"] = res.join(" ");
        return this;
    },
    
    renderAttributes = function (params) {
        var res = [];
        this.attr(params);
        for (var property in this.params) if (this.params.hasOwnProperty(property) && this.paramsWhiteList.hasOwnProperty(property)) {
            res.push(property + '="' + this.params[property].replace(/"/g, "&quot;") + '"');
        };
        return (res.length ? " " + res.join(" ") : "");
    };
    
    // TextField
    TM.textField = function (params) {
        this.params = {
            type: "text"
        };
        this.Class = {text: 1};
        this.paramsWhiteList = {
            "class": 1,
            id: 1,
            name: 1,
            title: 1,
            value: 1
        };
        this.attr(params);
    };
    TM.textField.prototype.attr = attr;
    TM.textField.prototype.renderAttributes = renderAttributes;
    TM.textField.prototype.toString = TM.textField.prototype.render = function (params) {
        return "<input" + this.renderAttributes(params) + ">";
    };
    TM.TextField = function (params) {
        return new TM.textField(params);
    };

    // TextArea
    TM.textArea = function (params) {
        this.params = {
            cols: 10,
            rows: 5
        };
        this.Class = {textarea: 1};
        this.attr(params);
    };
    TM.textArea.prototype.renderAttributes = renderAttributes;
    TM.textArea.prototype.attr = attr;
    TM.textArea.prototype.toString = TM.textArea.prototype.render = function (params) {
        return "<textarea" + this.renderAttributes(params) + ">" + this.params.text + "</textarea>";
    };


    // Label
    TM.label = function (id, params) {
        this.id = id;
        this.params = {};
        if (typeof params == "string") {
            params = {text: params};
        }
        this.Class = {label: 1};
        this.paramsWhiteList = {
            "class": 1,
            id: 1,
            name: 1,
            title: 1,
            "for": 1,
            text: 1
        };
        this.attr(params);
        this.children = {};
        this.childrenNames = [];
    };
    TM.label.prototype.renderAttributes = renderAttributes;
    TM.label.prototype.attr = attr;
    TM.label.prototype.render = function (writer, params) {
         writer.write("<label" + this.renderAttributes(params) + ">" + (this.params.text ? this.params.text : ""));
    };
    TM.label.prototype.renderEnd = function (writer, body) {
        console.log("body: " + body);
        writer.write(body);
        writer.write("</label>");
    };
    TM.label.prototype.add = function (component) {
        var id = component.id;
        this.childrenNames.push(id);
        this.children[id] = component;
    };
    TM.label.prototype.childNames = function () {
      return this.childrenNames;
    };
    TM.label.prototype.get = function (id) {
        return this.children[id];
    };
    TM.Label = function (id, params) {
        return new TM.label(id, params);
    };

})();
