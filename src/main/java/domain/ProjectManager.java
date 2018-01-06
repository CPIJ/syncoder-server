package domain;

import data.repository.MySqlProjectRepository;
import data.service.IProjectService;
import data.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectManager implements IProjectManager {

    private final Map<String, Project> projects;
    private final IProjectService service;
    private static IProjectManager instance;

    private ProjectManager(IProjectService service) {
        this.service = service;
        projects = new HashMap<>();
    }

    @Autowired
    public static IProjectManager instance(IProjectService service) {
        if (instance == null) {
            instance = new ProjectManager(service);
        }

        return instance;
    }

    @Override
    public Project load(String projectId) {
        Project project = projects.get(projectId);

        if (project != null) {
            return project;
        }

        project = service.find(projectId);

        if (project == null) {
            project = new Project(projectId);
        }

        projects.put(projectId, project);

        return project;
    }

    @Override
    public void unload(Project project) {
        if (projects.remove(project.getId(), project)) {
            service.save(project);
        }
    }

    @Override
    public Project find(String projectId) {
        return projects.get(projectId);
    }

    @Override
    public List<Project> getLiveProjects() {
        return new ArrayList<>(projects.values());
    }
}
