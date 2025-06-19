import java.util.*;

public class TGrafoNoDirigido extends TGrafoDirigido implements IGrafoNoDirigido, IGrafoKevinBacon{
    protected TAristas lasAristas = new TAristas();
    private int contador;

    /**
     *
     * @param vertices
     * @param aristas
     */
    public TGrafoNoDirigido(Collection<TVertice> vertices, Collection<TArista> aristas) {
        super(vertices, aristas);
        lasAristas.insertarAmbosSentidos(aristas);
        contador=0;

    }

    @Override
    public boolean insertarArista(TArista arista) {
        boolean tempbool = false;
        TArista arInv = new TArista(arista.getEtiquetaDestino(), arista.getEtiquetaOrigen(), arista.getCosto());
        tempbool = (super.insertarArista(arista) && super.insertarArista(arInv));
        return tempbool;
    }

    public TAristas getLasAristas() {
        return lasAristas;
    }

    @Override
    public TGrafoNoDirigido Prim() {
        if(this.getVertices().isEmpty()) return null;
        TGrafoNoDirigido mst=new TGrafoNoDirigido(this.getVertices().values(),new LinkedList<TArista>());
        Set<Comparable> visitados=new HashSet<>();
        PriorityQueue<TArista> pq=new PriorityQueue<>(Comparator.comparingDouble(TArista::getCosto));

        TVertice inicio=this.getVertices().values().iterator().next();
        visitados.add(inicio.getEtiqueta());
        for (TArista arista:lasAristas) {
            if (arista.getEtiquetaOrigen().equals(inicio.getEtiqueta()) && !visitados.contains(arista.getEtiquetaDestino())) {
                pq.add(arista);
            }
            if (arista.getEtiquetaDestino().equals(inicio.getEtiqueta()) && !visitados.contains(arista.getEtiquetaOrigen())) {
                pq.add(new TArista(arista.getEtiquetaDestino(), arista.getEtiquetaOrigen(), arista.getCosto()));
            }
        }

        while(!pq.isEmpty()&&visitados.size()<this.getVertices().size()) {
            TArista aristaMin=pq.poll();

            if(visitados.contains(aristaMin.getEtiquetaDestino())) continue;
            mst.insertarArista(aristaMin);
            Comparable nuevoVertice=aristaMin.getEtiquetaDestino();
            visitados.add(nuevoVertice);
            for(TArista arista:lasAristas) {
                if(arista.getEtiquetaOrigen().equals(nuevoVertice)&&!visitados.contains(arista.getEtiquetaDestino())) {
                    pq.add(arista);
                }
            }
        }
        return mst;
    }

    @Override
    public TGrafoNoDirigido Kruskal() {
        TGrafoNoDirigido mst=new TGrafoNoDirigido(this.getVertices().values(), new LinkedList<TArista>());
        LinkedList<TArista> aristasOrdenadas=new LinkedList<>(lasAristas);
        Collections.sort(aristasOrdenadas, new Comparator<TArista>() {
            @Override
            public int compare(TArista a1, TArista a2) {
                return Double.compare(a1.getCosto(), a2.getCosto());
            }
        });

        DisjointSet ds=new DisjointSet(this.getVertices().size());
        Map<Comparable, Integer> mapaIndices=new HashMap<>();
        int i = 0;
        for (TVertice v:this.getVertices().values()) {
            mapaIndices.put(v.getEtiqueta(), i++);
        }
        Set<String> aristasAgregadas=new HashSet<>();


        for (TArista arista:aristasOrdenadas) {
            String clave1=arista.getEtiquetaOrigen() + "-" + arista.getEtiquetaDestino();
            String clave2=arista.getEtiquetaDestino() + "-" + arista.getEtiquetaOrigen();
            if (aristasAgregadas.contains(clave1)||aristasAgregadas.contains(clave2)) continue;

            int origen=mapaIndices.get(arista.getEtiquetaOrigen());
            int destino=mapaIndices.get(arista.getEtiquetaDestino());

            if (ds.find(origen)!=ds.find(destino)) {
                mst.insertarArista(arista);
                ds.union(origen,destino);
                aristasAgregadas.add(clave1);
                if (mst.getLasAristas().size()==this.getVertices().size() - 1)
                    break;
            }
        }
        return mst;
    }

