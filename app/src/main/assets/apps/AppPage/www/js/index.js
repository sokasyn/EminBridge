	(function($, doc) {
		$.init();
		$.ready(function() {
			var sql="select * from logisticscompany where pid='0'";
			var lg = JSON.parse(EminBridge.pluginDb.queryWithSql(sql));
            var data = [];
			for(i=0;i<lg.length;i++){
			    var nData ={};
			    nData.value=lg[i].id;
			    nData.text= lg[i].name;
			    var childi = [];

			    var sql1="select * from logisticscompany where pid='"+lg[i].id+"'";
			    var lgi = JSON.parse(EminBridge.pluginDb.queryWithSql(sql1));
			    for(var j=0;j<lgi.length;j++){
			         var jData = {};
			         jData.value=lgi[j].id;
					 jData.text= lgi[j].name;
					 var childj = [];
					var sqlj="select * from logisticscompany where pid='"+lgi[j].id+"'";
			    	 var lgj = JSON.parse(EminBridge.pluginDb.queryWithSql(sqlj));
			    	  for(var z=0;z<lgj.length;z++){
			    	    var zData = {};
						zData.value=lgj[z].id;
						zData.text= lgj[z].name;
						childj.push(zData)
			    	  }
			    	  jData.children=childj;
					  childi.push(jData);
			          }
						nData.children=childi;
						data.push(nData);
			}
     //console.log("?"+JSON.stringify(data));
			var showLgcompanyPicker = new $.PopPicker({
					layer: 3
					});
			showLgcompanyPicker.setData(data);
			var showLgcompanyPickerButton = doc.getElementById('showLgcompanyPicker');
			var userResult = doc.getElementById('userResult');
			showLgcompanyPickerButton.addEventListener('tap', function(event) {
				showLgcompanyPicker.show(function(items) {
					showLgcompanyPickerButton.innerHTML =(items[0] || {}).text;
					var name=items[0];
					document.getElementById("vehicle").innerHTML=(items[1] || {}).text;
					document.getElementById("driver").innerHTML=(items[2] || {}).text;
				});
			}, false);
		});
	})(mui, document);
	//选择物流公司

		(function($, doc) {
		$.init();
		$.ready(function() {
			var sql="select * from warehouse";
			var lg = JSON.parse(EminBridge.pluginDb.queryWithSql(sql))
			var arr=[];
			for(i=0;i<lg.length;i++){
				data=lg[i].name;
				arr.push(data);
			}
			var showWarehousePicker = new $.PopPicker();
			showWarehousePicker.setData(arr);
			var showWarehousePickerButton = doc.getElementById('showWarehousePicker');
			var userResult = doc.getElementById('userResult');
			showWarehousePickerButton.addEventListener('tap', function(event) {
				showWarehousePicker.show(function(items) {
					showWarehousePickerButton.innerText = items[0];
					//返回 false 可以阻止选择框的关闭
					//return false;
				});
			}, false);
		});
	})(mui, document);
	//选择仓库
			(function($, doc) {
				$.init();
				$.ready(function() {
					//普通示例
		var sql="select * from Customer";
			var lg = JSON.parse(EminBridge.pluginDb.queryWithSql(sql))
			var arr=[];
			for(i=0;i<lg.length;i++){
				data=lg[i].name;
				arr.push(data);
			}
			var showCustomerPicker = new $.PopPicker();
			showCustomerPicker.setData(arr);
			var showCustomerPickerButton = doc.getElementById('showCustomerPicker');
			var userResult = doc.getElementById('userResult');
			showCustomerPickerButton.addEventListener('tap', function(event) {
				showCustomerPicker.show(function(items) {
					showCustomerPickerButton.innerText = items[0];
						var name=items[0];
						for(j=0;j<lg.length;j++){
							if(name==lg[j].name){
								var address=lg[j].address;
								document.getElementById("address").value=address
							}
						}
					//返回 false 可以阻止选择框的关闭
					//return false;
				});
			}, false);
		});
	})(mui, document);
