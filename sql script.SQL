-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(255),
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    phoneNumber VARCHAR(15)
);

-- Create tasks table
CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    taskContent TEXT,
    isDone BOOLEAN,
    usersId INT,
    FOREIGN KEY (usersId) REFERENCES users(id)
);

-- Insert data into users table
INSERT INTO users (status, firstName, lastName, password, email, phoneNumber)


-- Insert data into Task table
INSERT INTO tasks (taskContent, isDone, usersId)


-- 1. Select all users:
SELECT * FROM users;

-- 2. Select all tasks with user details:
SELECT tasks.*, users.firstName, users.lastName
FROM tasks
JOIN users ON tasks.usersId = users.id;

-- 3. Select incomplete tasks with user details:
SELECT tasks.*, users.firstName, users.lastName
FROM tasks
JOIN users ON tasks.usersId = users.id
WHERE tasks.isDone = false;

-- 4. Update a user's status:
UPDATE users
SET status = 'manager'
WHERE id = 3;

-- 5. Insert a new user:
INSERT INTO users (status, firstName, lastName, password, email, phoneNumber)
VALUES ('worker', 'New', 'users', 'newpassword', 'new.users@example.com', '1234567890');

-- 6. Delete a task:
DELETE FROM tasks
WHERE id = 5;

-- 7. Count the number of completed tasks for each user:
SELECT users.id, users.firstName, users.lastName, COUNT(tasks.id) AS completedTasks
FROM users
LEFT JOIN tasks ON users.id = tasks.usersId AND tasks.isDone = true
GROUP BY users.id, users.firstName, users.lastName;
