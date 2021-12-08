package com.raminzare.jpodcatcher.api;

import org.junit.jupiter.api.extension.*;

import java.util.Objects;

public class URIParameterResolverExtension implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(RSSURI.class) && parameterContext.getParameter().getType() == String.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String path = parameterContext.getParameter().getAnnotation(RSSURI.class).value();
        return Objects.requireNonNull(URIParameterResolverExtension.class.getClassLoader().getResource(path)).toString();
    }
}









