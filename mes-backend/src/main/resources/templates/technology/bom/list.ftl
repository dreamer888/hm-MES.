<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>bom 管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <#include "${request.contextPath}/common/common.ftl">
</head>
<body>
<div class="splayui-container">
    <div class="splayui-main">
        <!--查询参数-->
        <form id="js-search-form" class="layui-form" lay-filter="js-q-form-filter">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">BOM编号</label>
                    <div class="layui-input-inline">
                        <input type="text" name="bomCodeLike" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <a class="layui-btn" lay-submit lay-filter="js-search-filter"><i
                                class="layui-icon layui-icon-search layuiadmin-button-btn"></i></a>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label">物料编号</label>
                    <div class="layui-input-inline">
                        <input type="text" name="materielCodeLike" autocomplete="off" class="layui-input">
                    </div>
                </div>

            </div>
        </form>

        <!--表格-->
        <table class="layui-hide" id="js-record-table" lay-filter="js-record-table-filter"></table>
    </div>
</div>

<!--表格头操作模板-->
<script type="text/html" id="js-record-table-toolbar-top">
    <div class="layui-btn-container">
        <@shiro.hasPermission name="user:add">
            <button class="layui-btn layui-btn-sm" lay-event="add"><i class="layui-icon">&#xe61f;</i>添加</button>
        </@shiro.hasPermission>
        <button class="layui-btn layui-btn-sm" lay-event="getCheckData">获取选中行数据</button>
    </div>
</script>

<!--行操作模板-->
<script type="text/html" id="js-record-table-toolbar-right">
    <a class="layui-btn layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete"><i class="layui-icon layui-icon-delete"></i>删除</a>
</script>

<!--js逻辑-->
<script>
    layui.use(['form', 'table', 'spLayer', 'spTable', 'spLayui'], function () {
        var form = layui.form,
            table = layui.table,
            spLayer = layui.spLayer,
            spLayui = layui.spLayui,
            spTable = layui.spTable;

        // 表格及数据初始化
        var tableIns = spTable.render({
            url: '${request.contextPath}/technology/bom/page',
            cols: [
                [{
                    type: 'radio'
                }, {
                    field: 'bomCode', title: 'bom编号'
                }, {
                    field: 'materielCode', title: '物料编号'
                }, {
                    field: 'materielDesc', title: '物料名称'
                }, {
                    field: 'remark', title: '备注'
                }, {
                    field: 'deleted', title: '状态', templet: function (d) {
                        return spConfig.isDeletedDict[d.deleted];
                    }
                }, {
                    fixed: 'right',
                    field: 'operate',
                    title: '操作',
                    toolbar: '#js-record-table-toolbar-right',
                    unresize: true,
                    width: 150
                }]
            ],
            done: function (res, curr, count) {
            }
        });

        /*
         * 数据表格中form表单元素是动态插入,所以需要更新渲染下
         * http://www.layui.com/doc/modules/form.html#render
         */
        $(function () {
            form.render();
        });

        /**
         * 搜索按钮事件
         */
        form.on('submit(js-search-filter)', function (data) {
            tableIns.reload({
                where: data.field,
                page: {
                    // 重新从第 1 页开始
                    curr: 1
                }
            });

            // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
            return false;
        });

        /**
         * 头工具栏事件
         */
        table.on('toolbar(js-record-table-filter)', function (obj) {

            if (obj.event === 'getCheckData') {
                var checkStatus = table.checkStatus(obj.config.id);
                var data = checkStatus.data;  //获取选中行数据
                layer.alert(JSON.stringify(data));
            }

            // 批量删除
            if (obj.event === 'deleteBatch') {
                var checkStatus = table.checkStatus('record-table'),
                    data = checkStatus.data;
                if (data.length > 0) {
                    layer.confirm('确认要删除吗？', function (index) {

                    });
                } else {
                    layer.msg("请先选择需要删除的数据！");
                }
            }
            // 添加
            if (obj.event === 'add') {
                var index = spLayer.open({
                    title: '添加',
                    area: ['70%', '90%'],
                    content: '${request.contextPath}/technology/bom/add-or-update-ui'
                });
                //新开一个TAB标签 url  标题名称 图标
                //spLayui.createTableItem('/technology/bom/add-or-update-ui?mpi=bom-1','BOM信息','fa fa-dropbox');
            }
        });

        /**
         * 监听行工具事件
         */
        table.on('tool(js-record-table-filter)', function (obj) {
            var data = obj.data;

            // 编辑
            if (obj.event === 'edit') {
                spLayer.open({
                    title: '编辑',
                    area: ['80%', '90%'],
                    // 请求url参数
                    spWhere: {id: data.id},
                    content: '${request.contextPath}/technology/bom/add-or-update-ui'
                });
            }

            // 删除
            if (obj.event === 'delete') {
                layer.confirm('确认要删除吗？', function (index) {
                    spUtil.ajax({
                        url: '${request.contextPath}/technology/bom/delete',
                        async: false,
                        type: 'POST',
                        // 是否显示 loading
                        showLoading: true,
                        // 是否序列化参数
                        serializable: false,
                        // 参数
                        data: {
                            id: data.id
                        },
                        success: function (data) {
                            tableIns.reload();
                            layer.close(index);
                        },
                        error: function () {
                        }
                    });
                });
            }
        });
    });
</script>
</body>
</html>