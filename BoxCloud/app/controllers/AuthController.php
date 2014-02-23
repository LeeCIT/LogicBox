<?php

class AuthController extends Controller
{
    function register()
    {
        $v = Validator::make(Input::all(), User::getRegistrationRules());
        
        if($v->fails())
            return Response::json(Error::validator($v));
        
        $data = array(
            'email' => Input::get('email'),
            'password' => Hash::make(Input::get('password'))
        );
        
        $u = User::create($data);
        
        return $u->toJson();
    }
    
    function login()
    {
        $v = Validator::make(Input::all(), User::getLoginRules());
        
        if($v->fails())
            return Response::json(Error::validator($v));
        
        $data = array(
            'email' => Input::get('email'),
            'password' => Input::get('password')
        );
        
        if(!Auth::attempt($data))
            return Response::json(Error::data(array('invalid' => array('Invalid email or password'))));
        
        return Auth::getUser()->getUserInfo();
    }
}