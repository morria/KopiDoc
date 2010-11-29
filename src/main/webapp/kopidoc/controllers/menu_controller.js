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

     /*
     var sourcePath = localStorage.getItem('sourcePath');
     var classPath = localStorage.getItem('classPath');
     */

	   if(!$("#menu").length) {
	       $('#main').append($('<section/>').attr('id','menu').attr('class','list menu'));

         var fSuccess = this.callback('list');
         $.cometd.addListener('/getClassList', function(classList) { fSuccess(classList); });
         $.cometd.publish('/service/getClassList', {} );
         $.cometd.addListener('/addSources',  function(message) { 
             $.cometd.publish('/service/getClassList', {} );
         } );

         /*
         $.cometd.publish('/service/addSources', { sourcePath:  sourcePath,
                                                   classPath: classPath});
         */
 	   }
 },

 /**
 * Displays a list of class_documents and the submit form.
 * @param {Array} class_documents An array of kopidoc.Models.ClassDocument objects.
 */
 list: function( message ) {
     var classList = message.data.classList;
     var tree = Object();
     for(var i in classList) {
         var elements = classList[i].split(".");
         var packageName = elements.slice(0,elements.length-1).join(".");
         var className = elements[elements.length-1];
         if(!tree[packageName])
             tree[packageName] = Array();
         tree[packageName].push(className);
     }
	   $('#menu').html(this.view('init', {classTree: tree} ));
 },

'.menuItem.class click': function(el) {
    var className = $.trim(el.attr('rel'));
    $.cometd.publish('/service/getClass', { qualifiedClassName: className });
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