package io.zooobserver.controller

import io.zooobserver.annotation.AllOpen
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@AllOpen
@Controller
class IndexController {

    @GetMapping("/")
    fun get(): ModelAndView {
        return ModelAndView("index")
    }
}