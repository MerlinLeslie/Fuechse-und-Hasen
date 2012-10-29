package fueche.und.hasen;

import java.util.List;
import java.util.Random;

/**
 * Ein einfaches Modell eines Hasen.
 * Ein Hase altert, bewegt sich, geb�rt Nachwuchs und stirbt.
 * 
 * @author David J. Barnes und Michael K�lling
 * @version 2008.03.30
 */
public class Hase
{
    // Eigenschaften aller Hasen (statische Datenfelder).

    // Das Alter, in dem ein Hase geb�rf�hig wird.
    private static final int GEBAER_ALTER = 5;
    // Das H�chstalter eines Hasen.
    private static final int MAX_ALTER = 40;
    // Die Wahrscheinlichkeit, mit der ein Hase Nachwuchs geb�rt.
    private static final double GEBAER_WAHRSCHEINLICHKEIT = 0.15;
    // Die maximale Gr��e eines Wurfes (Anzahl der Jungen)
    private static final int MAX_WURFGROESSE = 4;
	// Ein gemeinsamer Zufallsgenerator, der die Geburten steuert.
    private static final Random rand = Zufallssteuerung.gibZufallsgenerator();
    
    // Individuelle Eigenschaften eines Hasen (Instanzfelder).
    
    // Das Alter dieses Hasen.
    private int alter;
    // Ist dieser Hase noch lebendig?
    private boolean lebendig;
    // Die Position dieses Hasen
    private Position position;
    // Das belegte Feld
    private Feld feld;

    /**
     * Erzeuge einen neuen Hasen. Ein neuer Hase kann das Alter 0 
     *(neu geboren) oder ein zuf�lliges Alter haben.
     * @param zufaelligesAlter soll der Hase ein zuf�lliges Alter haben?
     * @param feld Das aktuelle belegte Feld
     * @param position Die Position im Feld
     */
    public Hase(boolean zufaelligesAlter, Feld feld, Position position)
    {
        alter = 0;
        lebendig = true;
        this.feld = feld;
        setzePosition(position);
        if(zufaelligesAlter) {
            alter = rand.nextInt(MAX_ALTER);
        }
    }
    
    /**
     * Das ist was ein Hase die meiste Zeit tut - er l�uft herum.
     * Manchmal geb�rt er Nachwuchs und irgendwann stirbt er
     * an Altersschw�che.
     * @param neueHasen Eine Liste, in die neue Hasen eingef�gt werden.
     */
    public void laufe(List<Hase> neueHasen)
    {
        alterErhoehen();
        if(lebendig) {
            gebaereNachwuchs(neueHasen);
            // nur in das n�chste Feld setzen, wenn eine Position frei ist
            Position neuePosition = feld.freieNachbarposition(position);
            if(neuePosition != null) {
                setzePosition(neuePosition);
            }
            else {
                // �berpopulation 
                sterben();
            }
        }
    }

    /**
     * Pr�fe, ob dieser Hase noch lebendig ist.
     * @return true wenn dieser Hase noch lebt.
     */
    public boolean istLebendig()
    {
        return lebendig;
    }

    /**
     * Liefere die Position des Hasen.
     * @return die Position des Hasen.
     */
    public Position gibPosition()
    {
        return position;
    }
	
    /**
     * Anzeigen, dass der Hase nicht mehr laenger lebendig ist.
     * Hase aus dem Feld entfernen.
     */
    public void sterben()
    {
        lebendig = false;
        if(position != null) {
            feld.raeumen(position);
            position = null;
            feld = null;
        }
    }

    /**
     * Setze den Hasen auf die gegebene Position im aktuellen Feld.
     * @param neuePosition die neue Position dieses Hasen.
     */
    public void setzePosition(Position neuePosition)
    {
        if(position != null) {
            feld.raeumen(position);
        }
        position = neuePosition;
        feld.platziere(this, neuePosition);
    }

    /**
     * Erh�he das Alter. 
     * Dies kann zum Tod des Hasen f�hren.
     */
    private void alterErhoehen()
    {
        alter++;
        if(alter > MAX_ALTER) {
            sterben();
        }
    }
       
    /**
     * Pr�fe, ob dieser Hase in diesem Schritt geb�ren kann.
     * Neugeborene kommen in freie Nachbarpositionen.
     * @param neueHasen Liste, in die neugeborene Hasen eingetragen werden.
     */
    private void gebaereNachwuchs(List<Hase> neueHasen)
    {
        // Neugeborene kommen in freie Nachbarpositionen.
        // Freie Nachbarpositionen abfragen.
        List<Position> frei = feld.freieNachbarpositionen(position);
        int geburten = traechtig();
        for(int b = 0; b < geburten && frei.size() > 0; b++) {
            Position pos = frei.remove(0);
            Hase jung = new Hase(false, feld, pos);
            neueHasen.add(jung);
        }
    }

    /**
     * Erzeuge eine Zahl f�r die Wurfgroesse, wenn der Hase
     * gebaeren kann.
     * @return  Wurfgroesse (kann Null sein).
     */
    private int traechtig()
    {
        int wurfgroesse = 0;
        if(kannGebaeren() && rand.nextDouble() <= GEBAER_WAHRSCHEINLICHKEIT) {
            wurfgroesse = rand.nextInt(MAX_WURFGROESSE) + 1;
        }
        return wurfgroesse;
    }
    /**
     * Ein Hase kann geb�ren, wenn er das geb�rf�hige
     * Alter erreicht hat.
     */
    private boolean kannGebaeren()
    {
        return alter >= GEBAER_ALTER;
    }
}
