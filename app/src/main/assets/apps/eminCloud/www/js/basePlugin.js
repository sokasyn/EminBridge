(function(){

    // Web前端的javascript通过该JS提供的方法调用,实现通过反射机制执行Java原生层的方法

    // - - - - - - - - DatabasePlugin - - - - - - - -
    var dbPluginName = "com.emin.digit.mobile.android.hybrid.plugin.DatabasePlugin";
    var dbPluginObj = {
        insert : function(sqlString){
            //alert("[Custom Js Object] database plugin");
            var methodName = "insert";
            EminBridge.execSyncPlugin(dbPluginName,methodName,[sqlString]);
        }
    };
    window.EminBridge.dbPlugin = dbPluginObj;

    // - - - - - - - -  GPSPlugin - - - - - - - -
    var pgsPluginName = "com.emin.digit.mobile.android.hybrid.plugin.GPSPlugin";
    var gpsPlugin = {
        getLocation : function(args){
            var methodName = "getLocation";
            var result = EminBridge.execSyncPlugin(pgsPluginName,methodName,args)
            console.log("[basePlugin js] result:" + result);
            return result;
        },
        startGPS : function(){
            var methodName = "startGPS";
            EminBridge.execSyncPlugin(pgsPluginName,methodName);
        }
    }
    window.EminBridge.GPSPlugin = gpsPlugin;

    // - - - - - - - -  UIAlert - - - - - - - -
    var alertPluginName = "com.emin.digit.mobile.android.hybrid.plugin.UIAlert";
    var alertPlugin = {
        toast : function(showText){
            debug("[Custom Js Object alertPluginObj] toast argument length" + arguments.length);
            var methodName = "toast";
            EminBridge.toast(showText);
        }
    };
    window.EminBridge.UIAlert = alertPlugin;

    // - - - - - - - -  Barcode Plugin - - - - - - - -
    var barcodePluginName = "com.emin.digit.mobile.android.hybrid.plugin.PluginBarcode";
    var barcodePlugin = {
        start:function(type, callback){
            debug("[Custom Js Object barcodePluginObject] arguments length:" + arguments.length);
            var methodName = "startBarcode";
            console.log("callback:" + callback);
            if(callback typeof "function"){
                console.log("is function");
            }else{
                console.log("is not a function");
            }

            EminBridge.execSyncPlugin(barcodePluginName,methodName,[type,callback]);
        }
    }
    window.EminBridge.barcode = barcodePlugin;


    function debug(info){
        console.log(info);
    }
})()