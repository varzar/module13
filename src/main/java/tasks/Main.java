package tasks;

import tasks.user.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        int num = generateID();
        System.out.println("User with id: " + num);
        //Task1.1
        System.out.println("T A S K 1");
        User user = createDefaultUser();
        User createdUser = Utils.post(user);
        System.out.println("Task1 (create user): " + createdUser);
        //Task1.2
        User updateUser = Utils.put(createdUser);
        System.out.println("Task1 (update user): " + updateUser);
        //Task1.3
        System.out.print("Task1 (delete user): ");
        Utils.delete(updateUser);
        //Task1.4
        System.out.println("Task1 (list of users): ");
        List <User> users = Utils.getUsers();
        System.out.println(users);
        //Task1.5
        User getIDUser = Utils.get(num);
        System.out.println("Task1 (Info about user with id = " + num + "): " + getIDUser);
        //Task1.6
        String usernameID10 = "Moriah.Stanton";
        List <User> userByName = Utils.getUsersByName(usernameID10);
        System.out.println("Task1 (Info about user with username = " + usernameID10 + "): " + userByName);
        delimiter();

        //Task2
        System.out.println("T A S K 2");
        List <Post> posts = Utils.getPosts(num);
        int max = posts.stream()
                .mapToInt(Post::getId)
                .max()
                .getAsInt();
        System.out.println("Comment with max id: " + max);
        List <Comment> comments = Utils.getComments(max);
        String path = "src/main/resources/task2/user-" + num + "-post-" + max + "-comments.json";
        Utils.printComments(path, comments);
        System.out.println("Comments sorted by id reverse order and print in file with name " + path);
        delimiter();

        //Task3
        System.out.println("T A S K 3");
        List <UserTask> todos = Utils.getUncompletedTask(num);
        System.out.println(todos);
        delimiter();
    }

    public static int generateID(){
        return new Random().nextInt(10) +1;
    }

    private static User createDefaultUser() {
        Geo geo = new Geo(33.66d, 55.77d);
        Address address = new Address("Glushko", "4", "Odessa", "123456", geo);
        Company company = new Company("Bank", "Go", "engineer");
        User user = new User(555, "Oleh", "varzar", "varzar@i.ua", address, "222-555-888",
                "none", company);
        return user;
    }

    public static void delimiter(){
        System.out.println("********************************************************************\n");
    }
}
