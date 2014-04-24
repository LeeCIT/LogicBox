package logicBox.web;

import logicBox.web.RequestInterface.status;
import com.mashape.unirest.http.HttpResponse;

public interface DownloadInterface 
{
	void onDownloadResponse(HttpResponse<String> res, String file, Request req, status stat);
}
