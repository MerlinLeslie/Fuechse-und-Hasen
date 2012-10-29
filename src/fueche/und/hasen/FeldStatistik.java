package fueche.und.hasen;

import java.awt.Color;
import java.util.HashMap;

/**
 * Diese Klasse sammelt und liefert statistische Daten �ber den
 * Zustand eines Feldes. Auf sehr flexible Weise: Es wird ein
 * Z�hler angelegt und gepflegt f�r jede Objektklasse, die im
 * Feld gefunden wird.
 * 
 * @author David J. Barnes und Michael K�lling
 * @version 2008.03.30
 */
public class FeldStatistik
{
    // Die Z�hler f�r die jeweiligen Akteurstypen (Fuchs, Hase, etc.)
    // in der Simulation.
    private HashMap<Class, Zaehler> zaehler;
    // Sind die Z�hlerst�nde momentan aktuell?
    private boolean zaehlerAktuell;

    /**
     * Erzeuge ein FeldStatistik-Objekt.
     */
    public FeldStatistik()
    {
        // Wir legen eine Sammlung f�r die Z�hler an, die wir f�r
        // die gefundenen Tierarten erzeugen.
        zaehler = new HashMap<Class, Zaehler>();
        zaehlerAktuell = true;
    }

    /**
     * Liefere Informationen �ber die Bewohner im Feld.
     * @return Eine Beschreibung, welche Tiere das
     *          Feld bev�lkern.
     */
    public String gibBewohnerInfo(Feld feld)
    {
        StringBuffer buffer = new StringBuffer();
        if(!zaehlerAktuell) {
            ermittleZaehlerstaende(feld);
        }
        for (Class schluessel : zaehler.keySet()) {
            Zaehler info = zaehler.get(schluessel);
            buffer.append(info.gibName());
            buffer.append(": ");
            buffer.append(info.gibStand());
            buffer.append(' ');
        }
        return buffer.toString();
    }
    
    /**
     * Verwerfe alle bisher gesammelten Daten; setze alle Z�hler
     * auf Null zur�ck.
     */
    public void zuruecksetzen()
    {
        zaehlerAktuell = false;
        for(Class schluessel : zaehler.keySet()) {
            Zaehler einzelZaehler = zaehler.get(schluessel);
            einzelZaehler.zuruecksetzen();
        }
    }

    /**
     * Erh�he den Z�hler f�r eine Tierklasse.
     * @param tierklasse Klasse der Tierart, f�r die erh�ht werden soll.
     */
    public void erhoeheZaehler(Class tierklasse)
    {
        Zaehler einzelZaehler = zaehler.get(tierklasse);
        if(einzelZaehler == null) {
            // Wir haben noch keinen Z�hler f�r
            // diese Spezies - also neu anlegen
            einzelZaehler = new Zaehler(tierklasse.getName());
            zaehler.put(tierklasse, einzelZaehler);
        }
        einzelZaehler.erhoehen();
    }

    /**
     * Signalisiere, dass eine Tierz�hlung beendet ist.
     */
    public void zaehlungBeendet()
    {
        zaehlerAktuell = true;
    }

    /**
     * Stelle fest, ob die Simulation noch aktiv ist, also
     * ob sie weiterhin laufen sollte.
     * @return true wenn noch mehr als eine Spezies lebt.
     */
    public boolean istAktiv(Feld feld)
    {
        // Wieviele Z�hler sind nicht Null.
        int nichtNull = 0;
        if(!zaehlerAktuell) {
            ermittleZaehlerstaende(feld);
        }
        for(Class schluessel : zaehler.keySet()) {
            Zaehler info = zaehler.get(schluessel);
            if(info.gibStand() > 0) {
                nichtNull++;
            }
        }
        return nichtNull > 1;
    }
    
    /**
     * Erzeuge Z�hler f�r die Anzahl der F�chse und Hasen.
     * Diese werden nicht st�ndig aktuell gehalten, w�hrend
     * F�chse und Hasen in das Feld gesetzt werden, sondern
     * jeweils bei der Abfrage der Z�hlerst�nde berechnet.
     * @param feld das Feld, f�r das die Statistik erstellt
     *             werden soll.
     */
    private void ermittleZaehlerstaende(Feld feld)
    {
        zuruecksetzen();
        for(int zeile = 0; zeile < feld.gibTiefe(); zeile++) {
            for(int spalte = 0; spalte < feld.gibBreite(); spalte++) {
                Object tier = feld.gibObjektAn(zeile, spalte);
                if(tier != null) {
                    erhoeheZaehler(tier.getClass());
                }
            }
        }
        zaehlerAktuell = true;
    }
}
