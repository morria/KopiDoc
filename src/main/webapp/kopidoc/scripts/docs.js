//js kopidoc/scripts/doc.js

load('steal/rhino/steal.js');
steal.plugins("documentjs").then(function(){
	DocumentJS('kopidoc/kopidoc.html');
});