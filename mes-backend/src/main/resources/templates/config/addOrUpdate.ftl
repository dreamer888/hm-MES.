<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加/修改通讯配置</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <#include "${request.contextPath}/common/common.ftl">
    <link href="${request.contextPath}/css/effect.css" rel="stylesheet" type="text/css"/>
    <style>
        .flowProcss {
            font-size: 1.6rem;
            margin-left: 310PX;
            display: flex;
            justify-content: flex-start;
            flex-direction: row;
        }

    </style>

</head>
<body>
<div class="splayui-container">
    <div class="splayui-main">
        <form class="layui-form splayui-form" lay-filter="formTest">
            <div class="layui-row">
                <div class="layui-col-xs10 layui-col-sm10 layui-col-md10">

                    <div class="layui-form-item">
                        <label for="js-lineId" class="layui-form-label sp-required">工艺流程
                        </label>
                        <div class="layui-input-inline">
                            <select  id="js-lineId" name="lineId" lay-filter="lineId-filter" lay-verify="required" autocomplete="off" class="layui-input" value="${result.lineId}">
                            </select>
                        </div>
                    </div>


                    </div>


                    <div class="layui-form-item">
                        <label for="js-serverIp" class="layui-form-label sp-required">server ip
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-serverIp" name="serverIp"   autocomplete="off"
                                   class="layui-input" value="${result.serverIp}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-serverPort" class="layui-form-label ">server port
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-serverPort" name="serverPort"
                                   autocomplete="off" class="layui-input" value="${result.serverPort}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-interv" class="layui-form-label ">采集间隔(s)
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-interv" name="interv" autocomplete="off"
                                   class="layui-input" value="${result.interv}">
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-type" class="layui-form-label ">通讯类型
                        </label>
                        <div class="layui-input-inline">
                            <select id="js-type" name="type" lay-filter="type-filter">
                            </select>
                        </div>

                    </div>



                    <div class="layui-form-item">
                        <label for="js-useComm" class="layui-form-label ">是否启用串口
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-useComm" name="useComm" lay-verify=""
                                   autocomplete="off"
                                   class="layui-input" value="${result.useComm}">
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-bandrate" class="layui-form-label ">波特率
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-bandrate" name="bandrate" lay-verify=""
                                   autocomplete="off"
                                   class="layui-input" value="${result.bandrate}">
                        </div>
                    </div>

                </div>


                <div class="layui-form-item layui-hide">
                    <div class="layui-input-block">
                        <input id="js-id" name="id" value="${result.id}"/>
                        <button id="js-submit" class="layui-btn" lay-submit lay-filter="js-submit-filter">确定
                        </button>
                    </div>
                </div>
            </div>

        </form>
    </div>
</div>
<script>
    layui.use(['form', 'util'], function () {
        var form = layui.form,
            util = layui.util;
        var flowRows = [];
        var typeRows = [];

        //添加下拉框
        getFlowData();
        geTypeData();

        $('#js-flow').focus();

        /**
         * 初始化line数据
         */
        function getFlowData() {
            spUtil.ajax({
                url: '${request.contextPath}/basedata/flow/list',
                async: false,
                type: 'GET',
                // 是否显示 loading
                // showLoading: true,
                // 是否序列化参数
                serializable: false,
                // 参数
                data: {},
                success: function (data) {
                    $.each(data.data, function (index, item) {
                        $('#js-lineId').append(new Option(item.flow, item.flow));
                    });
                    //document.getElementById("js-line")[0].selected=true;
                }
            });

        }
        /**
         * 初始化type数据
         */
        function geTypeData() {
            spUtil.ajax({
                url: '${request.contextPath}/config/type/list',
                async: false,
                type: 'GET',
                // 是否显示 loading
                showLoading: true,
                // 是否序列化参数
                serializable: false,
                // 参数
                data: {},
                success: function (data) {
                    typeRows = data.data;

                    $.each(typeRows, function (index, item) {
                        $('#js-type').append(new Option(item, item));
                    });

                }
            });


        }


/*
        function  dealDigtal () {
            var reg = new RegExp(",","g");
            var ss=  document.getElementById("serverPort").value;//.innerHTML ;

            alert("br="+document.getElementById("bandrate").value) ;
            document.getElementById("serverPort").value = ss.replace(reg,"");
            alert("ss="+ss);
            ss=  document.getElementById("bandrate").value;//.innerHTML ;
            document.getElementById("bandrate").value = ss.replace(reg,"");
        }

        dealDigtal();

*/

        //给表单赋值
        form.val("formTest", {
            //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
            "lineId": "${result.lineId}",
            "type": "${result.type}"
        });




        //监听提交
        form.on('submit(js-submit-filter)', function (data) {
            //alert( JSON.stringify(data.field));

            spUtil.submitForm({
                url: "${request.contextPath}/config/add-or-update",
                data: data.field
            });
            return false;
        });

    });

/*
    window.onload = function () {
        var reg = new RegExp(",","g");
        var ss=  document.getElementById("serverPort").value;//.innerHTML ;

        alert("br="+document.getElementById("bandrate").value) ;
        document.getElementById("serverPort").value = ss.replace(reg,"");
        alert("ss="+ss);
        ss=  document.getElementById("bandrate").value;//.innerHTML ;
        document.getElementById("bandrate").value = ss.replace(reg,"");
    }

*/


</script>
</body>
</html>

