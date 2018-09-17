/**
 * 
 */
package it.allergydb.naivebayes;

/**
 * @author rvoyat
 *
 */
public class NaiveBayesResult {
    
    private double accuracy; 
    private String description;

    public NaiveBayesResult(double accuracy,   String description) {
        this.accuracy = accuracy; 
        this.description = description;
    }
    
    
    public double getAccuracy() {
		return accuracy;
	}


	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}


	/**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    

}
