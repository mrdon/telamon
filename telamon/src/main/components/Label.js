TM.label = function (id, params) {
    this.id = id;
    this.params = {};
    if (typeof params == "string") {
        params = {text: params};
    }
    this.Class = {label: 1};
    this.attr(params);
    this.children = {};
    this.childrenNames = [];
};
TM.label.prototype.renderAttributes = TM._renderAttributes;
TM.label.prototype.attr = TM._attr;
TM.label.prototype.render = function (writer, params) {
     writer.write("<label" + this.renderAttributes(params) + ">" + (this.params.text ? this.params.text : ""));
};
TM.label.prototype.renderEnd = function (writer, body) {
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