package com.apps.service;

import com.apps.exception.AppNotFoundException;
import com.apps.exception.DuplicateAppException;
import com.apps.model.App;
import com.apps.repository.AppRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppService {
    private final AppRepository appRepository;

    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public App createApp(App app) {
        if (appRepository.existsByName(app.getName())) {
            throw new DuplicateAppException("App name already exists: " + app.getName());
        }
        return appRepository.save(app);
    }

    public List<App> getAllApps() {
        return appRepository.findAll();
    }

    public App getAppById(Long id) {
        return appRepository.findById(id)
                .orElseThrow(() -> new AppNotFoundException("App not found with id: " + id));
    }

    public App updateApp(Long id, App appDetails) {
        App app = getAppById(id);
        if (!app.getName().equals(appDetails.getName()) &&
                appRepository.existsByName(appDetails.getName())) {
            throw new DuplicateAppException("App name already exists: " + appDetails.getName());
        }
        app.setName(appDetails.getName());
        app.setDescription(appDetails.getDescription());
        app.setCategory(appDetails.getCategory());
        return appRepository.save(app);
    }

    public void deleteApp(Long id) {
        App app = getAppById(id);
        appRepository.delete(app);
    }

    public Optional<App> findByName(String name) {
        return appRepository.findByName(name);
    }
}