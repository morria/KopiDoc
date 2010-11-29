/**
 * @tag models, home
 * Wraps backend keyboard services.  Enables 
 * [kopidoc.Models.Keyboard.static.findAll retrieving],
 * [kopidoc.Models.Keyboard.static.update updating],
 * [kopidoc.Models.Keyboard.static.destroy destroying], and
 * [kopidoc.Models.Keyboard.static.create creating] keyboards.
 */
$.Model.extend('kopidoc.Models.Keyboard',
/* @Static */
{
	/**
 	 * Retrieves keyboards data from your backend services.
 	 * @param {Object} params params that might refine your results.
 	 * @param {Function} success a callback function that returns wrapped keyboard objects.
 	 * @param {Function} error a callback function for an error in the ajax request.
 	 */
	findAll: function( params, success, error ){
		$.ajax({
			url: '/keyboard',
			type: 'get',
			dataType: 'json',
			data: params,
			success: this.callback(['wrapMany',success]),
			error: error,
			fixture: "//kopidoc/fixtures/keyboards.json.get" //calculates the fixture path from the url and type.
		});
	},
	/**
	 * Updates a keyboard's data.
	 * @param {String} id A unique id representing your keyboard.
	 * @param {Object} attrs Data to update your keyboard with.
	 * @param {Function} success a callback function that indicates a successful update.
 	 * @param {Function} error a callback that should be called with an object of errors.
     */
	update: function( id, attrs, success, error ){
		$.ajax({
			url: '/keyboards/'+id,
			type: 'put',
			dataType: 'json',
			data: attrs,
			success: success,
			error: error,
			fixture: "-restUpdate" //uses $.fixture.restUpdate for response.
		});
	},
	/**
 	 * Destroys a keyboard's data.
 	 * @param {String} id A unique id representing your keyboard.
	 * @param {Function} success a callback function that indicates a successful destroy.
 	 * @param {Function} error a callback that should be called with an object of errors.
	 */
	destroy: function( id, success, error ){
		$.ajax({
			url: '/keyboards/'+id,
			type: 'delete',
			dataType: 'json',
			success: success,
			error: error,
			fixture: "-restDestroy" // uses $.fixture.restDestroy for response.
		});
	},
	/**
	 * Creates a keyboard.
	 * @param {Object} attrs A keyboard's attributes.
	 * @param {Function} success a callback function that indicates a successful create.  The data that comes back must have an ID property.
	 * @param {Function} error a callback that should be called with an object of errors.
	 */
	create: function( attrs, success, error ){
		$.ajax({
			url: '/keyboards',
			type: 'post',
			dataType: 'json',
			success: success,
			error: error,
			data: attrs,
			fixture: "-restCreate" //uses $.fixture.restCreate for response.
		});
	}
},
/* @Prototype */
{});