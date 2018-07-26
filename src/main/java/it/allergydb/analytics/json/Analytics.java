package it.allergydb.analytics.json;

import java.util.List;

public class Analytics {
    
    private String dbSize;
    private String numCases = "0";
    private String dateUpdate;
    private List<ClinicCase> clinicCases;
    private List<Allergene> allergenes;
    private List<AllergieInsieme> allergieInsieme;
    private List<PercentualiPerCategoria> percentualiPerCategoria;
       
    
    /**
     * @return the dbSize
     */
    public String getDbSize() {
        return dbSize;
    }
    /**
     * @param dbSize the dbSize to set
     */
    public void setDbSize(String dbSize) {
        this.dbSize = dbSize;
    }
    
    /**
     * @return the numCases
     */
    public String getNumCases() {
        return numCases;
    }
    /**
     * @param numCases the numCases to set
     */
    public void setNumCases(String numCases) {
        this.numCases = numCases;
    }
    /**
     * @return the clinicCases
     */
    public List<ClinicCase> getClinicCases() {
        return clinicCases;
    }
    /**
     * @param clinicCases the clinicCases to set
     */
    public void setClinicCases(List<ClinicCase> clinicCases) {
        this.clinicCases = clinicCases;
    }
    /**
     * @return the allergenes
     */
    public List<Allergene> getAllergenes() {
        return allergenes;
    }
    /**
     * @param allergenes the allergenes to set
     */
    public void setAllergenes(List<Allergene> allergenes) {
        this.allergenes = allergenes;
    }
    /**
     * @return the dateUpdate
     */
    public String getDateUpdate() {
        return dateUpdate;
    }
    /**
     * @param dateUpdate the dateUpdate to set
     */
    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
    /**
     * @return the allergieInsieme
     */
    public List<AllergieInsieme> getAllergieInsieme() {
        return allergieInsieme;
    }
    /**
     * @param allergieInsieme the allergieInsieme to set
     */
    public void setAllergieInsieme(List<AllergieInsieme> allergieInsieme) {
        this.allergieInsieme = allergieInsieme;
    }
    /**
     * @return the percentualiPerCategoria
     */
    public List<PercentualiPerCategoria> getPercentualiPerCategoria() {
        return percentualiPerCategoria;
    }
    /**
     * @param percentualiPerCategoria the percentualiPerCategoria to set
     */
    public void setPercentualiPerCategoria(List<PercentualiPerCategoria> percentualiPerCategoria) {
        this.percentualiPerCategoria = percentualiPerCategoria;
    }
    
    
    
    

}
