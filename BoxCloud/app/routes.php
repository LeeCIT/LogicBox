<?php

Route::get('/', function()
{
	return 'Wassup mick?';
});

Route::post('/register', 'AuthController@register');