var ws;
var url = "ws://127.0.0.1:9001/ws";

function remote() {
    
}

remote.connect = function() {
    if (ws != null) {
		console.log("connecd");
		return ;
	}
	if ('WebSocket' in window) {
        ws = new WebSocket(url);
    } else if ('MozWebSocket' in window) {
        ws = new MozWebSocket(url);
    } else {
        alert("Your browser does not support WebSocket.");
        return ;
    }
    ws.onopen = function() {
        console.log("onopen");
        ws.binaryType = "arraybuffer";
        
    }
    ws.onmessage = function(e) {
        console.log("onmessage");
        var message = decoder.doDecoder(e.data);
        var msgcdType = message.getMsgcd() - message.getMsgcd() % 0x100;
        switch(msgcdType) {
        case 0x1000:
          roleAction.execute(message);
          break;
        }
    }
    ws.onclose = function(e) {
        console.log("onclose");
    }
    ws.onerror = function(e) {
        console.log("onerror");
    }
}

remote.sendmessge = function(msg) {
	if (ws != null) {
    	if (ws.readyState == WebSocket.OPEN) {
	        var bytes = encoder.doEncode(msg);
	        ws.send(bytes);
	    } else {
	        alert("connection is not turned on.");
	    }
    }
}