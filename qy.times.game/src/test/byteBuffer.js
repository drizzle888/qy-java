var byteBuffer = function (length){
    var buffer = [];
    var position = 0;
    var limit = length;
    var length = 0;

    var init = function(length) {
        this.buffer = new Array(length);
		this.position = 0;
		this.limit = length;
		this.length = 0;
    };
    
    init();
    
    this.putBytes = function(bytes, size, index) {
        if (!size) {
            size = bytes.length;
        }
        if (!index) {
            index = 0;
        }
        for (var i = 0; i < size; i++) {
			buffer[position + i] = bytes[i + index];
		}
		position += size;
		limit -= size;
        length += size;
    }
    
    this.getBytes = function(length) {
		var bytes = [];
		for (var i = 0; i < length; i++) {
			bytes[i] = buffer[i + position];
		}
		position += length;
		limit -= length;
		return bytes;
	};
    
    this.flip = function() {
		position = 0;
		limit = length;
	};

	this.getPosition = function() {
		return position;
	};

	this.getLimit = function() {
		return limit;
	};

	this.getLength = function() {
		return length;
	};
    
}