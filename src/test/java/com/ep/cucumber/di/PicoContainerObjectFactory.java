package com.ep.cucumber.di;

import com.ep.api.utils.APIBaseRestActions;
import com.ep.api.utils.Jsonconversion;
import com.ep.api.utils.LoggerManagement;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.picocontainer.PicoFactory;

public class PicoContainerObjectFactory implements ObjectFactory {

	private final PicoFactory delegate = new PicoFactory();

	public PicoContainerObjectFactory() {
		addClass(APIBaseRestActions.class);
		addClass(Jsonconversion.class);
		addClass(LoggerManagement.class);
	}

	@Override
	public void start() {
		delegate.start();
	}

	@Override
	public void stop() {
		delegate.stop();
	}

	@Override
	public boolean addClass(Class<?> clazz) {
		return delegate.addClass(clazz);
	}

	@Override
	public <T> T getInstance(Class<T> clazz) {
		return delegate.getInstance(clazz);
	}

}
