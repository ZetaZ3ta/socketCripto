/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 *
 * @author Arsenbasha
 */
public class Gestor implements Runnable {

    private Socket sckt;
    private InputStream fluxEntrada;
    private OutputStream fluxSortida;
    private DataOutputStream dadesFluxSortida;
    private DataInputStream dadesFluxEntrada;
    private Server svr;
    private File _dir;
    private FileWriter write;
    int cliente;

    public Gestor(Socket socolClient, int cl, File dir) {
        super();
        this.sckt = socolClient;
        this.cliente = cl;
        this._dir = dir;
    }

    @Override
    public void run() {

        int ok = 0;
        long peticio = -1;
        svr = new Server();
        System.out.println("Atenent petició del client...");

        try {

            fluxEntrada = sckt.getInputStream();
            fluxSortida = sckt.getOutputStream();
            dadesFluxSortida = new DataOutputStream(fluxSortida);
            dadesFluxEntrada = new DataInputStream(fluxEntrada);

            // Pas 3: Llegir i escriure del/o al flux segons el protocol del sòcol.
            System.out.println("Llegir i escriure del/o al flux segons el protocol del sòcol.");
            while (peticio != 0) {
                ok = 0;
                peticio = fluxEntrada.read();
                switch ((int) peticio) {
                    case 1:
                        ok = listarFicheros();
                        break;
                    case 2:
                        break;
                }
                fluxSortida.write(ok);
            }
            // Pas 4: Tanquem el flux d'entrada i el flux de sortida.
            fluxEntrada.close();
            fluxSortida.close();

            // Pas 5: Tanquem el sòcol.
            sckt.close();
            // S'ha atès la petició.
        } catch (IOException e) {
            System.out.println("S'ha produit un error tractant la petició del client ");
        }
    }

    private int listarFicheros() throws IOException {
        File[] archivos = _dir.listFiles();
        int ok=0;
        if (archivos.length!=0)ok=1;
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String[] strArchivos = new String[archivos.length];
        for (int i = 0; i < archivos.length; i++) {
            strArchivos[i] = String.format("%s  - %d - %s",
                    archivos[i].getName(),
                    archivos[i].length(),
                    sdf.format(archivos[i].lastModified()));
        }
        dadesFluxSortida.writeUTF(Arrays.toString(strArchivos));
        return ok;
    }

}
