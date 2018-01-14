package web.controller.api;

import data.service.IProjectService;
import data.service.ProjectService;
import domain.IProjectManager;
import domain.Project;
import domain.ProjectManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ProjectApiControllerTest {
    private ProjectApiController controller;
    private IProjectService service;
    private IProjectManager manager;
    private ResponseEntity result;


    @Before
    public void before() {
        service = mock(ProjectService.class);
        manager = mock(ProjectManager.class);
        controller = new ProjectApiController(service, manager);
    }

    @Test
    public void getLiveProjects_isCalled_projectManagerWasUsed() {
        result = controller.getLiveProjects();

        verify(manager).getLiveProjects();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getAllProjects_isCalled_correctServiceMethodWasCalled() {
        result = controller.getAllProject();

        verify(service).getAll();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getAllTemplates_isCalled_correctServiceMethodWasCalled() {
        result = controller.getAllTemplates();

        verify(service).getAllTemplates();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    //region reset
    @Test
    public void reset_noProjectsLoaded_terminatesEarly() {
        when(manager.getLiveProjects()).thenReturn(new ArrayList<>());

        result = controller.reset();

        verify(manager, never()).unload(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void reset_oneOrMoreProjectsAreLoaded_theyAreUnloaded() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project());
        when(manager.getLiveProjects())
                .thenReturn(projects)
                .thenReturn(projects)
                .thenReturn(new ArrayList<>());


        result = controller.reset();

        verify(manager).unload(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void reset_errorWhileUnloading_terminates() {
        when(manager.getLiveProjects()).thenReturn(new ArrayList<Project>() {{
            add(new Project());
        }});

        result = controller.reset();

        verify(manager).unload(any());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

    }
    //endregion
}