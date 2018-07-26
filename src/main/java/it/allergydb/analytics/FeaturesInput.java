package it.allergydb.analytics;

import it.allergydb.firebase4j.util.Filter;

public class FeaturesInput {

    private String path;
    private String nomeFile;
    private Filter filterFirebase;
    private String allergenLabel;

    public FeaturesInput(String path, String nomeFile, Filter filterFirebase, String allergenLabel) {
        this.path = path;
        this.nomeFile = nomeFile;
        this.filterFirebase = filterFirebase;
        this.allergenLabel = allergenLabel;
    }
    
    
    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }
    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
    /**
     * @return the nomeFile
     */
    public String getNomeFile() {
        return nomeFile;
    }
    /**
     * @param nomeFile the nomeFile to set
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }
    /**
     * @return the filterFirebase
     */
    public Filter getFilterFirebase() {
        return filterFirebase;
    }
    /**
     * @param filterFirebase the filterFirebase to set
     */
    public void setFilterFirebase(Filter filterFirebase) {
        this.filterFirebase = filterFirebase;
    }
    /**
     * @return the allergenLabel
     */
    public String getAllergenLabel() {
        return allergenLabel;
    }
    /**
     * @param allergenLabel the allergenLabel to set
     */
    public void setAllergenLabel(String allergenLabel) {
        this.allergenLabel = allergenLabel;
    }
    
}
