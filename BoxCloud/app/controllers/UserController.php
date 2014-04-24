<?php

class UserController extends Controller
{
    function index()
    {
        return Auth::User()->getUserInfo();
    }
}
