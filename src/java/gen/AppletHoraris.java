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
/* Fitxer: AppletHoraris.java                                                   */
/* Autor: Alvaro Guinda Rivero (alvaro.guinda@est.fib.upc.edu)                */
/* Data: 8/4/2011                                                             */
/* Versió: 3.0                                                                */
/*----------------------------------------------------------------------------*/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;

public class AppletHoraris extends Applet {
  private int MAX_ASSIG;
  private Panel panell1;
  private Panel panell2;
  private Panel panell3;
  private Panel panell4;
  private Panel panell5;
  private Button boto1;
  private Button boto2;
  private Button boto3;
  private Button boto4;
  private Button boto5;
  private Button botos1;
  private Button botos2;
  private Button botos3;
  private Button botos4;
  private Label etiqueta2;
  private Label etiqueta3;
  private Label etiqueta4;
  private Label etiqueta5;
  private Label etiqueta6;
  private Label etiqueta7;
  private Label etiqueta8;
  private Label etiqueta9;
  private Label etiqueta10;
  private Label etiqueta11;
  private Label etiqueta12;
  private Label etiqueta13;
  private Label etiqueta15;
  private Label etiqueta16;
  private Label [] etiqdies;
  private Label [] etiqhores;
  private List llista2;
  private List llista3;
  private TextField text1;
  private TextField text3;
  private TextField text4;
  private TextField text6;
  private Choice cho1;
  private Choice cho2;
  private Checkbox check1;
  private Checkbox check2;
  private CheckboxGroup checkgr;
  private Checkbox [] horespro;
  private ScrollPane scr1;
  private ScrollPane scr2;
  private ScrollPane scr3;
  private String [] dia = {"Dll","Dm","Dc","Dj","Dv"};
  private Interficie inter = new Interficie();

