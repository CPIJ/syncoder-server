package data.repository;

import domain.Project;

import java.util.List;

public interface IProjectRepository {
    boolean insert(Project project);
    boolean update(Project project);
    Project find(String projectId);
    List<Project> getAll();
}
