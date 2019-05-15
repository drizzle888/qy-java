if(bufferUtils){
    var bufferUtils={}||bufferUtils;
}
bufferUtils={
    short_length : 2,
        
	int_length : 4, 
        
	long_length : 8,
        
    str2bytes:function(str){
        var bytes = [];
        for (var i = 0; i < str.length; i++) {
            bytes[i] = str.charCodeAt(i);
        }
        return bytes;
    }, 

    bytes2str:function(m) {
        var value = "";
        for (var i in m) {
            value += String.fromCharCode(m[i]);
        }
        //value += '\n';
        return value;
    },

    short2bytes:function(x){
        var bytes = [];
        var i = this.short_length;
        do {
            bytes[--i] = x & 0xff;
            x = x>>8;
        } while ( i )
        return bytes;
    },

    bytes2short:function(x){
        var val = 0;
        for (var i = 0; i < x.length; ++i) {
            val += x[i];        
            if (i < x.length-1) {
                val = val << 8;
            }
        }
        return val;
    },

    int2bytes:function (x){
        var bytes = [];
        var i = this.int_length;
        do {
            bytes[--i] = x & 0xff;
            x = x >> 8;
        } while ( i )
        return bytes;
    },

    bytes2int:function (x){
        var val = 0;
        for (var i = 0; i < x.length; ++i) {
            val += x[i];        
            if (i < x.length-1) {
                val = val << 8;
            }
        }
        return val;
    },

    long2bytes:function (x){
        var byteArray = [0, 0, 0, 0, 0, 0, 0, 0];
        for ( var index = 0; index < byteArray.length; index ++ ) {
            var byte = x & 0xff;
            byteArray [ index ] = byte;
            x = (x - byte) / 0xff ;
        }
        return byteArray;
    },

    bytes2long:function (x){
        var value = 0;
        for ( var i = x.length - 1; i >= 0; i--) {
            value = (value * 0xff) + x[i];
        }
        return value;
    }
}