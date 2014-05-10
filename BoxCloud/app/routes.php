<?php

Route::get('/', 'MainController@index');
Route::get('/features', ['uses' => 'MainController@features', 'as' => 'features']);
Route::get('/about', ['uses' => 'MainController@about', 'as' => 'about']);

Route::post('/contact', ['uses' => 'MainController@contact', 'as' => 'contact']);

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