package es.httpserver.common;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by User: admin
 * Date: 19/10/2017
 * Time: 11:16
 */
public class Utils {

    public static List<String> splitFiledsWithDelimeter(String listaConDelimitadores, String delimitador) {

        List<String> listaSeparadaSinDelimitadores = new Vector<>();
        StringTokenizer serviceTokenizer = new StringTokenizer(listaConDelimitadores, delimitador);
        while (serviceTokenizer.hasMoreElements()) {
            listaSeparadaSinDelimitadores.add(serviceTokenizer.nextToken().trim());
        }
        return listaSeparadaSinDelimitadores;
    }

}
