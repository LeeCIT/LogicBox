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
            'message' => 'required|min:3',
            'subject' => 'required|min:3'
        ];
        
        $v = Validator::make(Input::all(), $r);
        
        if($v->fails())
            return Response::json(Error::validator($v));
        
        Mail::send('emails.contact', ['msg' => Input::get('message')], function($msg)
        {
            $msg->from('mailer@logicbox.info', Input::get('name'));
            $msg->replyTo(Input::get('email'), Input::get('name'));
            $msg->subject(Input::get('subject'));
        
            $msg->to('contact@logicbox.info');
        });
        
        return Response::json(['success' => true]);
    }
}