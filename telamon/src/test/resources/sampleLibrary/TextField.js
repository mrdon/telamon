TM.TextField = function(id, params) {
    this.id = id;
    this.params = (params ? params : {});
}
TM.TextField.prototype.render = function(writer, attrs) {
    var xml = '<div> \n' +
              '  <label for="' + this.id + '"/> \n' +
              '  <input type="text" id="' + this.id + '" name="' + this.id + '" title="' + attrs.label + '" \n' +
              '         value="' + this.params.value + '"/> \n' +
              '</div>';
    writer.write(xml);
}