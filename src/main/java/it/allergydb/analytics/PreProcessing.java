package it.allergydb.analytics;

import it.allergydb.analytics.json.Analytics;
import it.allergydb.firebase4j.error.FirebaseException;
import it.allergydb.firebase4j.error.JacksonUtilityException;
import it.allergydb.firebase4j.util.Filter;
import it.allergydb.firebase4j.util.FirebaseWrapperUtil;
import it.allergydb.firebase4j.util.JacksonUtility;
import it.allergydb.hdfs.HDFSUtil;
import it.allergydb.svm.MapToSVMMaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import net.iharder.Base64.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;

/**
 * 
 * @author rvoyat
 *
 */
public class PreProcessing {

    protected static final Logger LOGGER = Logger.getRootLogger();
    
    private HDFSUtil hdfsUtil;
    private FirebaseWrapperUtil firebaseWrapperUtil;
    private MapToSVMMaker svmMaker;
    private ResultUtil analyticsUtil;
    
    public Analytics createDatasetFeatures(FeaturesInput input) throws IOException {
         

        Analytics result = getAnalyticsUtil().generateVoidAnalytics(input);
        
        //cARICO IL FILE SU HDFS
        FSDataOutputStream fsOutStream = getHdfsUtil().openStream(input.getPath(), input.getNomeFile());
         
        // Inizio ciclo lettura / trasformazione / scrittura 
        boolean continueWhile = true; 
        int processati = 0;  
        try { 
            do{
                try {
                    // Leggo dati da firebase
                    String json =getFirebaseWrapperUtil().getJSON(input.getFilterFirebase());
                    Map<String,Object> data  = JacksonUtility.GET_JSON_STRING_AS_MAP( json );
                    
                    //Aggiorno Analytics
                    getAnalyticsUtil().updateAnalytics(result,json,data);
                    
                    // Trasformo nel formato desiderato
                    String svm = getSvmMaker().convert(data,input.getAllergenLabel());
                    
                    if(svm!= null){
                        // Scrivo su hdfs
                        getHdfsUtil().writeFile(fsOutStream, svm.getBytes()); 
                        //Aggiorno start
                        String start = String.valueOf((getSvmMaker().getLastKey().longValue()+1));
                        input.getFilterFirebase().setKey_start(start);
                        processati += input.getFilterFirebase().getRange();
                    } else {
                        continueWhile = false;
                        break;
                    }
                    
                } catch (Exception | FirebaseException e ) {
                    e.printStackTrace();
                    continueWhile = false;
                    break;
                } catch (JacksonUtilityException e) {
                    e.printStackTrace();
                    continueWhile = false;
                    break;
                } 
                
                
            }while(continueWhile && processati< input.getFilterFirebase().getSoglia());
            
        } catch (Exception   e ) { 
            //TODO log exception
            e.printStackTrace();
        } 
        // Chiudo  lo stream su hdfs
        getHdfsUtil().closeStream(fsOutStream);
        
        //Rifinisco dati analytics
        getAnalyticsUtil().rifinisciResult(result); 
        
        return result;
    }

   

    /**
     * @return the hdfsUtil
     */
    public HDFSUtil getHdfsUtil() {
        if(hdfsUtil==null)
            hdfsUtil = new HDFSUtil();
        return hdfsUtil;
    }

    /**
     * @param hdfsUtil the hdfsUtil to set
     */
    public void setHdfsUtil(HDFSUtil hdfsUtil) {
        this.hdfsUtil = hdfsUtil;
    }

    /**
     * @return the firebaseWrapperUtil
     */
    public FirebaseWrapperUtil getFirebaseWrapperUtil() {
        if(firebaseWrapperUtil == null)
            firebaseWrapperUtil = new FirebaseWrapperUtil();
        return firebaseWrapperUtil;
    }

    /**
     * @param firebaseWrapperUtil the firebaseWrapperUtil to set
     */
    public void setFirebaseWrapperUtil(FirebaseWrapperUtil firebaseWrapperUtil) {
        this.firebaseWrapperUtil = firebaseWrapperUtil;
    }

    /**
     * @return the csvMaker
     */
    public MapToSVMMaker getSvmMaker() {
        if(svmMaker==null)
            svmMaker = new MapToSVMMaker();
        return svmMaker;
    }

    /**
     * @param csvMaker the csvMaker to set
     */
    public void setSvmMaker(MapToSVMMaker csvMaker) {
        this.svmMaker = svmMaker;
    }



    /**
     * @return the analyticsUtil
     */
    public ResultUtil getAnalyticsUtil() {
        if(analyticsUtil==null)
            analyticsUtil = new ResultUtil();
        return analyticsUtil;
    }



    /**
     * @param analyticsUtil the analyticsUtil to set
     */
    public void setAnalyticsUtil(ResultUtil analyticsUtil) {
        this.analyticsUtil = analyticsUtil;
    }

        
    
}
