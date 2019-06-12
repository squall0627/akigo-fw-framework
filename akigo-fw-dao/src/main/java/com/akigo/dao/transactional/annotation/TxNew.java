package com.akigo.dao.transactional.annotation;

import com.akigo.core.exception.ApplicationException;

import javax.enterprise.inject.Stereotype;
import javax.interceptor.InterceptorBinding;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@InterceptorBinding
@Stereotype
@Transactional(value = TxType.REQUIRES_NEW, rollbackOn = {ApplicationException.class})
public @interface TxNew {
}
