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
	  onDocument: true,

    loadDocumentation: function(el) {
        var qualifiedClassName = el.attr('data-qualifiedName');
        var packageName = qualifiedClassName.split('.').slice(0,-1).join('.');
        var className = qualifiedClassName.split('.').slice(-1)[0];
        var docSource = 
            kopidoc.Controllers.ClassDocument.getDocumentationSource(packageName, className);

        if(null != docSource) {
            el.attr('href', docSource).attr('target','_NEW');
            return true;
        }

        $.address.value(qualifiedClassName);
        return false;
    },

    getDocumentationSource: function(packageName, className) {
        if(packageName.match(/^javax?\./))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://download.oracle.com/javase/6/docs/api" ,
                                                                     packageName, className);

        if(packageName.match(/^com\.sun\.javadoc/))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://download.oracle.com/javase/1.4.2/docs/tooldocs/javadoc/doclet",
                                                                     packageName, className);

        if(packageName.match(/^org\.codehaus\.jackson/))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://jackson.codehaus.org/1.6.2/javadoc",
                                                                     packageName, className);

        if(packageName.match(/^org\.apache\.lucene/))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://lucene.apache.org/java/3_0_2/api/all",
                                                                     packageName, className);

        if(packageName.match(/^org\.cometd\.bayeux/))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://download.cometd.org/bayeux-api-2.0.beta0-javadoc",
                                                                     packageName, className);

        if(packageName.match(/^org\.joda/))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://joda-time.sourceforge.net/apidocs",
                                                                     packageName, className);

        if(packageName.match(/^org\.postgres/))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://jdbc.postgresql.org/documentation/publicapi",
                                                                     packageName, className);

        if(packageName.match(/^org\.apache\.solr/))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://lucene.apache.org/solr/api",
                                                                     packageName, className);

        if(packageName.match(/^org\.apache\.thrift/))
            return kopidoc.Controllers.ClassDocument.buildJavadocURL("http://sundresh.org/docs/thrift-0.2.0-javadoc/",
                                                                     packageName, className);

        return null;
    },

    buildJavadocURL: function(root, packageName, className)
    {
        return root + "/"
            + packageName.replace(new RegExp('\\.','g'),"/")+"/" 
            + className + ".html";
    }

},
/* @Prototype */
{
 /**
 * When the page loads, gets all class_documents to be displayed.
 */
 load: function(){

     // Listen for address change events and load the document;
     $.address.change(function(event) {
         $.cometd.publish('/service/getClass', { qualifiedClassName: event.value.replace('/','') });
     });
  
	   if(!$("#class_document").length) {
         $('body').append($('<article />').attr('id','class_document'));
         var fSuccess = this.callback('show');
         $.cometd.addListener('/getClass', function(classDoc) { fSuccess(classDoc); });
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

    this.view.cache = false;
	  $('#class_document').html(this.view('show1', {document: classDoc, cache: false} ));
 },

'a[rel=type] click': function( el ) {
    return kopidoc.Controllers.ClassDocument.loadDocumentation(el);
},

'section header click': function( el ) {
    el.parent().children('.extraInfo').toggle(100);
},

'.filterScope mousedown': function(el) {
    if(1 == el.children().length)
        el.parents('section').find('ol li header').each(function(i,head) {
            if(0 == el.children('option[value='+$(head).attr('class')+']').length)
                el.append(new Option($(head).attr('class'), $(head).attr('class')));
        });
},

'.filterScope change': function(el) {
    el.parents('section').find('ol li header').each(function(i, head) {
        if('' == el.val() || $(head).hasClass(el.val()))
            $(head).parent('li').show(100);
        else
            $(head).parent('li').hide(100);
    });
},

'.filterParameters mousedown': function(el) {
    if(1 == el.children().length)
        el.parents('section').find('ol li .parameterList li a[rel=type]').each(function(i,type) {
            if(0 == el.children('option[value='+$(type).attr('data-qualifiedName')+']').length) {
                var qualifiedName = $(type).attr('data-qualifiedName');
                var simpleName = qualifiedName.split('.').slice(-1)[0];
                el.append(new Option(simpleName, qualifiedName));
            }
        });
},

'.filterParameters change': function(el) {
    var lookupName = el.val();
    el.parents('section').find('ol li .parameterList').each(function(i, pList) {
        var pListContains = ('' == el.val());
        $(pList).find('li a[rel=type]').each(function(i,a) {
            console.log($(a).attr('data-qualifiedName'));

            if($(a).attr('data-qualifiedName') == lookupName)
                pListContains = true;
        });

        if(pListContains)
            $(pList).parents('li').show(100);
        else
            $(pList).parents('li').hide(100);
    });
},

'.filterTypes mousedown': function(el) {
    if(1 == el.children().length)
        el.parents('section').find('ol li .methodSignature >*[rel=type]').each(function(i,type) {
            if(0 == el.children('option[value='+$(type).attr('data-qualifiedName')+']').length) {
                var qualifiedName = $(type).attr('data-qualifiedName');
                var simpleName = qualifiedName.split('.').slice(-1)[0];
                el.append(new Option(simpleName, qualifiedName));
            }
        });
},

'.filterTypes change': function(el) {
    var lookupName = el.val();
    el.parents('section').find('ol li .methodSignature >*[rel=type]').each(function(i, sig) {
        if(('' == el.val()) || ($(sig).attr('data-qualifiedName') == lookupName))
            $(sig).parents('li').show(100);
        else
            $(sig).parents('li').hide(100);
    });
},

 /**
 *	 Listens for class_documents being destroyed and removes them from being displayed.
 */
"class_document.destroyed subscribe": function(called, class_document){
	class_document.elements().remove();	 //removes ALL elements
 }
});