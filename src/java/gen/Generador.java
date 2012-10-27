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
/* Fitxer: Generador.java                                                     */
/* Autor: Josep Lluís Berral i Garcia (e6979560@est.fib.upc.edu)              */
/* Data: 22/02/2005                                                           */
/* Versió: 2.0                                                                */
/*----------------------------------------------------------------------------*/

/**
 * La Classe Generador és la contenidora dels Algorismes de Cerca Exhaustiva
 * que generen horaris i dels Algorismes de discriminació de Solucions.
 * <p>
 * Per al seu funcionament, el Generador s'ha de configurar abans d'usar. Els
 * paràmetres de configuració son:
 * <ul>
 * <li>Millor Solapament: Indica si la cerca es basarà en trobar solucions amb
 * el mínim solapament possible. Amb aquesta condició, durant la cerca
 * s'emmagatzemen les solucions tan bones com la millor solució trobada, i en
 * cas de trobar una de millor, es descarten les solucions anteriors i
 * s'emmagatzema l'actual.
 * <li>Màxim Solapament: Indica si la cerca es basarà en trobar solucions amb
 * un solapament igual o menor a l'indicat per l'usuari.
 * <li>Matins/Tardes: Indica si un cop trobades les solucions, se selecionaran
 * les que tinguin el màxim de matins o el màxim de tardes, o no es farà cap
 * tria retornant totes les solucions.
 * <li>Tolerància: Indica si, quan s'ha triat l'opció de Mati/tarda, quin marge
 * de tolerància s'aplica sobre la selecció. Per exemple, si la tolerància és
 * 0, es seleccionen només les solucions amb el maxim de matins/tardes, si la
 * tolerància és 1, es seleccionen les incloses en tolerància 0 i les que
 * tinguin el màxim de matins/tardes de les restants, etc..
 * <li>Hores Prohibides: Indica quines hores no podran ser ocupades per cap
 * assignatura durant la cerca.
 * </ul>
 * <p>
 * Un cop configurat el generador ja és possible fer la Cerca Exhaustiva. La
 * implementació consisteix a una Cerca Exhaustiva de Poda Basada en el Cost de
 * la Millor Solució en Curs. En aquest cas, la condició de poda recau en totes
 * les dades introduides durant la configuració.
 * A cada recursió s'afegeix a la solució els grups d'una assignatura un per
 * un, extenent-se així per l'espai de cerca. En cas de trobar-se que les
 * condicions de cost no son favorables es deixa d'avençar per l'espai de cerca.
 * En cas d'arribar a colocar un grup de cada assignatura, es fa la darrera
 * comprovació de factibilitat i si és favorable s'enregistra la Solució. Si
 * s'usa l'opció de Millor Solució, en cas de trobar solucions iguals a les
 * trobades, aquestes s'emmagatzemen amb les trobades, i en cas de trobar
 * solucions millors a les trobades, es descarten les darreres i s'emmagatzema
 * la nova solució trobada.
 * <p>
 * Exemple d'espai de cerca:
 * <ul>
 * <li>Assignatures[]
 * <ul>
 * <li>ADA 10
 * <ul>
 * <li>PI 11
 * <ul>
 * <li>SO 31
 * <ul>
 * <li>[ADA,10][PI,11][SO,31]
 * </ul>
 * <li>SO 32
 * <ul>
 * <li>[ADA,10][PI,11][SO,32]
 * </ul>
 * </ul>
 * <li>PI 12
 * <ul>
 * <li>SO 31
 * <ul>
 * <li>[ADA,10][PI,12][SO,31]
 * </ul>
 * <li>SO 32
 * <ul>
 * <li>[ADA,10][PI,12][SO,32]
 * </ul>
 * </ul>
 * </ul>
 * <li>ADA 20
 * <li>...
 * </ul>
 * </ul>
 * <p>
 * El resultat és retornat en forma de Llista de Solucions filtrades per el
 * criteri de matins/tardes.
 *
 * @author      Josep Lluís Berral Garcia
 * @version     2.0
 * @since       1.9
 * @see         Assignatura
 * @see         Grup
 * @see         LlistaSolucions
 * @see         Solucio
 */
