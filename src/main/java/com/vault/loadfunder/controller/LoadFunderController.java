package com.vault.loadfunder.controller;

import com.vault.loadfunder.models.Output;
import com.vault.loadfunder.service.LoadFunderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class LoadFunderController {

    private final LoadFunderService loadFunderService;

    @Autowired
    public LoadFunderController(LoadFunderService loadFunderService) {
        this.loadFunderService = loadFunderService;
    }

    @GetMapping("/loadFunder")
    public void loadFunder() throws IOException {
        List<Output> outputList = loadFunderService.loadFunderV2(loadFunderService.getInputsFromFile());

        for (Output o : outputList) {
            System.out.println(o);
        }
    }

}
