;(function(w){

    // - - - - - - - - - 二维码扫描插件 - - - - - - - - -
    var barcodePluginName = "com.emin.digit.mobile.android.meris.platform.plugin.PluginBarcode"
    var barcodePlugin = {
        loadBarcodeView : function(viewPosition){
            alert("loadBarcode view 11");
            var methodName = "loadBarcodeView";
            EminBridge.execSyncPlugin(barcodePluginName,methodName,[viewPosition]);
        }
    }
    w.EminBridge.pluginBarcode = barcodePlugin;

})(window);