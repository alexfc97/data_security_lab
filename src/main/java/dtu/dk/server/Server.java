package dtu.dk.server;

import dtu.dk.model.User;
import dtu.dk.server.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;


public class Server {
    public static void main(String[] args) throws RemoteException, FileNotFoundException {
        Registry registry = LocateRegistry.createRegistry(7070);
        registry.rebind("print-service", new PrintService());

        // Flag to create test users and store in a file
        Boolean shouldGenerateTestUsers = true;

        if (shouldGenerateTestUsers)
            generateTestUsers();
    }

    private static void generateTestUsers() {
        User user1 = new User();
        user1.setUsername("Alexander");
        user1.setPassword(BCrypt.hashpw("ThisIsThePasswordForAlexander",BCrypt.gensalt()));

        User user2 = new User();
        user2.setUsername("Roi");
        user2.setPassword(BCrypt.hashpw("ThisIsThePasswordForRoi",BCrypt.gensalt()));

        User user3 = new User();
        user3.setUsername("Florian");
        user3.setPassword(BCrypt.hashpw("ThisIsThePasswordForFlorian",BCrypt.gensalt()));

        User user4 = new User();
        user4.setUsername("Mohammad");
        user4.setPassword(BCrypt.hashpw("ThisIsThePasswordForMohammad",BCrypt.gensalt()));


        ArrayList<User> userlist = new ArrayList<>();
        userlist.add(user1);
        userlist.add(user2);
        userlist.add(user3);
        userlist.add(user4);

        try {
            FileWriter fileWriter = new FileWriter("userconfigfile.txt");
            for (User user: userlist) {
                fileWriter.write(user.getUsername() + "," + user.getPassword() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}