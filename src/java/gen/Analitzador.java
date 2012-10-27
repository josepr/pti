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
/* Fitxer: Analitzador.java                                                   */
/* Autor: Alvaro Guinda Rivero (alvaro.guinda@est.fib.upc.edu)                */
/* Data: 8/4/2011                                                             */
/* Versió: 3.0                                                                */
/*----------------------------------------------------------------------------*/

import java.net.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.io.*;

/**
 * La Classe Analitzador és la contenidora de les Funcions d'obtenció de Dades
 * a través d'Internet i d'analitzar-les per construir les Estructures de Dades
 * necessàries per al funcionament del Generador.
 * <p>
 * Per obtenir les dades útils al programa és necessari fer peticions al web de
 * la FIB, com el nom de totes les assignatures, i els horaris de les
 * assignatures demanades.
 * <p>
 * Analitzador té dues funcions principals:
 * <ul>
 * <li>Llegeix Assignatures: Fa una lectura de les assignatures disponibles en
 * aquest quadrimestre.
 * <li>Obté Horaris: Amb les assignatures senyalades obté els horaris i munta
 * les assignatures, amb un Grup per cada grup i subgrup. Després fusiona els
 * Grups amb els corresponents subgrups per tal de completar els horaris dels
 * subgrups ambels Problemes, Laboratoris i Teoria.
 * </ul>
 *
 * @author      Alvaro Guinda Rivero
 * @version     3.0
 * @since       1.0
 * @see         Generador
 * @see         Interficie
 */
public class Analitzador {

/**
 * Variable que emmagatzema el conjunt de Franjes Horàries per analitzar el
 * fitxer d'horaris d'assignatures.
 */
	private String franjes[]={"08:00","09:00","10:00","11:00","12:00","13:00",
		"14:00","15:00","16:00","17:00","18:00","19:00","20:00"};
/**
 * adreca Web d'on s'extreuen els Horaris de les assignatures.
 */
	private String web =
		"https://raco.fib.upc.edu/api/horaris/horari-assignatures.txt?";
        

