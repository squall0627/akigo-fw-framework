package com.akigo.core.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

public class CDIHelper {
    public static <T> T getReference(Class<T> clazz) {
        BeanManager bm = CDI.current().getBeanManager();
        Bean<?> bean = bm.resolve(bm.getBeans(clazz));
        CreationalContext<?> cc = bm.createCreationalContext(bean);
        return clazz.cast(bm.getReference(bean, clazz, cc));
    }
}
