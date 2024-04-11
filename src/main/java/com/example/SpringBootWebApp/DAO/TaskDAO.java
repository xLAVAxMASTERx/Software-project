package com.example.SpringBootWebApp.DAO;

import com.example.SpringBootWebApp.Models.Task;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskDAO {

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/codejavadb", "root", "bobo8161A");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection!");
        }
    }

    public void addTask(Task task) {
        try {
            String SQL = "INSERT INTO Tasks (usersid, taskContent, isDone) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, task.getUserId());
            preparedStatement.setString(2, task.getTaskContent());
            preparedStatement.setBoolean(3, task.isDone());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> getTasksByUser(int userId) {
        List<Task> usersTask = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Tasks";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                if (resultSet.getInt("usersid") == userId) {
                    Task task = new Task(null, userId);
                    task.setTaskContent(resultSet.getString("taskContent"));
                    task.setDone(resultSet.getBoolean("isDone"));
                    usersTask.add(task);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usersTask;
    }

    public List<Task> filterTasks(int userId, String filterType) {
        List<Task> allUsersTasks = getTasksByUser(userId);
        List<Task> filteredTasks = new ArrayList<>();

        switch (filterType) {
            case "all":
                filteredTasks.addAll(allUsersTasks);
                break;
            case "completed":
                for (Task task : allUsersTasks) {
                    if (task.isDone()) {
                        filteredTasks.add(task);
                    }
                }
                break;
            case "uncompleted":
                for (Task task : allUsersTasks) {
                    if (!task.isDone()) {
                        filteredTasks.add(task);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid filter type!");
        }

        return filteredTasks;
    }

    public void changeTaskStatus(int userId, String taskContent, boolean isDone) {
        try {
            String updateSQL = "UPDATE Tasks SET isDone = ? WHERE usersid = ? AND taskContent = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                updateStatement.setBoolean(1, isDone);
                updateStatement.setInt(2, userId);
                updateStatement.setString(3, taskContent);

                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Task status successfully updated");
                } else {
                    System.out.println("Task status NOT updated. Error occurred.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editTaskContentByUser(int userId, String taskContent, String newTaskContent) {
        try {
            String updateSQL = "UPDATE Tasks SET taskContent = ? WHERE usersid = ? AND taskContent = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                updateStatement.setString(1, newTaskContent);
                updateStatement.setInt(2, userId);
                updateStatement.setString(3, taskContent);

                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Task content for the user successfully edited.");
                } else {
                    System.out.println("No tasks found for the user to edit.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUserTask(int userId, String taskContent) {
        try {
            String SQL = "DELETE FROM Tasks WHERE usersid = ? AND taskContent = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, taskContent);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Task not found for deletion.");
            } else {
                System.out.println("Task successfully deleted.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

