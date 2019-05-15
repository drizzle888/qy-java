function decoder() {
    
}

var littleEndian = true;

decoder.doDecoder = function(arrayBuffer) {
	var index = 0;
	var dv = new DataView(arrayBuffer);
	var detector_value = dv.getInt32(index, littleEndian);
	index += 4;
	var msgLen = dv.getInt32(index, littleEndian);
	index += 4;
	var msgcd = dv.getInt32(index, littleEndian);
	index += 4;
	var roleIdLen = dv.getInt16(index, littleEndian);
	index += 2;
	var roleIdBuf = arrayBuffer.slice(index, index + roleIdLen);
    var roleId = String.fromCharCode.apply(null, new Int8Array(roleIdBuf));
    index += roleIdLen;
    var token = dv.getInt32(index, littleEndian);
	index += 4;
	var deviceType = dv.getInt32(index, littleEndian);
	index += 4;
	var bodyLen = dv.getInt32(index, littleEndian);
	index += 4;
	var body = arrayBuffer.slice(index, index + bodyLen);
	var message = new Message();
	message.setMsgcd(msgcd);
	message.setRoleId(roleId);
	message.setToken(token);
	message.setDeviceType(deviceType);
	message.body = body;
	return message;
};

function encoder() {
    
}

var littleEndian = true;

encoder.doEncode = function (message) {
    var body = message.getBody();
    var bodyLen = message.getRealBodyLen();
    var headerLen = message.headerLen();
    var msgLen = bodyLen + headerLen + 4;
    var msg = new ArrayBuffer(msgLen + 8);
    
    var index = 0;
    var dvMsg = new DataView(msg);
    dvMsg.setInt32(index, Message.detector_value, littleEndian);
	index += 4;
	dvMsg.setInt32(index, msgLen, littleEndian);
	index += 4;
	//----------header>>>>>>>>>>
	dvMsg.setInt32(index, message.getMsgcd(), littleEndian);
	index += 4;
	var byteLen = message.getRoleId().length;
	dvMsg.setInt16(index, byteLen, littleEndian);
	index += 2;
    for (var i = 0; i < message.getRoleId().length; i++) {
    	dvMsg.setInt8(index, message.getRoleId().charCodeAt(i), littleEndian);
        index += 1;
    }//20
    dvMsg.setInt32(index, message.getToken(), littleEndian);
    index += 4;//24
    dvMsg.setInt32(index, message.getDeviceType(), littleEndian);
    index += 4;//28
  //<<<<<<<<<<<<header----------
	dvMsg.setInt32(index, bodyLen, littleEndian);
	index += 4;
	dvBody = new DataView(body);
	for (var i = 0; i < bodyLen; i++) {
		dvMsg.setInt8(index, dvBody.getInt8(i), littleEndian);
		index++;
	}
	var a = new Uint8Array(msg);
    return a.buffer;
};