public class Generador {

/**
 * Variable que emmagatzema l'opció de Millor Solapament
 */
	private boolean mll_slp;
/**
 * Variable que emmagatzema les opcions Matí <code>matins[0]</code> i Tarda
 * <code>matins[1]</code>.
 */
	private boolean[] matins;
/**
 * Taula on s'emmagatzemen les Hores Prohibides
 */
	private boolean[][] hrs_prh;
/**
 * Variable que emmagatzema el valor Maxim Solapament de l'opció SolapamentMaxim
 */
	private int max_slp;
/**
 * Variable que emmagatzema la Tolerància de l'opció Matins/Tardes
 */
	private int tol;
/**
 * Variable que emmagatzema els Filtres. Index1 = num filtre, Index2 = assig/num
 */
	private String[][] filtres;

/**
 * Constructor de la Classe sense paràmetres. Crea un Generador sense
 * configurar.
 * <p>
 * Per defecte, l'opció Millor Solapament esta activada, el Màxim Solapament és
 * 99999, Matins i Tardes estan desactivats, la Tolerància és 0, i no hi ha cap
 * Hora prohibida.
 *
 * @see             Interficie
 */
	public Generador () {
		mll_slp = true;
		matins = new boolean[2];
		hrs_prh = new boolean[5][13];
		max_slp = 99999;
		tol = 0;
	}

/** 
 * Activa o Desactiva l'opció de Millor Solapament.
 * <p>
 * Aquesta opció és antagònica a Solapament Màxim. Si està activada Millor
 * Solapament, Solapament Màxim està desactivat.
 * 
 * @param b         True = Activar opció, False = Desactivar opció
 *
 * @see             #setMaximSolap(int)
 * @since           2.0
 */
	public void setMillorSolap(boolean b) {
		mll_slp = b;
	}

/** 
 * Activa o Desactiva l'opció de Matins.
 * <p>
 * Aquesta opció és antagònica a Tardes. Activar Matins implica desactivar
 * l'opció de Tardes.
 * 
 * @param b         True = Activar opció, False = Desactivar opció
 *
 * @see             #setTardes(boolean)
 * @see             #setTolerancia(int)
 * @since           2.0
 */
	public void setMatins(boolean b) {
		matins[0] = b; 
		if (b) matins[1] = false;
	}

/** 
 * Activa o Desactiva l'opció de Tardes.
 * <p>
 * Aquesta opció és antagònica a Matins. Activar tardes implica desactivar
 * l'opció de Matins.
 * 
 * @param b         True = Activar opció, False = Desactivar opció
 *
 * @see             #setMatins(boolean)
 * @see             #setTolerancia(int)
 * @since           2.0
 */
	public void setTardes(boolean b) {
		matins[1] = b;
		if (b) matins[0] = false;
	}

/** 
 * Estableix la Tolerància davant l'opció Matins/Tardes.
 * <p>
 * Aquesta opció només serà escoltada per el Generador si està activada l'opió
 * Matins o Tardes.
 * 
 * @param t         És la tolerància a establir per l'opció Matins/Tardes.
 *
 * @see             #setMatins(boolean)
 * @see             #setTardes(boolean)
 * @since           2.0
 */
	public void setTolerancia(int t) {
		tol = t;
	}

/** 
 * Estableix les Hores prohibides.
 * <p>
 * Les Hores Prohibides s'implementen en una taula 5x13 (5 dies, 13 franjes
 * horàries) de booleans. Una Hora = True implica que tota Solució que ocupi
 * aquesta hora serà descartat per el Generador.
 * 
 * @param hores     És la taula de booleans que contenen les hores prohibides.
 *
 * @see             Interficie
 * @since           2.0
 */
	public void setHoresProh(boolean[][] hores) {
		hrs_prh = hores;
	}

/** 
 * Estableix el Solapament Màxim per a l'opció 'Solapament Màxim'.
 * <p>
 * Si esta activada l'opció Màxim Solapament, el Solapament Màxim no serà
 * escoltat per el Generador
 * 
 * @param i         És el Màxim Solapament permés per l'usuari a les Solucions
 *
 * @see             #setMillorSolap(boolean)
 * @since           2.0
 */
	public void setMaximSolap(int i) {
		max_slp = i;
	}

/** 
 * Enregistra els filtres personalitzats indicats per l'usuari. Un filtre és el
 * nom d'una assignatura, amb un numero de grup, i una condició
 * [selecció/rebuig]. 
 * <p>
 * 
 * @param filts     És la llista de filtres indicats per l'usuari
 *
 * @see             Interficie
 * @see             #generar(Assignatura[])
 * @see             #filtrar(Assignatura[])
 * @since           2.0
 */
	public void setFiltres(String[] filts) {
		filtres = new String[filts.length][2];
		for (int i = 0; i < filts.length; i++) {
			String[] aux = filts[i].split(":");
			filtres[i][0] = aux[0];
			filtres[i][1] = aux[1];
		}
	}

/**
 * Filtra els Grups de les Assignatures a partir d'un patró de Filtres
 * continguts a la configuració del Generador.
 * <p>
 * Si existeix un o més filtres per a una Assignatura, aquesta assignatura
 * descarta tots els grups menys aquells que són seleccionats pel Filtre. Si no
 * és possible aplicar cap dels filtres, no es modifica l'assignatura.
 * 
 * @param assig     És la taula d'Assignatures que seran filtrades.
 *
 * @return          String amb avisos resultants del Filtratge
 * @see             #generar(Assignatura[])
 * @see             Assignatura
 * @see             Grup
 * @since           2.0
 */
	public String filtrar(Assignatura[] assig) {
		String ret = "";
		boolean[] noapp = new boolean[filtres.length];
		for(int i = 0; i < assig.length; i++) {
			boolean b = false;
			int[] grups = assig[i].llistaGrups();
			for(int j = 0; j < filtres.length; j++) {
				if(filtres[j][0].equals(assig[i].nom())) {
					for(int k = 0; k < grups.length; k++) {
						if(grups[k] == Integer.parseInt(filtres[j][1])) {
							grups[k] = 0;
							noapp[j] = true;
							b = true;
						}
					}
				}
			}
			for(int j = 0; j < grups.length; j++) {
				if (grups[j] != 0 && b) assig[i].esborrarGrup(grups[j]);
			}
		}
		boolean aplicat = true;
		for(int i = 0; i < noapp.length; i++) aplicat &= noapp[i];
		if (!aplicat) ret = " (Alerta! Hi ha filtres invàlids!)";
		return ret;
	}

/** 
 * Retorna la Llista de Solucions trobades per el Generador i Filtrades segons
 * les opcions Mati/tarda, Millor Solapament o Solapament Màxim, i Hores
 * Prohibides.
 * <p>
 * La funció <codi>generar</codi> crida a la funció privada Generadora
 * d'Horaris, que és la que s'encarrega de fer la Cerca Exhaustiva amb PBCMSC.
 * Funcions de la Cerca Exhaustiva amb PBCMSC:
 * <ul>
 * <li>Funció Factibilitat: Condició(Solucio)
 * <li>Funció Solució: Registre(LlistaSolucions,Solucio)
 * <li>Funció Selecció: Assignatura[n_assig]
 * <li>Funció Marcatge: Solució.afegirGrup(Grup)
 * </ul>
 *
 * @param assig     És la taula d'Assignatures amb les que es fara la Cerca.
 *
 * @return          La Llista de Solucions final.
 * @see             #generar(Assignatura[],LlistaSolucions,Solucio,int)
 * @see             Interficie
 * @see             Assignatura
 * @see             LlistaSolucions
 * @since           1.9
 */
	public LlistaSolucions generar(Assignatura[] assig) {
		LlistaSolucions llis = new LlistaSolucions();
		generar(assig,llis,new Solucio(),0);
		if (matins[0] || matins[1]) matins(llis,tol);
		return llis;
	}

/**
 * Funció privada Generadora d'Horaris.
 * <p>
 * Realitza la Cerca Exhaustiva amb PBCMSC:
 * <ul>
 * <li>Funció Factibilitat: Condició(Solucio)
 * <li>Funció Solució: Registre(LlistaSolucions,Solucio)
 * <li>Funció Selecció: Assignatura[n_assig]
 * <li>Funció Marcatge: Solució.afegirGrup(Grup)
 * </ul>
 * 
 * @param assig     És la taula d'Assignatures amb les que es fara la Cerca.
 * @param llis      És la Llista de Solucions amb les Millors Solucions Trobades
 * @param sol_curs  És la Solució en Curs
 * @param n_assig   És el nombre de l'assignatura en curs de la llista
 * d'assignatures
 *
 * @see             #generar(Assignatura[])
 * @see             #matins(LlistaSolucions,int)
 * @see             #condicio(Solucio)
 * @see             #registre(LlistaSolucions,Solucio)
 * @see             Assignatura
 * @see             LlistaSolucions
 * @see             Grup
 * @since           1.0
 */
	private void generar(Assignatura[] assig, LlistaSolucions llis,
		Solucio sol_curs, int n_assig) {
		if (n_assig >= assig.length) {
			registre(llis,sol_curs);
		} else if (assig[n_assig].longitud() == 0) {
			generar(assig,llis,sol_curs,n_assig+1);
		} else {
			for(int k = 0; k < assig[n_assig].longitud(); k++) {
				Grup gr = assig[n_assig].grupNum(k);
				sol_curs.afegirGrup(gr);
				if (condicio(sol_curs)) {
					generar(assig,llis,sol_curs,n_assig+1);
				}
				sol_curs.esborrarGrup(gr);
			}
		}
	}

/**
 * Retorna si la Solució <code>sol</code> acompleix els requeriments de la
 * configuració establerta al Generador.
 * <p>
 * Comprova els criteris de Màxim Solapament i les Hores Prohibides.
 * 
 * @param sol       És la Solució en Curs
 *
 * @return          True = Acompleix els Requeriments, False = No els Acompleix
 * @see             #generar(Assignatura[],LlistaSolucions,Solucio,int)
 * @see             Solucio
 * @see             Grup
 * @since           2.0
 */
	private boolean condicio(Solucio sol) {
		// Comprovador d'Hores Prohibides
		for(int i = 0; i < 5; i++) for(int j = 0; j < 13; j++) {
			if (hrs_prh[i][j]) {
				for(int k = 0; k < sol.longitud(); k++) {
					Grup aux = sol.grup(k);
					if (aux.hora(i,j) != '\0') return false;
				}
			}
		}
		// Comprovador de Solapaments
		if (sol.solapament() > max_slp) return false;
		// Ha passat totes les proves
		return true;
	}

/**
 * Registra la Solució en Curs tal com indica la configuració del Generador.
 * <p>
 * Si està activada l'opció de Millor Solapament, comprova si és igual que les
 * solucions trobades o és millor. Si és igual, la registra amb les altres, si
 * és millor, descarta les solucions trobades i insereix la Solució en Curs. Si
 * la solució no és millor, és descartada.
 * <p>
 * Si esta activada l'opció de Solapament Màxim, s'enregistra directament, ja
 * que es dona per comptat que s'ha efectuat la funció condició(Solucio) i la
 * Solució és vàlida.
 * 
 * @param llis      És la Llista de Solucions
 * @param sol       És la Solució en Curs
 *
 * @see             #generar(Assignatura[],LlistaSolucions,Solucio,int)
 * @see             #condicio(Solucio)
 * @see             Solucio
 * @see             LlistaSolucions
 * @since           1.9
 */
	private void registre(LlistaSolucions llis,Solucio sol){
		int suma = sol.solapament();
		if (mll_slp) {
			if (suma == max_slp) {
				llis.afegirSol(sol);
			} else if (suma < max_slp) {
				max_slp = suma;
				llis.destrueix();
				llis.afegirSol(sol);
			}
		} else {
			llis.afegirSol(sol);
		}
	}

/**
 * Filtra les solucions de <code>llis</code> segons el criteri de Tolerància
 * <code>tol</code>.
 * <p>
 * Selecciona les solucions que tenen un horari de més Matins o més Tardes, amb
 * un grau de tolerància <code>tol</code>.
 * 
 * @param llis      És la Llista de Solucions a filtrar
 * @param tol       És la Tolerància a usar
 *
 * @see             #generar(Assignatura[],LlistaSolucions,Solucio,int)
 * @see             LlistaSolucions
 * @see             Solucio
 * @see             Generador
 * @since           1.9
 */
	private void matins (LlistaSolucions llis, int tol) {
		LlistaSolucions nova = new LlistaSolucions();
		int pitjor = 0;
		int [] valors = new int[tol+1];
		for (int i = 0; i < tol+1; i++) valors[i] = (matins[0]?-99999:99999);
		if (matins[0]) {
			for(int j = 0; j < tol+1; j++) if (valors[j] < valors[pitjor]) pitjor = j;
		} else {
			for(int j = 0; j < tol+1; j++) if (valors[j] > valors[pitjor]) pitjor = j;
		}
		for (int i = 0; i < llis.longitud(); i++) {
			int auxiliar;
			Solucio aux = llis.solucioNum(i);
			if (matins[0]) {
				if (aux.nivell() > valors[pitjor]) {
					auxiliar = pitjor;
					for(int j = 0; j < tol+1; j++) {
						if (valors[j] < valors[pitjor]) pitjor=j;
					}
					for(int j = 0; j < tol+1; j++) {
						if (valors[j] == aux.nivell()) valors[j] = valors[pitjor];
					}
					valors[auxiliar] = aux.nivell();
				}
			} else {
				if (aux.nivell() < valors[pitjor]) {
					auxiliar = pitjor;
					for(int j = 0; j < tol+1; j++) {
						if (valors[j] > valors[pitjor]) pitjor=j;
					}
					for(int j = 0; j < tol+1; j++) {
						if (valors[j] == aux.nivell()) valors[j] = valors[pitjor];
					}
					valors[auxiliar] = aux.nivell();
				}
			}
			
		}
		
		for(int j = 0; j < tol+1; j++) {
			if ((valors[j] < valors[pitjor] && matins[0])
			|| (valors[j] > valors[pitjor] && !matins[0])) pitjor=j;
		}
		int tolerat = valors[pitjor];
		for (int i = 0; i < llis.longitud(); i++) {
			if ((llis.solucioNum(i).nivell() >= tolerat && matins[0])
			|| (llis.solucioNum(i).nivell() <= tolerat && !matins[0])) {
				nova.afegirSol(llis.solucioNum(i));
			}
		}
		llis.copia(nova);
	}
}