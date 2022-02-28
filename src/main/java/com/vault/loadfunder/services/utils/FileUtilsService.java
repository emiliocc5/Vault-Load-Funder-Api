package com.vault.loadfunder.services.utils;

import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileUtilsService {
    List<Input> getInputsFromFile(MultipartFile file) throws IOException;
    ByteArrayResource getOutputFromList(List<Output> outputList) throws IOException;
}
