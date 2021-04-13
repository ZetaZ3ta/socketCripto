package Logic;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Arsenbasha
 */
public class Server {

    final static int PORT = 65535;
    static int numClients = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ServerSocket srvSckt;
        
        try {
            numClients = 0;
            srvSckt = new ServerSocket(PORT);
            System.out.println("Escuchando por puerto " + PORT);
            while (numClients < 3) {
                numClients++;
                Thread fil = new Thread(new Gestor(srvSckt.accept(), numClients, init()));
                System.out.println("Cliente numero: " + numClients);
                fil.start();
            }
            srvSckt.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static File init() {
        File dir;
        String ruta = System.getProperty("user.home") + File.separator + "servidor";
        dir = new File(ruta);
        if (!dir.exists()) {
            System.out.println("No existe el directorio se creara uno nuevo");
            dir.mkdirs();
        }
        return dir;
    }

    public int numClientes() {
        return numClients;
    }
}
