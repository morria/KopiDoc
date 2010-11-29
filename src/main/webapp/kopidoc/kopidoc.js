steal.plugins(	
	  'jquery/controller',
	  'jquery/controller/subscribe',
	  'jquery/view/ejs',
	  'jquery/controller/view',
	  'jquery/model',
	  'jquery/dom/fixture',
	  'jquery/dom/form_params')
	  .css()
	  .resources()					// 3rd party script's (like jQueryUI), in resources folder
	  .models()
	  .controllers('menu','class_document','keyboard')
	  .views();