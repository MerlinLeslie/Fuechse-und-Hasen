package fueche.und.hasen;

/**
 * Diese Klasse definiert Z�hler f�r die Akteurstypen
 * in einer Simulation.
 * Ein Z�hler wird �ber einen Namen identifiziert und 
 * z�hlt, wieviele Akteure des Typs innerhalb der Simulation
 * jeweils existieren.
 * 
 * @author David J. Barnes und Michael K�lling
 * @version 2008.03.30
 */
public class Zaehler
{
    // Ein Name f�r den Akteurstyp in dieser Simulation
    private String name;
    // Wie viele von diesem Typ existieren in der Simulation.
    private int zaehler;

    /**
     * Initialisiere mit dem Namen des Typs.
     * @param name Ein Name, z.B. "Fuchs".
     */
    public Zaehler(String name)
    {
        this.name = name;
        zaehler = 0;
    }
    
    /**
     * @return den Namen des Typs dieses Z�hlers.
     */
    public String gibName()
    {
        return name;
    }

    /**
     * @return den aktuellen Z�hlerstand dieses Typs.
     */
    public int gibStand()
    {
        return zaehler;
    }

    /**
     * Erh�he diesen Z�hler um Eins.
     */
    public void erhoehen()
    {
        zaehler++;
    }
    
    /**
     * Setze diesen Z�hler auf Null zur�ck.
     */
    public void zuruecksetzen()
    {
        zaehler = 0;
    }
}
