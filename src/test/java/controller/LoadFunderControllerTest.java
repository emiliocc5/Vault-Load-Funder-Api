package controller;

import com.vault.loadfunder.controller.LoadFunderController;
import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;
import com.vault.loadfunder.services.loadfunder.LoadFunderService;
import com.vault.loadfunder.services.utils.FileUtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import service.FileUtilsServiceTest;
import service.LoadFunderServiceTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoadFunderControllerTest {

    private LoadFunderController loadFunderController;
    @Mock
    private FileUtilsService mockedFileUtilsService;
    @Mock
    private LoadFunderService mockedLoadFunderService;

    @BeforeEach
    public void setUp() {
        loadFunderController = new LoadFunderController(mockedLoadFunderService, mockedFileUtilsService);
    }

    @Test
    void happyPath() {
        when(mockedLoadFunderService.loadFunder(any())).thenReturn(getValidMockedOutputList());
        when(mockedFileUtilsService.getInputsFromFile(any())).thenReturn(new ArrayList<>());
        when(mockedFileUtilsService.getOutputFromList(any())).thenReturn(getByteArrayResource());

        ResponseEntity<Resource> response = loadFunderController.loadFunder(getValidMockedMultiPartFile());

        assert(response.hasBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    private List<Output> getValidMockedOutputList() {
        return new ArrayList<>();
    }

    private ByteArrayResource getByteArrayResource() {
        return new ByteArrayResource(new byte[0]);
    }

    private MockMultipartFile getValidMockedMultiPartFile() {
        return new MockMultipartFile(
                "file",
                "input.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "{\"id\":\"20476\",\"customer_id\":\"307\",\"load_amount\":\"$1515.26\",\"time\":\"2000-01-09T10:30:36Z\"}".getBytes()
        );
    }
}
