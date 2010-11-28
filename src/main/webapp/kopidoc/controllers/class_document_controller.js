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
	   if(!$("#class_document").length) {
         $('#main').append($('<div />').attr('id','class_document'));
		     kopidoc.Models.ClassDocument.findAll({}, this.callback('show'));
 	   }
 },
 /**
 * Displays a list of class_documents and the submit form.
 * @param {Array} class_documents An array of kopidoc.Models.ClassDocument objects.
 */
show: function( document ){
    var classDoc = $.parseJSON(document.data.document);

    console.log(classDoc);

    var splitName = classDoc.className.split('.');
    classDoc.shortName = splitName[splitName.length-1];
    classDoc.packageName = splitName.slice(0,splitName.length-1).join('.');

    var classId = classDoc.className.replace(new RegExp('\\.','g'),'_');

    $('#class_document').prepend($('<section />').attr('id', classId)
                                   .attr('class', 'list document'));

	  $('#'+classId).html(this.view('show', {document: classDoc} ));
 },

'a.type click': function( el ) {
    var qualifiedClassName = el.attr('rel');
    var packageName = qualifiedClassName.split('.').slice(0,-1).join('.');
    var className = qualifiedClassName.split('.').slice(-1)[0];

    var docSource = 
        this.getDocumentationSource(packageName, className);

    console.log("Source Is " + docSource);

    if(null != docSource) {
        el.attr('href', docSource);
        el.attr('target', '_NEW');
        return true;
/*
        var docId = qualifiedClassName.replace(new RegExp('\\.','g'),'_');

        $('#class_document').prepend($('<section />').attr('id', docId)
                                     .attr('class', 'list document'));

        $(window).bind('beforeunload', function() { 
            return "The remote document would like you not to use an iframe.";
        } );
        $('#'+docId).append($('<iframe></iframe>').attr('src',docSource).attr('width','100%').addClass('frameDoc'));
        $('#'+docId+" iframe").append($('<a href="'+docSource+'">'+docSoruce+'</a>'));
        $(window).unbind('beforeunload');
*/
    }

    return false;
},

getDocumentationSource: function(packageName, className) {

    console.log(packageName);

    if(packageName.match(/^java\./))
        return this.buildJavadocURL("http://download.oracle.com/javase/6/docs/api/" ,
                                    packageName, className);

    if(packageName.match(/^com\.sun\.javadoc/))
        return this.buildJavadocURL("http://download.oracle.com/javase/1.4.2/docs/tooldocs/javadoc/doclet/",
                                    packageName, className);

    if(packageName.match(/^org\.codehaus\.jackson/))
        return this.buildJavadocURL("http://jackson.codehaus.org/1.6.2/javadoc/",
                                    packageName, className);

    if(packageName.match(/^org\.apache\.lucene/))
        return this.buildJavadocURL("http://lucene.apache.org/java/3_0_2/api/all/",
                                    packageName, className);

    if(packageName.match(/^org\.cometd\.bayeux/))
        return this.buildJavadocURL("http://download.cometd.org/bayeux-api-2.0.beta0-javadoc",
                                    packageName, className);

    return null;
},

buildJavadocURL: function(root, packageName, className)
{
    return root
        + packageName.replace(new RegExp('\\.','g'),"/")+"/" 
        + className + ".html";
},

 /**
 *	 Listens for class_documents being destroyed and removes them from being displayed.
 */
"class_document.destroyed subscribe": function(called, class_document){
	class_document.elements().remove();	 //removes ALL elements
 }
});