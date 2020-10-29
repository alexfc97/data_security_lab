package dtu.dk.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dtu.dk.interfaces.IPrintService;
import dtu.dk.model.PrintQueueItem;
import dtu.dk.model.Printer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PrintService extends UnicastRemoteObject implements IPrintService {

    private final String symKey = "Kz+C2eid/Hwtc1ueZD0og2Rpw7bCnISsBKkGFBVLzxY=";
    private final String userFileName = "userconfigfile.txt";

    private Boolean isRunning = false;
    private final HashMap<String, String> config = new HashMap<>();

    private final ArrayList<Printer> printers = new ArrayList<>();

    protected PrintService() throws RemoteException {
        super();

        // Pre-populate some printers
        for (int i = 0; i < 4; i++) {
            printers.add(new Printer("Printer" + i));
        }
    }

    @Override
    public String echo(String input) throws RemoteException {
        MethodCallLog(null, String.format("echo() called with param: %s", input));

        return ClientResponse(null, "From server: " + input);
    }

    @Override
    public String authenticate(String userName, String password) throws RemoteException, FileNotFoundException {
        MethodCallLog(null, String.format("authenticate() called with params: %s, <hidden>", userName));

        if (!authenticateUser(userName, password))
            return ClientResponse(null, "Invalid login.");


        return generateToken(userName);
    }

    @Override
    public String print(String tkn, String filename, String printer) throws RemoteException {
        MethodCallLog(tkn, String.format("print() called with params: %s, %s", filename, printer));

        if (!isTokenValid(tkn))
            return ClientResponse(null, "Token invalid.");

        if (!isRunning)
            return ClientResponse(tkn,"Server is currently stopped.");

        Printer foundPrinter = null;

        for (Printer p : printers) {
            if (p.getName().equals(printer)) {
                foundPrinter = p;
                break;
            }
        }

        if (foundPrinter == null)
            return ClientResponse(tkn, "Could not find specified printer: " + printer);

        Boolean isPrinted = foundPrinter.print(filename);

        if (isPrinted)
            return ClientResponse(tkn, "Printed file: " + filename);


        return ClientResponse(tkn, "Could not find specified file: " + filename);
    }

    @Override
    public String queue(String tkn, String printer) throws RemoteException {
        MethodCallLog(tkn, String.format("queue() called with params: %s", printer));

        if (!isTokenValid(tkn))
            return ClientResponse(tkn, "Token invalid.");

        if (!isRunning)
            return ClientResponse(tkn,"Server is currently stopped.");

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
                sb.append("JobNo: ").append(pqi.getJobNumber()).append(", FileName: ").append(pqi.getFileName()).append("\n");
            }
            return ClientResponse(tkn, sb.toString());
        }


        return ClientResponse(tkn, "Could not find a printer with name: " + printer);
    }

    @Override
    public Boolean topQueue(String tkn, String printer, int job) throws RemoteException {
        MethodCallLog(tkn, String.format("topQueue() called with params: %s, %s", printer, job));

        if (!isTokenValid(tkn))
            return false;

        if (!isRunning)
            return false;

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
    public Boolean start(String tkn) throws RemoteException {
        MethodCallLog(tkn, "start() called");

        if (!isTokenValid(tkn))
            return false;

        isRunning = true;

        return true;
    }

    @Override
    public Boolean stop(String tkn) throws RemoteException {
        MethodCallLog(tkn, "stop() called");

        if (!isTokenValid(tkn))
            return false;

        isRunning = false;
        return true;
    }

    @Override
    public Boolean restart(String tkn) throws RemoteException {
        MethodCallLog(tkn, "restart() called");

        if (!isTokenValid(tkn))
            return false;

        isRunning = false;

        // Clear queue for all printers
        for (Printer p : printers) {
            p.clearQueue();
        }

        isRunning = true;

        return true;
    }

    @Override
    public String status(String tkn, String printer) throws RemoteException {
        MethodCallLog(tkn, String.format("status() called with param: %s", printer));

        if (!isTokenValid(tkn))
            return ClientResponse(tkn, "Token invalid.");

        if (!isRunning)
            return ClientResponse(tkn,"Server is currently stopped.");

        Printer foundPrinter = null;

        for (Printer p : printers) {
            if (p.getName().equals(printer)) {
                foundPrinter = p;
                break;
            }
        }

        if (foundPrinter != null)
            return ClientResponse(tkn, foundPrinter.getStatus());

        return ClientResponse(tkn,"Could not find specified printer: " + printer);
    }

    @Override
    public String readConfig(String tkn, String parameter) throws RemoteException {
        MethodCallLog(tkn, String.format("readConfig() called with param: %s", parameter));

        if (!isTokenValid(tkn))
            return ClientResponse(tkn, "Token invalid.");

        if (!isRunning)
            return ClientResponse(tkn,"Server is currently stopped.");

        String value = config.get(parameter);
        if (value == null)
            return ClientResponse(tkn, "This parameter does not exist");

        return ClientResponse(tkn, value);
    }

    @Override
    public String setConfig(String tkn, String parameter, String value) throws RemoteException {
        MethodCallLog(tkn, String.format("setConfig() called with param: %s, %s", parameter, value));

        if (!isTokenValid(tkn))
            return ClientResponse(tkn, "Token invalid.");

        if (!isRunning)
            return ClientResponse(tkn,"Server is currently stopped.");

        config.put(parameter, value);

        return ClientResponse(tkn,value);
    }

    private boolean authenticateUser(String username, String password) throws FileNotFoundException {
        Scanner input = new Scanner(new File(userFileName));
        input.useDelimiter(",|\n");

        while(input.hasNext()) {
            String txtusername = input.next();
            if (txtusername.equals(username)){
                return BCrypt.checkpw(password,input.next());
            }
        }
        return false;
    }

    private String generateToken(String user) {

        Instant now = Instant.now();
        byte[] secret = Base64.getDecoder().decode(symKey);

        return Jwts.builder()
                .setSubject(user)
                .setAudience("Server")
                .setIssuer("Server")
                .setIssuedAt(java.sql.Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }

    private Boolean isTokenValid(String token) {
        try {
            byte[] secret = Base64.getDecoder().decode(symKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Server")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }

    private String getUsername(String token) {
        try {
            byte[] secret = Base64.getDecoder().decode(symKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Server")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();

        } catch (JWTVerificationException exception){
            return null;
        }
    }

    private String ClientResponse(String token, String message) {
        String pattern = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        return "[" + date + "]: " + message;
    }

    private void MethodCallLog(String token, String message) {
        String pattern = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        if (token != null) {
            String username = getUsername(token);
            if (username == null) // Invalid token, method will not execute
                System.out.println("[" + date + "]:[Unknown user]: " + message);
            else
                System.out.println("[" + date + "]:[" + username + "]: " + message);

            return;
        }

        System.out.println("[" + date + "]:[NOT AUTHENTICATED]: " + message);
    }
}
