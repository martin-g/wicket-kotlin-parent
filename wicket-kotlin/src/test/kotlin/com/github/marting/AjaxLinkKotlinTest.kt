package com.github.marting

import org.apache.wicket.MarkupContainer
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.apache.wicket.util.tester.WicketTestCase
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import org.hamcrest.CoreMatchers.`is` as Is

/**
 * Kotlin tests for Wicket.kt ajaxLink() methods
 */
class AjaxLinkKotlinTest : WicketTestCase() {

    @Test
    fun ajaxLinkTest() {
        val counter = AtomicInteger(0)
        val lambda = { target: AjaxRequestTarget ->
            counter.incrementAndGet()
        }
        tester.startPage(AjaxLinkTestPage(lambda))

        assertThat(counter.get(), Is(0))

        tester.clickLink("link", true)
        assertThat(counter.get(), Is(1))
    }

    @Test
    fun ajaxLinkWithSelfTest() {
        val counter = AtomicInteger(0)
        tester.startPage(AjaxLinkSelfTestPage({ target: AjaxRequestTarget ->
            counter.incrementAndGet()
            assertThat(this, Is(instanceOf(AjaxLink::class.java)))
        }))

        assertThat(counter.get(), Is(0))

        tester.clickLink("link", true)
        assertThat(counter.get(), Is(1))
    }

    private class AjaxLinkTestPage(lambda: (target: AjaxRequestTarget) -> Any) : WebPage(), IMarkupResourceStreamProvider {

        init {
            add(ajaxLink<Any>("link", lambda))
        }

        override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream {
            return StringResourceStream("<html><body><a wicket:id='link'></a></body></html>")
        }
    }

    private class AjaxLinkSelfTestPage(lambda: AjaxLink<*>.(target: AjaxRequestTarget) -> Any) : WebPage(), IMarkupResourceStreamProvider {

        init {
            add(ajaxLink<Any>("link", lambda))
        }

        override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream {
            return StringResourceStream("<html><body><a wicket:id='link'></a></body></html>")
        }
    }
}
