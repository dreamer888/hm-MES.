<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加/修改操作</title>
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
                        <label for="js-oper" class="layui-form-label sp-required">操作
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-oper" name="oper" lay-verify="required" autocomplete="off"
                                   class="layui-input" value="${result.oper}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-operDesc" class="layui-form-label ">操作描述
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-operDesc" name="operDesc"
                                   autocomplete="off" class="layui-input" value="${result.operDesc}">
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
                url: "${request.contextPath}/basedata/operation/add-or-update",
                data: data.field
            });
            return false;
        });

    });
</script>
</body>
</html>

