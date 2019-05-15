function roleAction() {
    
}

roleAction.execute = function(message) {
	switch(message.getMsgcd()) {
    case 0x1001:
    	var s = message.getString();
    	console.log(s);
    	var i = message.getInt();
    	console.log(i);
    	var sh = message.getShort();
    	console.log(sh);
    	var b = message.getBoolean();
    	console.log(b);
      break;
    }
};