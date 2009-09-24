TM.TextField = function (id, params) {
    this.id = id;
    this.params = {
        type: "text"
    };
    this.Class = {text: 1};
    this.attr(params);
};
TM.TextField.prototype.attr = TM._attr;
TM.TextField.prototype.renderAttributes = TM._renderAttributes;
TM.TextField.prototype.render = function (writer, params) {
    writer.write("<input" + this.renderAttributes(params) + ">");
};