        public Analitzador(){}
        
/** 
 * Retorna la Llista de noms d'assignatures disponibles en aquest Quadrimestre
 *
 * @return          La Llista de Noms d'Assignatures
 * @see             #llegeixAssigs()
 * @see             Interficie
 * @since           2.0
 */
	public String[] obtencioAssignatures(String codi) {
		try {
			return llegeixAssigs(codi).split("\n");
		} catch (Exception e) {
			return null;
		}
	}

/** 
 * Retorna una Cadena sense Parsejar amb els noms de les assignatures
 * disponibles en aquest Quadrimestre.
 * <p>
 * adreca és la URL d'on obtenir el fixter amb els noms de les assignatures
 *
 * @return          La cadena amb els noms d'Assignatures
 * @throws          FileNotFoundException si l'adreca URL ha canviat
 * @throws          IOException si el sistema no permet fer entrades i sortides
 * @throws          MalformedURLException si la URL no és acceptada
 * @throws          ProtocolException si hi ha problemes amb la Xarxa
 * @throws          SocketException si no s'accepta la connexió
 * @see             #obtencioAssignatures()
 * @since           2.0
 */
	private String llegeixAssigs(String codi) throws Exception{
		//String adreca = "https://raco.fib.upc.edu/api/horaris/assignatures-titulacio.txt?codi="+codi;
                String adreca = "https://raco.fib.upc.edu/api/horaris/assignatures-titulacio.txt?codi=GRAU";
		//String adreca = "http://www.fib.upc.edu/FIB/plsql/PUB_HORARIS.assignatures";
		String buffer = "";
		String aux = "";
		try {
			URL url = new URL(adreca);
			URLConnection connexio = url.openConnection();
			InputStreamReader fluxe = new InputStreamReader(url.openStream());
			BufferedReader servidorWeb = new BufferedReader(fluxe);
			while((aux=servidorWeb.readLine()) != null) buffer += aux + "\n";
			servidorWeb.close();
		} catch (Exception e) { 
			throw e;
		}
		return buffer;
	}

/** 
 * Retorna la Llista d'Assignatures amb les llistes de (sub)grups amb els
 * horaris complets per a cada (sub)grup de cada Assignatura.
 * <p>
 * La funció contempla tres passos:
 * <ul>
 * <li>Cercar Web: Cerca al web el fitxer amb horaris d'assignatures
 * <li>Parser: Analitza el fitxer i omple les Estructures de Dades
 * <li>Mesclador: Completa els horaris dels Subgrups amb les classes de teoria
 * dels Grups globals
 * </ul>
 *
 * @param assig     És la taula d'Assignatures amb les que es fara la Cerca.
 *
 * @return          La Llista d'Assignatures amb els grups complets
 * @see             Interficie
 * @see             Assignatura
 * @since           1.0
 */
	public Assignatura[] obtencioDades(String[] assig) {
		try {
			Assignatura[] assigs = new Assignatura[assig.length];
			parser(cercaWeb(assig), assigs);
			mesclador(assigs);
			return assigs;
		} catch (Exception e) {
			return null;
		}
	}

/** 
 * Retorna la Cadena amb la lectura del Fitxer amb els horaris de les
 * assignatures demanades a través d'<code>assig</code>.
 *
 * @param assig     És la taula d'Assignatures amb les que es fara la Cerca.
 *
 * @return          La Cadena amb la informació dels horaris de les
 * assignatures.
 * @throws          FileNotFoundException si l'adreca URL ha canviat
 * @throws          IOException si el sistema no permet fer entrades i sortides
 * @throws          MalformedURLException si la URL no és acceptada
 * @throws          ProtocolException si hi ha problemes amb la Xarxa
 * @throws          SocketException si no s'accepta la connexió
 * @see             #obtencioDades(String[])
 * @since           1.0
 */
	private String cercaWeb(String[] assig) throws Exception{
		String adreca = web + "assignatures=";
		for (int i = 0; i < assig.length; i++) {
			adreca += assig[i];
			if (i < assig.length-1) adreca += "&assignatures=";
		}
		String buffer = "";
		String aux = "";
		try {
			URL url = new URL(adreca);
			URLConnection connexio = url.openConnection();
			InputStreamReader fluxe = new InputStreamReader(url.openStream());
			BufferedReader servidorWeb = new BufferedReader(fluxe);
			while((aux=servidorWeb.readLine()) != null) buffer += aux + "\n";
			servidorWeb.close();
		} catch (Exception e) { throw e; }
		return buffer;
	}

/** 
 * Funció analitzadora del Fixter amb la informació dels horaris.
 * <p>
 * Desmunta els horaris i els inserta als corresponents Grups de les
 * Corresponents Assignatures.
 *
 * @param web       És la cadena amb al informació dels horaris.
 * @param assigs    És la taula d'Assignatures amb les que es fara la Cerca.
 *
 * @see             #cercaWeb(String[])
 * @see             #obtencioDades(String[])
 * @see             Assignatura
 * @since           1.9
 */
	private void parser(String web, Assignatura[] assigs) {
		String[] hores = web.split("\n");
		for (int i = 0; i < hores.length; i++) {
			String[] div = hores[i].split("\t");
			int k = 0;
			boolean b = false;
			while(!b) {
				if (assigs[k] == null){ 
					assigs[k] = new Assignatura(div[0]);
					b = true;
				} else if(assigs[k].nom().toUpperCase().equals(div[0])) {
					b = true;
				} else k++;
			}
			int g = 0;
			b = false;
			Grup aux = null;
			while(!b) {
				Grup aux2 = assigs[k].grupNum(g);
				if (div[1].compareTo("MAS") == 0) {
					div[1] = "90"; // Descarta el Màster
					g++;
				} else {
					if (aux2 == null){
						aux = assigs[k].afegirGrup(Integer.parseInt(div[1]));
						b = true;
					}
					else if (aux2.valor() == Integer.parseInt(div[1])) {
						aux = aux2;
						b = true;
					} else g++;
				}
			}
			int f = 0;
			b = false;
			while(!b && f < franjes.length) {
				if (franjes[f].equals(div[3])) b = true;
				else f++;
			}
			if (aux != null && f < franjes.length) {
				aux.afegeixHora(Integer.parseInt(div[2]),f,div[4].charAt(0));
			}
		}
	}

/** 
 * Funció que Completa els Subgrups amb la informació dels Grups globals.
 * <p>
 * Comprova els grups x1, x2... i els completa amb la informació dels grups x0.
 *
 * @param assigs    És la taula de referències a Assignatures a completar.
 *
 * @see             #parser(String,Assignatura[])
 * @see             #obtencioDades(String[])
 * @see             Assignatura
 * @see             Grup
 * @since           1.9
 */
	private void mesclador(Assignatura[] assigs) {
		for(int i = 0; i < assigs.length; i++) {
			Assignatura grups = new Assignatura("aux");
			Assignatura subgr = new Assignatura(assigs[i].nom());
			Grup aux1 = null;
			boolean subgrs = false;
			for(int j = 0; j < assigs[i].longitud(); j++) {
				Grup aux2 = assigs[i].grupNum(j);
				if (aux2.valor()%10 == 0) {
					aux1 = aux2;
				} else {
					if (aux1 != null) {
						aux2.fusio(aux1);
						subgrs = true;
					}
				}
			}
			if (subgrs) for(int j = 0; j < assigs[i].longitud(); j++) {
				Grup aux2 = assigs[i].grupNum(j);
				if (aux2.valor()%10 == 0) assigs[i].esborrarGrup(aux2.valor());
			}
		}
	}
}
