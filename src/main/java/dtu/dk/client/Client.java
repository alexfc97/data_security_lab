package dtu.dk.client;

import dtu.dk.interfaces.IPrintService;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, FileNotFoundException {
        IPrintService service = (IPrintService) Naming.lookup("rmi://localhost:7070/print-service");
        System.out.println("--- " + service.echo("Hey server"));

        String token = service.authenticate("Roi", "ThisIsThePasswordForRoi");

        System.out.println(service.queue(token,"Printer1"));

        service.topQueue(token,"Printer1", 5);

        System.out.println(service.queue(token,"Printer1"));

        System.out.println(service.print(token,"FileName5", "Printer1"));

        System.out.println(service.queue(token,"Printer1"));

        // Try invalid token
        System.out.println(service.queue("invalid token","Printer1"));

        ArrayList<String> optionsList = new ArrayList<String>();
        optionsList.add("Print");
        optionsList.add("Queue");
        optionsList.add("TopQueue");
        optionsList.add("start");
        optionsList.add("stop");
        optionsList.add("restart");
        optionsList.add("status");
        optionsList.add("readConfig");
        optionsList.add("setConfig");
        while(true){
            for (String option: optionsList)
                System.out.format("%d. %s",optionsList.indexOf(option),option);
        }
    }
}