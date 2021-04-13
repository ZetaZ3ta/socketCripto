/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arsenbasha
 */
public class Client {

    static Scanner scanner = new Scanner(System.in);
    static int PORT = 65535;
    static String HOST = "localhost";
    static InputStream inputSt;
    static OutputStream outputSt;
    static DataInputStream dadesFluxEntrada;
    static DataOutputStream dadesFluxSortida;

    private static int menu() {
        System.out.println("Escoja una opción: ");
        System.out.println("****************");
        System.out.println("Opción 1:Listar Ficheros ");
        System.out.println("Opción 2:Descargar Fichero ");
        System.out.println("Opción 0: DESCONECTARSE!!");
        return scanner.nextInt();
    }

    public static void main(String[] args) {

        int tancaConnexio = 0;
        int opMenu = 0;

        try {
            // Intentem connectar amb el servidor.
            System.out.println("Conectando con el servidorr...");

            // Pas: 1
            Socket scktClient = new Socket(HOST, PORT);

            // Pas: 2
            inputSt = scktClient.getInputStream();
            outputSt = scktClient.getOutputStream();

            // Pas: 3
            while (tancaConnexio == 0) {
                opMenu = menu();
                outputSt.flush();
                dadesFluxEntrada = new DataInputStream(inputSt);
                dadesFluxSortida = new DataOutputStream(outputSt);

                switch (opMenu) {
                    case 1: // Opción 1
                        outputSt.write(1);
                        mostrarLista();
                        if ((int) inputSt.read() == 1) System.out.println("Petició processada correctament.");
                         else  System.out.println("No s'ha processat correctament la petició.");
                        break;
                    case 2:
                        outputSt.write(2);
                        if ((int) inputSt.read() == 1)System.out.println("Petició processada correctament.");
                         else System.out.println("No s'ha processat correctament la petició.");
                        
                        break;
                    case 0: 
                        tancaConnexio = 1;
                        outputSt.write(0);
                        break;

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void mostrarLista() throws IOException {
        ArrayList<String> listaFicheros = new ArrayList<String>(Arrays.asList(dadesFluxEntrada.readUTF().split(",")));
        for (String listaFichero : listaFicheros) {
            listaFichero = listaFichero.replaceAll("\\[", " ").replaceAll("\\]", "");
            System.out.println(listaFichero);
        }
    }
}
