/**
 * @tag controllers, home
 * Displays a table of menus.	 Lets the user 
 * ["kopidoc.Controllers.Menu.prototype.form submit" create], 
 * ["kopidoc.Controllers.Menu.prototype.&#46;edit click" edit],
 * or ["kopidoc.Controllers.Menu.prototype.&#46;destroy click" destroy] menus.
 */
$.Controller.extend('kopidoc.Controllers.Menu',
/* @Static */
{
	onDocument: true
},
/* @Prototype */
{
 /**
 * When the page loads, gets all menus to be displayed.
 */
 load: function(){

     var sourcePath = config.sourcePath;
     var classPath = config.classPath;

	   if(!$("#menu").length) {
	       $('#main').append($('<section/>').attr('id','menu').attr('class','list menu'));
		     kopidoc.Models.Menu.findAll({sourcePath: sourcePath, classPath: classPath}, this.callback('list'));
 	   }
 },

 /**
 * Displays a list of class_documents and the submit form.
 * @param {Array} class_documents An array of kopidoc.Models.ClassDocument objects.
 */
 list: function( message ) {
     var docList = message.data.documentList;
	   $('#menu').html(this.view('init', {documentList: docList, sourcePath: config.sourcePath} ));
 },

'.fileName click': function(el) {

    var cometd = $.cometd;
    var fileName = $.trim(el.attr('rel'));
    cometd.batch(function() {
        cometd.publish('/service/getFile', { absolutePath: fileName });
    });
},


 /**
 * Creates and places the edit interface.
 * @param {jQuery} el The menu's edit link element.
 */
'.edit click': function( el ){
	var menu = el.closest('.menu').model();
	menu.elements().html(this.view('edit', menu));
},
 /**
 * Removes the edit interface.
 * @param {jQuery} el The menu's cancel link element.
 */
'.cancel click': function( el ){
	this.show(el.closest('.menu').model());
},
 /**
 * Updates the menu from the edit values.
 */
'.update click': function( el ){
	var $menu = el.closest('.menu'); 
	$menu.model().update($menu.formParams());
},
 /**
 * Listens for updated menus.	 When a menu is updated, 
 * update's its display.
 */
'menu.updated subscribe': function( called, menu ){
	this.show(menu);
},
 /**
 * Shows a menu's information.
 */
show: function( menu ){
	menu.elements().html(this.view('show',menu));
},
 /**
 *	 Handle's clicking on a menu's destroy link.
 */
'.destroy click': function( el ){
	if(confirm("Are you sure you want to destroy?")){
		el.closest('.menu').model().destroy();
	}
 },
 /**
 *	 Listens for menus being destroyed and removes them from being displayed.
 */
"menu.destroyed subscribe": function(called, menu){
	menu.elements().remove();	 //removes ALL elements
 }
});