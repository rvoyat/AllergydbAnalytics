/**
 * 
 */
package it.allergydb.analytics;

import java.io.IOException;

import org.apache.log4j.Logger;

import it.allergydb.analytics.json.Analytics;
import it.allergydb.firebase4j.error.FirebaseException;
import it.allergydb.firebase4j.util.Filter;
import it.allergydb.svm.LSVMResult;
import it.allergydb.svm.LinearSupportVectorMachines;

/**
 * Classe che implementa la Knowledge Discovery from Data per i
 * dati clinici allergologici presenti sul database allergy-db su Firebase
 * I dati raw interrogati sono in formato documentale json
 * Procedura KDD:
 * 
 * FASE 1 : Nella fase di pre-processing oltre a recuperare i dati e salavare
 * in memoria dati analitici viene creato uno stream di dati in formato SVM
 * per la fase di machine learning successiva e salvati su Hdfs hadoop.
 * 
 * FASE 2: Algortimo di Machine Learning supervisionato di classificazione binaria
 * (Vedi Classe it.allergydb.svm.LinearSupportVectorMachines)
 * 
 * FASE 3: Pos-processing dei dati raccolti, scrittura su Firebase
 * di un documento 'Analytics' in formato documentale json
 * per la visualizzazione successiva sul site di Allergy-db
 * 
 * 
 * @author rvoyat
 *
 */
public class DataAnalytics {

    protected static final Logger LOGGER = Logger.getRootLogger();
    
    
    /**
     * @param args 
     */
    public static void main(String[] args)   {  
        
        LOGGER.info("Start Data Analytics - Allergy db");
        
        long start = System.currentTimeMillis();
        
        System.setProperty("HADOOP_USER_NAME", "hduser");
        System.setProperty("hadoop.home.dir", "/opt/hadoop");
        
        String path = "regressionAllergyDB";
        String nameFileDataset ="dataset.txt";
        String allergenLabel = "Pollen_Graminacee";
        String modelName = "SVMWithSGDModel_"+allergenLabel;
        String modelsDir ="target/tmp/"; 
        boolean saveModel = false;
        
        try {
            
            //phase 1 Pre-processing 
            LOGGER.info("Start phase 1- Preprocessing ...");
            FeaturesInput input = new FeaturesInput(path, nameFileDataset, new Filter(), allergenLabel);
            PreProcessing preProcessing = new PreProcessing();
            Analytics analytics =  preProcessing.createDatasetFeatures(input);
            LOGGER.info("End phase 1- Time elapsed: "+(System.currentTimeMillis()-start));
            
            //phase 2 MLib 
            LOGGER.info("Start phase 2- Machine learning algorithm ...");
            LinearSupportVectorMachines lSVM = new LinearSupportVectorMachines(path, nameFileDataset, allergenLabel, 
                    modelName, modelsDir, saveModel);
            LSVMResult resultSVM  = lSVM.run();
            LOGGER.info("End phase 2- Time elapsed: "+(System.currentTimeMillis()-start));
            
            //phase 3 Post-processing
            LOGGER.info("Start phase 3- Post processing ...");
            PostProcessing postProcessing = new PostProcessing();
            postProcessing.aggiornaAnalyticsToFirebase(analytics,resultSVM);
            LOGGER.info("End phase 3- Time elapsed: "+(System.currentTimeMillis()-start));
            
        } catch (IOException | FirebaseException e) { 
            LOGGER.error(e.getStackTrace());
        } finally{
            LOGGER.info("End Data Analytics - Time elapsed: "+(System.currentTimeMillis()-start));
        }
        
    }

}
