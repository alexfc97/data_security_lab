package dtu.dk.data_security_lab;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Base64;

public class PrintService extends UnicastRemoteObject implements IPrintService {

    byte[] secret = Base64.getDecoder().decode("Kz+C2eid/Hwtc1ueZD0og2Rpw7bCnISsBKkGFBVLzxY=");

    private void Authenticate(String jwt){
        Jws<Claims> result = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret))
                .parseClaimsJws(jwt);

        System.out.println(result);
    }


    protected PrintService() throws RemoteException {
        super();
    }

    @Override
    public String echo(String input,String jwt) throws RemoteException {
        Authenticate(jwt);
        return "From server: " + input;
    }

    @Override
    public String print(String filename, String printer, String jwt) throws RemoteException {
        Authenticate(jwt);
        return "From server: Printing " + filename + " on " + printer;
    }

    @Override
    public String queue(String printer, String jwt) throws RemoteException {
        Authenticate(jwt);
        return "From server: Now printing the queue list for " + printer;
    }

    @Override
    public void topQueue(String printer, int job, String jwt) throws RemoteException {

    }

    @Override
    public Boolean start(String jwt) throws RemoteException {
        return null;
    }

    @Override
    public Boolean stop(String jwt) throws RemoteException {
        return null;
    }

    @Override
    public Boolean restart(String jwt) throws RemoteException {
        return null;
    }

    @Override
    public String status(String printer, String jwt) throws RemoteException {
        return null;
    }

    @Override
    public String readConfig(String parameter, String jwt) throws RemoteException {
        return null;
    }

    @Override
    public void setConfig(String parameter, String value, String jwt) throws RemoteException {

    }
}
