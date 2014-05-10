@extends('layout')

@section('content')
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
@stop