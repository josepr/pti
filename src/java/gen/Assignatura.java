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
/* Fitxer: Assignatura.java                                                   */
/* Autor: Josep Lluís Berral i Garcia (e6979560@est.fib.upc.edu)              */
/* Data: 22/02/2005                                                           */
/* Versió: 2.0                                                                */
/*----------------------------------------------------------------------------*/

/**
 * Assignatura és la representació d'una assignatura amb tots els grups que la
 * formen, tant grups com subgrups.
 * <p>
 * Les assignatures estan implementades amb una llista de nodes encadenats, per
 * el qual, una Assignatura té un nom, una longitud, i una referència al primer
 * de la llista de (sub)grups.
 *
 * @author      Josep Lluís Berral Garcia
 * @version     2.0
 * @since       2.0
 */
public class Assignatura {

/**
 * Variable que emmagatzema el nom de l'Assignatura
 */
	private String nom;
/**
 * Variable que emmagatzema la Longitud de la llista de Nodes Encadenats
 */
	private int longitud;
/**
 * Referència al primer Node (grup) de la llista
 */
	private Grup inici;

/**
 * Constructor de la Classe. Especifica el nom de l'assignatura, i crea una
 * Assignatura sense (sub)grups.
 * @see             Grup#Grup(int,Grup,String)
 */
	public Assignatura(String n) {
		nom = n;
		longitud = 0;
		inici = new Grup(-1,null,"");
	}

/** 
 * Retorna el nom de l'assignatura.
 *
 * @return          El nom de l'assignatura.
 * @see             #setNom(String)
 * @since           2.0
 */
	public String nom() {
		return nom;
	}

/** 
 * Retorna el nombre de (sub)grups que conté l'assignatura.
 *
 * @return          El nombre de (sub)grups de l'assignatura.
 * @see             #grupNum(int)
 * @since           2.0
 */
	public int longitud() {
		return longitud;
	}

/** 
 * Retorna el Grup que encapçala la llista de (sub)grups
 *
 * @return          El nombre de (sub)grups de l'assignatura.
 * @see             #grupNum(int)
 * @since           2.0
 */
	public Grup inici() {
		return inici;
	}

/** 
 * Assigna a un nom <code>n</code> a l'assignatura.
 *
 * @param n         És el nom de l'Assignatura
 * @see             #nom()
 * @since           2.0
 */
	public void setNom(String n) {
		nom = n;
	}

/** 
 * Afegeix un grup buit a la llista de Grups de l'Assignatura.
 * <p>
 * Els grups son insertats amb ordre de valor (número de (sub)grup). No s'admet
 * a la llista dos grups amb valors repetits. En cas de fer-se, prevaleix el
 * primer en ser inserit.
 *
 * @param v         És el número de grup
 * @return          Retorna la referència al grup acabat d'afegir
 * @see             #esborrarGrup(int)
 * @see             Grup#Grup(int,Grup,String)
 * @since           2.0
 */
	public Grup afegirGrup(int v) {
		Grup aux1 = inici;
		while(aux1.seguent() != null){
			Grup aux2 = aux1.seguent();
			if (aux2.valor() == v) return aux2;
			if (aux2.valor() > v) {
				aux1.setSeguent(new Grup(v,aux2,nom));
				longitud++;
				return aux1.seguent();
			}
			aux1 = aux2;
		}
		aux1.setSeguent(new Grup(v,null,nom));
		longitud++;
		return aux1.seguent();
	}

/** 
 * Elimina de la llista de Grups de l'Assignatura el Grup amb valor
 * <code>v</code>. En cas de no trobar-se, no fa res.
 *
 * @param v         És el número de grup
 * @see             #afegirGrup(int)
 * @see             Grup#Grup(int,Grup,String)
 * @since           2.0
 */
	public void esborrarGrup (int v) {
		Grup aux1 = inici;
		while(aux1.seguent() != null){
			Grup aux2 = aux1.seguent();
			if (aux2.valor() == v) {
				aux1.setSeguent(aux2.seguent());
				longitud--;
				return;
			}
			if (aux2.valor() > v) return;
			aux1 = aux2;
		}
	}

/** 
 * Retorna de la llista de Grups de l'Assignatura la referència al Grup a la
 * posició <code>pos</code>. En cas de no trobar-se, retorna <code>null</code>.
 *
 * @param pos       És la posició del grup
 * @see             #inici()
 * @see             Grup#Grup(int,Grup,String)
 * @since           2.0
 */
	public Grup grupNum(int pos) {
		Grup aux1 = inici;
		int comptador = 0;
		while(aux1.seguent() != null){
			Grup aux2 = aux1.seguent();
			if (comptador == pos) return aux2;
			aux1 = aux2;
			comptador++;
		}
		return null;
	}

/** 
 * Afegeix un tipus de classe al Grup amb posició <code>pos</code>, al dia
 * <code>dia</code> i a l'hora <code>hora</code>, amb valor <cpde>tipus</tipus>.
 *
 * @param pos       És la posició del grup
 * @param dia       És el nombre de dia codificat del 0 al 4
 * @param hora      És l'hora del dia codificada del 0 al 12
 * @param tipus     És el tipus de classe que es dona ('\0','T','P' o 'L')
 * @see             Grup#Grup(int,Grup,String)
 * @since           2.0
 */
	public void afegirHora(int pos, int dia, int hora, char tipus) {
		Grup aux = grupNum(pos);
		if (aux != null) {
			aux.afegeixHora(dia,hora,tipus);
		}
	}
	
/** 
 * Retorna un Array amb el número dels grups que integren l'assignatura.
 *
 * @return          La llista de números de (sub)grups que formen l'assignatura
 * @see             Grup#valor()
 * @since           2.0
 */
	public int[] llistaGrups() {
		Grup aux1 = inici;
		int[] ret =  new int[longitud];
		int comptador = 0;
		while(aux1.seguent() != null){
			ret[comptador] = aux1.seguent().valor();
			comptador++;
			aux1 = aux1.seguent();
		}
		return ret;
	}
}

