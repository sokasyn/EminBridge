(function(){

    // - - - - - - - - DatabasePlugin - - - - - - - -
    var dbPluginName = "com.emin.digit.mobile.android.eminbridge.plugin.DatabasePlugin";
    var dbPluginObj = {
        insert : function(sqlString){
            alert("[Custom Js Object] dbPlugin");
            var methodName = "insert";
            EminBridge.execSyncPlugin(dbPluginName,methodName,sqlString);
        }
    };
    window.EminBridge.dbPlugin = dbPluginObj;

    // - - - - - - - -  GPSPlugin - - - - - - - -
    var pgsPluginName = "com.emin.digit.mobile.android.eminbridge.plugin.GPSPlugin";
    var gpsPluginObj = {
        getLocation : function(args){
            var methodName = "getLocation";
            var result = EminBridge.execSyncPlugin(pgsPluginName,methodName,args)
            console.log("[basePlugin js] result:" + result);
            return result;
        }
    }
    window.EminBridge.GPSPlugin = gpsPluginObj;

    // - - - - - - - -  UIAlert - - - - - - - -
    var alertPluginName = "com.emin.digit.mobile.android.eminbridge.plugin.UIAlert";
    var alertPluginObj = {
        toast : function(showText){
            debug("[Custom Js Object alertPluginObj] toast argument length" + arguments.length);
            var methodName = "toast";
            EminBridge.toast(showText);
        }
    };
    window.EminBridge.UIAlert = alertPluginObj;


    function debug(info){
        console.log(info);
    }



}())