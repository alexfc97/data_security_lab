package dtu.dk.data_security_lab;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(7070);
        registry.rebind("print-service", new PrintService());
    }
}