    @Override
    public int numBacon(Comparable actor) {
        desvisitarVertices();
        TVertice v = buscarVertice(actor);

        if(v != null){
            return v.caminosBacon("Kevin_Bacon", 0, Integer.MAX_VALUE);
        }
        return -1;
    }

    private class DisjointSet {
        private int[] padre,rango;
        public DisjointSet(int n) {
            padre=new int[n];
            rango=new int[n];
            for(int i=0;i<n;i++) padre[i]=i;
        }
        public int find(int x) {
            if(padre[x]!=x) padre[x]=find(padre[x]);
            return padre[x];
        }
        public void union(int x,int y) {
            int rx=find(x);
            int ry=find(y);
            if(rx==ry) return;
            if(rango[rx]<rango[ry]) padre[rx]=ry;
            else if(rango[ry]<rango[rx]) padre[ry]=rx;
            else { padre[ry]=rx; rango[rx]++; }
        }
    }

    @Override
    public Collection<TVertice> bea(Comparable etiquetaOrigen) {
        LinkedList<TVertice> visitados=new LinkedList<>();
        TVertice origen=getVertices().get(etiquetaOrigen);
        if(origen==null){
            return visitados; // vacía si no existe el vértice
        }
        Queue<TVertice> cola=new LinkedList<>();
        Set<Comparable> vistos=new HashSet<>();
        cola.add(origen);
        vistos.add(origen.getEtiqueta());

        while(!cola.isEmpty()){
            TVertice actual=cola.poll();
            visitados.add(actual);

            for(Object o:actual.getAdyacentes()){
                TAdyacencia ady=(TAdyacencia) o;
                TVertice verticeAdyacente=ady.getDestino();
                if(!vistos.contains(ady.getEtiqueta())){
                    cola.add(verticeAdyacente);
                    vistos.add(ady.getEtiqueta());
                }
            }
        }

        return visitados;
    }
    public boolean esConexo(){
        limpiarContador();
        for (TVertice vertice:getVertices().values()) {
            vertice.setVisitado(false);
        }
        TVertice primero=getVertices().values().iterator().next();
        dfsConContador(primero);
        if (contador!= getVertices().size()) {
            return false;
        }
        limpiarContador();
        TGrafoNoDirigido grafoTranspuesto= (TGrafoNoDirigido) construirTranspuesto();
        for (TVertice vertice:grafoTranspuesto.getVertices().values()) {
            vertice.setVisitado(false);
        }
        TVertice primeroTranspuesto=grafoTranspuesto.getVertices().get(primero.getEtiqueta());
        grafoTranspuesto.dfsConContador(primeroTranspuesto);
        return grafoTranspuesto.contador==grafoTranspuesto.getVertices().size();
    }
    private void limpiarContador(){
        contador = 0;
    }
    private void dfsConContador(TVertice vertice){
        vertice.setVisitado(true);
        contador++;
        for (Object o:vertice.getAdyacentes()) {
            TAdyacencia adyacencia= (TAdyacencia) o;
            TVertice adyacente= (TVertice) adyacencia.getDestino();
            if (!adyacente.getVisitado()) {
                dfsConContador(adyacente);
            }
        }
    }
    private TGrafoDirigido construirTranspuesto(){
        Collection<TVertice> nuevosVertices=new ArrayList<>();
        Map<Comparable, TVertice> mapaNuevosVertices=new HashMap<>();

        for (TVertice verticeOriginal:getVertices().values()) {
            TVertice nuevoVertice=new TVertice<>(verticeOriginal.getEtiqueta());
            nuevosVertices.add(nuevoVertice);
            mapaNuevosVertices.put(nuevoVertice.getEtiqueta(), nuevoVertice);
        }
        Collection<TArista> nuevasAristas=new ArrayList<>();
        for (TVertice verticeOriginal : getVertices().values()) {
            for (Object o:verticeOriginal.getAdyacentes()) {
                TAdyacencia adyacente=(TAdyacencia) o;
                Comparable origen=adyacente.getDestino().getEtiqueta();
                Comparable destino=verticeOriginal.getEtiqueta();
                double costo=adyacente.getCosto();
                nuevasAristas.add(new TArista(origen, destino, costo));
            }
        }
        return new TGrafoNoDirigido(nuevosVertices, nuevasAristas);
    }
    public boolean conectados(TVertice origen, TVertice destino){
        if(origen==null || destino==null){
            return false;
        }
        for (Object o:origen.getAdyacentes()) {
            TAdyacencia adyacencia=(TAdyacencia) o;
            if (adyacencia.getDestino().getEtiqueta().equals(destino.getEtiqueta())) {
                return true;
            }
        }
        for(Object o:origen.getAdyacentes()){
            TAdyacencia adyacencia=(TAdyacencia) o;
            if(adyacencia.getDestino().equals(destino)){
                return true;
            }
        }
        return false;
    }
    public LinkedList<TVertice> puntosArticulacion(Comparable etOrigen) {
        LinkedList<TVertice> puntosArticulacion=new LinkedList<>();
        TVertice origen=getVertices().get(etOrigen);
        if(origen==null){
            return puntosArticulacion; //vacia si no existe origen
        }

        Map<Comparable,Integer> tiempoDescubrimiento=new HashMap<>();
        Map<Comparable,Integer> tiempoBajo=new HashMap<>();
        Set<Comparable> visitados=new HashSet<>();
        Set<Comparable> articulaciones=new HashSet<>();
        int tiempo=0;

        dfsArticulacion(origen,null,tiempoDescubrimiento,tiempoBajo,visitados,articulaciones,tiempo);

        for(Comparable et:articulaciones){
            puntosArticulacion.add(getVertices().get(et));
        }

        return puntosArticulacion;
    }

