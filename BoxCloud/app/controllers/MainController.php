<?php

class MainController extends Controller
{
    function index()
    {
        return View::make('index');
    }
    
    function contact()
    {
        $r = [
            'email' => 'required|email',
            'name' => 'required|min:3|max:20',
            'message' => 'required|min:3'
        ];
        
        $v = Validator::make(Input::all(), $r);
        
        if($v->fails())
            return Response::json(Error::validator($v));
        
        Mail::send(Input::get('message'), [], function($msg)
        {
            $msg->from(Input::get('email'), Input::get('name'));
        
            $msg->to('contact@logicbox.info');
        });
        
        return Response::json(['success' => true]);
    }
}