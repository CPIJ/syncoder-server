package data.service;

import data.repository.IProjectRepository;
import domain.Project;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectServiceTest {

    private IProjectRepository repository;
    private ProjectService service;

    @Before
    public void before() {
        repository = mock(IProjectRepository.class);
        service = new ProjectService(repository);
    }

    @Test
    public void save_isCalled_andPassedToTheRepository() {
        service.save(null);

        verify(repository).save(null);
    }

    @Test
    public void find_isCalled_andPassedToTheRepository() {
        service.find("");

        verify(repository).find("");
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