package dtu.dk.client;

import dtu.dk.interfaces.IPrintService;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void wait(int i) throws InterruptedException {
        TimeUnit.SECONDS.sleep(i);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, FileNotFoundException, InterruptedException {
        IPrintService service = (IPrintService) Naming.lookup("rmi://localhost:7070/print-service");
        System.out.println("--- " + service.echo("Hey server"));

        String token = service.authenticate("Roi", "ThisIsThePasswordForRoi");

        /*System.out.println(service.queue(token,"Printer1"));

        service.topQueue(token,"Printer1", 5);

        System.out.println(service.queue(token,"Printer1"));

        System.out.println(service.print(token,"FileName5", "Printer1"));

        System.out.println(service.queue(token,"Printer1"));

        // Try invalid token
        System.out.println(service.queue("invalid token","Printer1"));*/

        ArrayList<String> optionsList = new ArrayList<>();
        optionsList.add("Print");
        optionsList.add("Queue");
        optionsList.add("TopQueue");
        optionsList.add("Start");
        optionsList.add("Stop");
        optionsList.add("Restart");
        optionsList.add("Status");
        optionsList.add("ReadConfig");
        optionsList.add("SetConfig");
        optionsList.add("Stop Program");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        service.start(token);
        while(running){
            for (String option: optionsList)
                System.out.format("%d. %s \n",optionsList.indexOf(option)+1,option);
            System.out.println("Please write the index of the option you want to run!");
            int selected;
            try {
                selected = scanner.nextInt();
            } catch(Exception e) {
                System.out.println("Not a valid option, please try again!");
                scanner.nextLine();
                wait(1);
                continue;
            }
            scanner.nextLine();
            if(selected == 0 | selected > 10){
                System.out.println("Not a valid option, Please try again");
                wait(2);
                continue;
            }
            switch (selected) {
                case 1 -> {
                    System.out.println("You have chosen: " + optionsList.get(0));
                    wait(2);
                    System.out.println("What is the filename?");
                    String selected1 = scanner.nextLine();
                    System.out.format("What is the printer name?");
                    String selected2 = scanner.nextLine();
                    //System.out.println("Running print command with, filename: " + selected1 + ", and printername: " + selected2);
                    System.out.println(service.print(token,selected1,selected2));
                    wait(2);
                }
                case 2 -> {
                    System.out.println("You have chosen: " + optionsList.get(1));
                    wait(2);
                    System.out.println("What is the printer name?");
                    String selected4 = scanner.nextLine();
                    //System.out.println("Running queue command with, printer name: " + selected4);
                    System.out.println(service.queue(token,selected4));
                    wait(2);
                }
                case 3 -> {
                    System.out.println("You have chosen: " + optionsList.get(2));
                    wait(2);
                    System.out.println("What is the printer name?");
                    String selected5 = scanner.nextLine();
                    System.out.format("What is the job index?");
                    int selected6;
                    while(true) {
                        try {
                            selected6 = scanner.nextInt();
                            break;
                        } catch (Exception e) {
                            System.out.println("Not a valid option, please try again!");
                            scanner.nextLine();
                            wait(1);
                        }
                    }
                    //System.out.println("Running topqueue command with, printer name: " + selected5 + ", and job : " + selected6);
                    System.out.println(service.topQueue(token,selected5,selected6));
                    wait(2);
                }
                case 4 -> {
                    System.out.println("You have chosen: " + optionsList.get(3));
                    wait(2);
                    //System.out.println("Running the start command");
                    System.out.println(service.start(token));
                    wait(2);
                }
                case 5 -> {
                    System.out.println("You have chosen: " + optionsList.get(4));
                    wait(2);
                    //System.out.println("Running the stop command");
                    System.out.println(service.stop(token));
                    wait(2);
                }
                case 6 -> {
                    System.out.println("You have chosen: " + optionsList.get(5));
                    wait(2);
                    //System.out.println("Running the restart command");
                    System.out.println(service.restart(token));
                    wait(2);
                }
                case 7 -> {
                    System.out.println("You have chosen: " + optionsList.get(6));
                    wait(2);
                    System.out.println("What is the printer name?");
                    String selected7 = scanner.nextLine();
                    //System.out.println("Running status command with, printer name: " + selected7);
                    System.out.println(service.status(token,selected7));
                    wait(2);
                }
                case 8 -> {
                    System.out.println("You have chosen: " + optionsList.get(7));
                    wait(2);
                    System.out.println("What is the parameter?");
                    String selected8 = scanner.nextLine();
                    //System.out.println("Running readconfig command with, parameter: " + selected8);
                    System.out.println(service.readConfig(token,selected8));
                    wait(2);
                }
                case 9 -> {
                    System.out.println("You have chosen: " + optionsList.get(8));
                    wait(2);
                    System.out.println("What is the parameter?");
                    String selected9 = scanner.nextLine();
                    System.out.println("What is the value?");
                    String selected10 = scanner.nextLine();
                    //System.out.println("Running setconfig command with, parameter: " + selected9 + ", and parameter value: " + selected10);
                    System.out.println(service.setConfig(token,selected9,selected10));
                    wait(2);
                }
                case 10 -> {
                    System.out.println("You have chosen to stop the program, Goodbye!");
                    running = false;
                }
            }
        }
    }
}