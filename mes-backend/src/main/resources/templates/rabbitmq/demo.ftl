<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Spring-rabbitMQ</title>
    <!-- Bootstrap -->
    <link href="${request.contextPath}/rabbitmq/css/bootstrap.min.css" rel="stylesheet"/>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="${request.contextPath}/rabbitmq/js/html5shiv.min.js"></script>
      <script src="${request.contextPath}/rabbitmq/js/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
<div class="container">
  <div class="row">
	  <div class="col-md-6">
	  	<div class="panel panel-danger">
			<div class="panel-heading">生产者-消费者模型，向队列生产消息</div>
			<div class="panel-body">
			<img src="${request.contextPath}/rabbitmq/img/1.png" width="100%" alt="生产者-消费者模型" class="img-rounded"/>
						<form id="f1" class="form-horizontal" role="form">
							<div class="form-group">
								<label for="orderNo" class="col-sm-2 control-label">orderNo</label>
								<div class="col-sm-10">
									<input type="text" name="orderNo" class="form-control" id="orderNo" placeholder="orderNo"/>
								</div>
							</div>
							<div class="form-group">
								<label for="productId" class="col-sm-2 control-label">productId</label>
								<div class="col-sm-10">
									<input type="text" name="productId" class="form-control" id="productId" placeholder="productId"/>
								</div>
							</div>
							<div class="form-group">
								<label for="quality" class="col-sm-2 control-label">quality</label>
								<div class="col-sm-10">
									<input type="text" name="quality" class="form-control" id="quality" placeholder="quality"/>
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button id="produce" type="button" class="btn btn-default">生产消息</button>
								</div>
							</div>
							
						</form>
					</div>
</div>
	  </div>
	  <div class="col-md-6">
	  	<div class="panel panel-success">
			<div class="panel-heading">发布-订阅模型，向所有队列广播消息</div>
			<div class="panel-body">
			<img src="${request.contextPath}/rabbitmq/img/2.png"  width="100%" alt="发布-订阅模型" class="img-rounded"/>
						<form id="f2" class="form-horizontal" role="form">
							<div class="form-group">
								<label for="orderNo" class="col-sm-2 control-label">orderCode</label>
								<div class="col-sm-10">
									<input type="text" name="orderNo" class="form-control" id="orderNo" placeholder="orderNo"/>
								</div>
							</div>
							<div class="form-group">
								<label for="productId" class="col-sm-2 control-label">productId</label>
								<div class="col-sm-10">
									<input type="text" name="productId" class="form-control" id="productId" placeholder="productId"/>
								</div>
							</div>
							<div class="form-group">
								<label for="quality" class="col-sm-2 control-label">quality</label>
								<div class="col-sm-10">
									<input type="text" name="quality" class="form-control" id="quality" placeholder="quality"/>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button id="address" type="button" class="btn btn-default">发布消息</button>
								</div>
							</div>
						</form>
					</div>
</div>
	  </div>
	  
  </div>
  
  <div class="row">
	  <div class="col-md-6">
	  	<div class="panel panel-info">
			<div class="panel-heading">直连交换机模型，向指定的routing key发送消息</div>
			<div class="panel-body">
			<img src="${request.contextPath}/rabbitmq/img/3.png" width="100%" alt="发布-订阅模型" class="img-rounded"/>
						<form id="f3" class="form-horizontal" role="form">
							<div class="form-group">
								<label for="orderNo" class="col-sm-2 control-label">orderNo</label>
								<div class="col-sm-10">
									<input type="text" name="orderNo" class="form-control" id="orderNo" placeholder="orderNo"/>
								</div>
							</div>
							<div class="form-group">
								<label for="productId" class="col-sm-2 control-label">product id</label>
								<div class="col-sm-10">
									<input type="text" name="productId" class="form-control" id="productId" placeholder="productId"/>
								</div>
							</div>
							<div class="form-group">
								<label for="quality" class="col-sm-2 control-label">weight</label>
								<div class="col-sm-10">
									<input type="text" name="quality" class="form-control" id="quality" placeholder="quality"/>
								</div>
							</div>
							<div class="form-group">
								<label for="weight" class="col-sm-3 control-label">Routing key</label>
								<div class="col-sm-9">
									<select class="form-control" name="routingkey">
									  <option value="orange">orange</option>
									  <option value="black">black</option>
									  <option value="green">green</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button id="direct" type="button" class="btn btn-default">发布消息</button>
								</div>
							</div>
						</form>
					</div>
</div>
	  </div>
	  <div class="col-md-6">
	  	<div class="panel panel-default">
			<div class="panel-heading">topic交换机模型，向匹配的routing key发送消息</div>
			<div class="panel-body">
			<img src="${request.contextPath}/rabbitmq/img/4.png" width="100%" alt="发布-订阅模型" class="img-rounded"/>
						<form id="f4" class="form-horizontal" role="form">
							<div class="form-group">
								<label for="orderNo" class="col-sm-2 control-label">orderNo</label>
								<div class="col-sm-10">
									<input type="text" name="orderNo" class="form-control" id="orderNo" placeholder="orderNo"/>
								</div>
							</div>
							<div class="form-group">
								<label for="productId" class="col-sm-2 control-label">product id </label>
								<div class="col-sm-10">
									<input type="text" name="productId" class="form-control" id="productId" placeholder="productId"/>
								</div>
							</div>
							<div class="form-group">
								<label for="quality" class="col-sm-2 control-label">quality</label>
								<div class="col-sm-10">
									<input type="text" name="quality" class="form-control" id="quality" placeholder="quality"/>
								</div>
							</div>
							<div class="form-group">
								<label for="weight" class="col-sm-3 control-label">Routing key</label>
								<div class="col-sm-9">
									<input type="text" name="routingkey" class="form-control" id="weight" placeholder="routingkey"/>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button id="mytopic" type="button" class="btn btn-default">发布消息</button>
								</div>
							</div>
						</form>
					</div>
</div>
	  </div>
  </div>
</div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="${request.contextPath}/rabbitmq/js/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${request.contextPath}/rabbitmq/js/bootstrap.min.js"></script>
    <script type="text/javascript">
	$(document).ready(function(){
	  $("#produce").click(function(){
	    $.post("${request.contextPath}/rabbitmq/produce",$("#f1").serialize(),function(){
	    	//alert("生产成功");
	    });
	  });
	  
	  $("#address").click(function(){
	    $.post("${request.contextPath}/rabbitmq/topic",$("#f2").serialize(),function(){
	    	alert("发布成功");
	    });
	  });
	  
	  $("#direct").click(function(){
	    $.post("${request.contextPath}/rabbitmq/direct",$("#f3").serialize(),function(){
	    	//alert("发布成功");
	    });
	  });
	  
	  $("#mytopic").click(function(){
	    $.post("${request.contextPath}/rabbitmq/mytopic",$("#f4").serialize(),function(){
	    	//alert("发布成功");
	    });
	  });
	});

</script>
  </body>
</html>