TM.Form = function(id, params) {
    this.id = id;
    this.params = (params ? params : {});
    this.children = {
        kid : new TM.TextField("kid", {value : "child"})
    };
    this.render = function(writer, attrs) {
        var xml = '<form name="' + this.id + '" action="' + this.params.action + '" method="' + attrs.method + '"> \n';
        writer.write(xml);
        return true;
    };
    this.renderEnd = function(writer, body) {
        var xml = '</form>';
        writer.write(body);
        writer.write(xml);
    };
    this.childNames = function() { return ["kid"]};
    this.get = function(id) {
        return this.children[id]; 
    };
}