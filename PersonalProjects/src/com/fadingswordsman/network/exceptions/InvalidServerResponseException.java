package com.fadingswordsman.network.exceptions;

public class InvalidServerResponseException extends Exception
{
	private static final long serialVersionUID = -7327022757633130457L;

	public InvalidServerResponseException(String response)
	{
		super("Invalid response from server:\n" + response + "\n");
	}
}
