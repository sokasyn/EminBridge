;(function(w){
        var ioPluginName = "com.emin.digit.mobile.android.meris.platform.plugin.PluginFileIO";
        var ioPlugin = {
           /**
            * 读取assets目录下的文件的内容
            */
            getFileContent : function(fileName){
                var methodName = "getFileContentFromAssets";
                return EminBridge.execSyncPlugin(ioPluginName,methodName,[fileName]);
            }
        };
        w.EminBridge.pluginIo = ioPlugin;
})(window);