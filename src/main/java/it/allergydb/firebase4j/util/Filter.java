package it.allergydb.firebase4j.util;

/**
 * 
 * @author rvoyat
 *
 */
public class Filter {

    private String path; 
    private String key_start = "1519652476192";
    private int range = 750;
    private int soglia = 5500;

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
     * @return the key_start
     */
    public String getKey_start() {
        return key_start;
    }

    /**
     * @param key_start the key_start to set
     */
    public void setKey_start(String key_start) {
        this.key_start = key_start;
    }

    /**
     * @return the range
     */
    public int getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * @return the soglia
     */
    public int getSoglia() {
        return soglia;
    }

    /**
     * @param soglia the soglia to set
     */
    public void setSoglia(int soglia) {
        this.soglia = soglia;
    }
    
    
}
