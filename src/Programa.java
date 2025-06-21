public class Programa {
    public static void main(String[] args) {
        TGrafoNoDirigido gnd = UtilGrafos.cargarGrafo("src/actores.csv", "src/en_pelicula.csv", false, TGrafoNoDirigido.class);

        String[] actores = {
                "John_Goodman",
                "Tom_Cruise",
                "Jason_Statham",
                "Lukas_Haas",
                "Djimon_Hounsou"
        };
        StringBuilder salida = new StringBuilder();

        for (String actor : actores) {
            int bacon = gnd.numBacon(actor);
            String linea = "Número de Bacon de " + actor + ": " + bacon;

            System.out.println(linea);
            salida.append(linea).append("\n");
        }

        String[] lineas = salida.toString().split("\n");
        ManejadorArchivosGenerico.escribirArchivo("salida.txt", lineas);
    }
}

