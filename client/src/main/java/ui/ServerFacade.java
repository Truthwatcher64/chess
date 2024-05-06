package ui;

import result.LoginResult;
import result.RegisterResult;
import com.google.gson.Gson;
import model.GameData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class ServerFacade {
    public ServerFacade() {

    }

    public LoginResult login(String username, String password) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        var body = Map.of("username", username, "password", password);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            LoginResult result = new Gson().fromJson(inputStreamReader, LoginResult.class);
            System.out.println(result.getAuthToken());
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void logout(String authString) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");


        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Authorization", authString);

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            Map temp = new Gson().fromJson(inputStreamReader, Map.class);
            System.out.println(temp);
        }
    }

    public RegisterResult register(String username, String password, String email) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");


        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        var body = Map.of("username", username, "password", password, "email", email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            RegisterResult result = new Gson().fromJson(inputStreamReader, RegisterResult.class);
            //System.out.println(result.getAuthToken());
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void joinGame(String authString, int gameNum, String userName, String color) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Authorization", authString);

        // Write out the body
        var body = Map.of("gameID", gameNum, "username", userName, "color", color);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            RegisterResult result = new Gson().fromJson(inputStreamReader, RegisterResult.class);
            System.out.println(result.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void createGame(String authString, String name) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");


        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Authorization", authString);

        // Write out the body
        var body = Map.of("gameName", name);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            RegisterResult result = new Gson().fromJson(inputStreamReader, RegisterResult.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<GameData> listGames(String authString) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");


        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Authorization", authString);

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            Map temp = new Gson().fromJson(inputStreamReader, Map.class);
            System.out.println(temp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
