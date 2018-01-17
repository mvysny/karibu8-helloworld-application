package org.test

import com.github.vok.karibudsl.*
import javax.servlet.annotation.WebServlet

import com.vaadin.annotations.Theme
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.navigator.PushStateNavigation
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 *
 * The UI is initialized using [init]. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@PushStateNavigation
class MyUI : UI() {

    @Override
    override fun init(vaadinRequest: VaadinRequest?) {
        lateinit var layout: VerticalLayout
        layout = verticalLayout {
            val name = textField {
                caption = "Type your name here:"
            }
            button("Click Me", {
                println("Thanks ${name.value}, it works!")
                layout.label("Thanks ${name.value}, it works!")
            })
        }
    }
}

@WebServlet(urlPatterns = ["/*"], name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI::class, productionMode = false)
class MyUIServlet : VaadinServlet()
