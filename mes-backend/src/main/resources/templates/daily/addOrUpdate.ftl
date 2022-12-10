<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加/修改计划</title>
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

                <#---------左边------>
                <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">


                    <div class="layui-form-item">
                        <label for="js-orderCode" class="layui-form-label ">订单编码
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-orderCode" name="orderCode" lay-verify="" autocomplete="off"
                                   class="layui-input" value="${result.orderCode}"     autofocus="autofocus" >

                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="js-productId" class="layui-form-label sp-required">目标日期
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-planDate" name="planDate" lay-verify="required" autocomplete="off"
                                   class="layui-input" value="${result.planDate}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-planQty" class="layui-form-label ">日计划产量
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-planQty" name="planQty"
                                   autocomplete="off" class="layui-input" value="${result.planQty}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-morningStart" class="layui-form-label ">早班开始时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-morningStart" name="morningStart"
                                   autocomplete="off" class="layui-input" value="${result.morningStart}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-morningEnd" class="layui-form-label ">早班结束时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-morningEnd" name="morningEnd"
                                   autocomplete="off" class="layui-input" value="${result.morningEnd}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-morningStart1" class="layui-form-label ">早班1开始时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-morningStart" name="morningStart1"
                                   autocomplete="off" class="layui-input" value="${result.morningStart1}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-morningEnd1" class="layui-form-label ">早班1结束时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-morningEnd1" name="morningEnd1"
                                   autocomplete="off" class="layui-input" value="${result.morningEnd1}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-afternoonStart" class="layui-form-label ">中班开始时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-afternoonStart" name="afternoonStart"
                                   autocomplete="off" class="layui-input" value="${result.afternoonStart}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-afternoonEnd" class="layui-form-label ">中班结束时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-afternoonEnd" name="afternoonEnd"
                                   autocomplete="off" class="layui-input" value="${result.afternoonEnd}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-afternoonStart1" class="layui-form-label ">中班1开始时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-afternoonStart1" name="afternoonStart1"
                                   autocomplete="off" class="layui-input" value="${result.afternoonStart1}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-afternoonEnd1" class="layui-form-label ">中班1结束时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-afternoonEnd1" name="afternoonEnd1"
                                   autocomplete="off" class="layui-input" value="${result.afternoonEnd1}">
                        </div>
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
                            <input type="radio" name="deleted" value="2" title="已禁用"
                                   <#if result.deleted == "2">checked</#if>>
                        </div>
                    </div>-->

                <#--===右边==-->

                <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">

                <div class="layui-form-item">
                    <label for="js-eveningStart" class="layui-form-label ">晚班开始时间
                    </label>
                    <div class="layui-input-inline">
                        <input type="datetime-local" id="js-eveningStart" name="eveningStart"
                               autocomplete="off" class="layui-input" value="${result.eveningStart}">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label for="js-eveningEnd" class="layui-form-label ">晚班结束时间
                    </label>
                    <div class="layui-input-inline">
                        <input type="datetime-local" id="js-eveningEnd" name="eveningEnd"
                               autocomplete="off" class="layui-input" value="${result.eveningEnd}">
                    </div>
                </div>
                    <div class="layui-form-item">
                        <label for="js-eveningStart1" class="layui-form-label ">晚班1开始时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-eveningStart1" name="eveningStart1"
                                   autocomplete="off" class="layui-input" value="${result.eveningStart1}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-eveningEnd1" class="layui-form-label ">晚班1结束时间
                        </label>
                        <div class="layui-input-inline">
                            <input type="datetime-local" id="js-eveningEnd1" name="eveningEnd1"
                                   autocomplete="off" class="layui-input" value="${result.eveningEnd1}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-pieceTime" class="layui-form-label ">标定每件耗时(s)
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-pieceTime" name="pieceTime" autocomplete="off"
                                     class="layui-input" value="${result.pieceTime}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label for="js-realPieceTime" class="layui-form-label ">实际每件耗时(s)
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-realPieceTime" name="realPieceTime" autocomplete="off"
                                   readonly="true"  class="layui-input" value="${result.realPieceTime}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-makedQty" class="layui-form-label ">实际产量
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-makedQty" name="makedQty"
                                   readonly="true"   autocomplete="off" class="layui-input" value="${result.makedQty}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-badQty" class="layui-form-label ">次品数量
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-badQty" name="badQty" lay-verify=""
                                   autocomplete="off"  readonly="true"
                                   class="layui-input" value="${result.badQty}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-finishRate" class="layui-form-label ">完成率
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-finishRate" name="finishRate" lay-verify=""
                                   autocomplete="off"  readonly="true"
                                   class="layui-input" value="${result.finishRate}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="js-passRate" class="layui-form-label ">一次性通过率
                        </label>
                        <div class="layui-input-inline">
                            <input type="text" id="js-passRate" name="passRate" lay-verify=""
                                   autocomplete="off"  readonly="true"
                                   class="layui-input" value="${result.passRate}">
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
        //getFlowData();

        $('#js-eveningStart').on('input',e=> {
            //$('#js-materiel').value= e.delegateTarget.value;
            var  temp = e.delegateTarget.value;

            //document.getElementById('js-pieceTime').value = arr[0];
        });


        $('#js-orderCode').focus();


        //给表单赋值
        form.val("formTest", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
            "flowId": "${result.flowId}"
        });

        //监听提交
        form.on('submit(js-submit-filter)', function (data) {
            //alert( JSON.stringify(data.field));

            spUtil.submitForm({
                url: "${request.contextPath}/daily/plan/add-or-update",
                data: data.field
            });
            return false;
        });

    });


    window.onload=function(){
        document.getElementById("js-makedQty").value = document.getElementById("js-makedQty").value.replace(/,/g,"");
        $("#js-planQty")[0].value = document.getElementById("js-planQty").value.replace(/,/g,"");
        document.getElementById("js-badQty").value = document.getElementById("js-badQty").value.replace(/,/g,"");


        //alert("status="+document.getElementById("js-status").value ) ;

    }
</script>
</body>
</html>

