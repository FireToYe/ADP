/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.entity;
import java.io.Serializable;

import com.zhilink.srm.common.persistence.DataEntity;
 
/**
 * 字典Entity
 * 
 * @author jaray
 * 
 */
public class CacheVersion extends DataEntity<CacheVersion>  {

	private static final long serialVersionUID = 1L;
	private String name;
	private long version;

	 
 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}