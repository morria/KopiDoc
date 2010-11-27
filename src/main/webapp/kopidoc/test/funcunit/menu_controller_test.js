/*global module: true, ok: true, equals: true, S: true, test: true */
module("menu", {
	setup: function () {
		// open the page
		S.open("//kopidoc/kopidoc.html");

		//make sure there's at least one menu on the page before running a test
		S('.menu').exists();
	},
	//a helper function that creates a menu
	create: function () {
		S("[name=name]").type("Ice");
		S("[name=description]").type("Cold Water");
		S("[type=submit]").click();
		S('.menu:nth-child(2)').exists();
	}
});

test("menus present", function () {
	ok(S('.menu').size() >= 1, "There is at least one menu");
});

test("create menus", function () {

	this.create();

	S(function () {
		ok(S('.menu:nth-child(2) td:first').text().match(/Ice/), "Typed Ice");
	});
});

test("edit menus", function () {

	this.create();

	S('.menu:nth-child(2) a.edit').click();
	S(".menu input[name=name]").type(" Water");
	S(".menu input[name=description]").type("\b\b\b\b\bTap Water");
	S(".update").click();
	S('.menu:nth-child(2) .edit').exists(function () {

		ok(S('.menu:nth-child(2) td:first').text().match(/Ice Water/), "Typed Ice Water");

		ok(S('.menu:nth-child(2) td:nth-child(2)').text().match(/Cold Tap Water/), "Typed Cold Tap Water");
	});
});

test("destroy", function () {

	this.create();

	S(".menu:nth-child(2) .destroy").click();

	//makes the next confirmation return true
	S.confirm(true);

	S('.menu:nth-child(2)').missing(function () {
		ok("destroyed");
	});

});