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
        
        $data = [
            'name' => Input::get('name'),
            'msg' => Input::get('message'),
            'email' => Input::get('email')
        ];
        
        Mail::send('emails.welcome', $data, function($msg)
        {
            $msg->from(Input::get('email'), Input::get('name'));
        
            $msg->to('contact@jatochnietdan.com');
        });
        
        return Response::json(['success' => true]);
    }
}
