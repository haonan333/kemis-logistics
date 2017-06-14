/**
 * 所有上传导入页面需引入此 js, 简化操做
 * Created by xuhailong on 16/8/18.
 */
var importFileManager = {
    token:null,
    header:null,
    initPage : function (id) {
        Date.prototype.Format = function(fmt)
        {
            var o = {
                "M+" : this.getMonth()+1,                 //月份
                "d+" : this.getDate(),                    //日
                "h+" : this.getHours(),                   //小时
                "m+" : this.getMinutes(),                 //分
                "s+" : this.getSeconds(),                 //秒
                "q+" : Math.floor((this.getMonth()+3)/3), //季度
                "S"  : this.getMilliseconds()             //毫秒
            };
            if(/(y+)/.test(fmt))
                fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
            for(var k in o)
                if(new RegExp("("+ k +")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            return fmt;
        };
        var time = new Date().Format("yyyyMM");
        $("#"+id).val(time);
    },
    uploadInfo:function (formId,url) {
        var form = new FormData($("#"+formId)[0]);
        $.ajax({
            url:url,
            type:"post",
            data:form,
            processData:false,
            contentType:false,
            beforeSend:function (xhr) {
                xhr.setRequestHeader(importFileManager.header, importFileManager.token);
            },
            success:function(data){
                eval("data="+data);
                alert(data.data.msg);
            },
            error:function(e){
                alert("上传失败");
            }
        });
    }
};
