package data.repository;

import domain.Project;

public interface ProjectRepository {
    boolean save(Project project);
    Project find(String projectId);
}
