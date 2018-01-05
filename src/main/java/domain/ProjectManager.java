package domain;

import data.repository.MySqlProjectRepository;
import data.service.ProjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectManager {

    private static final Map<String, Project> projects = new HashMap<>();
    private static final ProjectService service = new ProjectService(new MySqlProjectRepository());

    /**
     * @param projectId The ID associated with the project.
     * @return A: The project is already loaded into memory -> the project is retrieved and returned.
     * B: The project exists but is stored in the database -> The project is loaded from the database, added in memory and returned.
     * C: The project doesn't exist in the database or in memory -> a new project is added in memory and returned.
     */
    public static Project load(String projectId) {
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

    /**
     * @param project The project to be unloaded.
     */
    public static void unload(Project project) {
        if (projects.remove(project.getId(), project)) {
            service.save(project);
        }
    }

    /***
     * @param projectId The ID associated with the project.
     * @return When a project is found, the project else null
     */
    public static Project find(String projectId)
    {
        return projects.get(projectId);
    }
    /**
     * @return All projects that have active clients.
     */
    public static List<Project> getLiveProjects() {
      return new ArrayList<>(projects.values());
    }
}
