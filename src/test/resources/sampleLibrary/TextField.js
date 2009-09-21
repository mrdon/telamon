function TextField(id, params) {
    this.id = id;
    this.params = (params ? params : {});
    this.render = function(writer, attrs) {
        var xml = '<div> \n' +
                  '  <label for="' + this.id + '"/> \n' +
                  '  <input type="text" id="' + this.id + '" name="' + this.id + '" title="' + attrs.label + '" \n' +
                  '         value="' + this.params.value + '"/> \n' +
                  '</div>';
        writer.write(xml);
    };
}