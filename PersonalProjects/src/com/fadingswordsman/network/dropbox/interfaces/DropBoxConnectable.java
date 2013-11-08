package com.fadingswordsman.network.dropbox.interfaces;

import com.fadingswordsman.data.Tuple;
import com.fadingswordsman.network.exceptions.InvalidServerResponseException;

public interface DropBoxConnectable
{
	public boolean connect();
	
	public Tuple<String, String> requestToken() throws InvalidServerResponseException;
}
