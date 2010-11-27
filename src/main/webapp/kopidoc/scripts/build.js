//steal/js kopidoc/scripts/compress.js

load("steal/rhino/steal.js");
steal.plugins('steal/build','steal/build/scripts','steal/build/styles',function(){
	steal.build('kopidoc/scripts/build.html',{to: 'kopidoc'});
});
