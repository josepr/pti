package gen;


/*  Horaris - Generador d'horaris de la FIB
 *  Copyright (C) 2004, 2005  Josep Lluís Berral Garcia
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

 
/*----------------------------------------------------------------------------*/
/* Fitxer: Interficie.java                                                   */
/* Autor: Alvaro Guinda Rivero (alvaro.guinda@est.fib.upc.edu)                */
/* Data: 8/4/2011                                                             */
/* Versió: 3.0                                                                */
/*----------------------------------------------------------------------------*/

/**
 * La Classe Interficie s'encarrega connectar la Interfície Gràfica i el Domini
 * del programa. És iniciada per l'Applet o Programa Gràfic i gestiona la
 * ceració dels objectes i estructures de dades, i porta el fluxe principal del
 * programa.
 * <p>
 * La Classe Interficie porta a terme quatre funcionalitats de connexió amb la
 * capa gràfica:
 * <ul>
 * <li>Noms d'Assignatures: Connecta la GUI amb la Classe Analitzador perque es
 * puguin mostrar per pantalla les assignatures disponibles des del Web de la
 * FIB.
 * <li>Cercar Horaris: Inicia la cerca d'horaris amb els parametes obtinguts de
 * l'usuari a partir de la GUI.
 * <li>Nombre de Solucions: Indica a la GUI si s'han trobat solucions o s'ha
 * produit un error.
 * <li>Publicar Solucions: Formata les solucions trobades com a pàgina Web on
 * es mostren els horaris i els enllaços a la pàgina Web de la FIB
 * </ul>
 *
 * @author      Alvaro Guinda Rivero
 * @version     3.0
 * @since       2.0
 * @see         Generador
 * @see         Analitzador
 * @see         LlistaSolucions
 * @see         Assignatura
 * @see         Grup
 * @see         AppletHoraris
 */
public class Interficie {

/**
 * Objecte Analitzador.
 */
  private Analitzador pars = new Analitzador();
/**
 * Llista de Solucions per emmagatzemar les solucions.
 */
  private LlistaSolucions sol = null;
/**
 * Llista d'Assignatures per emmagatzemar les Sigles demanades per l'usuari.
 */
  private Assignatura [] assigs = null;
/**
 * Conjunt de Cadenes que identifiquen les Franjes Horàries.
 */
  private String franjes[] = {"08:00","09:00","10:00","11:00","12:00","13:00",
    "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00"};

