package application;

import data.repository.IAuthenticationRepository;
import data.repository.IProjectRepository;
import data.repository.MySqlAuthenticationRepository;
import data.repository.MySqlProjectRepository;
import data.service.*;
import domain.IProjectManager;
import domain.ProjectManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rmi.fontys.RemotePublisher;

import javax.annotation.Generated;
import java.rmi.RemoteException;
import java.sql.Connection;

@Generated(value = "")
@Configuration
public class AppConfig {
    @Bean
    public IProjectManager projectManager(IProjectService service) {
        return new ProjectManager(service);
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
    public IAuthenticationService authenticationService(IAuthenticationRepository repository, IRmiService service) {
        return new RmiAuthenticationService(repository, service);
    }

    @Bean
    public IAuthenticationRepository authenticationRepository(@Qualifier(value = "auth_db") Connection connection) {
        return new MySqlAuthenticationRepository(connection);
    }

    @Bean(name = "auth_db")
    public Connection connection() {
        return new Properties().getConnection(Properties.Db.AUTHENTICATION);
    }

    @Bean(name = "project_db")
    public Connection projectConnection() {
        return new Properties().getConnection(Properties.Db.PROJECT);
    }

    @Bean
    public IRmiService rmiService(RemotePublisher publisher) throws RemoteException {
        return new RmiService(publisher);
    }

    @Bean
    public RemotePublisher remotePublisher() throws RemoteException {
        return new RemotePublisher();
    }

    @Bean(name = "property")
    public String property() {
        return new Properties().get("rmi", "registerProperty");
    }

    @Bean
    public java.util.Properties properties() {
        return new java.util.Properties();
    }
}
