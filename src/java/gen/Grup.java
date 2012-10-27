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
/* Fitxer: Grup.java                                                          */
/* Autor: Josep Lluís Berral i Garcia (e6979560@est.fib.upc.edu)              */
/* Data: 22/02/2005                                                           */
/* Versió: 2.1                                                                */
/*----------------------------------------------------------------------------*/

/**
 * Grup és la representació d'un (sub)grup d'una assignatura. Es composa d'un
 * nom (el nom donat per l'assignatura), un valor (numero de subgrup), una
 * taula de caràcters 5x13 que emmagatzema l'horari del subgrup, i d'una
 * referència al seguent grup.
 * <p>
 * Les assignatures es composen de grups, i aquests estan implementats en una
 * llista de nodes encadenats, on cada node és un grup. D'aqui que un grup
 * tingui una referència al seguent.
 * <p>
 * El tipus de classe que es dona en aquest grup s'indica mitjançant un
 * caràcter posat a la taula d'horais de la següent manera:
 * <ul>
 * <li> '\0' si no hi ha classe d'aquest grup en aquesta hora
 * <li> 'T' si hi ha teoria
 * <li> 'P' si hi ha problemes
 * <li> 'L' si hi ha laboratori
 * </ul>
 * <p>
 * Els horaris estan implementats mitjançant una taula de caracters 5x13, on
 * els dies de la setmana estan codificats com a 0= dll, 1= dm, 2= dc, 3= dj i
 * 4= dv, mentre que les franjes horàries estan codificades com a 0= 8:00, 1=
 * 9:00, 2= 10:00... fins a 12= 20:00.
 *
 * @author      Josep Lluís Berral Garcia
 * @version     2.1
 * @since       2.0
 * @see         Assignatura
 * @see         Solucio
 */
public class Grup {

/**
 * Variable d'emmagatzemament del nom del Grup
 */
	private String nom;
/**
 * Variable d'emmagatzemament del número del Grup
 */
	private int valor;
/**
 * Taula d'emmagatzemament de les classes donades al Grup
 */
	private char[][] horari;
/**
 * Referència al seguent node de la llista de Grups
 */
	private Grup seguent;

/**
 * Constructor de la Classe. Especifica el numero del grup(v), el seguen grup de
 * la llista(s), i el nom de l'assignatura(n).
 * @see             Assignatura#Assignatura(String)
 */
	public Grup(int v, Grup s, String n) {
		valor = v;
		seguent = s;
		horari = new char[5][13];
		for (int i = 0; i < 5; i++) 
                    for (int j = 0; j < 13; j++) {
			horari[i][j] = '\0';
                    }
		nom = n;
	}

        public void print() {
            System.out.println("S_G_Nom: "+nom);
            System.out.println("S_G_Valor:"+valor);
           for (int i = 0; i < 13; i++) {
                for (int j = 0; j < 5; j++) {
                    if(horari[j][i] == '\0') horari[j][i] = '-';
                    System.out.print(horari[j][i]+" ");
                }
                System.out.println("");
            }
        }
        
/** 
 * Retorna el nom de l'assignatura.
 *
 * @return          El nom de l'assignatura a la que pertany el grup.
 * @since           2.0
 */
	public String nom() {
		return nom;
	}

/** 
 * Retorna el número de grup.
 *
 * @return          El número de (sub)grup.
 * @see             #setValor(int)
 * @since           2.0
 */
	public int valor() {
		return valor;
	}

/** 
 * Indica el tipus de classe que es dona en aquest grup el <code>dia</code> i
 * <code>hora</code> indicats.
 * 
 * @param dia       el dia de la setmana codificat del 0 al 4
 * @param franja    la franja horària codificada del 0 al 12
 *
 * @return          el caràcter indicat que refereix al tipus de classe
 * @see             #afegeixHora(int,int,char)
 * @since           2.0
 */
	public char hora(int dia, int franja) {
		return horari[dia][franja];
	}

/** 
 * Retorna el seguent grup en la llista de nodes encadenats.
 *
 * @return          El seguent (sub)grup.
 * @see             #setSeguent(Grup)
 * @since           2.0
 */
	public Grup seguent() {
		return seguent;
	}

/** 
 * Assigna a un (sub)grup el seu numero de(sub)grup.
 *
 * @param v         v és el numero de (sub)grup
 * @see             #valor()
 * @since           2.0
 */
	public void setValor(int v) {
		valor = v;
	}

/** 
 * Assigna un tipus d'assignatura a la taula d'horaris del (sub)grup.
 *
 * @param dia       el dia de la setmana codificat del 0 al 4
 * @param franja    la franja horària codificada del 0 al 12
 * @param tipus     el tipus de classe codificat com '\0','T','P' o 'L'
 *
 * @see             #hora(int,int)
 * @since           2.0
 */
	public void afegeixHora(int dia, int franja, char tipus) {
		if (tipus == '\0') return;
		horari[dia-1][franja] = tipus;
	}

/** 
 * Assigna a un (sub)grup el seguent (sub)grup de la llista de grups.
 *
 * @param seg       seg és el seguent grup de la llista
 * @see             #seguent()
 * @since           2.0
 */
	public void setSeguent(Grup seg) {
		seguent = seg;
	}

/** 
 * Fusiona les taules d'horaris de dos (sub)grups diferents, omplint les hores
 * buides de 'this' amb les hores plenes de <code>gr</code>. En cap cas es
 * sobreescriu un tipus de <code>this</code>, només s'afegeixen tipus a
 * aquelles hores que siguin buides.
 *
 * @param gr        gr es el grup que copiarà els seus horaris al grup objecte
 * @since           2.0
 */
	public void fusio(Grup gr) {
		for (int i = 0; i < 5; i++) for(int j = 0; j < 13; j++) {
			if (horari[i][j] == '\0' && gr.hora(i,j) != '\0') {
				horari[i][j] = gr.hora(i,j);
			}
		}
	}
} 
 

