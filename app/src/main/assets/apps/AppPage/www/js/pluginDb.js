;(function(w){
        var dbPluginName = "com.emin.digit.mobile.android.meris.platform.plugin.PluginDatabase";
        var dbPlugin = {
            initDatabase : function(tables){
                var methodName = "initDatabase";
                return EminBridge.execSyncPlugin(dbPluginName,methodName,[tables]);
            },
            insert : function(sql){
                console.log("sql" + sql);
                var methodName = "insert";
                return EminBridge.execSyncPlugin(dbPluginName,methodName,[sql]);
            },
            deleteWithSql: function(sql){
                var methodName = "executeSql";
                EminBridge.execSyncPlugin(dbPluginName,methodName,[sql]);
            },
            updateWithSql: function(sql){
                var methodName = "executeSql";
                EminBridge.execSyncPlugin(dbPluginName,methodName,[sql]);
            },
            query : function(tableName){
                var methodName = "query";
                return EminBridge.execSyncPlugin(dbPluginName,methodName,[tableName]);
            },
            queryWithSql : function(sql){
                var methodName = "queryWithSql";
                return EminBridge.execSyncPlugin(dbPluginName,methodName,[sql]);
            }
        };
        w.EminBridge.pluginDb = dbPlugin;
})(window);