package data.service;

import data.repository.MySqlProjectRepository;
import data.repository.ProjectRepository;
import domain.Project;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Project> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Project> getAllTemplates() {
        return repository
                .getAll()
                .stream()
                .filter(Project::getIsTemplate)
                .collect(Collectors.toList());
    }
}
