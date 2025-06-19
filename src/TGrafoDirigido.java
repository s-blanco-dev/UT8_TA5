import java.util.*;

public class TGrafoDirigido implements IGrafoDirigido {

    private final Map<Comparable, TVertice> vertices; // lista de vertices del grafo.-
    private Double[][] distanciasFloyd;
    private int[][] predecesoresFloyd;
    private Comparable origenTemporal;


    public TGrafoDirigido(Collection<TVertice> vertices, Collection<TArista> aristas) {
        this.vertices = new HashMap<>();
        for (TVertice vertice : vertices) {
            insertarVertice(vertice.getEtiqueta());
        }

    }

    /**
     * Metodo encargado de eliminar una arista dada por un origen y destino. En
     * caso de no existir la adyacencia, retorna falso. En caso de que las
     * etiquetas sean invalidas, retorna falso.
     *
     * @param nomVerticeOrigen
     * @param nomVerticeDestino
     * @return
     */
    @Override
    public boolean eliminarArista(Comparable nomVerticeOrigen, Comparable nomVerticeDestino) {
        if ((nomVerticeOrigen != null) && (nomVerticeDestino != null)) {
            TVertice vertOrigen = buscarVertice(nomVerticeOrigen);
            if (vertOrigen != null) {
                return vertOrigen.eliminarAdyacencia(nomVerticeDestino);
            }
        }
        return false;
    }

    /**
     * Metodo encargado de eliminar un vertice en el grafo. En caso de no
     * existir el v�rtice, retorna falso. En caso de que la etiqueta sea
     * inv�lida, retorna false.
     *
     * @param nombreVertice
     * @return
     */
    @Override
    public boolean eliminarVertice(Comparable nombreVertice) {
        if (nombreVertice != null) {
            getVertices().remove(nombreVertice);
            return getVertices().containsKey(nombreVertice);
        }
        return false;
    }

    /**
     * Metodo encargado de verificar la existencia de una arista. Las etiquetas
     * pasadas por par�metro deben ser v�lidas.
     *
     * @param etiquetaOrigen
     * @param etiquetaDestino
     * @return True si existe la adyacencia, false en caso contrario
     */
    @Override
    public boolean existeArista(Comparable etiquetaOrigen, Comparable etiquetaDestino) {
        TVertice vertOrigen = buscarVertice(etiquetaOrigen);
        TVertice vertDestino = buscarVertice(etiquetaDestino);
        if ((vertOrigen != null) && (vertDestino != null)) {
            return vertOrigen.buscarAdyacencia(vertDestino) != null;
        }
        return false;
    }

    /**
     * Metodo encargado de verificar la existencia de un vertice dentro del
     * grafo.-
     *
     * La etiqueta especificada como par�metro debe ser v�lida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a buscar.-
     * @return True si existe el vertice con la etiqueta indicada, false en caso
     *         contrario
     */
    @Override
    public boolean existeVertice(Comparable unaEtiqueta) {
        return getVertices().get(unaEtiqueta) != null;
    }

    /**
     * Metodo encargado de verificar buscar un vertice dentro del grafo.-
     *
     * La etiqueta especificada como parametro debe ser valida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a buscar.-
     * @return El vertice encontrado. En caso de no existir, retorna nulo.
     */
    public TVertice buscarVertice(Comparable unaEtiqueta) {
        return getVertices().get(unaEtiqueta);
    }

    /**
     * Matodo encargado de insertar una arista en el grafo (con un cierto
     * costo), dado su vertice origen y destino.- Para que la arista sea valida,
     * se deben cumplir los siguientes casos: 1) Las etiquetas pasadas por
     * parametros son v�lidas.- 2) Los vertices (origen y destino) existen
     * dentro del grafo.- 3) No es posible ingresar una arista ya existente
     * (miso origen y mismo destino, aunque el costo sea diferente).- 4) El
     * costo debe ser mayor que 0.
     *
     * @param arista
     * @return True si se pudo insertar la adyacencia, false en caso contrario
     */
    @Override
    public boolean insertarArista(TArista arista) {
        boolean tempbool = false;
        if ((arista.getEtiquetaOrigen() != null) && (arista.getEtiquetaDestino() != null)) {
            TVertice vertOrigen = buscarVertice(arista.getEtiquetaOrigen());
            TVertice vertDestino = buscarVertice(arista.getEtiquetaDestino());
            tempbool = (vertOrigen != null) && (vertDestino != null);
            if (tempbool) {
                // getLasAristas().add(arista);
                return vertOrigen.insertarAdyacencia(arista.getCosto(), vertDestino);
            }

        }
        return false;
    }

    /**
     * Metodo encargado de insertar un vertice en el grafo.
     *
     * No pueden ingresarse v�rtices con la misma etiqueta. La etiqueta
     * especificada como par�metro debe ser v�lida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a ingresar.
     * @return True si se pudo insertar el vertice, false en caso contrario
     */
    public boolean insertarVertice(Comparable unaEtiqueta) {
        if ((unaEtiqueta != null) && (!existeVertice(unaEtiqueta))) {
            TVertice vert = new TVertice(unaEtiqueta);
            getVertices().put(unaEtiqueta, vert);
            return getVertices().containsKey(unaEtiqueta);
        }
        return false;
    }

