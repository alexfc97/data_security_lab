package dtu.dk.data_security_lab;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Base64;

public class PrintService extends UnicastRemoteObject implements IPrintService {

    public Boolean isAuthenticated = false;

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
    public String echo(String input) throws RemoteException {

        String response = "From server: " + input + ", Authenticated: " + String.valueOf(isAuthenticated);
        isAuthenticated = true;
        return response;
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
