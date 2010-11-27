module("Model: kopidoc.Models.Menu")

test("findAll", function(){
	stop(2000);
	kopidoc.Models.Menu.findAll({}, function(menus){
		start()
		ok(menus)
        ok(menus.length)
        ok(menus[0].name)
        ok(menus[0].description)
	});
	
})

test("create", function(){
	stop(2000);
	new kopidoc.Models.Menu({name: "dry cleaning", description: "take to street corner"}).save(function(menu){
		start();
		ok(menu);
        ok(menu.id);
        equals(menu.name,"dry cleaning")
        menu.destroy()
	})
})
test("update" , function(){
	stop();
	new kopidoc.Models.Menu({name: "cook dinner", description: "chicken"}).
            save(function(menu){
            	equals(menu.description,"chicken");
        		menu.update({description: "steak"},function(menu){
        			start()
        			equals(menu.description,"steak");
        			menu.destroy();
        		})
            })

});
test("destroy", function(){
	stop(2000);
	new kopidoc.Models.Menu({name: "mow grass", description: "use riding mower"}).
            destroy(function(menu){
            	start();
            	ok( true ,"Destroy called" )
            })
})