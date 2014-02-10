<?php

use Illuminate\Auth\UserInterface;
use Illuminate\Auth\Reminders\RemindableInterface;

class User extends Eloquent implements UserInterface, RemindableInterface
{
	protected $table = 'users';
	protected $hidden = array('password');
	
	protected $fillable = array('email', 'password');

	public function getAuthIdentifier()
	{
		return $this->getKey();
	}

	public function getAuthPassword()
	{
		return $this->password;
	}

	public function getReminderEmail()
	{
		return $this->email;
	}
	
	public static function getRegistrationRules()
	{
		return array(
			'email' => 'required|email|unique:users',
			'password' => 'required'
		);
	}
}