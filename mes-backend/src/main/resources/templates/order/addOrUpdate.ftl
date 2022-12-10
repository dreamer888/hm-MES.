<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加修改当日工单计划</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <#include "${request.contextPath}/common/common.ftl">
</head>

<body>
<div class="splayui-container">
    <div class="splayui-main">
        <form class="layui-form splayui-form"   lay-filter="formTest">
            <div class="layui-row">
                <#--================left side======================-->
                <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">
                    <div class="layui-form-item">
                        <label for="js-orderCode" class="layui-form-label sp-required">订单号
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-orderCode" name="orderCode" lay-verify="required" autocomplete="off" class="layui-input" value="${result.orderCode}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-flow" class="layui-form-label sp-required">工艺流程
                        </label>
                        <div class="layui-input-inline">
                            <select  id="js-flow" name="flow" lay-filter="flow-filter" lay-verify="required" autocomplete="off" class="layui-input" value="${result.flow}">
                            </select>
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-planQty" class="layui-form-label sp-required">计划数量
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-planQty" name="planQty" lay-verify="required" autocomplete="off" class="layui-input" value="${result.planQty}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-planStartTime" class="layui-form-label sp-required">计划开始时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-planStartTime" name="planStartTime" lay-verify="required" autocomplete="off" class="layui-input" value="${result.planStartTime}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-planEndTime" class="layui-form-label sp-required">计划结束时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-planEndTime" name="planEndTime" lay-verify="required" autocomplete="off" class="layui-input" value="${result.planEndTime}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-status" class="layui-form-label sp-required">状态
                        </label>
                        <div class="layui-input-inline">
                            <select  id="js-status" name="status" lay-filter="status-filter" lay-verify="required" autocomplete="off" class="layui-input" value="${result.status}">
                            </select>
                        </div>
                    </div>


                </div>

                <#--================右边======================-->

                <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">
                    <div class="layui-form-item">
                        <label for="js-qrcode" class="layui-form-label ">
                            二维码
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-qrcode" name="qrcode" lay-verify="" autocomplete="off" class="layui-input" value="${result.qrcode}">
                        </div>
                    </div>



                    <div class="layui-form-item">
                        <label for="js-makedQty" class="layui-form-label ">
                            已经生产
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-makedQty" name="makedQty" lay-verify=""  readonly="true" autocomplete="off" class="layui-input" value="${result.makedQty}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-badQty" class="layui-form-label ">
                            不合格品
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-badQty" name="badQty" lay-verify="" readonly="true" autocomplete="off" class="layui-input" value="${result.badQty}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-passRate" class="layui-form-label ">
                            一次通过率
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-passRate" name="passRate" readonly="true" lay-verify="" autocomplete="off" class="layui-input" value="${result.passRate}">
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-finishRate" class="layui-form-label ">
                            订单完成率
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-finishRate" name="finishRate"   lay-verify="" autocomplete="off" class="layui-input" value="${result.finishRate}">
                        </div>
                    </div>



                    <div class="layui-form-item">
                        <label for="js-memo" class="layui-form-label ">
                            备注
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-memo" name="memo" lay-verify="" autocomplete="off" class="layui-input" value="${result.memo}">
                        </div>
                    </div>

                    <#--
                    <div class="layui-form-item">
                        <label for="js-is-deleted" class="layui-form-label sp-required">状态</label>
                        <div class="layui-input-block" id="js-is-deleted">
                            <input type="radio" name="deleted" value="0" title="正常" <#if result.deleted == "0" || !(result??)>checked</#if>>
                            <input type="radio" name="deleted" value="1" title="已删除" <#if result.deleted == "1">checked</#if>>
                            <input type="radio" name="deleted" value="2" title="已禁用" <#if result.deleted == "2">checked</#if>>
                        </div>
                    </div>-->


                </div>



                <div class="layui-form-item layui-hide">
                    <div class="layui-input-block">
                        <input id="js-id" name="id" value="${result.id}"/>
                        <button id="js-submit" class="layui-btn" lay-submit lay-filter="js-submit-filter">确定</button>
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
            //return false;

            spUtil.submitForm({
                url: "${request.contextPath}/order/add-or-update",
                data: data.field
            });

            return false;
        });

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
                        $('#js-flow').append(new Option(item.flow, item.flowDesc));
                    });
                    //document.getElementById("js-line")[0].selected=true;
                }
            });

        }
        function getStatusData() {
            spUtil.ajax({
                url: '${request.contextPath}/order/status',
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
                        $('#js-status').append(new Option(item, item));
                    });
                    //document.getElementById("js-status")[0].selected=true;
                    if(document.getElementById("js-status").value == "")
                        document.getElementById("js-status").value ="创建";
                }

            });

        }

        //"yyyy-MM-dd-hh-mm-ss".replace(/-/g,"/")
        //3,000

        function handleNumber() {
            $("#js-planQty").value= $("#js-planQty").value.replace(/,/g,"");
            $("#js-makedQty").value= $("#js-makedQty").value.replace(/,/g,"");

        }
        getStatusData();
        getFlowData();

        //给表单赋值
        form.val("formTest", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
            "status": "${result.status}",
            "flow": "${result.flow}"
        });

    });





    window.onload=function(){
        document.getElementById("js-makedQty").value = document.getElementById("js-makedQty").value.replace(/,/g,"");
        document.getElementById("js-planQty").value = document.getElementById("js-planQty").value.replace(/,/g,"");
        document.getElementById("js-badQty").value = document.getElementById("js-badQty").value.replace(/,/g,"");


        //alert("status="+document.getElementById("js-status").value ) ;

    }

</script>
</body>
</html>