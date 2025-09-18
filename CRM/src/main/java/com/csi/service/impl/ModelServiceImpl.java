package com.csi.service.impl;

import com.csi.domain.Model;
import com.csi.mapper.ModelMapper;
import com.csi.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Model> findAllModels() {
        return modelMapper.findAll();
    }

    @Override
    public Model findById(Integer id) {
        return modelMapper.findById(id);
    }

    @Override
    public void saveModel(Model model) {
        modelMapper.save(model);
    }

    @Override
    public void updateModel(Model model) {
        modelMapper.update(model);
    }

    @Override
    public void deleteModel(Integer id) {
        modelMapper.deleteById(id);
    }
}
