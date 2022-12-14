<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加/修改device 维修信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <#include "${request.contextPath}/common/common.ftl">
    <link href="${request.contextPath}/css/effect.css" rel="stylesheet" type="text/css"/>

    <style>

    </style>

</head>
<body>
<div class="splayui-container">
    <div class="splayui-main">
        <form class="layui-form splayui-form" lay-filter="formTest">
            <div class="layui-row">
                <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">

                    <div class="layui-form-item">
                        <label for="js-device" class="layui-form-label sp-required">设备名称
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-device" name="device" lay-verify="required" autocomplete="off"
                                   class="layui-input" value="${result.device}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-errorDesc" class="layui-form-label ">故障描述
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-errorDesc" name="errorDesc"
                                   autocomplete="off" class="layui-input" value="${result.errorDesc}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-dealer" class="layui-form-label ">处理人
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-dealer" name="dealer"
                                   autocomplete="off" class="layui-input" value="${result.dealer}">
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-result" class="layui-form-label ">处理结果
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-result" name="result"
                                   autocomplete="off" class="layui-input" value="${result.result}">
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-badTime" class="layui-form-label ">故障时长(分钟)
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-badTime" name="badTime"
                                   autocomplete="off" class="layui-input" value="${result.badTime}">
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-flag" class="layui-form-label sp-required">设备状态
                        </label>
                        <div class="layui-input-block" id="js-flag" style="width: 310px;">
                            <input type="radio" name="flag" value="0" title="正常"
                                   <#if result.flag == "0" || !(result??)>checked</#if>>
                            <input type="radio" name="flag" value="1" title="故障"
                                   <#if result.flag == "1">checked</#if>>
                            <input type="radio" name="flag" value="2" title="维修中"
                                   <#if result.flag == "2">checked</#if>>
                            <input type="radio" name="flag" value="3" title="禁用"
                                   <#if result.flag == "3">checked</#if>>
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

        //监听提交
        form.on('submit(js-submit-filter)', function (data) {
            //alert( JSON.stringify(data.field));

            spUtil.submitForm({
                //contentType: 'application/json;charset=UTF-8',  //@RequestBody  要对应起来，contentType默认按 x-from 处理得
                url: "${request.contextPath}/device/maintain/add-or-update",
                data: data.field
            });
            return false;
        });

    });
</script>
</body>
</html>

