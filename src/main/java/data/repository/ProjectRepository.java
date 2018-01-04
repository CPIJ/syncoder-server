package data.repository;

import domain.Project;

import java.util.List;

public interface ProjectRepository {
    boolean save(Project project);
    Project find(String projectId);
    List<Project> getAll();
}
