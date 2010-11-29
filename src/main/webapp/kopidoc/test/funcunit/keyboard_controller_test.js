/*global module: true, ok: true, equals: true, S: true, test: true */
module("keyboard", {
	setup: function () {
		// open the page
		S.open("//kopidoc/kopidoc.html");

		//make sure there's at least one keyboard on the page before running a test
		S('.keyboard').exists();
	},
	//a helper function that creates a keyboard
	create: function () {
		S("[name=name]").type("Ice");
		S("[name=description]").type("Cold Water");
		S("[type=submit]").click();
		S('.keyboard:nth-child(2)').exists();
	}
});

test("keyboards present", function () {
	ok(S('.keyboard').size() >= 1, "There is at least one keyboard");
});

test("create keyboards", function () {

	this.create();

	S(function () {
		ok(S('.keyboard:nth-child(2) td:first').text().match(/Ice/), "Typed Ice");
	});
});

test("edit keyboards", function () {

	this.create();

	S('.keyboard:nth-child(2) a.edit').click();
	S(".keyboard input[name=name]").type(" Water");
	S(".keyboard input[name=description]").type("\b\b\b\b\bTap Water");
	S(".update").click();
	S('.keyboard:nth-child(2) .edit').exists(function () {

		ok(S('.keyboard:nth-child(2) td:first').text().match(/Ice Water/), "Typed Ice Water");

		ok(S('.keyboard:nth-child(2) td:nth-child(2)').text().match(/Cold Tap Water/), "Typed Cold Tap Water");
	});
});

test("destroy", function () {

	this.create();

	S(".keyboard:nth-child(2) .destroy").click();

	//makes the next confirmation return true
	S.confirm(true);

	S('.keyboard:nth-child(2)').missing(function () {
		ok("destroyed");
	});

});