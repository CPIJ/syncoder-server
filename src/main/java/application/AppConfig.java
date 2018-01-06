package application;

import data.repository.IAuthenticationRepository;
import data.repository.MySqlAuthenticationRepository;
import data.repository.MySqlProjectRepository;
import data.repository.IProjectRepository;
import data.service.*;
import domain.IProjectManager;
import domain.ProjectManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rmi.fontys.IRemotePublisherForListener;
import rmi.fontys.RemotePublisher;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;

@Configuration
public class AppConfig {
    @Bean
    public IProjectManager projectManager(IProjectService service) {
        return ProjectManager.instance(service);
    }

    @Bean
    public IProjectService projectService(IProjectRepository repository) {
        return new ProjectService(repository);
    }

    @Bean
    public IProjectRepository projectRepository(@Qualifier("project_db") Connection connection) {
        return new MySqlProjectRepository(connection);
    }

    @Bean
    public IAuthenticationService authenticationService(IAuthenticationRepository repository) {
        return new AuthenticationService(repository);
    }

    @Bean
    public IAuthenticationRepository authenticationRepository(@Qualifier(value = "auth_db") Connection connection) {
        return new MySqlAuthenticationRepository(connection);
    }

    @Bean(name = "auth_db")
    public Connection connection() {
        return Properties.getConnection(Properties.Db.AUTHENTICATION);
    }

    @Bean(name = "project_db")
    public Connection projectConnection() {
        return Properties.getConnection(Properties.Db.PROJECT);
    }

    @Bean
    public IRmiService rmiService() throws RemoteException {
        int port = Integer.parseInt(Properties.get("rmi", "port"));
        String name = Properties.get("rmi", "registerPublisher");
        String property = Properties.get("rmi", "registerProperty");

        return RmiService.instance(new RemotePublisher(), port, name, property);
    }

    @Bean(name = "property")
    public String property() {
        return Properties.get("rmi", "registerProperty");
    }
}
