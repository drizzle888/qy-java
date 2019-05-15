function Message(bodyLen){
	this.msgcd;
    this.memberId;
    this.token;
    this.deviceType = 3;
    this.errorcd;
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
    
    this.setMemberId = function(arg) {
    	this.memberId = arg;
    }
    
    this.getMemberId = function() {
        return this.memberId;
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
    
    this.setErrorcd = function(arg) {
        this.errorcd = arg;
    }
    
    this.getErrorcd = function() {
        return this.errorcd;
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
    
	function encode_utf8(string) {
		var utf8 = unescape(encodeURIComponent(string));
		var octets = new Uint8Array(utf8.length), i;
		for (i = 0; i < utf8.length; i += 1) {
			octets[i] = utf8.charCodeAt(i);
		}
		return octets;
	}

	function decode_utf8(octets) {
		var utf8 = String.fromCharCode.apply(null, octets);
		return decodeURIComponent(escape(utf8));
	}
	
	/*this.putString = function(str) {
		debugger;
		var ts = TinyStream(str);
		var bytes = ts.getBytesArray();
		var byteLen = bytes.length;
		this.putShort(byteLen);
		for (var i = 0; i < byteLen; i++) {
			this.putByte(bytes[i]);
			console.log(bytes[i]);
		}
	}*/
	
	this.putString = function(str) {
		debugger;
		var octets = encode_utf8(str);
		this.putShort(octets.byteLength);
		for (var i = 0; i < octets.byteLength; i++) {
			console.log(octets[i]);
			this.putByte(octets[i]);
		}
	}

	this.getString = function() {
		debugger;
		var length = this.getShort();
		var buf = this.body.slice(this.position, this.position + length);
		/*var v = new Uint8Array(buf);
		for (var i = 0; i < v.length; i++) {
			v[i] &= 0xff;
			console.log(v[i]);
		}
		var str = String.fromCharCode.apply(null, new Uint8Array(buf));
		*/
		var v = new Uint8Array(buf);
		var str = decode_utf8(v);
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
        return 17 + this.token.length + 2;
    }
    
    this.getRealBodyLen = function() {
    	return this.bodyRealLen;
    }
}


