var controls = document.getElementById("segmentedControls");
			var contents = document.getElementById("segmentedControlContents");
			var html = [];
			var i = 1,
				j = 1,
				m = 16, //左侧选项卡数量+1
				n = 21; //每个选项卡列表数量+1
			for (; i < m; i++) {
				html.push('<a class="mui-control-item" href="#content' + i + '">选项' + i + '</a>');
			}
			controls.innerHTML = html.join('');
			html = [];
			for (i = 1; i < m; i++) {
				html.push('<div id="content' + i + '" class="mui-control-content"><ul class="mui-table-view">');
				for (j = 1; j < n; j++) {
					html.push('<li class="mui-table-view-cell">第' + i + '个选项卡子项-' + j + '</li>');
				}
				html.push('</ul></div>');
			}
			contents.innerHTML = html.join('');
			 //默认选中第一个
			controls.querySelector('.mui-control-item').classList.add('mui-active');
			contents.querySelector('.mui-control-content').classList.add('mui-active');