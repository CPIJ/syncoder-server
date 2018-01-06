package domain;

import java.util.List;

public interface IProjectManager {
    /**
     * @param projectId The ID associated with the project.
     * @return A: The project is already loaded into memory -> the project is retrieved and returned.
     * B: The project exists but is stored in the database -> The project is loaded from the database, added in memory and returned.
     * C: The project doesn't exist in the database or in memory -> a new project is added in memory and returned.
     */
    Project load(String projectId);

    /**
     * @param project The project to be unloaded.
     * Removes project from memory and stores in in the database.
     */
    void unload(Project project);

    /***
     * @param projectId The ID associated with the project.
     * @return When a project is found, the project else null
     */
    Project find(String projectId);

    /**
     * @return All projects that have active clients.
     */
    List<Project> getLiveProjects();
}
