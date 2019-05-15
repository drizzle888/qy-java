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