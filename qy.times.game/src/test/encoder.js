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