function Message(bodyLen){
	this.msgcd;
    this.roleId;
    this.token;
    this.deviceType = 1;
    this.body = new ArrayBuffer(2048);
    this.bodyRealLen = 0;
    Message.detector_value = 123456;
    
    var littleEndian = (function() {
    	var buffer = new ArrayBuffer(2);
    	new DataView(buffer).setInt16(0, 256, true);
    	var b = new Int16Array(buffer)[0] === 256;
    	return b == false;
	})();

    this.position = 0;

    this.setMsgcd = function(arg) {
        this.msgcd = arg;
    }
    
    this.getMsgcd = function() {
        return this.msgcd;
    }
    
    this.setRoleId = function(arg) {
    	this.roleId = arg;
    }
    
    this.getRoleId = function() {
        return this.roleId;
    }
    
    this.setToken = function(arg) {
        this.token = arg;
    }
    
    this.getToken = function() {
        return this.token;
    }
    
    this.setDeviceType = function(arg) {
        this.deviceType = arg;
    }
    
    this.getDeviceType = function() {
        return this.deviceType;
    }
    
    this.putInt = function(arg) {
    	var dv = new DataView(this.body);
    	dv.setInt32(this.position, arg, littleEndian);
    	this.position += 4;
    	this.bodyRealLen += 4;
    }
    
    this.getInt = function() {
    	var dv = new DataView(this.body);
    	var value = dv.getInt32(this.position, littleEndian);
    	this.position += 4;
    	return value;
	}
    
   this.putShort = function(arg) {
	   var dv = new DataView(this.body);
	   dv.setInt16(this.position, arg, littleEndian);
	   this.position += 2;
	   this.bodyRealLen += 2;
	}
	
	this.getShort = function() {
		var dv = new DataView(this.body);
		var value = dv.getUint16(this.position, littleEndian);
		this.position += 2;
		this.bodyRealLen += 2;
		return value;
	}
    
	this.putString = function(str) {
		var byteLen = str.length;
		this.putShort(byteLen);
		var dv = new DataView(this.body);
	    for (var i = 0; i < byteLen; i++) {
	    	console.log(str.charCodeAt(i));
	        this.putByte(str.charCodeAt(i));
	    }
	}

	this.getString = function() {
		var length = this.getShort();
		var buf = this.body.slice(this.position, this.position + length);
        var str = String.fromCharCode.apply(null, new Int8Array(buf));
        this.position += length;
        return str;
	}
	 
    this.putByte = function(arg) {
    	var dv = new DataView(this.body);
    	dv.setInt8(this.position, arg, littleEndian);
    	this.position += 1;
    	this.bodyRealLen += 1;
	}

	this.getByte = function() {
		var dv = new DataView(this.body);
		var value = dv.getInt8(this.position, littleEndian);
		this.position += 1;
		this.bodyRealLen += 1;
		return value;
	}
	
	this.putBoolean = function(arg) {
		this.putByte(arg ? 1 : 0);
	}

	this.getBoolean = function() {
		var data = this.getByte();
		return data > 0;
	}
    
    this.getBody = function () {
		return this.body;
	}
    
    this.flip = function() {
    	this.position = 0;
	}
    
    this.headerLen = function() {
        return 12 + this.roleId.length + 2;
    }
    
    this.getRealBodyLen = function() {
    	return this.bodyRealLen;
    }
}


var ws;
var host = "ws://127.0.0.1:9001/ws";

function remote() {
    
}

remote.connect = function(host, roleId, roleType, name, avatar) {
    if (ws != null) {
		console.log("connecd");
		return ;
	}
	if ('WebSocket' in window) {
        ws = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        ws = new MozWebSocket(host);
    } else {
        alert("Your browser does not support WebSocket.");
        return ;
    }
    ws.onopen = function() {
        console.log("onopen");
        ws.binaryType = "arraybuffer";
        remote.roleId = roleId;
        remote.roleType = roleType;
        remote.login(roleId, name, avatar);
    }
    ws.onmessage = function(e) {
        console.log("onmessage");
        var message = decoder.doDecoder(e.data);
        var actioncd = message.getMsgcd() - message.getMsgcd() % 0x100;
        var action = listener[actioncd];
        action.execute(message);
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
    		msg.setRoleId(remote.roleId);
    	   	msg.setToken(remote.token);
	        var bytes = encoder.doEncode(msg);
	        ws.send(bytes);
	    } else {
	        alert("connection is not turned on.");
	    }
    }
}

remote.roleId = "";
remote.token = 0;
remote.roleType = 0;

remote.login = function(roleId, name, avatar) {
	var msg = new Message();
   	msg.setMsgcd(messageCode.msg_code_login);
   	msg.setRoleId(roleId);
   	msg.setToken(remote.token);
   	msg.putInt(remote.roleType);
   	msg.putString(name);
   	msg.putString(avatar);
   	remote.sendmessge(msg);
}