package com.csi.service;

import com.csi.domain.Model;
import java.util.List;

public interface ModelService {
    List<Model> findAllModels();
    Model findById(Integer id);
    void saveModel(Model model);
    void updateModel(Model model);
    void deleteModel(Integer id);
}
