<?php

class Error
{
    public static function validator($v)
    {
        return array(
            'error' => true,
            'messages' => $v->messages()->toArray()
        );
    }
}
