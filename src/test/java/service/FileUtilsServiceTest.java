package service;

import com.vault.loadfunder.exceptions.FileException;
import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;
import com.vault.loadfunder.services.utils.FileUtilsService;
import com.vault.loadfunder.services.utils.FileUtilsServiceImpl;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilsServiceTest {

    private FileUtilsService fileUtilsService;


    @BeforeEach
    public void setUp() {
        fileUtilsService = new FileUtilsServiceImpl();
    }

    @Test
    void givenValidMultiPartFile_thenGetInputList() {
        Input validInput = getValidMockedInput();
        MultipartFile validMultiPart = getValidMockedMultiPartFile();
        List<Input> inputs = fileUtilsService.getInputsFromFile(validMultiPart);

        assertEquals(1, inputs.size());
        assert(inputs.stream().allMatch(x -> x.getId().equals(validInput.getId())));
        assert(inputs.stream().allMatch(x -> x.getCustomerId().equals(validInput.getCustomerId())));
        assert(inputs.stream().allMatch(x -> x.getTime().equals(validInput.getTime())));
        assert(inputs.stream().allMatch(x -> x.getLoadAmount().equals(validInput.getLoadAmount())));
    }

    @Test
    void givenNotValidMultiPartFile_thenThrowException() {
        FileException thrown = Assertions.assertThrows(FileException.class, () -> {
            fileUtilsService.getInputsFromFile(getMultiPartFile());
        }, "File is empty");
        Assertions.assertEquals("There was an error decoding file: ", thrown.getMessage());
    }

    @Test
    void givenValidOutputList_thenReturnByteArrayResource() {
        List<Output> validOutputList = new ArrayList<>();
        assertEquals(ByteArrayResource.class, fileUtilsService.getOutputFromList(validOutputList).getClass());
    }

    private Input getValidMockedInput() {
        Input input = new Input();
        input.setId("20476");
        input.setCustomerId("307");
        input.setLoadAmount("$1515.26");
        input.setTime(ZonedDateTime.from(LocalDateTime.of(2000, 1, 9, 10, 30, 36).atZone(ZoneId.of("Z"))));
        return input;
    }

    private MockMultipartFile getValidMockedMultiPartFile() {
        return new MockMultipartFile(
                "file",
                "input.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "{\"id\":\"20476\",\"customer_id\":\"307\",\"load_amount\":\"$1515.26\",\"time\":\"2000-01-09T10:30:36Z\"}".getBytes()
        );
    }

    private MultipartFile getMultiPartFile() {
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
    }
}
