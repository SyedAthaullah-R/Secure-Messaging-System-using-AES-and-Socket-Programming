package com.securechat.Two_Way_Communication_Socket_Programming;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.crypto.SecretKey;

import com.securechat.JAVA_AES.*;
public class twoWayClient {
    private static final String AUTH_PASS = System.getenv("AUTH_PASS");
    public static void main(String[] args) throws Exception{
        AES a = new AES();
        Socket socket = new Socket("localhost", 8000);
        System.out.println("Connected to the Server Successfully");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        String serverAuth = br.readLine();
        if(serverAuth.equals("AUTH_REQUIRED")){
            pw.println(AUTH_PASS);
            String authResponse = br.readLine();
            if(authResponse.equals("AUTH_FAILED")){
                System.out.println("Authentication Failed. Closing Connection.");
                socket.close();
                return;
            }
            else System.out.println("Authenticated Successfully with the Server");
            String encodeKeyString = br.readLine();
            SecretKey secKey = a.keyStringToKey(encodeKeyString);
            System.out.println("Secret Key Received Successfully from the Server");
            String serverMsg, clientMsg;
            while(true){
                System.out.print("Client(Type your Message): ");
                clientMsg = br1.readLine();
                if(clientMsg.equals("exit")){
                    pw.println(a.encode("exit", secKey));
                    System.out.println("Connection Closed by the Client...");
                    break;
                }
                else pw.println(a.encode(clientMsg, secKey));
                serverMsg = br.readLine();
                serverMsg = a.decrypt(serverMsg, secKey);
                if(serverMsg.equals("exit")){
                    System.out.println("Server disconnected from the Client...");
                    break;
                }
                else System.out.println("Server Message: " + serverMsg);
            }
            pw.close();
            br.close();
            br1.close();
            socket.close();
            
        }
    }
}
