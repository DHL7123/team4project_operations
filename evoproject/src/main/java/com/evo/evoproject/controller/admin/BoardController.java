package com.evo.evoproject.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping("/admin/manageBoard")
    public String manageBoard() {
        return "admin/manageBoard";
    }
}
