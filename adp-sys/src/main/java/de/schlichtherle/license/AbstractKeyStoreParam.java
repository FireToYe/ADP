package de.schlichtherle.license;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public abstract class AbstractKeyStoreParam implements KeyStoreParam {
	protected final Class clazz;
	private String resource;

	protected InputStream storeLocalInputStream;

	protected AbstractKeyStoreParam(Class paramClass) {
		this.clazz = paramClass;
	}

	protected AbstractKeyStoreParam(Class paramClass, String paramString) {
		this.clazz = paramClass;
		this.resource = paramString;
	}

	protected AbstractKeyStoreParam(Class paramClass, InputStream storeLocalInputStream) {
		this.clazz = paramClass;
		this.storeLocalInputStream = storeLocalInputStream;
	}

	public InputStream getStream() throws IOException {

		if (storeLocalInputStream != null) {
			return storeLocalInputStream;
		}
		InputStream localInputStream = this.clazz.getResourceAsStream(this.resource);
		localInputStream = new FileInputStream(this.resource);// zjs
		if (localInputStream == null) {
			throw new FileNotFoundException(this.resource);
		}
		return localInputStream;
	}

	public boolean equals(Object paramObject) {
		if (!(paramObject instanceof KeyStoreParam)) {
			return false;
		}
		AbstractKeyStoreParam localAbstractKeyStoreParam = (AbstractKeyStoreParam) paramObject;
		return (this.clazz.getResource(this.resource)
				.equals(localAbstractKeyStoreParam.clazz.getResource(localAbstractKeyStoreParam.resource)))
				&& (getAlias().equals(localAbstractKeyStoreParam.getAlias()));
	}
}
