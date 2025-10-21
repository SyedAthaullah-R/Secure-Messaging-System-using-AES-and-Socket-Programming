package com.securechat.Two_Way_Communication_Socket_Programming;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.SecretKey;

import com.securechat.JAVA_AES.*;
public class twoWayServer {
    private static final String AUTH_PASS = System.getenv("AUTH_PASS");
    public static void main(String[] args) throws Exception{
        AES a = new AES();
        int port = 8000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server Started Successfully at port " + port + ", Waiting for the CLient...");
        Socket socket = serverSocket.accept();
        System.out.println("Client Connected Successfully");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        pw.println("AUTH_REQUIRED");
        String clientAuth = br.readLine();
        if(!clientAuth.equals(AUTH_PASS)){
            pw.println("AUTH_FAILED");
            System.out.println("Client Authentication Failed. Closing Connection.");
            socket.close();
            serverSocket.close();
            return;
        }
        else{
            pw.println("AUTH_SUCCESS");
            System.out.println("Client Authenticated Successfully");
        }
        SecretKey secKey = a.generate_Secret_Key();
        String encodeKey = a.keyToString(secKey);
        pw.println(encodeKey);
        System.out.println("Secret key Successfully sent to the client");
        String clientMsg, serverMsg;
        while(true){
            clientMsg = br.readLine();
            clientMsg = a.decrypt(clientMsg, secKey);
            if(clientMsg.equals("exit")){
                System.out.println("Client disconnected from the Server...");
                pw.println(a.encode("exit", secKey));
                break;
            }
            System.out.println("Clinet Message: " + clientMsg);
            System.out.print("Server(Type your Message): ");
            serverMsg = br1.readLine();
            if(serverMsg.equals("exit")){
                pw.println(a.encode("exit", secKey));
                System.out.println("Connection Closed by the Server...");
                break;
            }
            else pw.println(a.encode(serverMsg, secKey));
        }
        pw.close();
        br.close();
        br1.close();
        socket.close();
        serverSocket.close();
    }
}
