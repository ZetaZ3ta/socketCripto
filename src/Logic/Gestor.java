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
import java.util.ArrayList;

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
        System.out.println("Atendiendo peticiones del cliente");

        try {
            abrirFLujos();
            System.out.println("Leer y escribir datos mediante el Socket");
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
            cerrarFlujos();
            sckt.close();
        } catch (IOException e) {
            System.out.println("Se ha producido un error con el cliente = " + cliente);
        }
    }

    private void abrirFLujos() {
        try {
            fluxEntrada = sckt.getInputStream();
            fluxSortida = sckt.getOutputStream();
            dadesFluxEntrada = new DataInputStream(fluxEntrada);
            dadesFluxSortida = new DataOutputStream(fluxSortida);
        } catch (IOException e) {
            System.out.println("Error al establecer canal de datos " + e.getMessage());
        }
    }

    private void cerrarFlujos() {
        try {
            fluxEntrada.close();
            fluxSortida.close();
        } catch (IOException e) {
            System.out.println("Erro cerrando Flujos " + e.getMessage());
        }
    }

    private int listarFicheros() throws IOException {
        File[] ficheros = _dir.listFiles();
        int ok = 0;
        int cont = 0;
        ArrayList<String> strArchivos = new ArrayList<>();
        ok = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (File fichero : ficheros) {
            if (!fichero.isHidden()) {
                cont++;
                strArchivos.add(String.format("id = %d - Nombre = %s  - Tamaño = %d - Fecha = %s", cont,
                        fichero.getName(),
                        fichero.length(),
                        sdf.format(fichero.lastModified())));
            }
        }
        if (cont == 0) {
            strArchivos.add("No existen ficheros");
        }
        escribirDatos(strArchivos.toString());
        return ok;
    }

    public void escribirDatos(int entero) {
        try {
            System.out.println(entero);
            dadesFluxSortida.write(entero);
        } catch (IOException e) {
            System.out.println("No se han podido escribir datos en el flujo");
        }
    }

    public void escribirDatos(String str) {
        try {
            System.out.println(str);
            dadesFluxSortida.writeUTF(str);
        } catch (IOException e) {
            System.out.println("No se han podido escribir datos en el flujo");
        }
    }
}
