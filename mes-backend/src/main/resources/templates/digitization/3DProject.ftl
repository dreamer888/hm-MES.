<!DOCTYPE html>
<html>
<head includeDefault="true">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <title>华夏3D数字仿真</title>
    <style>
        body {
            margin: 0;
            overflow: hidden;
        }

        #label {
            position: absolute;
            padding: 10px;
            background: rgba(255, 255, 255, 0.6);
            line-height: 1;
            border-radius: 5px;
        }

        #video {
            position: absolute;
            width: 0;
            height: 0;
        }
    </style>

    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/three.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/stats.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/DragControls.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/OrbitControls.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/FirstPersonControls.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/TransformControls.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/dat.gui.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/EffectComposer.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/RenderPass.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/OutlinePass.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/FXAAShader.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/CopyShader.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/ShaderPass.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/ThreeBSP.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/ThreeJs_Drag.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/ThreeJs_Composer.js"
            charset="UTF-8"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/Modules.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/js/Tween.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/echarts/echarts.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/lib/ThreeJs/config.js"></script>
</head>

<body>
<div id="label"></div>
<div id="container"></div>
<video id="video" autoplay loop >
    <source src="${request.contextPath}/video/videoPlane.mp4">
</video>

<script>

    var stats = initStats();
    var scene, camera, renderer, controls, light, composer, transformControls, options;
    var matArrayA = []; //内墙
    var matArrayB = []; //外墙
    var group = new THREE.Group();

    // 初始化场景
    function initScene() {
        scene = new THREE.Scene();
    }

    // 初始化相机
    function initCamera() {
        camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 10000);
        camera.position.set(0, 50, 200);
    }

    // 初始化灯光
    function initLight() {
        var directionalLight = new THREE.DirectionalLight(0xffffff, 0.3); //模拟远处类似太阳的光源
        directionalLight.color.setHSL(0.1, 1, 0.95);
        directionalLight.position.set(0, 200, 0).normalize();
        scene.add(directionalLight);

        var ambient = new THREE.AmbientLight(0xffffff, 1); //AmbientLight,影响整个场景的光源
        ambient.position.set(0, 0, 0);
        scene.add(ambient);
    }

    // 初始化性能插件
    function initStats() {
        var stats = new Stats();

        stats.domElement.style.position = 'absolute';
        stats.domElement.style.left = '0px';
        stats.domElement.style.top = '0px';

        document.body.appendChild(stats.domElement);
        return stats;
    }

    // 初始化GUI
    function initGui() {
        options = new function () {
            this.batchNo = '';
            this.qty = 0;
            this.qtyUom = '';
            this.qty2 = 0;
            this.实时全景监控 = function () {
                window.open("3DVideo.html");
            };
        };
        var gui = new dat.GUI();
        gui.domElement.style = 'position:absolute;top:10px;right:0px;height:600px';
        gui.add(options, 'batchNo').name("物料批号：").listen();
        gui.add(options, 'qty').name("数量：").listen();
        gui.add(options, 'qtyUom').name("单位：").listen();
        gui.add(options, 'qty2').name("件数：").listen();
        gui.add(options, '实时全景监控');
    }

    // 初始化渲染器
    function initRenderer() {
        renderer = new THREE.WebGLRenderer({
            antialias: true
        });
        renderer.setSize(window.innerWidth, window.innerHeight);
        renderer.setClearColor(0x4682B4, 1.0);
        document.body.appendChild(renderer.domElement);
    }

    //创建地板
    function createFloor() {
        var loader = new THREE.TextureLoader();
        loader.load("/lib/ThreeJs/images/floor.jpg", function (texture) {
            texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
            texture.repeat.set(10, 10);
            var floorGeometry = new THREE.BoxGeometry(2600, 1400, 1);
            var floorMaterial = new THREE.MeshBasicMaterial({
                map: texture,
            });
            var floor = new THREE.Mesh(floorGeometry, floorMaterial);
            floor.rotation.x = -Math.PI / 2;
            floor.name = "地面";
            scene.add(floor);
        });
    }

    //创建墙
    function createCubeWall(width, height, depth, angle, material, x, y, z, name) {
        var cubeGeometry = new THREE.BoxGeometry(width, height, depth);
        var cube = new THREE.Mesh(cubeGeometry, material);
        cube.position.x = x;
        cube.position.y = y;
        cube.position.z = z;
        cube.rotation.y += angle * Math.PI; //-逆时针旋转,+顺时针
        cube.name = name;
        scene.add(cube);
    }

    //创建门_左侧
    function createDoor_left(width, height, depth, angle, x, y, z, name) {
        var loader = new THREE.TextureLoader();
        loader.load("/lib/ThreeJs/images/door_left.png", function (texture) {
            var doorgeometry = new THREE.BoxGeometry(width, height, depth);
            doorgeometry.translate(50, 0, 0);
            var doormaterial = new THREE.MeshBasicMaterial({
                map: texture,
                color: 0xffffff
            });
            doormaterial.opacity = 1.0;
            doormaterial.transparent = true;
            var door = new THREE.Mesh(doorgeometry, doormaterial);
            door.position.set(x, y, z);
            door.rotation.y += angle * Math.PI; //-逆时针旋转,+顺时针
            door.name = name;
            scene.add(door);
        });
    }

    //创建门_右侧
    function createDoor_right(width, height, depth, angle, x, y, z, name) {
        var loader = new THREE.TextureLoader();
        loader.load("/lib/ThreeJs/images/door_right.png", function (texture) {
            var doorgeometry = new THREE.BoxGeometry(width, height, depth);
            doorgeometry.translate(-50, 0, 0);
            var doormaterial = new THREE.MeshBasicMaterial({
                map: texture,
                color: 0xffffff
            });
            doormaterial.opacity = 1.0;
            doormaterial.transparent = true;
            var door = new THREE.Mesh(doorgeometry, doormaterial);
            door.position.set(x, y, z);
            door.rotation.y += angle * Math.PI; //-逆时针旋转,+顺时针8
            door.name = name;
            scene.add(door);
        });
    }

    //创建窗户
    function createWindow(width, height, depth, angle, x, y, z, name) {
        var loader = new THREE.TextureLoader();
        loader.load("/lib/ThreeJs/images/window.png", function (texture) {
            var windowgeometry = new THREE.BoxGeometry(width, height, depth);
            var windowmaterial = new THREE.MeshBasicMaterial({
                map: texture,
                color: 0xffffff
            });
            windowmaterial.opacity = 1.0;
            windowmaterial.transparent = true;
            var windows = new THREE.Mesh(windowgeometry, windowmaterial);
            windows.position.set(x, y, z);
            windows.rotation.y += angle * Math.PI; //-逆时针旋转,+顺时针
            windows.name = name;
            scene.add(windows);
        });
    }

    //返回墙对象
    function returnWallObject(width, height, depth, angle, material, x, y, z, name) {
        var cubeGeometry = new THREE.BoxGeometry(width, height, depth);
        var cube = new THREE.Mesh(cubeGeometry, material);
        cube.position.x = x;
        cube.position.y = y;
        cube.position.z = z;
        cube.rotation.y += angle * Math.PI;
        cube.name = name;
        return cube;
    }

    //墙上挖门，通过两个几何体生成BSP对象
    function createResultBsp(bsp, objects_cube) {
        var material = new THREE.MeshPhongMaterial({
            color: 0x9cb2d1,
            specular: 0x9cb2d1,
            shininess: 30,
            transparent: true,
            opacity: 1
        });
        var BSP = new ThreeBSP(bsp);
        for (var i = 0; i < objects_cube.length; i++) {
            var less_bsp = new ThreeBSP(objects_cube[i]);
            BSP = BSP.subtract(less_bsp);
        }
        var result = BSP.toMesh(material);
        result.material.flatshading = THREE.FlatShading;
        result.geometry.computeFaceNormals(); //重新计算几何体侧面法向量
        result.geometry.computeVertexNormals();
        result.material.needsUpdate = true; //更新纹理
        result.geometry.buffersNeedUpdate = true;
        result.geometry.uvsNeedUpdate = true;
        scene.add(result);
    }

    //创建墙纹理
    function createWallMaterail() {
        matArrayA.push(new THREE.MeshPhongMaterial({color: 0xafc0ca}));  //前  0xafc0ca :灰色
        matArrayA.push(new THREE.MeshPhongMaterial({color: 0xafc0ca}));  //后
        matArrayA.push(new THREE.MeshPhongMaterial({color: 0xd6e4ec}));  //上  0xd6e4ec： 偏白色
        matArrayA.push(new THREE.MeshPhongMaterial({color: 0xd6e4ec}));  //下
        matArrayA.push(new THREE.MeshPhongMaterial({color: 0xafc0ca}));  //左    0xafc0ca :灰色
        matArrayA.push(new THREE.MeshPhongMaterial({color: 0xafc0ca}));  //右

        matArrayB.push(new THREE.MeshPhongMaterial({color: 0xafc0ca}));  //前  0xafc0ca :灰色
        matArrayB.push(new THREE.MeshPhongMaterial({color: 0x9cb2d1}));  //后  0x9cb2d1：淡紫
        matArrayB.push(new THREE.MeshPhongMaterial({color: 0xd6e4ec}));  //上  0xd6e4ec： 偏白色
        matArrayB.push(new THREE.MeshPhongMaterial({color: 0xd6e4ec}));  //下
        matArrayB.push(new THREE.MeshPhongMaterial({color: 0xafc0ca}));  //左   0xafc0ca :灰色
        matArrayB.push(new THREE.MeshPhongMaterial({color: 0xafc0ca}));  //右
    }


    // 初始化模型
    function initContent() {
        createFloor();
        createWallMaterail();
        createCubeWall(10, 200, 1400, 0, matArrayB, -1295, 100, 0, "墙面");
        createCubeWall(10, 200, 1400, 1, matArrayB, 1295, 100, 0, "墙面");
        createCubeWall(10, 200, 2600, 1.5, matArrayB, 0, 100, -700, "墙面");
        //创建挖了门的墙
        var wall = returnWallObject(2600, 200, 10, 0, matArrayB, 0, 100, 700, "墙面");
        var door_cube1 = returnWallObject(200, 180, 10, 0, matArrayB, -600, 90, 700, "前门1");
        var door_cube2 = returnWallObject(200, 180, 10, 0, matArrayB, 600, 90, 700, "前门2");
        var window_cube1 = returnWallObject(100, 100, 10, 0, matArrayB, -900, 90, 700, "窗户1");
        var window_cube2 = returnWallObject(100, 100, 10, 0, matArrayB, 900, 90, 700, "窗户2");
        var window_cube3 = returnWallObject(100, 100, 10, 0, matArrayB, -200, 90, 700, "窗户3");
        var window_cube4 = returnWallObject(100, 100, 10, 0, matArrayB, 200, 90, 700, "窗户4");
        var objects_cube = [];
        objects_cube.push(door_cube1);
        objects_cube.push(door_cube2);
        objects_cube.push(window_cube1);
        objects_cube.push(window_cube2);
        objects_cube.push(window_cube3);
        objects_cube.push(window_cube4);
        createResultBsp(wall, objects_cube);
        //为墙面安装门
        createDoor_left(100, 180, 2, 0, -700, 90, 700, "左门1");
        createDoor_right(100, 180, 2, 0, -500, 90, 700, "右门1");
        createDoor_left(100, 180, 2, 0, 500, 90, 700, "左门2");
        createDoor_right(100, 180, 2, 0, 700, 90, 700, "右门2");
        //为墙面安装窗户
        createWindow(100, 100, 2, 0, -900, 90, 700, "窗户");
        createWindow(100, 100, 2, 0, 900, 90, 700, "窗户");
        createWindow(100, 100, 2, 0, -200, 90, 700, "窗户");
        createWindow(100, 100, 2, 0, 200, 90, 700, "窗户");
    }

    // 初始化轨迹球控件
    function initControls() {
        controls = new THREE.OrbitControls(camera, renderer.domElement);
        controls.enableDamping = true;
        controls.dampingFactor = 0.5;
        // 视角最小距离
        controls.minDistance = 100;
        // 视角最远距离
        controls.maxDistance = 1000;
        // 最大角度
        controls.maxPolarAngle = Math.PI / 2.2;
        controls.target = new THREE.Vector3(50, 50, 0);
    }

    function initEcharts() {
        pieChart = echarts.init($("<canvas width='512' height='512'></canvas>")[0]);
        option = {
            color: ['#3398DB'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '直接访问',
                    type: 'bar',
                    barWidth: '60%',
                    data: [10, 52, 200, 334, 390, 330, 220]
                }
            ]
        };
        pieChart.setOption(option);

        pieChart.on('finished', function () {
            var infoEchart = new THREE.TextureLoader().load(pieChart.getDataURL());

            var infoEchartMaterial = new THREE.MeshBasicMaterial({
                transparent: true,
                map: infoEchart,
                side: THREE.DoubleSide
            });

            var echartPlane = new THREE.Mesh(new THREE.PlaneGeometry(100, 100), infoEchartMaterial);
            echartPlane.position.set(100, 150, 0);
            scene.add(echartPlane);

        });

        pieChart2 = echarts.init($("<canvas width='512' height='512'></canvas>")[0]);
        option2 = {
            title: {
                text: '华夏数字仿真数据',
                subtext: '华夏MES数据',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['库区A-1', '库区A-2', '库区B-1', '库区B-2', '库区C-1']
            },
            series: [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: [
                        {value: 335, name: '库区A-1'},
                        {value: 310, name: '库区A-2'},
                        {value: 234, name: '库区B-1'},
                        {value: 135, name: '库区B-2'},
                        {value: 1548, name: '库区C-1'}
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        pieChart2.setOption(option2);

        pieChart2.on('finished', function () {
            var spriteMap = new THREE.TextureLoader().load(pieChart2.getDataURL());

            var spriteMaterial = new THREE.SpriteMaterial({
                transparent: true,
                map: spriteMap,
                side: THREE.DoubleSide
            });

            var sprite = new THREE.Sprite(spriteMaterial);
            sprite.scale.set(150, 150, 1)
            sprite.position.set(-100, 180, 0);
            scene.add(sprite);

        });
    }

    // 初始化
    function init() {
        initMat();
        initScene();
        addSkybox(10000, scene);
        addVideoPlane(0, 60, -690, 200, 100, scene, 'video');
        initCamera();
        initRenderer();
        initContent();
        initLight();
        initControls();
        initGui();
        initEcharts();

        addArea(0, 0, 1000, 500, scene, "ID1$库区1号", "FF0000", 20, "左对齐");

        addShelf(scene);

        //添加货物
        var shelf_list = GET_SHELF_LIST();
        for (var i = 1; i <= GET_LAYER_NUM(); i++) {
            for (var j = 1; j <= GET_COLUMN_NUM(); j++) {
                for (var k = 1; k <= shelf_list.length; k++) {
                    addOneUnitCargos(shelf_list[k - 1].shelfId, i, j, scene);
                }
            }
        }

        //添加选中时的蒙版
        composer = new THREE.ThreeJs_Composer(renderer, scene, camera, options);

        //添加拖动效果
        // 过滤不是 Mesh 的物体,例如辅助网格
        var objects = [];
        for (var i = 0; i < scene.children.length; i++) {
            var Msg = scene.children[i].name.split("$");
            if (scene.children[i].isMesh && Msg[0] == "货物") {
                objects.push(scene.children[i]);
            }
        }

        var dragControls = new THREE.DragControls(objects, camera, renderer.domElement);
        dragControls.addEventListener('dragstart', function (event) {
            controls.enabled = false;
            isPaused = true;
        });
        dragControls.addEventListener('dragend', function (event) {
            controls.enabled = true;
            isPaused = false;
        });

        document.addEventListener('resize', onWindowResize, false);
    }

    // 窗口变动触发的方法
    function onWindowResize() {
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(window.innerWidth, window.innerHeight);
    }

    function animate() {
        requestAnimationFrame(animate);
        renderer.render(scene, camera);
        composer.render();
        update();
    }

    // 更新控件
    function update() {
        stats.update();
        controls.update();
        TWEEN.update();
        RollTexture.offset.x += 0.001;
    }

    init();
    animate();
</script>
</body>

</html>
