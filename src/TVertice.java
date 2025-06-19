import java.util.Collection;
import java.util.LinkedList;

public class TVertice<T> implements IVertice {

    private Comparable etiqueta;
    private LinkedList<TAdyacencia> adyacentes;
    private boolean visitado;
    private T datos;

    public Comparable getEtiqueta() {
        return etiqueta;
    }

    public LinkedList<TAdyacencia> getAdyacentes() {
        return adyacentes;
    }

    public T getDatos() {
        return datos;
    }

    public TVertice(Comparable unaEtiqueta) {
        this.etiqueta = unaEtiqueta;
        adyacentes = new LinkedList();
        visitado = false;
    }

    public void setVisitado(boolean valor) {
        this.visitado = valor;
    }

    public boolean getVisitado() {
        return this.visitado;
    }

    @Override
    public TAdyacencia buscarAdyacencia(TVertice verticeDestino) {
        if (verticeDestino != null) {
            return buscarAdyacencia(verticeDestino.getEtiqueta());
        }
        return null;
    }

    @Override
    public Double obtenerCostoAdyacencia(TVertice verticeDestino) {
        TAdyacencia ady = buscarAdyacencia(verticeDestino);
        if (ady != null) {
            return ady.getCosto();
        }
        return Double.MAX_VALUE;
    }

    @Override
    public boolean insertarAdyacencia(Double costo, TVertice verticeDestino) {
        if (buscarAdyacencia(verticeDestino) == null) {
            TAdyacencia ady = new TAdyacencia(costo, verticeDestino);
            return adyacentes.add(ady);
        }
        return false;
    }

    @Override
    public boolean eliminarAdyacencia(Comparable nomVerticeDestino) {
        TAdyacencia ady = buscarAdyacencia(nomVerticeDestino);
        if (ady != null) {
            adyacentes.remove(ady);
            return true;
        }
        return false;
    }

    @Override
    public TVertice primerAdyacente() {
        if (this.adyacentes.getFirst() != null) {
            return this.adyacentes.getFirst().getDestino();
        }
        return null;
    }

    @Override
    public TAdyacencia buscarAdyacencia(Comparable etiquetaDestino) {
        for (TAdyacencia adyacencia : adyacentes) {
            if (adyacencia.getDestino().getEtiqueta().compareTo(etiquetaDestino) == 0) {
                return adyacencia;
            }
        }
        return null;
    }

    @Override
    public void bpf(Collection<TVertice> visitados) {
        if (!visitados.contains(this)) {
            visitados.add(this);
            for (Object o:this.getAdyacentes()) {
                TAdyacencia adyacencia=(TAdyacencia) o;
                TVertice destino=(TVertice) adyacencia.getDestino();
                if (!visitados.contains(destino)) {
                    destino.bpf(visitados);
                }
            }
        }
    }

   @Override
    public TCaminos todosLosCaminos(Comparable etVertDest, TCamino caminoPrevio, TCaminos todosLosCaminos) {
    this.setVisitado(true);
    for (TAdyacencia adyacencia : this.getAdyacentes()) {
        TVertice destino = adyacencia.getDestino();
        if (!destino.getVisitado()) {
            if (destino.getEtiqueta().compareTo(etVertDest) == 0) {
                TCamino copia = caminoPrevio.copiar();
                copia.agregarAdyacencia(adyacencia);
                todosLosCaminos.getCaminos().add(copia);
            } else {
                TCamino copia = caminoPrevio.copiar();
                copia.agregarAdyacencia(adyacencia);
                destino.todosLosCaminos(etVertDest, copia, todosLosCaminos);
            }
        }
    }
        this.setVisitado(false);
        return todosLosCaminos;
    }

    public int caminosBacon(Comparable etVertDest, int profundidadActual, int caminoMasCorto) {
        this.setVisitado(true);

        if (this.getEtiqueta().compareTo(etVertDest) == 0) {
            this.setVisitado(false);
            return Math.min(profundidadActual, caminoMasCorto);
        }

        for (TAdyacencia adyacencia : this.getAdyacentes()) {
            TVertice destino = adyacencia.getDestino();
            if (!destino.getVisitado()) {
                int posibleCamino = destino.caminosBacon(etVertDest, profundidadActual + 1, caminoMasCorto);
                caminoMasCorto = Math.min(caminoMasCorto, posibleCamino);
            }
        }

        this.setVisitado(false);
        return caminoMasCorto;
    }


    @Override
    public void bea(Collection<TVertice> visitados) {
        if (!visitados.contains(this)) {
            visitados.add(this);
            for (Object o:this.getAdyacentes()) {
                TAdyacencia adyacencia=(TAdyacencia) o;
                TVertice destino=(TVertice) adyacencia.getDestino();
                if (!visitados.contains(destino)) {
                    destino.bea(visitados);
                }
            }
        }
    }

    @Override
    public TVertice siguienteAdyacente(TVertice w) {
        TAdyacencia adyacente=buscarAdyacencia(w.getEtiqueta());
        int index=adyacentes.indexOf(adyacente);
        if (index + 1 < adyacentes.size()) {
            return adyacentes.get(index + 1).getDestino();
        }
        return null;
    }

    @Override
    public boolean tieneCiclo(LinkedList<Comparable> camino) {
        this.setVisitado(true);
        for (TAdyacencia ady : adyacentes) {
            TVertice destino= ady.getDestino();
            Comparable etiquetaDestino= destino.getEtiqueta();
            if (camino.contains(etiquetaDestino)){
                return true;
            }
            if (!destino.getVisitado()) {
                camino.add(etiquetaDestino);
                if (destino.tieneCiclo(camino)) {
                    return true;
                }
                camino.removeLast();
            }
        }
        return false;
    }

    @Override
    public boolean conectadoCon(TVertice destino) {
        if (destino != null) {
            return buscarAdyacencia(destino.getEtiqueta()) != null;
        }
        return false;
    }
    @Override
    public String toString(){
        return this.etiqueta.toString();
    }
    public void setAdyacentes(LinkedList<TAdyacencia> adyacentes) {
        this.adyacentes = adyacentes;
    }

}
