package com.loix.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sound.midi.Soundbank;

@Controller
public class WorkbenchIndexController {
    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }
}
