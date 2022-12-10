<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>工单计划界面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <#include "${request.contextPath}/common/common.ftl">
    <link rel="stylesheet" href="${request.contextPath}/lib/gantt/css/style.css" media="all">

    <style type="text/css">
        body {
            font-family: Helvetica, Arial, sans-serif;
            font-size: 13px;
            padding: 0 0 50px 0;
        }

        .contain {
            width: 800px;
            margin: 0 auto;
        }
    </style>
</head>
<body>
<div class="splayui-container">
    <div class="splayui-main">
        <!--查询参数-->
        <form id="js-search-form" class="layui-form" lay-filter="js-q-form-filter">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <a class="layui-btn " lay-submit lay-filter="js-add-order"><i
                                class="layui-icon ">&#xe608</i>新建生产订单</a>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">订单编号</label>
                    <div class="layui-input-inline">
                        <input type="orderCode" name="orderCode"  id="orderCode"  autocomplete="off" class="layui-input">
                    </div>
                </div>


                <div class="layui-inline">
                    <label or="js-status"   class="layui-form-label">订单状态</label>
                    <div class="layui-input-inline">
                        <select id="js-status" name="status"   οnchange="getvalue(this)"   lay-filter="js-status-filter" lay-verify="" >
                        </select>
                    </div>
                </div>

                <#--<div class="layui-inline">
                    <label class="layui-form-label">订单状态</label>
                    <div class="layui-input-inline">
                        <input type="status" name="status"  id="js_status"  autocomplete="off" class="layui-input">
                    </div>
                </div>
-->
                <div class="layui-inline">
                    <a class="layui-btn" lay-submit lay-filter="js-search-filter"><i
                                class="layui-icon layui-icon-search layuiadmin-button-btn"></i></a>
                </div>
            </div>
        </form>

        <!--表格-->
        <#--        <table class="layui-hide" id="js-record-table" lay-filter="js-record-table-filter"></table>-->
        <div id="js-gantt" class="gantt"></div>
    </div>
</div>


<!--js逻辑-->
<script src="${request.contextPath}/lib/gantt/js/jquery.fn.gantt.js" charset="utf-8"></script>

<script>
    layui.use(['form', 'table', 'spLayer', 'spTable'], function () {
        var ganttData = [];
        // 获取数据
        spUtil.ajax({
            url: '${request.contextPath}/order/gantt/list',
            async: false,
            type: 'POST',
            // 是否序列化参数
            serializable: false,
            // 参数
            data: {},
            success: function (data) {
                ganttData = data.data;
            },
            error: function () {
            }
        });

        //初始化gantt
        var $gantt = $("#js-gantt").gantt({
            source: ganttData,
            navigate: 'scroll',//buttons  scroll
            scale: "days",// months  weeks days  hours
            maxScale: "months",
            minScale: "days",
            waitText: "加载中...",
            itemsPerPage: 14,
            tnTitle1: '工单编码',
            tnTitle2: '计划/实际',
            onItemClick: function (data) {
                modifyPlan(data);
            },
            onAddClick: function (dt, rowId) {
                //alert("add a record") ;
                console.log(dt)
                console.log(rowId)
                console.log("onAddClick");
            },
            onRender: function () {
                console.log('onRender');
            }
        });

        var form = layui.form,
            table = layui.table,
            spLayer = layui.spLayer,
            spTable = layui.spTable;


        /**
         * 修改
         * @param
         */
        function modifyPlan(id) {
            //alert('id=' +id) ;
            var index = spLayer.open({
                title: '修改',
                area: ['90%', '90%'],
                // 请求url参数
                spWhere: {id: id},
                content: '${request.contextPath}/order/add-or-update-ui'
            });
        }

        function addPlan() {
            var index = spLayer.open({
                title: '添加',
                area: ['90%', '90%'],
                content: '${request.contextPath}/order/add-or-update-ui'
            });
        }

        function getvalue(obj)
        {
            alert(obj.options[obj.options.selectedIndex].value);
            var value=obj.options[obj.selectedIndex].value

            var text=obj.options[obj.selectedIndex].text
            alert("value="+value+"text="+text);
        }

        function getOrderStatusData() {
            spUtil.ajax({
                url: '${request.contextPath}/order/status2',
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
                        //alert("index="+index+"item="+item);
                        $('#js-status').append(new Option(item, item));
                        //document.getElementById("js-status").append(new Option(item, item));
                        //document.getElementById("元素ID")与jquery中的$("#元素ID")是不等同的,应该用$("#元素ID").get(0)

                    });
                }
            });

        }

        /**
         * 搜索按钮事件
         */

        form.on('submit(js-search-filter)', function (data) {
            //alert("orderCode="+ $("#orderCode").val());


            var option=document.getElementById("js-status");
            var  v =  option.options[option.options.selectedIndex].value;

            spUtil.ajax({
                url: '${request.contextPath}/order/gantt/list',
                async: false,
                type: 'POST',
                // 是否序列化参数
                serializable: false,
                // 参数
                data: {
                    'orderCode':$("#orderCode").val(),
                    'status':v
                },
                success: function (data) {
                    var ganttData2 = data.data;

                    /*
                    $gantt.reload({

                    }); */

                    var $gantt2 = $("#js-gantt").gantt({
                        source: ganttData2,
                        navigate: 'scroll',//buttons  scroll
                        scale: "days",// months  weeks days  hours
                        maxScale: "months",
                        minScale: "days",
                        waitText: "加载中...",
                        itemsPerPage: 14,
                        tnTitle1: '工单编码',
                        tnTitle2: '计划/实际',
                        onItemClick: function (data) {
                            modifyPlan(data);
                        },
                        onAddClick: function (dt, rowId) {
                            //alert("add a record") ;
                            console.log(dt)
                            console.log(rowId)
                            console.log("onAddClick");
                        },
                        onRender: function () {
                            console.log('onRender');
                        }
                    });


                },
                error: function () {
                }
            });


            /*$gantt.reload({
                    where: data.field,
                    /!*
                    where : {
                        productId : $('#materiel').val(),
                        batchNo : $('#batchNo').val()
                    },
                   *!/
                    page: {
                        // 重新从第 1 页开始
                        curr: 1
                    }
                }
            );

            */




            // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
            return false;
        });



        /**
         * 新增订单功能
         */
        form.on('submit(js-add-order)', function (data) {
            addPlan();
            // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
            return false;
        });

        getOrderStatusData();

        /*
        * 数据表格中form表单元素是动态插入,所以需要更新渲染下
        * http://www.layui.com/doc/modules/form.html#render
        */
        $(function () {
            form.render();
        });
    });
</script>
</body>
</html>
