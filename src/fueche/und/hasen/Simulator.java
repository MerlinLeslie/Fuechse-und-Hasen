package fueche.und.hasen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Ein einfacher J�ger-Beute-Simulator, basierend auf einem
 * Feld mit F�chsen und Hasen.
 * 
 * @author David J. Barnes und Michael Kolling
 * @version 2008.03.30
 */
public class Simulator
{
    // Konstanten f�r Konfigurationsinformationen �ber die Simulation.
    // Die Standardbreite f�r ein Feld.
    private static final int STANDARD_BREITE = 50;
    // Die Standardtiefe f�r ein Feld.
    private static final int STANDARD_TIEFE = 50;
    // Die Wahrscheinlichkeit f�r die Geburt eines Fuchses an
    // einer beliebigen Position im Feld.
    private static final double FUCHSGEBURT_WAHRSCHEINLICH = 0.02;
    // Die Wahrscheinlichkeit f�r die Geburt eines Hasen an
    // einer beliebigen Position im Feld.
    private static final double HASENGEBURT_WAHRSCHEINLICH = 0.08;    

    // Listen der Tiere im Feld. Getrennte Listen vereinfachen das Iterieren.
    private List<Hase> hasen;
    private List<Fuchs> fuechse;
    // Der aktuelle Zustand des Feldes
    private Feld feld;
    // Der aktuelle Schritt der Simulation
    private int schritt;
    // Eine grafische Ansicht der Simulation
    private Simulationsansicht ansicht;
    
    /**
     * Erzeuge ein Simulationsfeld mit einer Standardgr��e.
     */
    public Simulator()
    {
        this(STANDARD_TIEFE, STANDARD_BREITE);
    }
      
    /**
     * Erzeuge ein Simulationsfeld mit der gegebenen Gr��e.
     * @param tiefe die Tiefe des Feldes (muss gr��er als Null sein).
     * @param breite die Breite des Feldes (muss gr��er als Null sein).
     */
    public Simulator(int tiefe, int breite)
    {
        if(breite <= 0 || tiefe <= 0) {
            System.out.println("Abmessungen m�ssen gr��er als Null sein.");
            System.out.println("Benutze Standardwerte.");
            tiefe = STANDARD_TIEFE;
            breite = STANDARD_BREITE;
        }
        hasen = new ArrayList<Hase>();
        fuechse = new ArrayList<Fuchs>();
        feld = new Feld(tiefe, breite);

        // Eine Ansicht der Zust�nde aller Positionen im Feld erzeugen.
        ansicht = new Simulationsansicht(tiefe, breite);
        ansicht.setzeFarbe(Fuchs.class, Color.blue);
        ansicht.setzeFarbe(Hase.class, Color.orange);
        
        // Einen g�ltigen Startzustand einnehmen.
        zuruecksetzen();
    }
    
    /**
     * Starte die Simulation vom aktuellen Zustand aus f�r einen l�ngeren
     * Zeitraum, etwa 2000 Schritte.
     */
    public void starteLangeSimulation()
    {
        simuliere(10000);
    }
    
    /**
     * F�hre vom aktuellen Zustand aus die angegebene Anzahl an
     * Simulationsschritten durch.
     * Brich vorzeitig ab, wenn die Simulation nicht mehr aktiv ist.
     * @param schritte die Anzahl der auszuf�hrenden Schritte.
     */
    public void simuliere(int schritte)
    {
        for(int schritt = 1; schritt <= schritte && ansicht.istAktiv(feld); schritt++) {
            simuliereEinenSchritt();
        }
    }
    
    /**
     * F�hre einen einzelnen Simulationsschritt aus:
     * Durchlaufe alle Feldpositionen und aktualisiere den 
     * Zustand jedes Fuchses und Hasen.
     */
    public void simuliereEinenSchritt()
    {
        schritt++;
        
        // Platz f�r neugeborene Hasen anlegen.
        List<Hase> neueHasen = new ArrayList<Hase>();
        // Alle Hasen agieren lassen.
        for(Iterator<Hase> iter = hasen.iterator(); iter.hasNext(); ) {
            Hase hase = iter.next();
            hase.laufe(neueHasen);
            if(!hase.istLebendig()) {
                iter.remove();
            }
        }
        
        // Platz f�r neugeborene F�chse anlegen.
        List<Fuchs> neueFuechse = new ArrayList<Fuchs>();
        // Alle F�chse agieren lassen.
        for(Iterator<Fuchs> iter = fuechse.iterator(); iter.hasNext(); ) {
            Fuchs fuchs = iter.next();
            fuchs.jage(neueFuechse);
            if(!fuchs.istLebendig()) {
                iter.remove();
            }
        }

        // Neu geborene F�chse und Hasen in die Hauptlisten einf�gen.
        hasen.addAll(neueHasen);
        fuechse.addAll(neueFuechse);

        ansicht.zeigeStatus(schritt, feld);
    }
        
    /**
     * Setze die Simulation an den Anfang zur�ck.
     */
    public void zuruecksetzen()
    {
        schritt = 0;
        hasen.clear();
        fuechse.clear();
        bevoelkere();
        
        // Zeige den Startzustand in der Ansicht.
        ansicht.zeigeStatus(schritt, feld);
    }
    
    /**
     * Bev�lkere das Feld mit F�chsen und Hasen.
     */
    private void bevoelkere()
    {
        Random rand = Zufallssteuerung.gibZufallsgenerator();
        feld.raeumen();
        for(int zeile = 0; zeile < feld.gibTiefe(); zeile++) {
            for(int spalte = 0; spalte < feld.gibBreite(); spalte++) {
                if(rand.nextDouble() <= FUCHSGEBURT_WAHRSCHEINLICH) {
                    Position position = new Position(zeile, spalte); 
                    Fuchs fuchs = new Fuchs(true, feld, position);
                    fuechse.add(fuchs);
                }
                else if(rand.nextDouble() <= HASENGEBURT_WAHRSCHEINLICH) {
                    Position position = new Position(zeile, spalte); 
                    Hase hase = new Hase(true, feld, position);
                    hasen.add(hase);
                }
                // ansonsten die Position leer lassen
            }
        }
    }
}