    @Override
    public boolean insertarVertice(TVertice vertice) {
        Comparable unaEtiqueta = vertice.getEtiqueta();
        if ((unaEtiqueta != null) && (!existeVertice(unaEtiqueta))) {
            getVertices().put(unaEtiqueta, vertice);
            return getVertices().containsKey(unaEtiqueta);
        }
        return false;
    }

    public Object[] getEtiquetasOrdenado() {
        TreeMap<Comparable, TVertice> mapOrdenado = new TreeMap<>(this.getVertices());
        return mapOrdenado.keySet().toArray();
    }

    @Override
    public void desvisitarVertices() {
        for (TVertice vertice : this.vertices.values()) {
            vertice.setVisitado(false);
        }
    }

    /**
     * @return the vertices
     */
    @Override
    public Map<Comparable, TVertice> getVertices() {
        return vertices;
    }

    @Override
    public Collection<TVertice> bpf(TVertice vertice) {
        Set<TVertice> visitados= new HashSet<>();
        List<TVertice> resultado= new ArrayList<>();
        bpf(vertice, visitados,resultado);
        return resultado;                                                                       // Tools | Templates.
    }
    private void bpf(TVertice vertice, Set<TVertice> visitados, List<TVertice> resultado) {
        visitados.add(vertice);
        resultado.add(vertice);

        for (Object o:vertice.getAdyacentes()) {
            TAdyacencia adyacencia= (TAdyacencia) o;
            TVertice adyacente= (TVertice) adyacencia.getDestino();
            if (!visitados.contains(adyacente)) {
                bpf(adyacente, visitados, resultado);
            }
        }
    }

