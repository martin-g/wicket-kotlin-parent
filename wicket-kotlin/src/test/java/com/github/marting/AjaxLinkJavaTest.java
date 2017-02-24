package com.github.marting;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.tester.WicketTestCase;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

import static com.github.marting.WicketKt.ajaxLink;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Java tests for {@link WicketKt#ajaxLink(String, Function1)}.
 */
public class AjaxLinkJavaTest extends WicketTestCase
{
    @Test
    public void ajaxLinkTest()
    {
        final AtomicInteger counter = new AtomicInteger(0);
        final Function1<AjaxRequestTarget, Object> lambda = (target) -> counter.incrementAndGet();

        tester.startPage(new AjaxLinkTestPage(lambda));

        assertThat(counter.get(), is(0));

        tester.clickLink("link", true);
        assertThat(counter.get(), is(1));
    }

    @Test
    public void ajaxLinkWithSelfTest()
    {
        final AtomicInteger counter = new AtomicInteger(0);
        final Function2<AjaxLink<?>, AjaxRequestTarget, Object> lambda = (self, target) -> {
            counter.incrementAndGet();
            assertThat(self, is(instanceOf(AjaxLink.class)));
            return null;
        };

        tester.startPage(new AjaxLinkTestPage(lambda));

        assertThat(counter.get(), is(0));

        tester.clickLink("link", true);
        assertThat(counter.get(), is(1));
    }

    private static class AjaxLinkTestPage extends WebPage implements IMarkupResourceStreamProvider {

        private AjaxLinkTestPage(Function1<AjaxRequestTarget, Object> lambda) {
            add(ajaxLink("link", lambda));
        }

        private AjaxLinkTestPage(Function2<AjaxLink<?>, AjaxRequestTarget, Object> lambda) {
            add(ajaxLink("link", lambda));
        }

        public IResourceStream getMarkupResourceStream(final MarkupContainer container, final Class<?> containerClass) {
            return new StringResourceStream("<html><body><a wicket:id='link'></a></body></html>");
        }
    }
}
