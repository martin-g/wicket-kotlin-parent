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
        doTest { counter ->
            tester.startPage(AjaxLinkTestPage { target: AjaxRequestTarget ->
                counter.incrementAndGet()
            })
        }
    }

    @Test
    fun ajaxLinkWithSelfTest() {
        doTest { counter ->
            tester.startPage(AjaxLinkSelfTestPage({ target: AjaxRequestTarget ->
                counter.incrementAndGet()
                assertThat(this, Is(instanceOf(AjaxLink::class.java)))
            }))
        }
    }

    private fun doTest(startPage: (counter: AtomicInteger) -> Unit) {
        val counter = AtomicInteger(0)
        startPage(counter)

        assertThat(counter.get(), Is(0))

        tester.clickLink("link", true)
        assertThat(counter.get(), Is(1))
    }

    private interface MarkupResourceStreamProvider : IMarkupResourceStreamProvider {
        override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream {
            return StringResourceStream("<html><body><a wicket:id='link'></a></body></html>")
        }
    }

    private class AjaxLinkTestPage(lambda: (target: AjaxRequestTarget) -> Any) : WebPage(), MarkupResourceStreamProvider {
        init {
            add(ajaxLink<Any>("link", lambda))
        }
    }

    private class AjaxLinkSelfTestPage(lambda: AjaxLink<*>.(target: AjaxRequestTarget) -> Any) : WebPage(), MarkupResourceStreamProvider {
        init {
            add(ajaxLink<Any>("link", lambda))
        }
    }
}
