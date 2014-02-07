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
}