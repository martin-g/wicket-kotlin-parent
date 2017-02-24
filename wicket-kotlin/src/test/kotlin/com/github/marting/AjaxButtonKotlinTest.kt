package com.github.marting

import org.apache.wicket.MarkupContainer
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.apache.wicket.util.tester.WicketTestCase
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import org.hamcrest.CoreMatchers.`is` as Is

/**
 * Kotlin tests for Wicket.kt ajaxButton() methods
 */
class AjaxButtonKotlinTest : WicketTestCase() {

    @Test
    fun ajaxButtonTest() {
        val counter = AtomicInteger(0)
        val lambda = { target: AjaxRequestTarget ->
            counter.incrementAndGet()
        }
        tester.startPage(AjaxButtonTestPage(lambda))

        assertThat(counter.get(), Is(0))

        val formTester = tester.newFormTester("form")
        formTester.submit("button")
        assertThat(counter.get(), Is(1))
    }

    @Test
    fun ajaxButtonWithSelfTest() {
        val counter = AtomicInteger(0)
        tester.startPage(AjaxButtonSelfTestPage({ target: AjaxRequestTarget ->
            counter.incrementAndGet()
            assertThat(this, Is(instanceOf(AjaxButton::class.java)))
        }))

        assertThat(counter.get(), Is(0))

        val formTester = tester.newFormTester("form")
        formTester.submit("button")
        assertThat(counter.get(), Is(1))
    }

    private interface MarkupResourceStreamProvider : IMarkupResourceStreamProvider {
        override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream {
            return StringResourceStream("<html><body><form wicket:id='form'><button wicket:id='button'></button></form></body></html>")
        }
    }

    private class AjaxButtonTestPage(lambda: (target: AjaxRequestTarget) -> Any) : WebPage(), MarkupResourceStreamProvider {

        init {
            val form = Form<Unit>("form")
            form.add(ajaxButton("button", lambda))
            add(form)
        }
    }

    private class AjaxButtonSelfTestPage(lambda: AjaxButton.(target: AjaxRequestTarget) -> Any) : WebPage(), MarkupResourceStreamProvider {

        init {
            val form = Form<Unit>("form")
            form.add(ajaxButton("button", lambda))
            add(form)
        }
    }
}
