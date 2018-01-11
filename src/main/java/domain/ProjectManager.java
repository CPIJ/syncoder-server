package domain;

import data.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectManager implements IProjectManager {

    private static Map<String, Project> projects = new HashMap<>();
    private final IProjectService service;

    @Autowired
    public ProjectManager(IProjectService service) {
        this.service = service;
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

    //region For testing purposes
    void addProject(Project project) {
        projects.put(project.getId(), project);
    }

    static void reset() {
        projects = new HashMap<>();
    }
    //endregion
}