  public void init() {
    //MAX_ASSIG = new Integer(getParameter("maxim")).intValue();
    MAX_ASSIG = 10;
    setBackground(new Color(0xe0e0e0));
    setLayout(null);
    /* Opcions Bàsiques */
    panell2 = new Panel();
    panell2.setLayout(null);
    panell2.setBounds(5,35,180,235);
    panell2.setBackground(new Color(0xffffff));
    panell2.setVisible(false);
    etiqueta7 = new Label("Opcions Matí/Tarda:");
    etiqueta7.setBounds(5,0,200,25);
    etiqueta2 = new Label("Preferiblement:");
    etiqueta2.setBounds(5,30,200,25);
    check1 = new Checkbox("Matins");
    check1.setBounds(5,55,90,25);
    check2 = new Checkbox("Tardes");
    check2.setBounds(95,55,90,25);
    etiqueta3 = new Label("Tolerància:");
    etiqueta3.setBounds(5,80,80,25);
    text3 = new TextField("0");
    text3.setBounds(85,80,50,25);
    text3.setBackground(new Color(0xffffff));
    checkgr = new CheckboxGroup();
    etiqueta4 = new Label("Tipus de Solapament:");
    etiqueta4.setBounds(5,115,130,25);
    Panel paux = new Panel();
    paux.setBounds(5,140,170,50);
    paux.setLayout(new GridLayout(2,1));
    paux.add(new Checkbox("Mínim Solapament", checkgr, true));
    paux.add(new Checkbox("Indicar Màxim Permés", checkgr, false));
    etiqueta9 = new Label("Màxim Permés:");
    etiqueta9.setBounds(5,190,120,25);
    text4 = new TextField("0");
    text4.setBounds(125,190,50,25);
    text4.setBackground(new Color(0xffffff));
    panell2.add(paux);
    panell2.add(etiqueta4);
    panell2.add(etiqueta9);
    panell2.add(text4);
    panell2.add(etiqueta2);
    panell2.add(etiqueta7);
    panell2.add(check1);
    panell2.add(check2);
    panell2.add(etiqueta3);
    panell2.add(text3);
    add(panell2);
    /* Hores Permeses */
    panell3 = new Panel();
    panell3.setLayout(null);
    panell3.setBounds(5,35,180,235);
    panell3.setBackground(new Color(0xffffff));
    panell3.setVisible(false);
    etiqueta5 = new Label("Hores Prohibides:");
    etiqueta5.setBounds(5,0,100,25);
    etiqdies = new Label[5];
    etiqhores = new Label[13];
    horespro = new Checkbox[65];
    for (int i = 0; i < 5; i++) {
      etiqdies[i] = new Label(dia[i]);
      etiqdies[i].setBounds(50+25*(i%5),20,20,20);
      panell3.add(etiqdies[i]);
    }
    for (int i = 0; i < 13; i++) {
      etiqhores[i] = new Label((i+8)+":00");
      etiqhores[i].setBounds(5,35+15*i,40,20);
      panell3.add(etiqhores[i]);
    }
    for (int i = 0; i < 65; i++) {
      horespro[i] = new Checkbox();
      horespro[i].setBounds(50+25*(i%5),38+15*(i/5),15,15);
      panell3.add(horespro[i]);
    }
    panell3.add(etiqueta5);
    add(panell3);
    /* Panell d'Assignatures */
    panell4 = new Panel();
    panell4.setLayout(null);
    panell4.setBounds(5,35,180,235);
    panell4.setBackground(new Color(0xffffff));
    etiqueta6 = new Label("Selecciona les assignatures:");
    etiqueta6.setBounds(5,0,170,25);
    cho2 = new Choice();
    cho2.setBounds(5,35,170,25);
    cho2.setBackground(new Color(0xffffff));
    llista2 = new List();
    llista2.setEnabled(true);
    scr1 = new ScrollPane();
    scr1.add(llista2);
    scr1.setBounds(5,70,170,125);
    //String[] assig = inter.assignatures(getParameter("GRAU_CAIM").toString());
    String[] assig = inter.assignatures("W");
    for(int i = 0; i < assig.length; i++) {
          cho2.add(assig[i]);
      }
    boto2 = new Button("Afegeix");
    boto2.addActionListener(new GestorBoto());
    boto2.setBounds(5,200,85,30);
    boto3 = new Button("Retira");
    boto3.addActionListener(new GestorBoto());
    boto3.setBounds(90,200,85,30);
    panell4.add(etiqueta6);
    panell4.add(scr1);
    panell4.add(cho2);
    panell4.add(boto2);
    panell4.add(boto3);
    add(panell4);
    /* Panell de Filtres */
    panell1 = new Panel();
    panell1.setLayout(null);
    panell1.setBounds(5,35,180,235);
    panell1.setBackground(new Color(0xffffff));
    panell1.setVisible(false);
    etiqueta15 = new Label("Filtres Opcionals:");
    etiqueta15.setBounds(5,0,170,20);
    etiqueta16 = new Label("Selecció Explícita de Grups:");
    etiqueta16.setBounds(5,25,170,20);
    etiqueta12 = new Label("Assig:");
    etiqueta12.setBounds(5,45,110,20);
    cho1 = new Choice();
    cho1.setBounds(5,65,110,25);
    cho1.setBackground(new Color(0xffffff));
    etiqueta13 = new Label("Grup:");
    etiqueta13.setBounds(125,45,60,20);
    text6 = new TextField("");
    text6.setBounds(125,65,50,25);
    text6.setBackground(new Color(0xffffff));
    llista3 = new List();
    llista3.setEnabled(true);
    scr3 = new ScrollPane();
    scr3.add(llista3);
    scr3.setBounds(5,95,170,100);
    boto4 = new Button("Inserta");
    boto4.addActionListener(new GestorBoto());
    boto4.setBounds(5,200,80,30);
    boto5 = new Button("Esborra");
    boto5.addActionListener(new GestorBoto());
    boto5.setBounds(95,200,80,30);
    panell1.add(boto4);
    panell1.add(boto5);
    panell1.add(scr3);
    panell1.add(etiqueta12);
    panell1.add(etiqueta13);
    panell1.add(etiqueta15);
    panell1.add(etiqueta16);
    panell1.add(text6);
    panell1.add(cho1);
    add(panell1);
    /* Panell de Iniciar */
    panell5 = new Panel();
    panell5.setLayout(null);
    panell5.setBounds(5,280,180,65);
    panell5.setBackground(new Color(0xffffff));
    boto1 = new Button("Cercar Horaris");
    boto1.addActionListener(new GestorBoto());
    boto1.setBounds(5,5,100,30);
    etiqueta8 = new Label("Cliqueu aqui per Començar");
    etiqueta8.setBounds(5,30,170,30);
    etiqueta8.setFont(new Font("TimesRoman",Font.BOLD,12));
    panell5.add(boto1);
    panell5.add(etiqueta8);
    add(panell5);
    /* Botons Selectors */
    botos1 = new Button("Assigs");
    botos1.addActionListener(new GestorBoto());
    botos1.setBounds(5,5,45,30);
    botos1.setBackground(new Color(0xffffff));
    botos2 = new Button("Matins");
    botos2.addActionListener(new GestorBoto());
    botos2.setBounds(50,5,45,30);
    botos2.setBackground(new Color(0xe0e0e0));
    botos3 = new Button("Hores");
    botos3.addActionListener(new GestorBoto());
    botos3.setBounds(95,5,45,30);
    botos3.setBackground(new Color(0xe0e0e0));
    botos4 = new Button("Grups");
    botos4.addActionListener(new GestorBoto());
    botos4.setBounds(140,5,45,30);
    botos4.setBackground(new Color(0xe0e0e0));
    add(botos1);
    add(botos2);
    add(botos3);
    add(botos4);
  }

  public String getResultat() {
    String s = inter.publicaWeb(0,0);
      System.out.println("h");
    return s;
  }

  public boolean hiHaSolucions() {
      //System.out.println("hola2");
    return (inter.numSolucions() > -1);
    
  }

