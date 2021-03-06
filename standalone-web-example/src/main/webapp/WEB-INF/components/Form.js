function Form(id, params) {
    this.id = id;
    this.params = (params ? params : {});
    this.children = {
        kid : new TextField("kid", {value : "child"})
    };
    this.render = function(writer, attrs) {
        var xml = '<form name="' + this.id + '" action="' + this.params.action + '" method="' + attrs.method + '"> \n';
        writer.write(xml);
    };
    this.renderEnd = function(writer, body) {
        var xml = '</form>';
        writer.write(body);
        writer.write(xml);
    };
    this.childrenNames = ["kid"];
    this.get = function(id) {
        return this.children[id]; 
    };
}