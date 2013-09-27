package com.fadingswordsman.network.interfaces;

public interface PackageSender<T>
{
	public T getResponse(String query, String targetName);
}
