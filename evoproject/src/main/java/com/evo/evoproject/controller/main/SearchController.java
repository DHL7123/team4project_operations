package com.evo.evoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam("query") String query) {
        // 검색어를 쿼리 파라미터로 포함하여 제품 목록 페이지로 리디렉션
        return "redirect:/product-list.html?query=" + query;
    }


}
