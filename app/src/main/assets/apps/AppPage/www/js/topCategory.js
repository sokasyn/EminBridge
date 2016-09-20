var sql="select * from product where pid = '0'";
var topCategoryName = JSON.parse(EminBridge.pluginDb.queryWithSql(sql))

for(j=0;j<4;j++){
    data=topCategoryName[j].name;
    var html='<a class="mui-control-item" href="#item'+(j+1)+'mobile">'+data+'</a>';
    alert(html)
    document.getElementById("sliderSegmentedControl").innerHTML+=html;
}


/*
(function($) {
    $('.mui-scroll-wrapper').scroll({
        indicators: true //是否显示滚动条
    });
    var html1='<ul class="mui-table-view"><li class="mui-table-view-cell">第一个选项卡子项-1</li><li class="mui-table-view-cell">第一个选项卡子项-2</li><li class="mui-table-view-cell">第一个选项卡子项-3</li><li class="mui-table-view-cell">第一个选项卡子项-4</li><li class="mui-table-view-cell">第一个选项卡子项-5</li></ul>';
    var html2 = '<ul class="mui-table-view"><li class="mui-table-view-cell">第二个选项卡子项-1</li><li class="mui-table-view-cell">第二个选项卡子项-2</li><li class="mui-table-view-cell">第二个选项卡子项-3</li><li class="mui-table-view-cell">第二个选项卡子项-4</li><li class="mui-table-view-cell">第二个选项卡子项-5</li></ul>';
    var html3 = '<ul class="mui-table-view"><li class="mui-table-view-cell">第三个选项卡子项-1</li><li class="mui-table-view-cell">第三个选项卡子项-2</li><li class="mui-table-view-cell">第三个选项卡子项-3</li><li class="mui-table-view-cell">第三个选项卡子项-4</li><li class="mui-table-view-cell">第三个选项卡子项-5</li></ul>';
    var html4 = '<ul class="mui-table-view"><li class="mui-table-view-cell">第四个选项卡子项-1</li><li class="mui-table-view-cell">第四个选项卡子项-2</li><li class="mui-table-view-cell">第四个选项卡子项-3</li><li class="mui-table-view-cell">第四个选项卡子项-4</li><li class="mui-table-view-cell">第四个选项卡子项-5</li></ul>';

    var item1 = document.getElementById('item1mobile');
    var item2 = document.getElementById('item2mobile');
    var item3 = document.getElementById('item3mobile');
    var item4 = document.getElementById('item4mobile');
    document.getElementById('slider').addEventListener('slide', function(e) {
        if (e.detail.slideNumber === 1) {
            if (item2.querySelector('.mui-loading')) {
                setTimeout(function() {
                    item2.querySelector('.mui-scroll').innerHTML = html2;
                }, 500);
            }
        } else if (e.detail.slideNumber === 2) {
            if (item3.querySelector('.mui-loading')) {
                setTimeout(function() {
                    item3.querySelector('.mui-scroll').innerHTML = html3;
                }, 500);
            }
        }else if (e.detail.slideNumber === 3) {
            if (item4.querySelector('.mui-loading')) {
                setTimeout(function() {
                    item4.querySelector('.mui-scroll').innerHTML = html4;
                }, 500);
            }
        }else if (e.detail.slideNumber === 0) {
            if (item1.querySelector('.mui-loading')) {
                setTimeout(function() {
                    item1.querySelector('.mui-scroll').innerHTML = html1;
                }, 500);
            }
        }
    });

})(mui);*/
