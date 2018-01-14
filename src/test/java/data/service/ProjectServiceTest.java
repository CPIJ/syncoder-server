package data.service;

import data.repository.IProjectRepository;
import domain.Project;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    private IProjectRepository repository;
    private ProjectService service;

    @Before
    public void before() {
        repository = mock(IProjectRepository.class);
        service = new ProjectService(repository);
    }

    @Test
    public void save_projectDoesNotYetExist_insertIsCalled() {
        Project project = new Project("test");
        when(repository.find(anyString())).thenReturn(null);

        service.save(project);

        verify(repository).insert(project);
        verify(repository, never()).update(project);
    }

    @Test
    public void save_projectAlreadyExists_updateIsCalled() {
        Project project = new Project("test");
        when(repository.find(anyString())).thenReturn(project);

        service.save(project);

        verify(repository).update(project);
        verify(repository, never()).insert(project);
    }

    @Test
    public void find_isCalled_andPassedToTheRepository() {
        service.find("");

        verify(repository).find(anyString());
    }

    @Test
    public void getAll_isCalled_andPassedToTheRepository() {
        service.getAll();

        verify(repository).getAll();
    }

    @Test
    public void getAllTemplates_isCalled_isFilteredFromAllProjects() {
        Project p1 = new Project();
        Project p2 = new Project();
        p1.setIsTemplate(true);
        p2.setIsTemplate(true);
        when(repository.getAll()).thenReturn(new ArrayList<Project>(){{add(p1); add(p2);}});

        List<Project> projects = service.getAllTemplates();

        verify(repository).getAll();
        assertTrue(projects.stream().allMatch(Project::getIsTemplate));
    }
}