<?php

class FileController extends Controller
{
    function index()
    {
        $u = Auth::getUser();
        
        $path = public_path('files/'.$u->id);
		$files = scandir($path);
        
		$circuits = array();
        
		foreach ($files as $key => $file)
        {
            if($file != '.' && $file != '..')
                $circuits[] = $files[$key];
		}
        
        return Response::json($circuits);
    }
}