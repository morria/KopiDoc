/**
 * @tag models, home
 * Wraps backend menu services.  Enables 
 * [kopidoc.Models.Menu.static.findAll retrieving],
 * [kopidoc.Models.Menu.static.update updating],
 * [kopidoc.Models.Menu.static.destroy destroying], and
 * [kopidoc.Models.Menu.static.create creating] menus.
 */
$.Model.extend('kopidoc.Models.Menu',
/* @Static */
{
	/**
 	 * Retrieves menus data from your backend services.
 	 * @param {Object} params params that might refine your results.
 	 * @param {Function} success a callback function that returns wrapped menu objects.
 	 * @param {Function} error a callback function for an error in the ajax request.
 	 */
	findAll: function( params, success, error ){

      var cometd = $.cometd;

      cometd.addListener('/addSources',  function(message) { 
          cometd.publish('/service/getFileList', {} );
          cometd.publish('/service/getClassList', {} );
      } );

      cometd.addListener('/getFileList', function(fileList) { success(fileList); });

      cometd.publish('/service/addSources', { sourcePath:  params.sourcePath,
                                              classPath: params.classPath});
	},

},
/* @Prototype */
{});