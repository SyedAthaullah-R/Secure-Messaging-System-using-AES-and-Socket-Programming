package Two_Way_Programming;
import JAVA_AES.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.crypto.SecretKey;

public class twoWayClient {
    public static void main(String[] args) throws Exception{
        AES a = new AES();
        Socket socket = new Socket("192.168.29.142", 8000);
        System.out.println("Connected to the Server Successfully...");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        String encodedKeyString = br.readLine();
        SecretKey secKey = a.keyStringToKey(encodedKeyString);
        System.out.println("Received the AES key from the Server Successfully");
        String serverMsg, clientMsg;
        while(true){
            System.out.print("Client (Type your Message): ");
            clientMsg = br1.readLine();
            if(clientMsg.equalsIgnoreCase("exit")){
                pw.println(a.encode("exit", secKey));
                System.out.println("The Client disconnected from the Server...");
                break;
            }
            
            clientMsg = a.encode(clientMsg, secKey);
            pw.println(clientMsg);
            serverMsg = br.readLine();
            serverMsg = a.decrypt(serverMsg, secKey);
            if(serverMsg.equalsIgnoreCase("exit")){
                System.out.println("Server disconnected");
                break;
            }
            else System.out.println("Server Message: " + serverMsg);
        }
        socket.close();
        pw.close();
        br.close();
        br1.close();
    }
}
