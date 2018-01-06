package data.repository;

import domain.Project;

import java.util.List;

public interface IProjectRepository {
    void save(Project project);
    Project find(String projectId);
    List<Project> getAll();
}
