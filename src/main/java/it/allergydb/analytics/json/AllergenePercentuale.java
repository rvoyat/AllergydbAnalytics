package it.allergydb.analytics.json;

public class AllergenePercentuale {
    
    private String allergene;
    private Integer persone;
    private Double percentuale;
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
     * @return the percentuale
     */
    public Double getPercentuale() {
        return percentuale;
    }
    /**
     * @param percentuale the percentuale to set
     */
    public void setPercentuale(Double percentuale) {
        this.percentuale = percentuale;
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
