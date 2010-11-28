/**
 * @tag models, home
 * Wraps backend class_document services.  Enables 
 * [kopidoc.Models.ClassDocument.static.findAll retrieving],
 * [kopidoc.Models.ClassDocument.static.update updating],
 * [kopidoc.Models.ClassDocument.static.destroy destroying], and
 * [kopidoc.Models.ClassDocument.static.create creating] class_documents.
 */
$.Model.extend('kopidoc.Models.ClassDocument',
/* @Static */
{
	/**
 	 * Retrieves class_documents data from your backend services.
 	 * @param {Object} params params that might refine your results.
 	 * @param {Function} success a callback function that returns wrapped class_document objects.
 	 * @param {Function} error a callback function for an error in the ajax request.
 	 */
	findAll: function( params, success, error ){
      $.cometd.addListener('/getFile', function(file) { success(file); });
	}
},
/* @Prototype */
{});