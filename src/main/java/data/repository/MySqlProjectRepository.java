package data.repository;

import application.Properties;
import domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class MySqlProjectRepository implements IProjectRepository {

    private final Connection connection;

    @Autowired
    public MySqlProjectRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean insert(Project project) {
        int rowsAffected = 0;

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO project VALUES(?, ?, ?)")) {

            statement.setString(1, project.getId());
            statement.setString(2, project.getContent());
            statement.setBoolean(3, false);

            rowsAffected = statement.executeUpdate();

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, Properties.ERROR_MESSAGE, e);
        }

        return rowsAffected > 0;
    }

    @Override
    public boolean update(Project project) {
        int rowsAffected = 0;

        try (PreparedStatement statement = connection.prepareStatement("UPDATE project SET content = ? WHERE id LIKE ?")) {
            statement.setString(1, project.getContent());
            statement.setString(2, project.getId());

            statement.executeUpdate();

            rowsAffected = statement.executeUpdate();

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, Properties.ERROR_MESSAGE, e);
        }

        return rowsAffected > 0;
    }

    @Override
    public Project find(String projectId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM project WHERE id = ?")) {
            statement.setString(1, projectId);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Project project = new Project(resultSet.getString("id"));
                    project.setContent(resultSet.getString("content"));
                    project.setIsTemplate(resultSet.getBoolean("isTemplate"));

                    return project;
                }
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, Properties.ERROR_MESSAGE, e);
        }

        return null;
    }

    @Override
    public List<Project> getAll() {
        List<Project> projects = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM project")) {
            try(ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Project project = new Project(result.getString("id"));
                    project.setContent(result.getString("content"));
                    project.setIsTemplate(result.getBoolean("isTemplate"));

                    projects.add(project);
                }
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, Properties.ERROR_MESSAGE, e);
        }

        return projects;
    }
}
