(function($)
{
    var cometd = $.cometd;

    $(document).ready(function()
    {
        function _connectionEstablished() { console.log('CometD Connection Established'); }
        function _connectionBroken()  { console.log('CometD Connection Broken'); }
        function _connectionClosed() { console.log('CometD Connection Closed'); }

        // Function that manages the connection status with the Bayeux server
        var _connected = false;
        function _metaConnect(message)
        {
          if (cometd.isDisconnected()) {
            _connected = false;
            _connectionClosed();
            return;
          }
          var wasConnected = _connected;
          _connected = message.successful === true;
          if (!wasConnected && _connected)
            _connectionEstablished();
          else if (wasConnected && !_connected)
            _connectionBroken();
        }

        // Function invoked when first contacting the server and
        // when the server has lost the state of this client
        function _metaHandshake(handshake)
        {
            if (handshake.successful === true)
            {
            }
        }

        function getDocument(doc) { alert(doc); }

        // Disconnect when the page unloads
        $(window).unload(function() { cometd.disconnect(true); });

        var cometURL = location.protocol + "//" + location.host + config.contextPath + "/cometd";

        cometd.configure({
            url: cometURL,
            logLevel: 'debug'
        });

        cometd.addListener('/meta/handshake', _metaHandshake);
        cometd.addListener('/meta/connect', _metaConnect);
        cometd.handshake();

        cometd.addListener('/getDocument', function(message) {
            var classInfo = $.parseJSON(message.data.class);
            $('#document').html('');

            $('#document').append('<h1>'+classInfo.className+'</h1>');
            $('#document').append('<p>'+classInfo.comment+'</p>');
            
            $('#document').append('<ol>');
            for(var i  in classInfo.importedClasses)
              $('#document').append('<li>import '+classInfo.importedClasses[i]+'</li>');
            $('#document').append('</ol>');

            $('#document').append('<ol>');
            for(var i  in classInfo.methods)
              $('#document').append('<li>'+classInfo.methods[i].name+'</li>');
            $('#document').append('</ol>');
          });
    });
})(jQuery);