    private void dfsArticulacion(TVertice u,TVertice padre, Map<Comparable,Integer> tiempoDescubrimiento, Map<Comparable,Integer> tiempoBajo, Set<Comparable> visitados, Set<Comparable> articulaciones, int tiempoActual){
        visitados.add(u.getEtiqueta());
        tiempoDescubrimiento.put(u.getEtiqueta(),tiempoActual);
        tiempoBajo.put(u.getEtiqueta(),tiempoActual);
        tiempoActual++;

        int hijos=0;

        for(Object o:u.getAdyacentes()){
            TAdyacencia ady=(TAdyacencia) o;
            TVertice v=ady.getDestino();
            if(!visitados.contains(v.getEtiqueta())){
                hijos++;
                dfsArticulacion(v,u,tiempoDescubrimiento,tiempoBajo,visitados,articulaciones,tiempoActual);

                tiempoBajo.put(u.getEtiqueta(),Math.min(tiempoBajo.get(u.getEtiqueta()),tiempoBajo.get(v.getEtiqueta())));

                if(padre!=null && tiempoBajo.get(v.getEtiqueta())>=tiempoDescubrimiento.get(u.getEtiqueta())){
                    articulaciones.add(u.getEtiqueta());
                }
            }else if(padre==null || !v.getEtiqueta().equals(padre.getEtiqueta())){
                tiempoBajo.put(u.getEtiqueta(),Math.min(tiempoBajo.get(u.getEtiqueta()),tiempoDescubrimiento.get(v.getEtiqueta())));
            }
        }

        if(padre==null && hijos>1){
            articulaciones.add(u.getEtiqueta());
        }
    }

}
