(function(){

    var name = "sam";

    var dbPluginObj = {
        insert : function(sql){
            alert(99999999);
        }
    }
    window.dbPlugin = dbPluginObj;
}(window))