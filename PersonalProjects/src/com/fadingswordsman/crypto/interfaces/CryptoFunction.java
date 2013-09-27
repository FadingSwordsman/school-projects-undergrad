package com.fadingswordsman.crypto.interfaces;

import java.util.Collection;

public interface CryptoFunction<T>
{
	public Collection<String> encrypt(int key, T object);
	public T decrypt(int firstKey, int secondKey, String...values);
}
