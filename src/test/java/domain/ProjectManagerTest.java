package domain;

import data.service.IProjectService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ProjectManagerTest {

    private IProjectService service;
    private ProjectManager projectManager;
    private Project project;

    @Before
    public void before() {
        service = mock(IProjectService.class);
        projectManager = new ProjectManager(service);
        projectManager.reset();

        project = new Project("Test");
    }

    //region load
    @Test
    public void load_projectExistsInMemory_itIsReturned() {
        projectManager.addProject(project);

        Project found = projectManager.load(project.getId());

        verify(service, never()).find(project.getId());
        assertEquals(project, found);
    }

    @Test
    public void load_projectIsNotLiveButDoesExistInDataStore_itIsLoadedAndReturned() {
        when(service.find(project.getId())).thenReturn(project);

        Project found = projectManager.load(project.getId());

        verify(service).find(project.getId());
        assertEquals(project, found);
    }

    @Test
    public void load_projectDoesNotExistAnywhere_itIsCreatedAndReturned() {
        when(service.find(project.getId())).thenReturn(null);

        Project found = projectManager.load(project.getId());

        verify(service).find(project.getId());
        assertNotNull(found);
    }
    //endregion

    //region unload
    @Test
    public void unload_projectCouldNotBeRemoved_itIsNotSaved() {
        projectManager.unload(project);

        verify(service, never()).save(project);
    }

    @Test
    public void unload_projectWasRemovedFromMemory_itWasSaved() {
        projectManager.addProject(project);

        projectManager.unload(project);

        verify(service).save(project);
    }
    //endregion

    //region find
    @Test
    public void find_itIsInMemory_ReturnsTheProject() {
        projectManager.addProject(project);

        Project found = projectManager.find(project.getId());

        assertNotNull(found);
    }

    @Test
    public void find_itIsNotInMemory_returnsNull() {
        Project found = projectManager.find(project.getId());

        assertNull(found);
    }
    //endregion

    //region getAll
    @Test
    public void getAll_isCalled_returnsListOfProjects() {
        List<Project> projects = projectManager.getLiveProjects();

        assertNotNull(projects);
    }
    //endregion

}