  private class GestorBoto implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand() == "Cercar Horaris") {
        if (llista2.getItemCount() == 0) {
          etiqueta8.setText("Introdueix les Assignatures!");
          return;
        }
        /* Parametres de Hores prohibides */
        boolean [][] hores = new boolean[5][13];
        for (int i = 0; i < 65; i++) {
        hores[i%5][i/5] = horespro[i].getState();
        }
        /* Parametres de Matins/tardes */
        boolean [] matins = new boolean[2];
        matins[0] = check1.getState();
        matins[1] = check2.getState();
        /* Tolerancia i Límits */
        int tolerancia = 0;
        int maxim = 0;
        try {
          tolerancia = Integer.parseInt(text3.getText());
          maxim = Integer.parseInt(text4.getText());
        } catch (Exception ex) {
          etiqueta8.setText("Comproveu els paràmetres!");
          return;
        }
        boolean millor = 
          (checkgr.getSelectedCheckbox().getLabel() == "Mínim Solapament");
        if (maxim < 0 && !millor) {
          etiqueta8.setText("Màxim Invàlid!");
          return;
        }
        if (tolerancia < 0 && (matins[0] || matins[1])) {
          etiqueta8.setText("Tolerància Invàlida!");
          return;
        }
        if ( llista2.getItems().length > MAX_ASSIG) {
          etiqueta8.setText("Alerta! Màxim " + MAX_ASSIG + " Assignatures");
          return;
        }
        /* Filtres d'Assignatures */
        String[] filtres = llista3.getItems();
        /* Inici de la Cerca */
        etiqueta8.setText("Espereu uns moments...");
          System.out.println("Empieza Inicia");
          System.out.println("");
        String status = inter.inicia(llista2.getItems(),hores,matins,tolerancia,
          maxim,millor,filtres);
          System.out.println(status);
          System.out.println("");
          System.out.println("Acaba inicia");
        etiqueta8.setText("Trobades " + inter.numSolucions() + " solucions"
          + status);
          System.out.println("Num_Solucions: "+inter.numSolucions());
          
          
          //LLAMADAA A LO LOCO
          System.out.println("");
          String superauux = inter.publicaWeb(0,0);
          
          
      } else if (e.getActionCommand() == "Afegeix") {
        /* Botó d'Afegir Assignatura a la llista */
        String nom = cho2.getSelectedItem();
        if (nom != null) {
          cho2.remove(nom);
          int i = 0;
          while (i < llista2.getItemCount() &&
            nom.compareTo(llista2.getItem(i)) > 0) i++;
          llista2.add(nom,i);
          i = 0;
	  while(i < cho1.getItemCount() && nom.compareTo(cho1.getItem(i)) > 0) i++;
          cho1.insert(nom,i);
        }
      } else if (e.getActionCommand() == "Retira") {
        /* Botó de Retirar Assignatura de la llista */
        String nom = llista2.getSelectedItem();
        if (nom != null) {
          llista2.remove(nom);
          cho1.remove(nom);
          String[] aux = llista3.getItems();
          for (int i = 0; i < aux.length; i++) {
            String[] aux2 = aux[i].split(":");
            if (aux2[0].equals(nom))llista3.remove(aux[i]);
          }
          int i = 0;
          while(i < cho2.getItemCount() && nom.compareTo(cho2.getItem(i)) > 0) i++;
          cho2.insert(nom,i);
        }
      } else if (e.getActionCommand() == "Inserta") {
        String assig = cho1.getSelectedItem();
        String grup = text6.getText();
        try {
          int aux = Integer.parseInt(grup);
          if (aux < 0) return;
        } catch (Exception ex) {
          return;
        }
        if (assig.equals("") || grup.equals("")) return;
        String filtre = assig + ":" + grup;
        for (int i = 0; i < llista3.getItemCount(); i++) {
          if (llista3.getItem(i).equals(filtre)) return;
        }
        llista3.add(filtre);
      } else if (e.getActionCommand() == "Esborra") {
        String nom = llista3.getSelectedItem();
        if (nom != null) llista3.remove(nom);
      } else {
        panell1.setVisible(false);
        panell2.setVisible(false);
        panell3.setVisible(false);
        panell4.setVisible(false);
        botos1.setBackground(new Color(0xe0e0e0));
        botos2.setBackground(new Color(0xe0e0e0));
        botos3.setBackground(new Color(0xe0e0e0));
        botos4.setBackground(new Color(0xe0e0e0));
        if (e.getActionCommand() == "Matins") {
          panell2.setVisible(true);
          botos2.setBackground(new Color(0xffffff));
        } else if (e.getActionCommand() == "Assigs") {
          panell4.setVisible(true);
          botos1.setBackground(new Color(0xffffff));
        } else if (e.getActionCommand() == "Hores") {
          panell3.setVisible(true);
          botos3.setBackground(new Color(0xffffff));
        } else if (e.getActionCommand() == "Grups") {
          panell1.setVisible(true);
          botos4.setBackground(new Color(0xffffff));
        }
      }
    }
  }
}