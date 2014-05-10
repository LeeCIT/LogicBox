@extends('layout')

@section('content')
	<div id="dg">
		<div class="container">
			<div class="row centered">
				<h4>Black Boxing</h4>
				With LogicBox, it's easy to build a component and re-use it as much as you want with
				our awesome black boxing feature.
				<br><br>
				<div class="col-lg-12">
					<img src="/assets/img/blackboxing.png" alt="BlackBoxing">
				</div>
			</div>
		</div>
	</div>
	
    <div class="container w">
		<div class="row centered">
			<br><br>
			<div class="col-lg-4">
				<i class="fa fa-undo"></i>
				<h4>Undo and Redo</h4>
				<p>Made a mistake? No problem, we have full undo/redo support for up to 512 actions.</p>
			</div>
				
			<div class="col-lg-4">
				<i class="fa fa-check"></i>
				<h4>Mass Selection</h4>
				<p>Click and drag to select multiple components to move or delete them in one swift action.</p>
			</div>
				
			<div class="col-lg-4">
				<i class="fa fa-globe"></i>
				<h4>Unlimited Canvas</h4>
				<p>Don't worry about running out of space or building into corners, your canvas is unlimited.</p>
			</div>
				
			<div class="col-lg-4">
				<i class="fa fa-thumb-tack"></i>
				<h4>Trace Snapping</h4>
				<p>Don't worry about trying to link your traces up, it'll automatically snap to the closest input/output.</p>
			</div>

			<div class="col-lg-4">
				<i class="fa fa-print"></i>
				<h4>Efficient Printing</h4>
				<p>Our colour scheme adapts for printing so you don't have to waste tons of ink.</p>
			</div>

			<div class="col-lg-4">
				<i class="fa fa-magic"></i>
				<h4>Live Changes</h4>
				<p>Feel free to make changes while the circuit is powered on and see the effects of your changes immediately.</p>
			</div>
				
			<div class="col-lg-4">
				<i class="fa fa-eye"></i>
				<h4>Intuitive Design</h4>
				<p>Simple to understand, nicely-sized icons, context sensitive help menus and graceful failure when errors occur. Easy to see where traces are connected by hovering over them.</p>
			</div>
				
			<div class="col-lg-4">
				<i class="fa fa-crosshairs"></i>
				<h4>Panning and Zooming</h4>
				<p>Pan around your unlimited canvas with ease using the middle mouse button or zoom out really far to get an overview of your work.</p>
			</div>
				
			<div class="col-lg-4">
				<i class="fa fa-keyboard-o"></i>
				<h4>Keyboard Shortcuts</h4>
				<p>We know power users want stuff too, so that's why just about every action is mapped to a combination of keys.</p>
			</div>
		</div>
		<br>
		<br>
	</div>
@stop

@section('title')
	The awesome features of LogicBox
@stop