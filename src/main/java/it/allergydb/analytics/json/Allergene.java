/**
 * 
 */
package it.allergydb.analytics.json;


/**
 * @author rvoyat
 *
 */
public class Allergene {
    
    private String organ;
    private String[] numPeoples;
    private Double average;
    /**
     * @return the organ
     */
    public String getOrgan() {
        return organ;
    }
    /**
     * @param organ the organ to set
     */
    public void setOrgan(String organ) {
        this.organ = organ;
    }
    /**
     * @return the numPeoples
     */
    public String[] getNumPeoples() {
        return numPeoples;
    }
    /**
     * @param numPeoples the numPeoples to set
     */
    public void setNumPeoples(String[] numPeoples) {
        this.numPeoples = numPeoples;
    }
    /**
     * @return the average
     */
    public Double getAverage() {
        return average;
    }
    /**
     * @param average the average to set
     */
    public void setAverage(Double average) {
        this.average = average;
    }
    
    

}
