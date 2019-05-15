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
	var memberId = dv.setFloat64(index, littleEndian);
	index += 8;
	var errorcd = dv.getInt32(index, littleEndian);
	index += 4;
	var deviceType = dv.getInt8(index, littleEndian);
	index += 1;
	var tokenLen = dv.getInt16(index, littleEndian);
	index += 2;
	var tokenBuf = arrayBuffer.slice(index, index + tokenLen);
    var token = String.fromCharCode.apply(null, new Int8Array(tokenBuf));
    index += tokenLen;
	var bodyLen = dv.getInt32(index, littleEndian);
	index += 4;
	var body = arrayBuffer.slice(index, index + bodyLen);
	var message = new Message();
	message.setMsgcd(msgcd);
	message.setMemberId(memberId);
	message.setToken(token);
	message.setErrorcd(errorcd);
	message.setDeviceType(deviceType);
	message.body = body;
	return message;
};