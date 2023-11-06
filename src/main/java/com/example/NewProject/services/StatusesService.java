package com.example.NewProject.services;

import com.example.NewProject.entities.Statuses;
import com.example.NewProject.repos.StatusesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusesService {

    private final StatusesRepo statusesRepo;

    public String getStatusById(long statusId){

        Statuses status = statusesRepo.findByStatusId(statusId);
        return status.getTitle();
    }
}
