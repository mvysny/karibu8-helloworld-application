package org.test

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing._click
import com.github.karibu.testing._get
import com.vaadin.ui.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

class MyUITest {
    @BeforeEach
    fun mockVaadin() {
        MockVaadin.setup({ MyUI() })
    }

    @Test
    fun simpleUITest() {
        val layout = UI.getCurrent().content as VerticalLayout
        _get<TextField> { caption = "Type your name here:" }.value = "Baron Vladimir Harkonnen"
        _get<Button> { caption = "Click Me" }._click()
        expect(3) { layout.componentCount }
        expect("Thanks Baron Vladimir Harkonnen, it works!") { (layout.last() as Label).value }
        expect("Thanks Baron Vladimir Harkonnen, it works!") { _get<Label>().value }
    }
}
