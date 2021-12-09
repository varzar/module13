package tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import tasks.user.Comment;
import tasks.user.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static tasks.Main.generateID;

public class Utils {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final String URL_COMMENTS = "https://jsonplaceholder.typicode.com/posts";
    private static final String URL_POSTS = "https://jsonplaceholder.typicode.com/users";

    public static User post(User user) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_POSTS))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse <String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("code: 201 - create, 200 - processing " + response);
        final User userNew = GSON.fromJson(response.body(), User.class);
        return userNew;
    }

    public static User put(User user) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(user);
        String urlID = String.format("%s/%d", URL_POSTS, generateID());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlID))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse <String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("code: 201 - create, 200 - processing " + response);
        User userNew = GSON.fromJson(response.body(), User.class);
        return userNew;
    }

    public static void delete(User user) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(user);
        String urlID = String.format("%s/%d", URL_POSTS, user.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlID))
                .header("Content-type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse <String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("code: 2xx - delete " + response);
    }

    public static List<User> getUsers() throws IOException, InterruptedException {
        HttpResponse<String> response = getStringHttpResponse(URI.create(URL_POSTS), CLIENT);
        System.out.println("code: 200 - found, 404 - not found " + response);
        List <User> users = GSON.fromJson(response.body(), new TypeToken<List <User>>() {}.getType());
        return users;
    }

    public static List<User> getUsersByUri(URI uri) throws IOException, InterruptedException {
        HttpResponse<String> response = getStringHttpResponse(uri, CLIENT);
        System.out.println("code: 200 - found, 404 - not found " + response);
        List <User> users = GSON.fromJson(response.body(), new TypeToken<List <User>>() {}.getType());
        return users;
    }

    public static List<User> getUsersByName(String name) throws IOException, InterruptedException {
        URI uri = URI.create(String.format("%s?username=%s", URL_POSTS, name));
        HttpResponse<String> response = getStringHttpResponse(uri, CLIENT);
        System.out.println("code: 200 - found, 404 - not found " + response);
        List <User> users = GSON.fromJson(response.body(), new TypeToken<List <User>>() {}.getType());
        return users;
    }

    public static User get(int num) throws IOException, InterruptedException {
        URI uriID = URI.create(String.format("%s/%d", URL_POSTS, num));
        HttpResponse<String> response = getStringHttpResponse(uriID, CLIENT);
        System.out.println("code: 200 - found, 404 - not found " + response);

        final User user = GSON.fromJson(response.body(), User.class);
        return user;
    }


    public static List <Post> getPosts(int num) throws IOException, InterruptedException {
        URI uriID = URI.create(String.format("%s/%d/posts", URL_POSTS, num));
        HttpResponse<String> response = getStringHttpResponse(uriID, CLIENT);
        System.out.println("Create list with posts of user with id=" + num + " " + response);
        List <Post> posts = GSON.fromJson(response.body(), new TypeToken<List<Post>>(){}.getType());
        return posts;
    }

    public static List <Comment> getComments(int num) throws IOException, InterruptedException {
        URI uriComments = URI.create(String.format("%s/%d/comments", URL_COMMENTS, num));
        HttpResponse<String> responseComments = getStringHttpResponse(uriComments, CLIENT);
        System.out.println("Create list with comments of user post with max id=" + num + " " + responseComments);
        List <Comment> comments = GSON.fromJson(responseComments.body(),
                new TypeToken<List<Comment>>(){}.getType());
        List<Comment> result = comments.stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Comment::getId)))
                .collect(Collectors.toList());
        return  result;
    }

    public static void printComments(String path, List <Comment> comments){
        File file = new File(path);
        checkFileAvailable(file);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(GSON.toJson(comments));
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public static List <UserTask> getUncompletedTask(int num) throws IOException, InterruptedException {
        URI uriTodos = URI.create(String.format("%s/%d/todos", URL_POSTS, num));

        HttpResponse<String> responseTasks = getStringHttpResponse(uriTodos, CLIENT);
        System.out.println("Create list with uncompleted tasks user`s with id=" + num + " " + responseTasks);
        List <UserTask> todos = GSON.fromJson(responseTasks.body(), new TypeToken <List<UserTask>>(){}.getType());
        List <UserTask> result = todos.stream()
                .filter(userTask -> userTask.isCompleted() == false)
                .collect(Collectors.toList());
        return result;
    }


    private static HttpResponse<String> getStringHttpResponse(URI uri, HttpClient client) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void checkFileAvailable(File file) {
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            }catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
    }
}
