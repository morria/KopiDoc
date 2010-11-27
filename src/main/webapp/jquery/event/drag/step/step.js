/**
 * @add jQuery.Drag.prototype
 */

steal.plugins('jquery/event/drag','jquery/dom/cur_styles').then(function($){
	var round = function(x, m){
		return Math.round(x / m) * m;
	}
	
	$.Drag.prototype.
	/**
	 * @function step
	 * @plugin jquery/event/drag/step
	 * @download jquery/dist/jquery.event.drag.step.js
	 * makes the drag move in steps
	 * @codestart
	 * drag.step({x: 5}, $('foo'))
	 * @codeend
	 * @param {number} amount
	 * @param {jQuery} container
	 */
	step = function(amount, container){
		//on draws ... make sure this happens
		
		if(typeof amount == 'number'){
			amount = {x: amount, y:amount}
		}
		container = container || $(document.body);
		this._step = amount;
		
		var styles = container.curStyles("borderTopWidth","paddingTop","borderLeftWidth","paddingLeft");
		var left = parseInt( styles.borderTopWidth ) + parseInt( styles.paddingTop ),
			top = parseInt( styles.borderLeftWidth ) + parseInt( styles.paddingLeft );
		
		this._step.offset =  container.offsetv().plus(left, top);
	};
	
	
	var oldPosition = $.Drag.prototype.position;
	$.Drag.prototype.position = function(offsetPositionv){
		//adjust required_css_position accordingly
		if(this._step){
			var movingSize = this.movingElement.dimensionsv(),
			    lot = this._step.offset.top(),
				lof = this._step.offset.left();
			
			//console.log(round(offsetPositionv.left() - lof,  this._step.x))
			if(this._step.x){
				offsetPositionv.left(  lof + round(offsetPositionv.left() - lof,  this._step.x) )
			}
			if(this._step.y){
				offsetPositionv.top(  lot + round(offsetPositionv.top() - lot,  this._step.y) )
			}
		}
		
		oldPosition.call(this, offsetPositionv)
	}
	
})