import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.converter.ConvertWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


public class TestTA {
    private Collection<TVertice> vertices;
    private Collection<TArista> aristas;

    private Collection <TVertice> verticesActores;
    private Collection <TArista> aristasActores;

    @BeforeEach
    void setUp() {
        TVertice vA = new TVertice("A");
        TVertice vB = new TVertice("B");
        TVertice vC = new TVertice("C");
        TVertice vD = new TVertice("D");
        TVertice vE = new TVertice("E");
        vertices = Arrays.asList(vA, vB, vC, vD, vE);
        aristas = new ArrayList<>();
        aristas.add(new TArista("A", "B", 2));
        aristas.add(new TArista("A", "C", 3));
        aristas.add(new TArista("B", "C", 1));
        aristas.add(new TArista("B", "D", 4));
        aristas.add(new TArista("C", "D", 5));
        aristas.add(new TArista("C", "E", 6));
        aristas.add(new TArista("D", "E", 7));

        TVertice vKevin=new TVertice("Kevin_Bacon");
        TVertice vJoseph=new TVertice("Joseph_Karp");
        TVertice vMark=new TVertice("Markov_Chain");
        TVertice vJohn=new TVertice("John_Travolta");
        verticesActores=Arrays.asList(vKevin,vJoseph,vMark,vJohn);
        aristasActores=new ArrayList<>();
        aristasActores.add(new TArista(vKevin.getEtiqueta(),vJoseph.getEtiqueta(),20));
        aristasActores.add(new TArista(vKevin.getEtiqueta(),vMark.getEtiqueta(),10));
        aristasActores.add(new TArista(vKevin.getEtiqueta(),vJohn.getEtiqueta(),7));
        aristasActores.add(new TArista(vJoseph.getEtiqueta(),vMark.getEtiqueta(),15));
        aristasActores.add(new TArista(vMark.getEtiqueta(),vJohn.getEtiqueta(),89));
        aristasActores.add(new TArista(vJohn.getEtiqueta(),vKevin.getEtiqueta(),76));

    }

    @Test
    public void testPrim(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        TGrafoNoDirigido mst=grafo.Prim();
        assertEquals(4,mst.getLasAristas().size());
    }
    @Test
    public void testPrim_No_Conectados(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, new LinkedList<>());
        TGrafoNoDirigido mst=grafo.Prim();
        assertEquals(0,mst.getLasAristas().size());
    }
    @Test
    public void testKruskal(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        TGrafoNoDirigido mst=grafo.Kruskal();
        assertEquals(4,mst.getLasAristas().size());
    }
    @Test
    public void testKruskal_NoConectado(){
        Collection<TVertice> vertices=Arrays.asList(new TVertice("A"), new TVertice("B"));
        Collection<TArista> aristas=new ArrayList<>();
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        TGrafoNoDirigido mst=grafo.Kruskal();
        assertEquals(0,mst.getLasAristas().size());
    }
    @Test
    public void testKruskal_NoCiclos(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        TGrafoNoDirigido mst=grafo.Kruskal();
        assertFalse(mst.tieneCiclo());
    }
    @Test
    public void testBacon(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(verticesActores, aristasActores);
        assertEquals(1,grafo.numBacon("Joseph_Karp"));
        assertEquals(1,grafo.numBacon("John_Travolta"));
        assertEquals(0,grafo.numBacon("Kevin_Bacon"));
        assertEquals(-1,grafo.numBacon("Al_Pacino"));
    }
    @Test
    public void testBea(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        Collection<TVertice> bea=grafo.bea("A");
        assertEquals(5,bea.size());
        boolean contieneA=false;
        boolean contieneB=false;
        boolean contieneC=false;
        boolean contieneD=false;
        boolean contieneE=false;
        for (TVertice vertice:bea) {
            if (vertice.getEtiqueta().equals("A")) contieneA=true;
            if (vertice.getEtiqueta().equals("B")) contieneB=true;
            if (vertice.getEtiqueta().equals("C")) contieneC=true;
            if (vertice.getEtiqueta().equals("D")) contieneD=true;
            if (vertice.getEtiqueta().equals("E")) contieneE=true;
        }
        assertTrue(contieneA);
        assertTrue(contieneB);
        assertTrue(contieneC);
        assertTrue(contieneD);
        assertTrue(contieneE);
    }
    @Test
    public void testEs_Conexo(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        assertTrue(grafo.esConexo());
    }
    @Test
    public void testEs_Conexo_No_Conectados(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, new LinkedList<>());
        assertFalse(grafo.esConexo());
    }
    @Test
    public void testConectados(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        TVertice vA=grafo.getVertices().get("A");
        TVertice vB=grafo.getVertices().get("B");
        assertTrue(grafo.conectados(vA, vB));
    }
    @Test
    public void testNo_Conectados(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        TVertice vA=grafo.getVertices().get("A");
        TVertice vE=grafo.getVertices().get("E");
        assertFalse(grafo.conectados(vA, vE));
    }
    @Test
    public void testArticulacion(){
        TGrafoNoDirigido grafo=new TGrafoNoDirigido(vertices, aristas);
        LinkedList<TVertice> puntosArticulacion=grafo.puntosArticulacion("A");
        assertEquals(0,puntosArticulacion.size());
    }
    @Test
    public void testArticulacionConPuntos(){
        TVertice vA=new TVertice("A");
        TVertice vB=new TVertice("B");
        TVertice vC=new TVertice("C");
        Collection<TVertice> verticesCadena=Arrays.asList(vA, vB, vC);
        Collection<TArista> aristasCadena=new ArrayList<>();
        aristasCadena.add(new TArista("A", "B", 1));
        aristasCadena.add(new TArista("B", "C", 1));
        TGrafoNoDirigido grafoCadena=new TGrafoNoDirigido(verticesCadena, aristasCadena);
        LinkedList<TVertice> articulaciones=grafoCadena.puntosArticulacion("A");
        assertEquals(1, articulaciones.size());
    }

}
