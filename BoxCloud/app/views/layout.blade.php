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
			<li{{ Request::is('/') ? ' class="active"' : '' }}><a href="/">Home</a></li>
			<li{{ Request::is('about') ? ' class="active"' : '' }}><a href="{{ route('about') }}">About</a></li>
            <li><a data-toggle="modal" data-target="#myModal" href="#myModal"><i class="fa fa-envelope-o"></i> Contact Us</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

    @yield('content')

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
                    <label for="name" class="col-sm-2 control-label">Subject</label>
                    <div class="col-sm-10">
                      <input type="text" class="form-control" name="subject" id="name" placeholder="What you're contacting us about">
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