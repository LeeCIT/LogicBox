<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="/assets/ico/favicon.png">

    <title>LogicBox - Awesome logic gate simulation for free</title>

    <!-- Bootstrap core CSS -->
    <link href="/assets/css/bootstrap.css" rel="stylesheet">
    <link href="/assets/css/font-awesome.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/assets/css/main.css" rel="stylesheet">
  </head>

  <body>

    <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/">LogicBox</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a data-toggle="modal" data-target="#myModal" href="#myModal"><i class="fa fa-envelope-o"></i> Contact Us</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

    <div id="dg">
		<div class="container">
			<div class="row centered">
				<h4>Awesome logic gate simulation!</h4>
				<br>
				<div class="col-lg-12">
					<img src="/assets/img/p01.png" alt="">
				</div>
                <br>
                <a href="/download/LogicBox.jar" class="btn btn-lg btn-primary"><i class="fa fa-download"></i> Download LogicBox</a>
			</div><!-- row -->
		</div><!-- container -->
	</div><!-- DG -->
    
	<div class="container w">
		<div class="row centered">
			<br><br>
			<div class="col-lg-4">
				<i class="fa fa-money"></i>
				<h4>Free and Open Source</h4>
				<p>LogicBox is completely free, unlike a lot of other logic gate simulation software. It's also open source on <a href="https://github.com/LeeCIT/LogicBox" target="_blank">Github!</a></p>
			</div><!-- col-lg-4 -->

			<div class="col-lg-4">
				<i class="fa fa-laptop"></i>
				<h4>Multi-Platform</h4>
				<p>It runs on multiple platforms without any hassle. The only requirement is that you must have Java installed!</p>
			</div><!-- col-lg-4 -->

			<div class="col-lg-4">
				<i class="fa fa-cloud"></i>
				<h4>Cloud Saving</h4>
				<p>We even offer a free cloud based circuit saving solution, so you can backup your circuits to our servers for safekeeping!</p>
			</div><!-- col-lg-4 -->
		</div><!-- row -->
		<br>
		<br>
	</div><!-- container -->

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
          <form method="POST" action="{{ route('contact') }}" class="form-horizontal" id="contactform">
            <div class="modal-header">
              <h4 class="modal-title" id="myModalLabel">Contact Us</h4>
            </div>
            <div class="modal-body" id="modelbody">
              <div class="row centered">
                <p>Send an email to <a href="mailto:contact@logicbox.info">contact@logicbox.info</a> or use the form below.</p>
                <hr>
                <div id="errors"></div>
                <div class="form-group">
                    <label for="email" class="col-sm-2 control-label">Email</label>
                    <div class="col-sm-10">
                      <input type="email" class="form-control" name="email" id="email" placeholder="Your email (so we can respond to you)">
                    </div>
                </div>
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label">Name</label>
                    <div class="col-sm-10">
                      <input type="text" class="form-control" name="name" id="name" placeholder="Your name">
                    </div>
                </div>
                <div class="form-group">
                    <label for="message" class="col-sm-2 control-label">Message</label>
                    <div class="col-sm-10">
                      <textarea rows="5" class="form-control" name="message" id="message" placeholder="The message you want to send us"></textarea>
                    </div>
                </div>
              </div>
            </div>
            <div class="modal-footer">
              <input type="submit" class="btn btn-danger" value="Send" id="contactbtn">
            </div>
          </form>
	    </div>
	  </div>
	</div>

    <script src="/assets/js/jquery.min.js"></script>
    <script src="/assets/js/jquery.form.min.js"></script>
    <script src="/assets/js/bootstrap.min.js"></script>
    <script type="text/javascript">
    $(document).ready(function()
    {
        var options =
        { 
            success:    function(data)
            { 
                if (data.error)
                {
                    var errors = '<div class="alert alert-danger"><b>Error</b>';
                    
                    $.each(data.messages, function(i, e) {
                       errors += '<li>' + e[0] + '</li>'; 
                    });
                    
                    errors += '</div>';
                
                    $('#errors').html(errors);
                    
                    $('#contactbtn').attr('disabled', false);
                    $('#contactbtn').val('Send');
                }
                else
                {
                    $('#modelbody').html('<div class="alert alert-success">Thank you for contacting us, we will get back to you as quickly as we can!</div>');
                    $('#contactbtn').val('Message sent');
                }
            },
            beforeSubmit: function()
            { 
                $('#contactbtn').attr('disabled', true);
                $('#contactbtn').val('Sending...');
            }
        }; 
    
        $('#contactform').ajaxForm(options);
    });
    </script>
  </body>
</html>