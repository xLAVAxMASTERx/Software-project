package com.example.SpringBootWebApp.Controller;

import com.example.SpringBootWebApp.DAO.TaskDAO;
import com.example.SpringBootWebApp.DAO.UserDAO;
import com.example.SpringBootWebApp.Models.Task;
import com.example.SpringBootWebApp.Models.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class UserController {

    private final UserDAO userDAO;
    private final TaskDAO taskDAO;

    public UserController(UserDAO userDAO, TaskDAO taskDAO) {
        this.userDAO = userDAO;
        this.taskDAO = taskDAO;
    }

    @PostMapping("/create-user")
    public String createUser(@ModelAttribute("user") User newUser, @RequestParam("status") String status) {
        status = status.toLowerCase();
        newUser.setStatus(status);
        if (userDAO.addUser(newUser)) {
            return "redirect:/user/" + newUser.getId();
        }
        return "redirect:/registration";
    }

    @PostMapping("/login-user")
    public String login(@ModelAttribute("user") User newUser) {
        System.out.println("User from form: " + newUser.getFirstName() + " " + newUser.getPassword());
        User existingUser = userDAO.findUser(newUser);
        if (existingUser != null) {
            return "redirect:/user/" + existingUser.getId();
        }
        return "redirect:/login";
    }

    @GetMapping("user/infinity.gif")
    public ResponseEntity<byte[]> getInfinityGif() {
        ClassPathResource resource = new ClassPathResource("static/infinity.gif");
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] gifBytes = inputStream.readAllBytes();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_GIF)
                    .body(gifBytes);
        } catch (IOException ioe) {
            System.err.println("ERROR WHILE READING FILE WITH GIF");
            ioe.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{id}")
    public String getUserProfile(@PathVariable("id") int id, Model model) {
        User user = userDAO.findById(id);
        model.addAttribute("user", user);

        switch (user.getStatus()) {
            case "worker":
                return "worker";
            case "manager":
                model.addAttribute("workers", userDAO.getAllWorkers());
                return "manager";
            case "director":
                model.addAttribute("workers", userDAO.getAllWorkers());
                model.addAttribute("managers", userDAO.getAllManagers());
                return "director";
        }
        return null;
    }

    @GetMapping("/user/{userId}/get-tasks")
    @ResponseBody
    public List<Task> getUserTasks(@PathVariable("userId") int userId) {
        List<Task> userTasks = taskDAO.getTasksByUser(userId);
        return userTasks;
    }

    @GetMapping("/user/{userId}/filter-tasks")
    public ResponseEntity<List<Task>> filterUserTasks(@PathVariable("userId") int userId, @RequestParam("filterType") String filterType) {
        List<Task> filteredTasks = taskDAO.filterTasks(userId, filterType);
        return new ResponseEntity<>(filteredTasks, HttpStatus.OK);
    }

    @PostMapping("/user/{userId}/new-task")
    public String createTask(@PathVariable int userId, @RequestParam("taskContent") String taskContent) {
        Task task = new Task(taskContent, userId);
        taskDAO.addTask(task);
        return "redirect:/user/" + userId;
    }

    @PatchMapping("/user/{userId}/updateTaskStatus")
    @ResponseBody
    public ResponseEntity<String> changeMyTaskStatus(@PathVariable("userId") int userId, @RequestParam("taskContent") String taskContent,
                                                     @RequestParam("isDone") boolean isDone) {
        taskDAO.changeTaskStatus(userId, taskContent, isDone);
        return ResponseEntity.ok("Changed task Status successfully");
    }

    @PatchMapping("/user/{userId}/edit-task")
    @ResponseBody
    public ResponseEntity<String> editUserTaskByUser(@PathVariable int userId, @RequestParam("taskOldContent") String taskOldText,
                                                     @RequestParam("taskNewContent") String taskNewText) {
        taskDAO.editTaskContentByUser(userId, taskOldText, taskNewText);
        return ResponseEntity.ok("Task updated successfully");
    }

    @DeleteMapping("/user/{userId}/delete-task/{taskContent}")
    @ResponseBody
    public ResponseEntity<String> deleteUserTaskByUser(@PathVariable int userId, @PathVariable String taskContent) {
        taskDAO.deleteUserTask(userId, taskContent);
        return ResponseEntity.ok("Task deleted successfully");
    }
    @PostMapping("/assign-task/{userId}/new-task")
    public String assignTask(@PathVariable int userId, @RequestParam("taskContent") String taskContent) {
        Task task = new Task(taskContent, userId);
        taskDAO.addTask(task);
        return "redirect:/registration"; // Change the redirect URL accordingly
    }

    @GetMapping("/assign-task/{userId}/filter-tasks")
    public ResponseEntity<List<Task>> filterSomeoneTasks(@PathVariable("userId") int userId, @RequestParam("filterType") String filterType) {
        List<Task> filteredTasks = taskDAO.filterTasks(userId, filterType);
        return new ResponseEntity<>(filteredTasks, HttpStatus.OK);
    }

    @PatchMapping("/assign-task/{userId}/updateTaskStatus")
    @ResponseBody
    public ResponseEntity<String> changeSomeoneTaskStatus(@PathVariable("userId") int userId, @RequestParam("taskContent") String taskContent,
                                                     @RequestParam("isDone") boolean isDone) {
        taskDAO.changeTaskStatus(userId, taskContent, isDone);
        return ResponseEntity.ok("Changed task Status successfully");
    }

    @GetMapping("/assign-task/{userId}")
    public String showUsersTasks(@PathVariable int userId, Model model) {
        model.addAttribute("user",userDAO.findById(userId));
        model.addAttribute("tasks",taskDAO.getTasksByUser(userId));
        return "userTasks"; // Change the view name accordingly
    }

    @PatchMapping("/assign-task/{userId}/edit-task")
    @ResponseBody
    public ResponseEntity<String> editUserTaskBySomeone(@PathVariable int userId, @RequestParam("taskOldContent") String taskOldText,
                                                        @RequestParam("taskNewContent") String taskNewText) {
        taskDAO.editTaskContentByUser(userId, taskOldText, taskNewText);
        return ResponseEntity.ok("Task updated successfully");
    }

    @DeleteMapping("/assign-task/{userId}/delete-task/{taskContent}")
    @ResponseBody
    public ResponseEntity<String> deleteUserTaskBySomeone(@PathVariable int userId, @PathVariable String taskContent) {
        taskDAO.deleteUserTask(userId,taskContent);
        return ResponseEntity.ok("Task deleted successfully");
    }

}
