package com.fadingswordsman.network;

public interface ConnectionRegistrar {

	public void registerConnection(Connection connectionToRegister);
	public boolean unregisterConnection(Connection connectionToUnregister);
}
