/**
 * 
 */
package com.zhilink.srm.common.persistence.proxy;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * <p>
 * 自定义Mybatis的配置，扩展.
 * </p>
 *
 * @author poplar.yfyang
 * 
 * @since JDK 1.5
 */
public class PageConfiguration extends Configuration {
	
    protected MapperRegistry mapperRegistry = new PaginationMapperRegistry(this);

    
    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    
    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }
}
