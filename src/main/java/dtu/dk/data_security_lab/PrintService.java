package dtu.dk.data_security_lab;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrintService extends UnicastRemoteObject implements IPrintService {

    protected PrintService() throws RemoteException {
        super();
    }

    @Override
    public String echo(String input) throws RemoteException {
        return "From server: " + input;
    }

    @Override
    public String print(String filename, String printer) throws RemoteException {
        return null;
    }

    @Override
    public String queue(String printer) throws RemoteException {
        return null;
    }

    @Override
    public void topQueue(String printer, int job) throws RemoteException {

    }

    @Override
    public Boolean start() throws RemoteException {
        return null;
    }

    @Override
    public Boolean stop() throws RemoteException {
        return null;
    }

    @Override
    public Boolean restart() throws RemoteException {
        return null;
    }

    @Override
    public String status(String printer) throws RemoteException {
        return null;
    }

    @Override
    public String readConfig(String parameter) throws RemoteException {
        return null;
    }

    @Override
    public void setConfig(String parameter, String value) throws RemoteException {

    }
}
