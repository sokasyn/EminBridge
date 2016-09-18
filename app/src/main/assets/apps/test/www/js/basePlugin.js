

//document.addEventListener('EminBridgeReady',function(){
//(function(){

    // Web前端的javascript通过该JS提供的方法调用,实现通过反射机制执行Java原生层的方法
    var successCallback = function(){};
    var failCallback = function(){};

    // - - - - - - - - DatabasePlugin - - - - - - - -
    var dbPluginName = "com.emin.digit.mobile.android.hybrid.plugin.PluginDatabase";
    var dbPluginObj = {
        insert : function(sqlString){
            var methodName = "insert";
            EminBridge.execSyncPlugin(dbPluginName,methodName,[sqlString]);
        }
    };
    window.EminBridge.dbPlugin = dbPluginObj;

    // - - - - - - - -  GPSPlugin - - - - - - - -
    var pgsPluginName = "com.emin.digit.mobile.android.hybrid.plugin.PluginGPS";
    var gpsPlugin = {
        getLocation : function(arg){
            var methodName = "getLocation";
            var result = EminBridge.execSyncPlugin(pgsPluginName,methodName,[arg])
            return result;
        },
        startGPS : function(){
            var methodName = "startGPS";
            EminBridge.execSyncPlugin(pgsPluginName,methodName);
        }
    };
    window.EminBridge.GPSPlugin = gpsPlugin;

    // - - - - - - - -  UIAlert - - - - - - - -
    var alertPluginName = "com.emin.digit.mobile.android.hybrid.plugin.PluginAlert";
    var alertPlugin = {
        toast : function(showText){
            var methodName = "toast";
            EminBridge.toast(showText);
        }
    };
    window.EminBridge.UIAlert = alertPlugin;


    // - - - - - - - -  Camera Plugin - - - - - - - -
    var cameraPluginClass = "com.emin.digit.mobile.android.hybrid.plugin.PluginCamera";
    var cameraPlugin = {
        startCamera : function(){
            console.log("startCamera");
            var methodName = "startCamera";
            EminBridge.execSyncPlugin(cameraPluginClass,methodName,[]);
        },
        startSurface : function(){
            console.log("startSurface");
            var methodName = "startSurface";
            EminBridge.execSyncPlugin(cameraPluginClass,methodName,[]);
        }
    };
    window.EminBridge.camera = cameraPlugin;

    // - - - - - - - -  Barcode Plugin - - - - - - - -
    var barcodePluginName = "com.emin.digit.mobile.android.hybrid.plugin.PluginBarcode";
    var barcodePlugin = {
        start:function(type){
            var methodName = "startBarcode";
            EminBridge.execSyncPlugin(barcodePluginName,methodName,[type]);
        },
        testCallback : function(codeType,callback){
            console.log("callback:" + callback);
            var methodName = "testCallbackFun";

            var type_fn_callback = typeof(callback);
            console.log("type of fn_callback:" + type_fn_callback);
            if(typeof(callback) == 'function'){
                console.log("callback is function");
                successCallback = callback;
            }
            EminBridge.execSyncPlugin(barcodePluginName,methodName,[codeType,"successCallback"]);
        },
        loadBarcodeView : function(position){
            var methodName = "loadBarcodeView";
            EminBridge.execSyncPlugin(barcodePluginName,methodName,[position]);
        },
        loadSurfaceView :function(position){
            var methodName = "testSurface";
            EminBridge.execSyncPlugin(barcodePluginName,methodName,[position]);
        },
        startBarcodeScan:function(position){
             var methodName = "startBarcodeScan";
             EminBridge.execSyncPlugin(barcodePluginName,methodName,[position]);
        }
    };
    window.EminBridge.barcode = barcodePlugin;

//})()
//},true);