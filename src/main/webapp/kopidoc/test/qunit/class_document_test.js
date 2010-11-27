module("Model: kopidoc.Models.ClassDocument")

test("findAll", function(){
	stop(2000);
	kopidoc.Models.ClassDocument.findAll({}, function(class_documents){
		start()
		ok(class_documents)
        ok(class_documents.length)
        ok(class_documents[0].name)
        ok(class_documents[0].description)
	});
	
})

test("create", function(){
	stop(2000);
	new kopidoc.Models.ClassDocument({name: "dry cleaning", description: "take to street corner"}).save(function(class_document){
		start();
		ok(class_document);
        ok(class_document.id);
        equals(class_document.name,"dry cleaning")
        class_document.destroy()
	})
})
test("update" , function(){
	stop();
	new kopidoc.Models.ClassDocument({name: "cook dinner", description: "chicken"}).
            save(function(class_document){
            	equals(class_document.description,"chicken");
        		class_document.update({description: "steak"},function(class_document){
        			start()
        			equals(class_document.description,"steak");
        			class_document.destroy();
        		})
            })

});
test("destroy", function(){
	stop(2000);
	new kopidoc.Models.ClassDocument({name: "mow grass", description: "use riding mower"}).
            destroy(function(class_document){
            	start();
            	ok( true ,"Destroy called" )
            })
})