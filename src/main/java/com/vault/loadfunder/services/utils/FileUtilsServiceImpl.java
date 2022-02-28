package com.vault.loadfunder.services.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.vault.loadfunder.exceptions.FileException;
import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileUtilsServiceImpl implements FileUtilsService {

    @Override
    public List<Input> getInputsFromFile(MultipartFile file) {
        List<Input> inputList = new ArrayList<>();
        Gson g = getGsonBuilder();

        try {
            InputStream inputStream = file.getInputStream();
            List<String> linesList = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.toList());

            //linesList.forEach(x -> inputList.add(g.fromJson(x, Input.class)));

            for (String s : linesList) {
                Input input = g.fromJson(s, Input.class);
                //TODO THIS LOGIC GOES INTO LOADFUNDER SERVICE
                if (inputList.stream().noneMatch(x -> x.getId().equals(input.getId()) && x.getCustomerId().equals(input.getCustomerId()))) {
                    inputList.add(input);
                }
            }
        } catch (Exception e) {
            throw new FileException("There was an error decoding file: ", e.getMessage());
        }
        return inputList;
    }

    @Override
    public ByteArrayResource getOutputFromList(List<Output> outputList) {
        List<String> outputString = outputList.stream().map(Output::toString).collect(Collectors.toList());
        try {
            Path filePath = Files.write(Paths.get("src", "main", "resources", "tmp", "output.txt"), outputString);
            return new ByteArrayResource(Files.readAllBytes(filePath));
        } catch (Exception e) {
            throw new FileException("There was an error encoding file: ", e.getMessage());
        } finally {
            deleteFile();
        }
    }

    private void deleteFile() {
        try {
            Files.delete(Paths.get("src", "main", "resources", "tmp", "output.txt"));
        } catch (IOException e) {
            throw new FileException("There was an error deleting file: ", e.getMessage());
        }
    }

    private Gson getGsonBuilder() {
        return new GsonBuilder().registerTypeAdapter(ZonedDateTime.class,
                        (JsonDeserializer<ZonedDateTime>)
                                (json, type, jsonDeserializationContext)
                                        -> ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).withZoneSameInstant(ZoneId.of("Z")))
                .create();
    }
}
