package com.csi.mapper;

import com.csi.domain.Model;
import java.util.List;

public interface ModelMapper {
    List<Model> findAll();
    Model findById(Integer id);
    void save(Model model);
    void update(Model model);
    void deleteById(Integer id);
}
