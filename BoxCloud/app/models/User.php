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
	
	protected static function boot()
    {
        parent::boot();

        static::creating(function($model)
        {
            return mkdir(public_path('files/'.$model->id));
        });
    }

	public function getReminderEmail()
	{
		return $this->email;
	}
	
	public function getFiles()
	{
        $path = public_path('files/'.$this->id);
		$files = scandir($path);
        
		$circuits = array();
        
		foreach ($files as $key => $file)
        {
			$filedata = pathinfo($file);
			
            if($filedata['extension'] == 'lb')
                $circuits[] = $files[$key];
		}
		
		return $circuits;
	}
	
	public function getUserInfo()
	{
		$data = array(
			'user' => $this->toArray(),
			'files' => $this->getFiles()
		);
		
		return $data;
	}
	
	public function addFile($f)
	{
		$path = public_path('files/'.$this->id);
		
		$f->move($path, $f->getClientOriginalName());
	}
	
	public static function getRegistrationRules()
	{
		return array(
			'email' => 'required|email|unique:users',
			'password' => 'required'
		);
	}
	
	public static function getLoginRules()
	{
		return array(
			'email' => 'required|email',
			'password' => 'required'
		);
	}
}