package com.vault.loadfunder.controller;

import com.vault.loadfunder.models.Output;
import com.vault.loadfunder.services.loadfunder.LoadFunderService;
import com.vault.loadfunder.services.utils.FileUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class LoadFunderController {

    private final LoadFunderService loadFunderService;
    private final FileUtilsService fileUtilsService;

    @Autowired
    public LoadFunderController(LoadFunderService loadFunderService, FileUtilsService fileUtilsService) {
        this.loadFunderService = loadFunderService;
        this.fileUtilsService = fileUtilsService;
    }

    @PostMapping("/load-funder")
    public ResponseEntity<Resource> loadFunder(@RequestParam("file") MultipartFile inputFile) throws IOException {

        ByteArrayResource output = fileUtilsService.getOutputFromList(
                loadFunderService.loadFunder(fileUtilsService.getInputsFromFile(inputFile)));

        return ResponseEntity.ok()
                .contentLength(output.contentLength())
                .contentType(MediaType.TEXT_PLAIN)
                .body(output);
    }
}
