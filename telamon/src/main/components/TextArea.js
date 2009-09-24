TM.TextArea = function (params) {
    this.params = {
        cols: 10,
        rows: 5
    };
    this.Class = {textarea: 1};
    this.attr(params);
};
TM.TextArea.prototype.renderAttributes = TM._renderAttributes;
TM.TextArea.prototype.attr = TM._attr;
TM.TextArea.prototype.toString = TM.TextArea.prototype.render = function (params) {
    return "<textarea" + this.renderAttributes(params) + ">" + this.params.text + "</textarea>";
};