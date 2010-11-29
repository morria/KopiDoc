/**
 * @tag controllers, home
 * Displays a table of keyboards.	 Lets the user 
 * ["kopidoc.Controllers.Keyboard.prototype.form submit" create], 
 * ["kopidoc.Controllers.Keyboard.prototype.&#46;edit click" edit],
 * or ["kopidoc.Controllers.Keyboard.prototype.&#46;destroy click" destroy] keyboards.
 */
$.Controller.extend('kopidoc.Controllers.Keyboard',
/* @Static */
{
	onDocument: true
},
/* @Prototype */
{
 /**
 * When the page loads, gets all keyboards to be displayed.
 */
 load: function(){
	   if(!$("#keyboard").length)
	       $(document.body).append($('<div/>').attr('id','keyboard'));

     this.startKeypressListener();

     var fSearchResult = this.callback('searchResult');
     $.cometd.addListener('/searchResult', function(searchResult) { fSearchResult(searchResult); });
 },

startKeypressListener: function() {
    var fKeypress = this.callback('keypressListener');
    $(window).keypress(fKeypress);
},

stopKeypressListener: function() {
    $(window).unbind('keypress');
},

keypressListener: function(event) {
    var key = String.fromCharCode(event.charCode);
    switch(key) {
    case "/":
        this.showSearch();
        break;
    case "?":
        this.showRepositoryConfig();
        break;
    }
    return false;
},

showRepositoryConfig: function() {
    this.stopKeypressListener();
    if(!$('#repositoryConfig').length)
	      $('#keyboard').append(this.view('showRepositoryConfig'));
    $('#repositoryConfig').show(500);

    $('#sourcePath').val(localStorage.getItem('sourcePath'));
    $('#classPath').html(localStorage.getItem('classPath'));
},

'#buttonClose click': function() {
    this.startKeypressListener();
    $('#repositoryConfig').hide(500);
},

'#classPath change': function() {
    localStorage.setItem('classPath', $('#classPath').val());
},

'#sourcePath change': function() {
    localStorage.setItem('sourcePath', $('#sourcePath').val());
},

'#buttonLoadSource click': function() {
    var sourcePath = localStorage.getItem('sourcePath');
    var classPath = localStorage.getItem('classPath');

    $.cometd.publish('/service/addSources', { sourcePath:  sourcePath,
                                              classPath: classPath});
},

showSearch: function() {
    this.stopKeypressListener();
    if(!$('#searchWindow').length) {
	      $('#keyboard').append(this.view('showSearch'));
        $(window).keydown(function(e) { 
            switch(e.keyCode) {
            case 38: // up
            case 80: // ctrl-p
                if(80 == e.keyCode && false == e.ctrlKey) break;
                if($('#searchResult li.selected').prev().length) {
                    var selected = $('#searchResult li.selected');
                    selected.removeClass('selected');
                    selected.prev().addClass('selected');
                }
                return false;
            case 40: // down
            case 78: // ctrl-n
                if(78 == e.keyCode && false == e.ctrlKey) break;
                if($('#searchResult li.selected').next().length) {
                    var selected = $('#searchResult li.selected');
                    selected.removeClass('selected');
                    selected.next().addClass('selected');
                }
                return false;
            case 13: // enter
                kopidoc.Controllers.ClassDocument.loadDocumentation($('#searchResult li.selected a'));
                $('#searchInput').blur();
                return false;
            }
        });
    }
    $('#searchWindow').show(500);
    $('#searchInput').focus();
    $('#searchInput').val('');
},

'a.type click': function( el ) {
    // TODO: this doesn't get events
    return kopidoc.Controllers.ClassDocument.loadDocumentation(el);
},

lastSearchQuery: null,

'#searchInput keyup': function() {
    if("" != $('#searchInput').val() &&
       $('#searchInput').val() != this.lastSearchQuery)
        $.cometd.publish('/service/search', { query: $('#searchInput').val() });
    this.lastSearchQuery = $('#searchInput').val();
},

'#searchInput blur':function(event) {
    $('#searchWindow').hide(500);
    $('#searchResult').html('');
    this.startKeypressListener();
},

searchResult: function(data) {
    var searchResult = data.data.searchResult;
    $('#searchResult').html('');
    for(var i in searchResult)
    {
        var item = $('<li><a class="type" href="#" rel="'+searchResult[i]+'">'+searchResult[i]+'</a></li>');
        if(0 == i)
            item.addClass('selected');
        $('#searchResult').append(item);
    }
},


});