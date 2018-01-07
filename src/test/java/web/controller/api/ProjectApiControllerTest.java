package web.controller.api;

import data.service.IProjectService;
import data.service.ProjectService;
import domain.IProjectManager;
import domain.Project;
import domain.ProjectManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;


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
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getAllProjects_isCalled_correctServiceMethodWasCalled() {
        result = controller.getAllProject();

        verify(service).getAll();
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getAllTemplates_isCalled_correctServiceMethodWasCalled() {
        result = controller.getAllTemplates();

        verify(service).getAllTemplates();
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }
}