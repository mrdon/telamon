TM.TextField = function (params) {
    this.params = {
        type: "text"
    };
    this.Class = {text: 1};
    this.attr(params);
};
TM.TextField.prototype.attr = TM._attr;
TM.TextField.prototype.renderAttributes = TM._renderAttributes;
TM.TextField.prototype.toString = TM.TextField.prototype.render = function (params) {
    return "<input" + this.renderAttributes(params) + ">";
};
