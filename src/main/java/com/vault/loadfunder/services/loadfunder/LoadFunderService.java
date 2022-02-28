package com.vault.loadfunder.services.loadfunder;

import com.vault.loadfunder.models.Input;
import com.vault.loadfunder.models.Output;

import java.util.List;

public interface LoadFunderService {

    List<Output> loadFunder(List<Input> inputList);
}
