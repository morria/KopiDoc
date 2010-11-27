/**
 * @tag controllers, home
 * Displays a table of class_documents.	 Lets the user 
 * ["kopidoc.Controllers.ClassDocument.prototype.form submit" create], 
 * ["kopidoc.Controllers.ClassDocument.prototype.&#46;edit click" edit],
 * or ["kopidoc.Controllers.ClassDocument.prototype.&#46;destroy click" destroy] class_documents.
 */
$.Controller.extend('kopidoc.Controllers.ClassDocument',
/* @Static */
{
	onDocument: true
},
/* @Prototype */
{
 /**
 * When the page loads, gets all class_documents to be displayed.
 */
 load: function(){
	   if(!$("#classDocument").length) {
         $('#main').append($('<section />').attr('id','classDocument').attr('class','list document'));
		     kopidoc.Models.ClassDocument.findAll({}, this.callback('show'));
 	   }
 },
 /**
 * Displays a list of class_documents and the submit form.
 * @param {Array} class_documents An array of kopidoc.Models.ClassDocument objects.
 */
show: function( class_document ){
    var doc = $.parseJSON(class_document.data.class);
    console.warn(doc);
	  $('#classDocument').html(this.view('show', {doc: doc} ));
 },

 /**
 *	 Handle's clicking on a class_document's destroy link.
 */
'.destroy click': function( el ){
	if(confirm("Are you sure you want to destroy?")){
		el.closest('.class_document').model().destroy();
	}
 },

 /**
 *	 Listens for class_documents being destroyed and removes them from being displayed.
 */
"class_document.destroyed subscribe": function(called, class_document){
	class_document.elements().remove();	 //removes ALL elements
 }
});