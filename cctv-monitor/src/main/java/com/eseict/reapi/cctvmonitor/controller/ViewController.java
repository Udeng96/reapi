package com.eseict.reapi.cctvmonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SPA 진입점 — /api/cctvMonitor 및 하위 경로 요청을 index.html로 포워딩
 */
@Controller
public class ViewController {

    @GetMapping({"/cctvMonitor", "/cctvMonitor/**"})
    public String index() {
        return "forward:/index.html";
    }
}
