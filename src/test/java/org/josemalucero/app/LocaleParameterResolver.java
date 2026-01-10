package org.josemalucero.app;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.util.Locale;

class LocaleParameterResolver implements org.junit.jupiter.api.extension.ParameterResolver {
    private final Locale locale;

    public LocaleParameterResolver(Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().equals(Locale.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) {
        return locale;
    }
}