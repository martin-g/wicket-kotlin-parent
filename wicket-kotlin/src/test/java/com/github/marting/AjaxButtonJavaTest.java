package com.github.marting;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTestCase;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

import static com.github.marting.WicketKt.ajaxButton;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Java tests for {@link WicketKt#ajaxButton(String, Function1)}.
 */
public class AjaxButtonJavaTest extends WicketTestCase
{
    @Test
    public void ajaxButtonTest()
    {
        final AtomicInteger counter = new AtomicInteger(0);
        final Function1<AjaxRequestTarget, Object> lambda = (target) -> counter.incrementAndGet();

        tester.startPage(new AjaxButtonTestPage(lambda));

        assertThat(counter.get(), is(0));

        final FormTester formTester = tester.newFormTester("form");
        formTester.submit("button");
        assertThat(counter.get(), is(1));
    }

    @Test
    public void ajaxButtonWithSelfTest()
    {
        final AtomicInteger counter = new AtomicInteger(0);
        final Function2<AjaxButton, AjaxRequestTarget, Object> lambda = (self, target) -> {
            counter.incrementAndGet();
            assertThat(self, is(instanceOf(AjaxButton.class)));
            return null;
        };

        tester.startPage(new AjaxButtonTestPage(lambda));

        assertThat(counter.get(), is(0));

        final FormTester formTester = tester.newFormTester("form");
        formTester.submit("button");
        assertThat(counter.get(), is(1));
    }

    private static class AjaxButtonTestPage extends WebPage implements IMarkupResourceStreamProvider {

        private AjaxButtonTestPage(Function1<AjaxRequestTarget, Object> lambda) {
            this(ajaxButton("button", lambda));
        }

        private AjaxButtonTestPage(Function2<AjaxButton, AjaxRequestTarget, Object> lambda) {
            this(ajaxButton("button", lambda));
        }

        private AjaxButtonTestPage(AjaxButton button) {
            final Form form = new Form("form");
            add(form);
            form.add(button);
        }

        public IResourceStream getMarkupResourceStream(final MarkupContainer container, final Class<?> containerClass) {
            return new StringResourceStream("<html><body><form wicket:id='form'><button type='button' wicket:id='button'></button></form></body></html>");
        }
    }
}
