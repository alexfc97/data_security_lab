package dtu.dk.data_security_lab;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrintService extends Remote {
    // For testing purposes
    String echo(String input,String jwt) throws RemoteException;

    // prints file filename on the specified printer
    String print(String filename, String printer, String jwt) throws RemoteException;

    // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    String queue(String printer, String jwt) throws RemoteException;

    // moves job to the top of the queue
    void topQueue(String printer, int job, String jwt) throws RemoteException;

    // starts the print server
    Boolean start(String jwt) throws RemoteException;

    // stops the print server
    Boolean stop(String jwt) throws RemoteException;

    // stops the print server, clears the print queue and starts the print server again
    Boolean restart(String jwt) throws RemoteException;

    // prints status of printer on the user's display
    String status(String printer, String jwt) throws RemoteException;

    // prints the value of the parameter on the user's display
    String readConfig(String parameter, String jwt) throws RemoteException;

    // sets the parameter to value
    void setConfig(String parameter, String value, String jwt) throws RemoteException;
}