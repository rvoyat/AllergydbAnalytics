/**
 * 
 */
package it.allergydb.svm;

/**
 * @author rvoyat
 *
 */
public class LSVMResult {
    
    private double auROC;
    private double MSE;
    private String description;

    public LSVMResult(double auROC, double mSE, String description) {
        this.auROC = auROC;
        this.MSE = mSE;
        this.description = description;
    }
    
    /**
     * @return the auROC
     */
    public double getAuROC() {
        return auROC;
    }
    /**
     * @param auROC the auROC to set
     */
    public void setAuROC(double auROC) {
        this.auROC = auROC;
    }
    /**
     * @return the mSE
     */
    public double getMSE() {
        return MSE;
    }
    /**
     * @param mSE the mSE to set
     */
    public void setMSE(double mSE) {
        MSE = mSE;
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
