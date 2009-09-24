function TextFieldTest(name) {
    TestCase.call(this, name);
}

function TextFieldTest_testRender() {
    var tag = new TM.TextField("foo", {value: "jim"});
    var html = TM.render(tag);
    this.assertTrue(html.indexOf('type="text"') > -1);
    this.assertTrue(html.indexOf('value="jim"') > -1);
}

TextFieldTest.prototype = new TestCase();
TextFieldTest.prototype.testRender = TextFieldTest_testRender;