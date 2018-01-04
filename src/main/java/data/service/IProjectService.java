package data.service;

import domain.Project;

public interface IProjectService {
    void save(Project project);
    Project find(String projectId);
}
