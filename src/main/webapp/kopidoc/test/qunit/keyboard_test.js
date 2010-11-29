module("Model: kopidoc.Models.Keyboard")

test("findAll", function(){
	stop(2000);
	kopidoc.Models.Keyboard.findAll({}, function(keyboards){
		start()
		ok(keyboards)
        ok(keyboards.length)
        ok(keyboards[0].name)
        ok(keyboards[0].description)
	});
	
})

test("create", function(){
	stop(2000);
	new kopidoc.Models.Keyboard({name: "dry cleaning", description: "take to street corner"}).save(function(keyboard){
		start();
		ok(keyboard);
        ok(keyboard.id);
        equals(keyboard.name,"dry cleaning")
        keyboard.destroy()
	})
})
test("update" , function(){
	stop();
	new kopidoc.Models.Keyboard({name: "cook dinner", description: "chicken"}).
            save(function(keyboard){
            	equals(keyboard.description,"chicken");
        		keyboard.update({description: "steak"},function(keyboard){
        			start()
        			equals(keyboard.description,"steak");
        			keyboard.destroy();
        		})
            })

});
test("destroy", function(){
	stop(2000);
	new kopidoc.Models.Keyboard({name: "mow grass", description: "use riding mower"}).
            destroy(function(keyboard){
            	start();
            	ok( true ,"Destroy called" )
            })
})