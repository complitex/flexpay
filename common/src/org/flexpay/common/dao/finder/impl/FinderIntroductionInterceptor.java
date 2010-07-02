package org.flexpay.common.dao.finder.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.flexpay.common.dao.finder.FinderExecutor;
import org.flexpay.common.dao.finder.MethodExecutor;
import org.springframework.aop.IntroductionInterceptor;

/**
 * Connects the Spring AOP magic with the Hibernate DAO magic For any method beginning
 * with "find" this interceptor will use the FinderExecutor to call a Hibernate named
 * query
 */
public class FinderIntroductionInterceptor implements IntroductionInterceptor {

    @Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {

		FinderExecutor executor = (FinderExecutor) methodInvocation.getThis();
		MethodExecutor methodExecutor = (MethodExecutor)methodInvocation.getThis();

		String methodName = methodInvocation.getMethod().getName();
		if (methodName.startsWith("find") || methodName.startsWith("list")) {
			Object[] arguments = methodInvocation.getArguments();
			return executor.executeFinder(methodInvocation.getMethod(), arguments);
		}
		if (!"delete".equals(methodName) && methodName.startsWith("delete")) {
			Object[] arguments = methodInvocation.getArguments();
			return methodExecutor.executeUpdate(methodInvocation.getMethod(), arguments);
		}
//		else if (methodName.startsWith("iterate")) {
//			Object[] arguments = methodInvocation.getArguments();
//			return executor.iterateFinder(methodInvocation.getMethod(), arguments);
//		}
//        else if(methodName.startsWith("scroll"))
//        {
//            Object[] arguments = methodInvocation.getArguments();
//            return executor.scrollFinder(methodInvocation.getMethod(), arguments);
//        }
		else {
			return methodInvocation.proceed();
		}
	}

    @Override
	public boolean implementsInterface(Class intf) {
		return intf.isInterface() && (FinderExecutor.class.isAssignableFrom(intf) || MethodExecutor.class.isAssignableFrom(intf));
	}
}
