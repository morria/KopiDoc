/*global module: true, ok: true, equals: true, S: true, test: true */
module("class_document", {
	setup: function () {
		// open the page
		S.open("//kopidoc/kopidoc.html");

		//make sure there's at least one class_document on the page before running a test
		S('.class_document').exists();
	},
	//a helper function that creates a class_document
	create: function () {
		S("[name=name]").type("Ice");
		S("[name=description]").type("Cold Water");
		S("[type=submit]").click();
		S('.class_document:nth-child(2)').exists();
	}
});

test("class_documents present", function () {
	ok(S('.class_document').size() >= 1, "There is at least one class_document");
});

test("create class_documents", function () {

	this.create();

	S(function () {
		ok(S('.class_document:nth-child(2) td:first').text().match(/Ice/), "Typed Ice");
	});
});

test("edit class_documents", function () {

	this.create();

	S('.class_document:nth-child(2) a.edit').click();
	S(".class_document input[name=name]").type(" Water");
	S(".class_document input[name=description]").type("\b\b\b\b\bTap Water");
	S(".update").click();
	S('.class_document:nth-child(2) .edit').exists(function () {

		ok(S('.class_document:nth-child(2) td:first').text().match(/Ice Water/), "Typed Ice Water");

		ok(S('.class_document:nth-child(2) td:nth-child(2)').text().match(/Cold Tap Water/), "Typed Cold Tap Water");
	});
});

test("destroy", function () {

	this.create();

	S(".class_document:nth-child(2) .destroy").click();

	//makes the next confirmation return true
	S.confirm(true);

	S('.class_document:nth-child(2)').missing(function () {
		ok("destroyed");
	});

});