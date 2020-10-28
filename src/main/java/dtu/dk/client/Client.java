package dtu.dk.client;

import dtu.dk.interfaces.IPrintService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IPrintService service = (IPrintService) Naming.lookup("rmi://localhost:7070/print-service");
        System.out.println("--- " + service.echo("Hey server"));

        Instant now = Instant.now();
        byte[] secret = Base64.getDecoder().decode("Kz+C2eid/Hwtc1ueZD0og2Rpw7bCnISsBKkGFBVLzxY=");


        String jwt = Jwts.builder()
                .setSubject("Alexander Christensen")
                .setAudience("Server")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();

        System.out.println(jwt);

        Jws<Claims> result = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret))
                .parseClaimsJws(jwt);

        System.out.println(result);

        System.out.println(service.queue("Printer1"));

        service.topQueue("Printer1", 5);

        System.out.println(service.queue("Printer1"));

        System.out.println(service.print("FileName5", "Printer1"));

        System.out.println(service.queue("Printer1"));
    }
}