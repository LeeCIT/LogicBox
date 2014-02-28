<?php

class FileController extends Controller
{
    function index()
    {
        $u = Auth::getUser();

        return Response::json($u->getFiles());
    }
    
    function upload()
    {
        $rules = array(
            'file' => 'required|max:10240'  
        );
		
		$v = Validator::make(Input::all(), $rules);
		
		if($v->fails())
			return Error::validator($v);
        
        $u = Auth::getUser();
        
		if(!Input::hasFile('file'))
			return Error::data(array('file' => 'A valid file was not uploaded'));
		
		$f = Input::file('file');
		
		$u->addFile($f);
		
		return Response::json(array('success' => 'File was uploaded to user\'s account!'));
    }
}