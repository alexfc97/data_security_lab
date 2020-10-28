package dtu.dk.interfaces;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrintService extends Remote {
    // For testing purposes
    String echo(String input) throws RemoteException;

    // Authenticate user and generate a token
    String authenticate(String userName, String password) throws RemoteException, FileNotFoundException;

    // prints file filename on the specified printer
    String print(String tkn, String filename, String printer) throws RemoteException;

    // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    String queue(String tkn, String printer) throws RemoteException;

    // moves job to the top of the queue
    Boolean topQueue(String tkn, String printer, int job) throws RemoteException;

    // starts the print server
    Boolean start(String tkn) throws RemoteException;

    // stops the print server
    Boolean stop(String tkn) throws RemoteException;

    // stops the print server, clears the print queue and starts the print server again
    Boolean restart(String tkn) throws RemoteException;

    // prints status of printer on the user's display
    String status(String tkn, String printer) throws RemoteException;

    // prints the value of the parameter on the user's display
    String readConfig(String tkn, String parameter) throws RemoteException;

    // sets the parameter to value
    void setConfig(String tkn, String parameter, String value) throws RemoteException;

}