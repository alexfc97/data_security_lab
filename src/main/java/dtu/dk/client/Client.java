package dtu.dk.client;

import dtu.dk.interfaces.IPrintService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IPrintService service = (IPrintService) Naming.lookup("rmi://localhost:7070/print-service");
        System.out.println("--- " + service.echo("Hey server"));

        String token = service.authenticate("Rói", "1234");

        System.out.println(service.queue(token,"Printer1"));

        service.topQueue(token,"Printer1", 5);

        System.out.println(service.queue(token,"Printer1"));

        System.out.println(service.print(token,"FileName5", "Printer1"));

        System.out.println(service.queue(token,"Printer1"));

        // Try invalid token
        System.out.println(service.queue("invalid token","Printer1"));
    }
}