package data.service;

import data.repository.IProjectRepository;
import domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {

    private final IProjectRepository repository;

    @Autowired
    public ProjectService(IProjectRepository repository) {
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
