package com.example.NewProject.services;

import com.example.NewProject.entities.Colors;
import com.example.NewProject.repos.ColorsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorsService {

    private final ColorsRepo colorsRepo;

    public List<Colors> findAllColors(){

        return colorsRepo.findAll();
    }
}
