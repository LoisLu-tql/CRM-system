package com.loix.crm.workbench.service;

import com.loix.crm.workbench.domain.Clue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface ClueService {
    int saveCreateClue(Clue clue);

    Clue queryClueForDetailById(String id);

    List<Clue> queryAllClues();

    void saveConvert(Map<String, Object> map);
}
