<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>维护BOM信息</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <#include "${request.contextPath}/common/common.ftl">
</head>
<body>
<div class="splayui-container">
    <div class="splayui-main">
        <form class="layui-form" style="margin: 0 auto;max-width: 460px;padding-top: 40px;">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="js-bomCode" class="layui-form-label sp-required">BOM编码
                    </label>
                    <div class="layui-input-inline">
                        <input type="text" id="js-bomCode" name="bomCode" lay-verify="required"
                               autocomplete="off"
                               class="layui-input" value="${result.bomCode}">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="js-materielCode" class="layui-form-label sp-required">物料编码
                </label>
                <div style="display: flex;   flex-direction: row;">
                    <button type="button" id="js-test-btn" class="layui-btn" style="height:37px "><i
                                class="layui-icon layui-icon-search "></i>
                    </button>
                    <input id="js-materiel-code" name="materielCode"  lay-verify="required"
                           placeholder="搜索物料" autocomplete="off"
                           value="${result.materielCode}"
                           class="layui-input" style="width: 133PX">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="js-materielName" class="layui-form-label sp-required">物料名称
                    </label>
                    <div class="layui-input-inline">
                        <input id="js-materiel-name"    name="materielDesc" lay-verify="required"
                               autocomplete="off"
                               class="layui-input" value="${result.materielDesc}">
                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label for="js-version-number" class="layui-form-label sp-required">版本号
                    </label>
                    <div style="display: flex;   flex-direction: row;">
                        <input type="text" id="js-versionNumber" readonly="true" name="versionNumber"
                               lay-verify="required"
                               autocomplete="off"
                               class="layui-input"
                               {value="${result.versionNumber}" | value='1' style=" width: 163PX;" }>
                        <div style="display: flex;   flex-direction: column;">
                            <div style="display: flex">
                                <button onclick="FN('plus')" type="button" style="height: 19PX"
                                        class="layui-btn  layui-btn-xs"><i
                                            class="layui-icon layui-icon-up"></i>
                                </button>
                            </div>
                            <div style="display: flex">
                                <button onclick="FN('minus')" type="button" style="height: 19PX"
                                        class="layui-btn  layui-btn-xs"><i
                                            class="layui-icon layui-icon-down"></i>
                                </button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="js-is-deleted" class="layui-form-label sp-required">状态
                </label>
                <div class="layui-input-block" id="js-is-deleted" style="width: 310px;">
                    <input type="radio" name="deleted" value="0" title="正常"
                           <#if result.deleted == "0" || !(result??)>checked</#if>>
                    <input type="radio" name="deleted" value="1" title="已删除"
                           <#if result.deleted == "1">checked</#if>>
                    <input type="radio" name="deleted" value="2" title="已禁用"
                           <#if result.deleted == "2">checked</#if>>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">备注说明:</label>
                <div class="layui-input-block">
                    <textarea placeholder="BOM备注" name="remark" value="${result.remark}"
                              class="layui-textarea"></textarea>
                </div>
            </div>
            <div class="layui-form-item layui-hide">
                <div class="layui-input-block">
                    <input id="js-id" name="id" value="${result.id}"/>
                    <button id="js-submit" class="layui-btn" lay-demotransferactive="getData" lay-submit
                            lay-filter="js-submit-filter">确定
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    layui.use(['form', 'util', 'layer', 'step', 'spLayer', 'spTable', 'table'], function () {
        var form = layui.form,
            spLayer = layui.spLayer,
            layer = layui.layer,
            table = layui.table;
        //监听提交
        form.on('submit(js-submit-filter)', function (data) {
            spUtil.submitForm({
                url: "${request.contextPath}/technology/bom/add-or-update",
                data: data.field
            });
            return false;
        });

        /**
         * 监听行工具事件
         */
        table.on('tool(js-record-table-filter)', function (obj) {
            // 编辑
            if (obj.event === 'delete') {
                layer.confirm('确认要删除吗？', function (index) {
                    obj.del();
                    layer.close(index);
                })
            }
        })

        // 物料主数据搜索弹框
        $('#js-test-btn').click(function () {
            searchMaterile();
        });
        /**
         * 物料搜索功能
         * @param obj
         */
        window.searchMaterile = function (obj) {
            spLayer.open({
                type: 2,
                area: ['680px', '500px'],
                reload: false,
                content: '${request.contextPath}/admin/common/ui/searchPanelMaterile',
                // 如果是搜索弹窗，需要添加回调函数来获取选中数据
                spCallback: function (result) {
                    if (result.code === 0 && result.data.length > 0) {
                        console.log(result);
                        $('#js-materiel-code').val(result.data[0].materiel);
                        $('#js-materiel-name').val(result.data[0].materielDesc);

                    }
                }
            });
        }
    });

    //版本号
    function FN(btnType) {
        var versionNumber = $('#js-versionNumber')
        if (btnType == 'plus') {
            versionNumber.val(parseInt(versionNumber.val()) + 1);
        } else if (btnType == 'minus') {

            versionNumber.val(parseInt(versionNumber.val()) - 1);
            if (parseInt(versionNumber.val()) <= 0) {
                versionNumber.val('1')
                layer.alert('版本号最小为1', {
                    icon: 2
                });
            }
        }
    }

</script>
</body>
</html>