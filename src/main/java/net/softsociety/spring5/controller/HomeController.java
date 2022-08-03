package net.softsociety.spring5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * projectName     :spring5
 * fileName        :HomeController
 * author          :yuuna
 * since           :2022/07/21
 */

@Controller
public class HomeController {

    /* home */
    @GetMapping({"","/"})
    public String home() {
        return "home";
    }
}