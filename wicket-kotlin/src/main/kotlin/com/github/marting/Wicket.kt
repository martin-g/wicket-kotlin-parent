package com.github.marting

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink

/**
 * A factory method for AjaxLink
 */
fun <T> ajaxLink(id: String, body: (target: AjaxRequestTarget) -> Any): AjaxLink<T> {
    return object : AjaxLink<T>(id) {
        override fun onClick(target: AjaxRequestTarget) {
            body(target)
        }
    }
}

/**
 * A factory method for AjaxLink that in:
 * <ul>
 *   <li>Kotlin exports the instantiated link as `this` insides body()</li>
 *   <li>Java passes the instantiated link as first parameter to body()</li>
 * </ul>
 */
fun <T> ajaxLink(id: String, body: AjaxLink<T>.(target: AjaxRequestTarget) -> Any): AjaxLink<T> {
    return object : AjaxLink<T>(id) {
        override fun onClick(target: AjaxRequestTarget) {
            body(target)
        }
    }
}
