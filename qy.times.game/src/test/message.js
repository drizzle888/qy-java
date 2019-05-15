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
	    for (var i = 0, strLen = str.length; i < strLen; i++) {
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


