<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加/修改产品</title>
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
                <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">
                    <div class="layui-form-item">
                        <label for="js-qrcode" class="layui-form-label ">二维码/条形码
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-qrcode" name="qrcode" lay-verify="" autocomplete="off"
                                   class="layui-input" value="${result.qrcode}"     autofocus="autofocus"
                                   oninput="OnQrcodeInput()"
                                   style="width:300px;"
                            >

                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-productId" class="layui-form-label sp-required">产品编码
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-productId" name="productId" lay-verify="required" autocomplete="off"
                                   class="layui-input"  style="width:300px;"   value="${result.productId}"       >
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-productDesc" class="layui-form-label ">产品描述
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-productDesc" name="name"
                                   autocomplete="off" class="layui-input" value="${result.name}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-bomCode" class="layui-form-label ">BOM编码
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-bomCode" name="bomCode" autocomplete="off"
                                   class="layui-input" value="${result.bomCode}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-batchNo" class="layui-form-label ">批次
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-batchNo" name="batchNo" lay-verify=""
                                   autocomplete="off"
                                   class="layui-input" value="${result.batchNo}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-orderNo" class="layui-form-label ">订单编码
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-orderNo" name="orderNo" lay-verify=""
                                   autocomplete="off"
                                   class="layui-input" value="${result.orderNo}">
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-flowId" class="layui-form-label ">工艺流程
                        </label>
                        <div class="layui-input-inline">
                            <select id="js-flowId" name="flowId" lay-filter="flow-filter">
                            </select>
                        </div>
                        <div class=" text-effect flowProcss  " id="js-flowProcess" name="flowProcss">
                        </div>
                    </div>

                    <#--<div class="layui-form-item">
                        <label for="js-is-deleted" class="layui-form-label ">状态
                        </label>
                        <div class="layui-input-block" id="js-is-deleted" style="width: 310px;">
                            <input type="radio" name="deleted" value="0" title="正常"
                                   <#if result.deleted == "0" || !(result??)>checked</#if>>
                            <input type="radio" name="deleted" value="1" title="已删除"
                                   <#if result.deleted == "1">checked</#if>>
                            <input type="eradio" name="deleted" value="2" title="已禁用"
                                   <#if result.deleted == "2">checked</#if>>
                        </div>
                    </div>-->


                </div>



                <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">

                        <div class="layui-form-item">
                            <label for="js-quality" class="layui-form-label ">生产品质
                            </label>
                            <div class="layui-input-inline">
                                <input type="text" id="js-quality" name="quality" lay-verify=""
                                       autocomplete="off"
                                       class="layui-input" value="${result.quality}">
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label for="js-badPos" class="layui-form-label ">生产工位
                            </label>
                            <div class="layui-input-inline">
                                <input type="text" id="js-badPos" name="badPos" lay-verify=""
                                       autocomplete="off"
                                       class="layui-input" value="${result.badPos}">
                            </div>
                        </div>



                        <div class="layui-form-item">
                        <label for="js-createTime" class="layui-form-label ">生产日期
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-createTime" name="createTime" lay-verify=""
                                   autocomplete="off"
                                   class="layui-input" value="${result.createTime}">
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
        //流程添加下拉框
        getFlowData();

        function OnQrcodeInput111() {
           alert("fff") ;
           // $('#js-materiel').value= $('#js-qrcode').value;
            document.getElementById('js-materiel').value =document.getElementById('js-qrcode').value;
        }

        $('#js-qrcode').on('input',e=> {
            //$('#js-materiel').value= e.delegateTarget.value;
            var  temp = e.delegateTarget.value;
            var arr=  temp.split("#");
            var len = arr.length;
            if(len>=4) {
                document.getElementById('js-productId').value = arr[0];
                document.getElementById('js-batchNo').value = arr[1];
                document.getElementById('js-orderNo').value = arr[3];
            }
        });


        $('#js-qrcode').focus();

        /**
         * 初始化流程数据
         */
        function getFlowData() {
            spUtil.ajax({
                url: '${request.contextPath}/basedata/flow/list',
                async: false,
                type: 'GET',
                // 是否显示 loading
                showLoading: true,
                // 是否序列化参数
                serializable: false,
                // 参数
                data: {},
                success: function (data) {
                    flowRows = data.data;
                }
            });

            $.each(flowRows, function (index, item) {
                $('#js-flowId').append(new Option(item.flowDesc, item.id));
            });
            //编辑时候根据回显的ID 绘制流程
            flowProssbyId("${result.flowId}")
        }

        //下拉框选择 绘制流程时序图
        form.on('select(flow-filter)', function (data) {
            flowProssbyId(data.value)
        });

        //通过ID 获取流程时序 绘制
        function flowProssbyId(flowId) {
            var newArr = flowRows.filter(function (obj) {
                return obj.id == flowId;
            });
            if (newArr.length > 0) {
                procssArr = newArr[0].process.split("->")
                $("#js-flowProcess").empty();
                $.each(procssArr, function (i, val) {

                    if (i == procssArr.length - 1) {
                        $("#js-flowProcess").append("<span style='display: inline-flex;' >" + val + "</span>");
                    } else {
                        $("#js-flowProcess").append("<span style='display: inline-flex;' >" + val + '->' + "</span>");
                    }
                });
            }
        }

        //给表单赋值
        form.val("formTest", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
            "flowId": "${result.flowId}",
            "createTime":  handleCreateTime("${result.createTime}")
        });

        function  handleCreateTime(createTime)
        {
            createTime= createTime.replace("T"," ");
            return createTime;

        }
        //监听提交
        form.on('submit(js-submit-filter)', function (data) {
            //alert( JSON.stringify(data.field));

            spUtil.submitForm({
                url: "${request.contextPath}/product/add-or-update",
                data: data.field
            });
            return false;
        });

    });
</script>
</body>
</html>

