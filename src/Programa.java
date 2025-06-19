public class Programa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // cargar grafo con actores y relaciones
        TGrafoNoDirigido gnd = UtilGrafos.cargarGrafo("src/actores.csv", "src/en_pelicula.csv",false, TGrafoNoDirigido.class);
        System.out.println(gnd.numBacon("Harrison_Ford"));
        System.out.println(gnd.numBacon("Jason_Statham"));
        // invocar a numBacon como indica la letra y mostrar en consola el resultado
    }

}
