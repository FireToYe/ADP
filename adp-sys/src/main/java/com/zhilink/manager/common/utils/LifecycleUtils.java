/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.zhilink.manager.common.utils;

import java.util.Collection;

import javax.security.auth.Destroyable;

/**
 * Utility class to help call {@link org.apache.shiro.util.Initializable#init()
 * Initializable.init()} and {@link org.apache.shiro.util.Destroyable#destroy()
 * Destroyable.destroy()} methods cleanly on any object.
 *
 * @since 0.2
 */
public abstract class LifecycleUtils {

	public static void destroy(Object o) {
		if (o instanceof Destroyable) {
			destroy((Destroyable) o);
		} else if (o instanceof Collection) {
			destroy((Collection) o);
		}
	}

	public static void destroy(Destroyable d) {
		if (d != null) {
			try {
				d.destroy();
			} catch (Throwable t) {
				String msg = "Unable to cleanly destroy instance [" + d + "] of type [" + d.getClass().getName() + "].";
			}
		}
	}

	/**
	 * Calls {@link #destroy(Object) destroy} for each object in the collection.
	 * If the collection is {@code null} or empty, this method returns quietly.
	 *
	 * @param c
	 *            the collection of objects to destroy.
	 * @since 0.9
	 */
	public static void destroy(Collection c) {
		if (c == null || c.isEmpty()) {
			return;
		}

		for (Object o : c) {
			destroy(o);
		}
	}
}
