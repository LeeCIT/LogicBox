<?php

class Error
{
    public static function data($s)
    {
        return array(
            'error' => true,
            'messages' => $s
        );
    }
    
    public static function validator($v)
    {
        return array(
            'error' => true,
            'messages' => $v->messages()->toArray()
        );
    }
}
