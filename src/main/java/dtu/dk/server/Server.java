package dtu.dk.server;

import dtu.dk.model.User;
import dtu.dk.server.PrintService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;


public class Server {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(7070);
        registry.rebind("print-service", new PrintService());
        User user1 = new User();
        user1.setUsername("Alexander");
        user1.setPassword("iuiwheguowehgfuhsekufh");

        User user2 = new User();
        user2.setUsername("Roi");
        user2.setPassword("oaiæwehgoiæsahgoihgooeqirhg");

        User user3 = new User();
        user3.setUsername("Florian");
        user3.setPassword("sjdæfjsldkfjlksfjsdfjåw");

        User user4 = new User();
        user4.setUsername("Mohammad");
        user4.setPassword("æspuhrnslcioeuu3498grglnj9034");

        ArrayList<User> userlist = new ArrayList<>();
        userlist.add(user1);
        userlist.add(user2);
        userlist.add(user3);
        userlist.add(user4);

        try {
            FileWriter fileWriter = new FileWriter("userconfigfile.txt");
            for (User user: userlist) {
                fileWriter.write(user.toString() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}