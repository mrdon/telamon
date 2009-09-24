var TM = {};

TM._Writer = function () {
    this.buffer = "";
}
TM._Writer.prototype.write = function(text) {
    this.buffer += text;
};
TM._Writer.prototype.toString = function() {
    return this.buffer;
};

TM._render = function(component, output) {
    component.render(output, {});
    if (component.childNames) {
        var subWriter = new TM._Writer();
        for (var x in component.childNames()) {
            var id = component.childNames()[x];
            TM._render(component.get(id), subWriter);
        }
        component.renderEnd(output, subWriter.toString());
    }
};

TM.render = function(component) {
    var thisWriter = new TM._Writer();
    TM._render(component, thisWriter);
    return thisWriter.toString();
};

TM._attr = function (params) {
    for (var property in params) if (params.hasOwnProperty(property)) {
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
};

TM._renderAttributes = function (params) {
    var res = [];
    this.attr(params);
    for (var property in this.params) if (this.params.hasOwnProperty(property)) {
        res.push(property + '="' + this.params[property].replace(/"/g, "&quot;") + '"');
    };
    return (res.length ? " " + res.join(" ") : "");
};