package it.allergydb.analytics.json;

import java.util.List;

/**
 *  
 * 
 * @author rvoyat
 *
 */
public class PercentualiPerCategoria {
    
    private String categoria;
    private Integer persone;
    private List<AllergenePercentuale> allergenePercentuale;
    /**
     * @return the categoria
     */
    public String getCategoria() {
        return categoria;
    }
    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    /**
     * @return the allergenePercentuale
     */
    public List<AllergenePercentuale> getAllergenePercentuale() {
        return allergenePercentuale;
    }
    /**
     * @param allergenePercentuale the allergenePercentuale to set
     */
    public void setAllergenePercentuale(List<AllergenePercentuale> allergenePercentuale) {
        this.allergenePercentuale = allergenePercentuale;
    }
    /**
     * @return the persone
     */
    public Integer getPersone() {
        return persone;
    }
    /**
     * @param persone the persone to set
     */
    public void setPersone(Integer persone) {
        this.persone = persone;
    }
    
    
    
    

}
