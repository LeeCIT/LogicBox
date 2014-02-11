<?php

Route::get('/', function()
{
	return 'Wassup mick?';
});

Route::post('/register', 'AuthController@register');
Route::post('/login', 'AuthController@login');