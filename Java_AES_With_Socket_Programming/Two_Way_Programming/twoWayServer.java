package Two_Way_Programming;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.SecretKey;

import JAVA_AES.*;
public class twoWayServer {
    public static void main(String[] args) throws Exception{
        AES a = new AES();
        SecretKey secKey = a.generate_Secret_Key();
        try{
            String portEnv = System.getenv("PORT");
            int port = (portEnv != null && !portEnv.isEmpty()) ? Integer.parseInt(portEnv) : 8000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started successfully, Waiting for the Client...");
            Socket socket = serverSocket.accept();
            System.out.println("Client Connected Successfully");
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            String encodedKey = a.keyToString(secKey);
            pw.println(encodedKey);
            System.out.println("Shared AES key sent to the Client Successfully");
            String clientMsg, serverMsg;
            while(true){
                clientMsg = br.readLine();
                clientMsg = a.decrypt(clientMsg, secKey);
                if(clientMsg.equalsIgnoreCase("exit")){
                    System.out.println("The Client disconnected from server");
                    break;
                }
                System.out.println("Client Message: " + clientMsg);
                System.out.print("Server (Type your message): ");
                serverMsg = br1.readLine();
                if(serverMsg.equalsIgnoreCase("exit")){
                    pw.println(a.encode("exit", secKey));
                    System.out.println("The Server disconnected");
                    break;
                }
                else{
                    serverMsg = a.encode(serverMsg, secKey);
                    pw.println(serverMsg);
                }
                
            }
            serverSocket.close();
            socket.close();
            pw.close();
            br.close();
            br1.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
