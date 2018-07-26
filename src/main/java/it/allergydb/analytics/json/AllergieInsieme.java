package it.allergydb.analytics.json;

import java.util.List;

/**
 * PEr ogni allergene
 * 
 * @author rvoyat
 *
 */
public class AllergieInsieme {
    
    private String allergene;
    private List<Concorrenza> concorrenza;
    /**
     * @return the allergene
     */
    public String getAllergene() {
        return allergene;
    }
    /**
     * @param allergene the allergene to set
     */
    public void setAllergene(String allergene) {
        this.allergene = allergene;
    }
    /**
     * @return the concorrenza
     */
    public List<Concorrenza> getConcorrenza() {
        return concorrenza;
    }
    /**
     * @param concorrenza the concorrenza to set
     */
    public void setConcorrenza(List<Concorrenza> concorrenza) {
        this.concorrenza = concorrenza;
    }
    
    
    

}
