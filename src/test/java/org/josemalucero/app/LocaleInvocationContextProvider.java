package org.josemalucero.app;

import org.josemalucero.servicio.providers.Messages;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class LocaleInvocationContextProvider implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext extensionContext) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
        return Stream.of(
                localeContext(Locale.forLanguageTag("es")),
                localeContext(Locale.forLanguageTag("en"))
        );
    }
    private TestTemplateInvocationContext localeContext(Locale locale) {
        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return "Locale = " + locale.getLanguage();
            }
            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(new BeforeEachCallback() {
                    @Override
                    public void beforeEach(ExtensionContext context) {
                        Messages.init(locale); // tu inicialización centralizada
                    }
                });
            }

        };
    }
}