    @Override
    public boolean tieneCiclo(TCamino camino) {
        desvisitarVertices();
        for (TVertice vertice:vertices.values()) {
            if (!vertice.getVisitado()) {
                LinkedList caminoActual=new LinkedList();
                caminoActual.add(vertice.getEtiqueta());
                if (vertice.tieneCiclo(caminoActual)) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public Collection<TVertice> bpf() {
        Set<TVertice> visitados=new HashSet<>();
        List<TVertice> resultado=new ArrayList<>();

        for (IVertice v:vertices.values()) {
            if (!visitados.contains(v)) {
                bpf((TVertice) v, visitados, resultado);
            }
        }
        return resultado;    }

    @Override
    public Collection<TVertice> bpf(Comparable etiquetaOrigen) {
        Collection<TVertice> resultado=new ArrayList<>();
        Set<Comparable> visitados=new HashSet<>();
        bpfRecursivo(etiquetaOrigen, visitados);
        for (Comparable v: visitados) {
            resultado.add(buscarVertice(v));
        }
        return resultado;
    }
    private void bpfRecursivo(Comparable actual, Set<Comparable> visitados) {
        visitados.add(actual);
        TVertice verticeActual=vertices.get(actual);
        if (verticeActual!=null) {
            for (Object o: verticeActual.getAdyacentes()) {
                TAdyacencia adyacente=(TAdyacencia) o;
                Comparable destino=adyacente.getDestino().getEtiqueta();
                if (!visitados.contains(destino)) {
                    bpfRecursivo(destino, visitados);
                }
            }
        }
    }
    @Override
    public Comparable centroDelGrafo() {
        Double excentricidadMinima=Double.MAX_VALUE;
        TVertice verticeCentro= null;
        for (TVertice vertice : this.vertices.values()) {
            Double excentricidadMaxima= (Double) obtenerExcentricidad(vertice.getEtiqueta());
            if(excentricidadMaxima<excentricidadMinima){
                excentricidadMinima=excentricidadMaxima;
                verticeCentro=vertice;
            }
        }
        if (verticeCentro!= null) {
            return verticeCentro.getEtiqueta();
        }
        return null;
    }

    @Override
    public Double[][] floyd() {
        int n= vertices.size();
        Object[] etiquetas=getEtiquetasOrdenado();
        distanciasFloyd= new Double[n][n];
        predecesoresFloyd=new int[n][n];
        for (int i= 0; i < n;i++) {
            for (int j= 0; j< n;j++) {
                if (i== j) {
                    distanciasFloyd[i][j]= 0.0;
                    predecesoresFloyd[i][j]= -1;
                } else {
                    IVertice vi= buscarVertice((Comparable) etiquetas[i]);
                    IAdyacencia ady=vi.buscarAdyacencia((Comparable) etiquetas[j]);
                    distanciasFloyd[i][j]=(ady != null) ? ady.getCosto(): Double.MAX_VALUE;
                    predecesoresFloyd[i][j]= (ady!=null) ? i:-1;
                }
            }
        }
        for (int k= 0; k < n; k++) {
            for (int i= 0; i < n; i++) {
                for (int j= 0; j < n; j++) {
                    if (distanciasFloyd[i][k]!= Double.MAX_VALUE && distanciasFloyd[k][j]!= Double.MAX_VALUE) {
                        double nuevo= distanciasFloyd[i][k] + distanciasFloyd[k][j];
                        if (nuevo< distanciasFloyd[i][j]) {
                            distanciasFloyd[i][j]= nuevo;
                            predecesoresFloyd[i][j]= predecesoresFloyd[k][j];
                        }
                    }
                }
            }
        }
        return distanciasFloyd;
    }

    @Override
    public Comparable obtenerExcentricidad(Comparable etiquetaVertice) {
        TVertice vertice= this.buscarVertice(etiquetaVertice);
        if (vertice== null) {
            return -1d;
        }

        Map<Comparable, Double> distancias= new HashMap<>();
        for (Comparable etiqueta: this.vertices.keySet()) {
            distancias.put(etiqueta, Double.POSITIVE_INFINITY);
        }
        distancias.put(etiquetaVertice,0d);

        PriorityQueue<TVertice> cola= new PriorityQueue<>(Comparator.comparingDouble(v -> distancias.get(v.getEtiqueta())));
        cola.add(vertice);

        while (!cola.isEmpty()) {
            TVertice actual= (TVertice) cola.poll();
            for (Object o: actual.getAdyacentes()) {
                TAdyacencia ady= (TAdyacencia) o;
                TVertice destino= ady.getDestino();
                double nuevoCosto= distancias.get(actual.getEtiqueta()) + ady.getCosto();
                if (nuevoCosto < distancias.get(destino.getEtiqueta())) {
                    distancias.put(destino.getEtiqueta(), nuevoCosto);
                    cola.add(destino);
                }
            }
        }
        double excentricidad= 0d;
        for (double d: distancias.values()) {
            if (d!= Double.POSITIVE_INFINITY && d>excentricidad) {
                excentricidad=d;
            }
        }

        return excentricidad;                                                              // Tools | Templates.
    }

    @Override
    public boolean[][] warshall() {
        int n= vertices.size();
        Object[] etiquetas= getEtiquetasOrdenado();
        boolean[][] accesibilidad= new boolean[n][n];

        for (int i= 0; i< n; i++) {
            IVertice vi= buscarVertice(etiquetas[i].toString());
            for (int j= 0; j < n;j++) {
                if (i== j) {
                    accesibilidad[i][j]= true;
                } else {
                    IAdyacencia ady= vi.buscarAdyacencia((Comparable) etiquetas[j]);
                    accesibilidad[i][j]= (ady != null);
                }
            }
        }
        for (int k= 0; k < n; k++) {
            for (int i= 0; i < n; i++) {
                for (int j= 0; j < n; j++) {
                    accesibilidad[i][j]= accesibilidad[i][j] || (accesibilidad[i][k] && accesibilidad[k][j]);
                }
            }
        }

        return accesibilidad;
    }

    @Override
    public TCaminos todosLosCaminos(Comparable etiquetaOrigen, Comparable etiquetaDestino) {
        TCaminos todosLosCaminos = new TCaminos();
        TVertice v = buscarVertice(etiquetaOrigen);

        if(v != null){
            TCamino caminoPrevio = new TCamino(v);
            v.todosLosCaminos(etiquetaDestino, caminoPrevio, todosLosCaminos);
            return todosLosCaminos;
        }
        return null;
    }


    @Override
    public boolean tieneCiclo(Comparable etiquetaOrigen) {
        desvisitarVertices();
        boolean res=false;

        for (TVertice vertV:vertices.values()) {
            if (!vertV.getVisitado()) {
                LinkedList camino=new LinkedList();
                camino.add(vertV.getEtiqueta());
                res=((TVertice) vertV).tieneCiclo(camino);
                return true;
            }
        }
        return res;
    }

    @Override
    public boolean tieneCiclo() {
        desvisitarVertices();
        boolean res=false;

        for (TVertice vertV:vertices.values()) {
            if (!vertV.getVisitado()) {
                LinkedList camino=new LinkedList();
                camino.add(vertV.getEtiqueta());
                res=((TVertice) vertV).tieneCiclo(camino);
                if (res) {
                    return true;
                }
            }
        }
        return res;    }

    @Override
    public Collection<TVertice> bea() {
        Set<TVertice> visitados= new HashSet<>();
        List<TVertice> resultado= new ArrayList<>();
        for (TVertice vertice : vertices.values()) {
            if (!visitados.contains(vertice)) {
                beaDesdeVertice(vertice,visitados, resultado);
            }
        }
        return resultado;
    }
    private void beaDesdeVertice(TVertice origen, Set<TVertice> visitados, List<TVertice> resultado) {
        Queue<TVertice> cola= new LinkedList<>();
        cola.add(origen);
        visitados.add(origen);
        while (!cola.isEmpty()) {
            TVertice actual= cola.poll();
            resultado.add(actual);
            for (Object o:actual.getAdyacentes()) {
                TAdyacencia ady= (TAdyacencia) o;
                TVertice vecino= ady.getDestino();
                if (!visitados.contains(vecino)) {
                    cola.add(vecino);
                    visitados.add(vecino);
                }
            }
        }
    }

}