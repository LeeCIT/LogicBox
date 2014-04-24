<?php

Route::get('/', function()
{
	return 'Wassup mick?';
});

Route::post('/register', 'AuthController@register');
Route::post('/login', 'AuthController@login');

Route::group(array('before' => 'auth', 'prefix' => 'user'), function()
{
	Route::get('', 'UserController@index');
	Route::get('files', 'FileController@index');
	Route::get('files/{file}', 'FileController@download');
	Route::get('delete/{file}', 'FileController@delete');
	Route::post('upload', 'FileController@upload');
	Route::get('logout', 'AuthController@logout');
});