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
/* Fitxer: LlistaSolucions.java                                               */
/* Autor: Josep Lluís Berral i Garcia (e6979560@est.fib.upc.edu)              */
/* Data: 03/03/2005                                                           */
/* Versió: 2.1                                                                */
/*----------------------------------------------------------------------------*/

/**
 * Una LlistaSolucions és un conjunt de solucions.
 * <p>
 * La Llista de Solucions esta implementada amb taules dinàmiques, de manera
 * que en cas d'omplir.se la llista, aquesta augmenta de tamany, i en cas de
 * buidar-se, aquesta es redueix.
 *
 * @author      Josep Lluís Berral Garcia
 * @version     2.1
 * @since       2.0
 * @see         Generador
 * @see         Solucio
 */
public class LlistaSolucions {

/**
 * Variable d'emmagatzemament de la Longitud de les Dades contingudes
 */
	private int longitud;
/**
 * Variable d'emmagatzemament de l'espai de la Taula Dinàmica base
 */
	private int espai;
/**
 * Taula Dinàmica que emmagatzema les referències a Solucions
 */
	private Solucio [] llista;

/**
 * Constructor de la Classe. Crea una Solució buida, sense Solucions.
 * @see             Solucio
 */
	public LlistaSolucions () {
		longitud = 0;
		espai = 100;
		llista = new Solucio[100];
	}

/** 
 * Retorna el nombre de solucions contingudes a la Llista de Solucions.
 *
 * @return          El nombre de solucions a la llista.
 * @see             #solucioNum(int)
 * @see             #espai()
 * @since           2.0
 */
	public int longitud() {
		return longitud;
	}

/** 
 * Retorna la longitud total de la llista dinàmica que conté les referències a
 * les solucions.
 *
 * @return          La longitud de la llista dinàmica de solucions
 * @see             #longitud()
 * @since           2.0
 */
	public int espai() {
		return espai;
	}

/** 
 * Afegeix la refercia a una Solució <code>sol</code> a la llista de solucions.
 * <p>
 * En cas d'omplir-se la taula base, aquesta augmenta de tamany.
 *
 * @param sol       És la referència a la solució a inserir a la taula.
 * @see             #esborrarSol(int)
 * @since           2.0
 */
	public void afegirSol(Solucio sol) {
		if (longitud >= espai) {
			espai += 100;
			Solucio[] aux = new Solucio[espai];
			for (int i = 0; i < longitud; i++) aux[i] = llista[i];
			llista = aux;
		}
		llista[longitud] = new Solucio(sol);
		longitud++;
	}

/** 
 * Treu de llista de solucions la refercia a una Solució amb posició
 * <code>pos</code>.
 * <p>
 * En cas de quedar massa reduida la taula base, aquesta disminueix de tamany.
 *
 * @param pos       És la posició de la solució a la taula a treure.
 * @see             #afegirSol(Solucio)
 * @since           2.0
 */
	public void esborrarSol(int pos) {
		if (pos >= longitud) return;
		longitud--;
		llista[pos] = llista[longitud];
		if (espai - longitud > 100 && espai > 100) {
			espai -= 100;
			Solucio[] aux = new Solucio[espai];
			for(int i = 0; i < longitud; i++) aux[i] = llista[i];
			llista = aux;
		}
	}

/** 
 * Retorna la Solució situada a la posició <code>pos</code>.
 * <p>
 * Si la posició és superior a la longitud <code>longitud()</code> de la llista
 * retorna null.
 *
 * @param pos       És la posició de la solució a retornar.
 *
 * @return          El solapament de la solució.
 * @see             #longitud()
 * @see             Solucio
 * @since           2.0
 */
	public Solucio solucioNum(int pos) {
		if (pos >= longitud) return null;
		return llista[pos];
	}

/** 
 * Reinicia completament la Llista de Solucions, desreferenciant a totes les
 * solucions incloses a la llista.
 *
 * @see             #LlistaSolucions()
 * @see             #copia(LlistaSolucions)
 * @since           2.0
 */
	public void destrueix() {
		longitud = 0;
		espai = 100;
		llista = new Solucio[100];
	}

/** 
 * Reinicia completament la Llista de Solucions, i copia tota la Llista de
 * Solucions a partir de LlistaSolucions <code>llis</code>.
 * <p>
 *
 * @param llis      És la llista a copiar després de reiniciar la Llista.
 *
 * @see             #LlistaSolucions()
 * @see             #destrueix()
 * @since           2.0
 */
	public void copia (LlistaSolucions llis) {
		longitud = llis.longitud();
		espai = llis.espai();
		llista = new Solucio[espai];
		for (int i = 0; i < longitud; i++)  {
			llista[i]= new Solucio(llis.solucioNum(i));
		}
	}
}
