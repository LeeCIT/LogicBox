@extends('layout')

@section('content')
    <div id="dg">
		<div class="container">
			<div class="row centered">
				<h4>About LogicBox</h4>
				<br>
				<div class="col-lg-6 col-lg-offset-3">
					LogicBox was created as a final year project in Cork Institute of Technology.
                    During our course, we encountered a logic gate simulation software called "EasySim".
                    This piece of software was extremely old and outdated. It was hard and inefficient to use.
                    Instead of acting as a learning tool, it acted as a barrier to learning. It is only compatible with
                    32 bit Windows XP! It was even missing simple, yet crucial actions such as undo/redo.
                    <p><p></p>
                        So for our final year project, we decided to create a new logic gate simulation software which
                        had a nicer interface, feature improvements, additional features and multi-platform support.
                    </p>
				</div>
			</div>
		</div>
	</div>
    <div class="container w">
		<div class="row centered">
			<h4>The Team</h4><br>
			<div class="col-lg-3">
				<h4>Lee Coakley</h4>
                <b>Github: </b><a href="https://github.com/LeeCIT" target="_blank">https://github.com/LeeCIT</a></p>
			</div>
            <div class="col-lg-3">
				<h4>John Murphy</h4>
                <b>Github: </b><a href="https://github.com/JohnCIT" target="_blank">https://github.com/JohnCIT</a></p>
			</div>
            <div class="col-lg-3">
				<h4>Shaun O' Donovan</h4>
                <b>Github: </b><a href="https://github.com/ShaunodCIT" target="_blank">https://github.com/ShaunodCIT</a></p>
			</div>
            <div class="col-lg-3">
				<h4><a href="http://jatochnietdan.com" target="_blank">Robert O' Leary</a></h4>
                <b>Github: </b><a href="https://github.com/JaTochNietDan" target="_blank">https://github.com/JaTochNietDan</a>
                <b>Email: </b><a href="mailto:contact@jatochnietdan.com" target="_blank">contact@jatochnietdan.com</a>
			</div>
		</div>
	</div>
@stop

@section('title')
  About LogicBox, where did it come from?
@stop