  public Interficie(){}
  
/** 
 * Retorna la Llista de noms d'Assignatures disponibles al Web de la FIB.
 * <p>
 * Usa l'objecte Analitzador per obtenir del Web de la FIB la llista
 * d'assignatures disponibles per al Quadrimestre.
 *
 * @return          Llista de noms de les Assignatures
 * @see             Analitzador
 * @since           2.0
 */
  public String[] assignatures(String codi) {
    return pars.obtencioAssignatures(codi);
  }

/** 
 * Configura el Generador a partir de les dades obtingudes de l'usuari, obté
 * de l'Analitzador la llista d'Assignatures amb els horaris complets, i Inicia
 * el Generador.
 * <p>
 * El resultat queda emmagatzemat a l'objecte LlistaSolucions.
 * 
 * @param noms      Llista de noms d'assignatures seleccionades per l'usuari.
 * @param hores     Taula amb les Hores Prohibides indicades per l'usuari.
 * @param mat       Indica si s'ha activat les opcions de Mati/Tarda.
 * @param tol       Conté la tolerància introduida per l'usuari.
 * @param maxim     Conté el Solapament Màxim introduit per l'usuari.
 * @param millor    Indica si l'usuari ha activat l'opció Millor Solapament.
 * @param filtres   Llista de filtres respecte els grups "Assig:NumGrup"
 *
 * @return          String amb els avisos sorgits del FIltre d'Assignatures
 * @see             Analitzador
 * @see             Generador
 * @see             LlistaSolucions
 * @since           2.0
 */
  public String inicia(String noms[],boolean[][] hores, boolean[] mat, int tol,
    int maxim, boolean millor, String[] filtres) {
    /* Obtenció de Dades */
    Generador gen = new Generador();
    if ((assigs = pars.obtencioDades(noms)) == null) {
      return " (Alerta! No s'han pogut obtenir les dades!)";
    }
    /* Configuració del Generador */
    gen.setHoresProh(hores);
    gen.setTolerancia(tol);
    gen.setMillorSolap(millor);
    gen.setMatins(mat[0]);
    gen.setTardes(mat[1]);
    gen.setFiltres(filtres);
    if (!millor) gen.setMaximSolap(maxim);
    /* Filtrat de les Assignatures */
    String aux = gen.filtrar(assigs);
    /* Generació de Solucions */
    sol = gen.generar(assigs);
    return aux;
  }

/** 
 * Retorna el nombre de solucions trobades pel generador amb les condicions
 * indicades per l'usuari.
 * <p>
 * En cas de produir-se un error, el retorn és "-1"
 *
 * @return          Nombre de solucions trobades pel Generador
 * @see             Solucio
 * @since           2.0
 */
  public int numSolucions() {
    if (sol == null) return -1;
    return sol.longitud();
  }

/** 
 * Retorna la llista de Solucions en format Web.
 * <p>
 * El format de les solucions son en forma de taules, amb el solapament
 * de cadascuna de les solucions i un enllaç al web de la FIB corresponent a
 * l'horari triat.
 *
 * @param any       Any del curs Actual
 * @param sem       Quadrimestre Actual
 *
 * @return          Cadena del Web a mostrar amb les Solucions
 * @see             Solucio
 * @see             #capceleraFIB()
 * @see             #tancaFIB()
 * @since           2.0
 */
  public String publicaWeb (int any, int sem) {
    if (sol == null) return null;
    String buffer = "<html><body>\n";
    buffer += "\t<p><h4>Resultats trobats:</h4></p>\n";
    System.out.println("Sol.long: "+sol.longitud());
    for (int i = 0; i < sol.longitud(); i++) {
        System.out.println("i: "+i);
        System.out.println("Sol.Num:");
        Solucio soool = sol.solucioNum(i);
        soool.print();
        System.out.println("Final Sol.num");
      buffer += webSolucio(soool,any,sem) + "\n\t<hr>\n";
    }
    buffer += "\t<p>Solucions Trobades: " + sol.longitud() + "</p>\n";
    return buffer + "</body></html>";
  }

/** 
 * Retorna la llista d'una Solució amb format web.
 * <p>
 * El format de les solucions son en forma de taules, amb el solapament
 * de cadascuna de les solucions i un enllaç al web de la FIB corresponent a
 * l'horari triat.
 * <p>
 * Per futures reformes del web de la FIB, cal indicar que aquesta funció
 * inclou un Link al Web de la FIB.
 *
 * @param sol       Solució a Formatar
 * @param any       Any del curs Actual
 * @param sem       Quadrimestre Actual
 *
 * @return          Cadena formatada Web de la solució per paràmetre.
 * @see             Analitzador
 * @since           2.0
 */
  private String webSolucio(Solucio sol, int any, int sem) {
    String buffer = "\t<table border=1>\n";
    buffer += "\t\t<tr><th>&nbsp;</th><th>Dilluns</th><th>Dimarts</th>";
    buffer += "<th>Dimecres</th><th>Dijous</th><th>Divendres</th></tr>\n";
    for (int j = 0; j < 13; j++) {
      buffer += "\t\t<tr><td>" + franjes[j] + "-" + franjes[j+1] + "</td>";
      for (int i = 0; i < 5; i++) {
        buffer += "<td>";
        boolean buit = true;
        for (int k = 0; k < sol.longitud(); k++) {
          if (sol.grup(k).hora(i,j) != '\0') {
            buffer += sol.grup(k).nom() + " " + sol.grup(k).valor();
            buffer += " " + sol.grup(k).hora(i,j);
            buit = false;
            if (k < sol.longitud()-1) buffer += "<br>";
          }
        }
        if (buit) buffer += "&nbsp;";
        buffer += "</td>";
      }
      buffer += "</tr>\n";
    }
    buffer += "\t</table>\n\t<h5>";
    String adreca = "\t<h5><a href=\"https://raco.fib.upc.edu/api/";
    //adreca += "PUB_Horaris.pinta_els_subgrups2b?anyet=";
    //adreca += any + "&semestre_in=" + sem + "&a=0";
    for (int i = 0; i < sol.longitud(); i++) {
      Grup aux = sol.grup(i);
      buffer += "[" + aux.nom() + "," + aux.valor() + "]";
      adreca += "&a=" + aux.nom() + "%7C" + aux.valor();
      if (aux.valor()%10 != 0) {
        adreca += "&a=" + aux.nom() + "%7C" + ((aux.valor()/10)*10);
      }
    }
    buffer += "<p>Hores Solapades: " + sol.solapament() + "</p></h5>\n";
    adreca += "\" target=\"_top\"> Veure aquest Horari al web de la FIB";
    adreca += "</a></h5>";
    return buffer + adreca;
  }
}