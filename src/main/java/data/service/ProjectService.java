package data.service;

import data.repository.MySqlProjectRepository;
import data.repository.ProjectRepository;
import domain.Project;

public class ProjectService implements IProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Project project) {
        repository.save(project);
    }

    @Override
    public Project find(String projectId) {
        return repository.find(projectId);
    }
}
