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
/* Fitxer: Solucio.java                                                       */
/* Autor: Josep Lluís Berral i Garcia (e6979560@est.fib.upc.edu)              */
/* Data: 22/02/2005                                                           */
/* Versió: 2.0                                                                */
/*----------------------------------------------------------------------------*/

/**
 * Una Solució és un conjunt de (sub)grups que formen un horari trobat.
 * <p>
 * Una Solució esta implementada mitjançant una llísta dinàmica de grups, on
 * quan s'omple la taula, aquesta augmenta de tamany, i si disminueix s'escurça.
 *
 * @author      Josep Lluís Berral Garcia
 * @version     2.0
 * @since       2.0
 * @see         Grup
 * @see         LlistaSolucions
 */
public class Solucio {

/**
 * Variable d'emmagatzemament de la Longitud de les Dades contingudes
 */
	private int longitud;
/**
 * Variable d'emmagatzemament de l'espai de la Taula Dinàmica base
 */
	private int espai;
/**
 * Taula Dinàmica que emmagatzema les referencies a Grups
 */
	private Grup[] grups;

/**
 * Constructor de la Classe sense paràmetres. Crea una Solució buida, sense
 * Grups.
 * @see             Solucio#Solucio(Solucio)
 */
	public Solucio () {
		longitud = 0;
		espai = 10;
		grups = new Grup[espai];
	}
        
        public void print() {
            System.out.println("___________________________");
            System.out.println("S_longitud: "+longitud);
            System.out.println("S_espai: " + espai);
            for(int i=0;i< grups.length; ++i) {
                if(grups[i]!=null) {
                    Grup gs = grups[i];
                    gs.print();
                    while(gs.seguent() != null) {
                        gs = gs.seguent();
                        gs.print();
                    }
                }
            }
            
        }

/**
 * Constructor de la Classe per Còpia. Crea una Solució copiant una altra
 * Solucio <code>sol</code>.
 * @see             Solucio#Solucio()
 */
	public Solucio (Solucio sol) {
		longitud = sol.longitud();
		espai = sol.espai();
		grups = new Grup[espai];
		for (int i = 0; i < longitud; i++) {
			grups[i] = sol.grup(i);
		}
	}

/** 
 * Retorna el solapament que es produeix en l'horari format en aquesta solució.
 * <p>
 * El Solapament és la coincidència de dos o més grups en un mateix dia, i una
 * mateixa franja horària. En cas de coincidir 2, aquella hora té solapament 1,
 * en cas de coincidir més, el solapament és tantes hores coincidents -1.
 *
 * @return          El solapament de la solució
 * @see             Generador
 * @since           2.0
 */
	public int solapament() {
		int solapament = 0;
		for(int i = 0; i < 5; i++) for(int j = 0; j < 13; j++) {
			int aux = 0;
			for (int k = 0; k < longitud; k++){
				if (grups[k].hora(i,j) != '\0') aux++;
			}
			if (aux > 1) solapament += aux-1;
		}
		return solapament;
	}

/** 
 * Retorna el nombre de Grups que formen la Solucio
 *
 * @return          El nombre de Grups que formen la Solució
 * @see             #grup(int)
 * @see             #espai()
 * @see             #llistaGrups()
 * @see             Grup
 * @since           2.0
 */
	public int longitud() {
		return longitud;
	}

/** 
 * Retorna l'espai existent a la taula on s'implementa la llista dinàmica.
 *
 * @return          La longitud de la taula base de Grups
 * @see             #grup(int)
 * @see             #longitud()
 * @see             #llistaGrups()
 * @see             Grup
 * @since           2.0
 */
	public int espai() {
		return espai;
	}

/** 
 * Retorna el Grup de la solució corresponent a la posició <codi>i</codi>.
 * 
 * @param i         La posició del Grup a retornar
 *
 * @return          El Grup corresponent a la posició <codi>i</codi>.
 * @see             #longitud()
 * @see             #espai()
 * @see             #llistaGrups()
 * @see             Grup
 * @since           2.0
 */
	public Grup grup(int i) {
		if (i >= longitud) return null;
		return grups[i];
	}

/** 
 * Retorna la llista de Grups de la solució. Els Grups estan referenciats a la
 * llista.
 *
 * @return          La llista de referències a Grups de la solució
 * @see             #longitud()
 * @see             #espai()
 * @see             #grup(int)
 * @see             Grup
 * @since           2.0
 */
	public Grup[] llistaGrups() {
		return grups;
	}

/** 
 * Afegeix un Grup <codi>gr</codi> a la llista dinàmica de Grups. En cas de ser
 * tota la llista plena, incrementa el tamany de la taula base.
 * 
 * @param gr        La referència al Grup a afegir a la Solució.
 *
 * @see             #llistaGrups()
 * @see             Grup
 * @since           2.0
 */
	public void afegirGrup(Grup gr) {
		if (longitud == espai) {
			espai += 5;
			Grup[] aux = new Grup[espai];
			for(int i = 0; i < longitud; i++) {
				aux[i] = grups[i];
			}
			grups = aux;
		}
		grups[longitud] = gr;
		longitud++;
	}

/** 
 * Esborra el Grup referenciat per <codi>gr</codi> de la llista dinàmica de
 * Grups. En cas de disminuir el tamany de la llista fina cert nivell, la
 * llista es minva.
 * 
 * @param gr        Referència del Grup a esborrar
 *
 * @see             #llistaGrups()
 * @see             Grup
 * @since           2.0
 */
	public void esborrarGrup(Grup gr) {
		boolean b = false;
		int comptador = longitud - 1;
		while (comptador > -1) {
			if (grups[comptador] == gr) {
				for(int i = comptador; i < longitud; i++) {
					grups[i] = grups[i+1];
				}
				longitud--;
				comptador = -1;
			}
			comptador--;
		}
		if (espai - longitud > 5 && espai > 10) {
			espai -= 5;
			Grup[] aux = new Grup[espai];
			for(int i = 0; i < longitud; i++) {
				aux[i] = grups[i];
			}
			grups = aux;
		}
	}

/** 
 * Retorna la diferència entre hores de matí i hores de tarda de la solució. El
 * nivell pot ser positiu (majoritariament matí) o negatiu (majoritariament
 * tarda).
 *
 * @return          La diferència entre hores de matí i tarda
 * @see             Generador#matins(LlistaSolucions,int)
 * @since           2.0
 */
	public int nivell() {
		int comptador = 0;
		for(int i = 0; i < 5; i++) for(int j = 0; j < 13; j++) {
			for(int k = 0; k < longitud; k++) {
				if (grups[k].hora(i,j) != '\0') {
					if (j < 7) comptador++; else comptador--;
				}
			}
		}
		return comptador;
	}
}  