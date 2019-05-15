var socket;

//connect('ws://192.168.1.105:2048/ws');

function connect(host, memberId) {
    if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket;
    }

    if (window.WebSocket) {
        socket = new WebSocket(host);
        socket.binaryType = "arraybuffer";
        socket.onmessage = function(event) {
            console.log("onmessage");
        };
        socket.onopen = function(event) {
        	console.log("onopen");
        };
        socket.onclose = function(event) {
        	console.log("onclose");
        };
    } else {
        alert("your browser does not support!");
    }
}


function send(msg) {
    if (!window.WebSocket) {
        return;
    }
    if (socket.readyState == WebSocket.OPEN) {
        var bytes = Encoder.doEncode(msg);
        socket.send(bytes);
    } else {
        alert("connection is not turned on.");
    }
}