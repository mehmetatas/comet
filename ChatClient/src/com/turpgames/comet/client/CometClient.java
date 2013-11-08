package com.turpgames.comet.client;

import gdx.net.Net;
import gdx.net.NetJavaImpl;
import gdx.net.Net.HttpResponse;

public class CometClient {

	public static void main(String[] args) {
		try {
			NetJavaImpl net = new NetJavaImpl();
			Net.HttpRequest request = new Net.HttpRequest("GET");
			request.setContent("Test");
			request.setUrl("");
			request.setTimeOut(0);
			net.sendHttpRequest(request, new Net.HttpResponseListener() {				
				@Override
				public void handleHttpResponse(HttpResponse httpResponse) {
					
				}
				
				@Override
				public void failed(Throwable t) {
					
				}
			});
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
