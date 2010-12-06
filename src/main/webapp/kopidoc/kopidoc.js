steal.plugins(	
	  'jquery/controller',
	  'jquery/controller/subscribe',
	  'jquery/view/ejs',
	  'jquery/controller/view',
	  'jquery/model',
	  'jquery/dom/fixture',
	  'jquery/dom/form_params',
    'steal/less')
	  .css('css/vendor/reset-fonts-grids')
	  .resources('jquery.address-1.3.1.min')					// 3rd party script's (like jQueryUI), in resources folder
	  .models()
	  .controllers('menu','class_document','keyboard')
	  .views()
    .then(function() {
        steal.less('css/style');
        <!--
        less.env = "development";
        less.watch();
        -->

    });
