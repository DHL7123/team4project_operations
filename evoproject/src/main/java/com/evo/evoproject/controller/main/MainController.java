package com.evo.evoproject.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/notice")
    public String notice() {
        return "notice";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("servicePolicy")
    public String serviecePolicy() {
        return "servicePolicy";
    }

    @GetMapping("privacyPolicy")
    public String privacyPolicy() {
        return "privacyPolicy";
    }

    @GetMapping("deliveryNrefundPolicy")
    public String delivery() {
        return "deliveryNrefundPolicy";
    }

}
