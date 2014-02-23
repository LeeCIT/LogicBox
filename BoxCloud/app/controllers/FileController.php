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
			$filedata = pathinfo($file);
			
            if($filedata['extension'] == '.lb')
                $circuits[] = $files[$key];
		}
        
        return Response::json($circuits);
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
        
        $path = public_path('files/'.$u->id);
		
		if(!Input::hasFile('file'))
			return Error::data(array('file' => 'A valid file was not uploaded'));
		
		$f = Input::file('file');
		
		$f->move($path, $f->getClientOriginalName());
		
		return Response::json(array('success' => 'File was uploaded to user\'s account!'));
    }
}