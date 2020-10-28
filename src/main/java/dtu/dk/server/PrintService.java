package dtu.dk.server;

import dtu.dk.interfaces.IPrintService;
import dtu.dk.model.PrintQueueItem;
import dtu.dk.model.Printer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class PrintService extends UnicastRemoteObject implements IPrintService {

    private Boolean isRunning = false;
    private HashMap<String, String> config = new HashMap<String, String>();
    public Boolean isAuthenticated = false;

    private ArrayList<Printer> printers = new ArrayList<Printer>();

    protected PrintService() throws RemoteException {
        super();

        // Pre-populate some printers
        for (int i = 0; i < 4; i++) {
            printers.add(new Printer("Printer" + i));
        }
    }

    @Override
    public String echo(String input) throws RemoteException {

        String response = "From server: " + input + ", Authenticated: " + String.valueOf(isAuthenticated);
        isAuthenticated = true;
        return response;
    }

    @Override
    public String print(String filename, String printer) throws RemoteException {
        Printer foundPrinter = null;

        for (Printer p : printers) {
            if (p.getName().equals(printer)) {
                foundPrinter = p;
                break;
            }
        }

        if (foundPrinter == null)
            return "Could not find specified printer: " + printer;

        Boolean isPrinted = foundPrinter.print(filename);

        if (isPrinted)
            return "User: XX, printed file: " + filename + ", at time: YY";


        return "Could not find specified file: " + filename;
    }

    @Override
    public String queue(String printer) throws RemoteException {
        Printer foundPrinter = null;

        for (Printer p : printers) {
            if (p.getName().equals(printer)) {
                foundPrinter = p;
                break;
            }
        }

        if (foundPrinter != null) {
            ArrayList<PrintQueueItem> queueItems = foundPrinter.getQueueItems();
            Collections.reverse(queueItems);

            StringBuilder sb = new StringBuilder();
            // User called queue at time ...
            for (PrintQueueItem pqi : queueItems) {
                sb.append("JobNo: " + pqi.getJobNumber() + ", FileName: " + pqi.getFileName() + "\n");
            }
            return sb.toString();
        }


        return "Could not find a printer with name: " + printer;
    }

    @Override
    public Boolean topQueue(String printer, int job) throws RemoteException {
        Printer foundPrinter = null;

        for (Printer p : printers) {
            if (p.getName().equals(printer)) {
                foundPrinter = p;
                break;
            }
        }

        if (foundPrinter == null)
            return false;

        return foundPrinter.topQueue(job);
    }

    @Override
    public Boolean start() throws RemoteException {
        isRunning = true;
        return isRunning;
    }

    @Override
    public Boolean stop() throws RemoteException {
        isRunning = false;
        return isRunning;
    }

    @Override
    public Boolean restart() throws RemoteException {
        isRunning = false;

        // Clear queue for all printers
        for (Printer p : printers) {
            p.clearQueue();
        }

        isRunning = true;

        return true;
    }

    @Override
    public String status(String printer) throws RemoteException {
        Printer foundPrinter = null;

        for (Printer p : printers) {
            if (p.getName() == printer) {
                foundPrinter = p;
                break;
            }
        }

        if (foundPrinter != null)
            return foundPrinter.getStatus();

        return "Could not find specified printer: " + printer;
    }

    @Override
    public String readConfig(String parameter) throws RemoteException {
        String value = config.get(parameter);
        if (value == null)
            return "This parameter does not exist";

        return value;
    }

    @Override
    public void setConfig(String parameter, String value) throws RemoteException {
        config.put(parameter, value);
    }
}
