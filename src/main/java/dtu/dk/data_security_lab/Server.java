package dtu.dk.data_security_lab;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Base64;


public class Server{

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(7070);
        registry.rebind("print-service", new PrintService());
    }
}