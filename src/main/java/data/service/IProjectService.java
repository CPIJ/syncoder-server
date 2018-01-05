package data.service;

import domain.Project;

import java.util.List;

public interface IProjectService {
    void save(Project project);
    Project find(String projectId);
    List<Project> getAll();
    List<Project> getAllTemplates();
}
