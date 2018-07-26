package it.allergydb.svm;
 
import it.allergydb.firebase4j.util.IAllergyConstants;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author rvoyat
 *
 */
public class MapToSVMMaker {
 
    private List<String> simptoms_keys = generateKeys(); 
    private Map<String,Integer> indexSymptoms;
    private Map<String,Integer> indexAllergens; 
    
    private String DELIMITER = " ";
    private String DOUBLE_DOT = ":";
    private String END = "\n";
    private Double featureOK = Double.valueOf(1.8);
    private String LABEL_TRUE = "1";
    private String LABEL_FALSE = "0";
     
    private Long last;
     
   /**
    * Metodo che prende in input la map generata dal file json e crea in output una stringa
    * in formato SVM (SupportVectorMachine) pronto per l'algoritmo SVM di Machine Learning
    * La riga del file è formata così:
    *    L  INDEX1:VALUE1   INDEX2:VALUE2 .... INDEXN:VALUE:N
    * Dove L è la label binaria di classificazione con valore  0/1;
    * INDEX1,  INDEX2 ecc sono le etichiette di features in numero intero con la regola: INDEX(N) > INDEX(N-1)
    * VALUE1,  VALUE2 ecc sono i valori in formato decimale delle features
    * 
    * @param data
    * @param allergenLabel
    * @return String FILE IN SVM FORMAT : es.  1  100:2.5  101:0 ....
    */
    public String convert(Map<String, Object> data, String allergenLabel){ 
        //RESET DEL LAST
        last = Long.valueOf(0);
        if(data != null&&  data.keySet() !=null && !data.keySet().isEmpty()){
            StringBuilder result = new StringBuilder("");
            data.keySet().forEach(key ->{
                
                if(!key.equals(IAllergyConstants.KEY_ANALYTICS)){
                Long chiaveRecord = Long.valueOf(key);
                if(chiaveRecord > last){
                    last = chiaveRecord;
                }
                 
                Map<String, Object> allergies = (Map<String, Object>)((Map<String, Object>)data.get(key)).get(IAllergyConstants.KEY_ALLERGY);
                if(allergies != null && !allergies.keySet().isEmpty()){
    
                    for(String keyAll: allergies.keySet()){   
                             Map<String, Object> allergyCorr = (Map<String, Object>)(Map<String, Object>)allergies.get(keyAll) ;
                             String severity = allergyCorr.get(IAllergyConstants.KEY_SEVERITY).toString();
                            //Per ogni allergia metto una riga al csv con i sintomi
                            //Cilco i simtomi
                            Map<String, Object> symptoms = (Map<String, Object>)(allergyCorr).get(IAllergyConstants.KEY_SYMPTOMS);
                            if(symptoms!= null && !symptoms.keySet().isEmpty()){
    
                                Map<Integer, Double> sintomiDefault = creaDefault();
                                //IMPOSTO LE FEATURES PRESENTI
                                symptoms.keySet().forEach(
                                        keySymp ->  { 
                                            Map<String, Object> symptomCorr = (Map<String, Object>)(Map<String, Object>)symptoms.get(keySymp) ; 
                                            String frequency = symptomCorr.get(IAllergyConstants.KEY_FREQUENCY).toString();
                                            
                                            sintomiDefault.put(getIndexSymptoms().get(keySymp),setValueFeature(frequency,severity)  );
                                });
                                
                                try{
                                    //Write IN SVM 
                                    if(getIndexAllergens().get(keyAll)!=null){
                                        StringBuilder corr = new StringBuilder("");
                                        corr.append(keyAll.equals(allergenLabel)?LABEL_TRUE:LABEL_FALSE);
                                        corr.append(DELIMITER);
                                        
                                        for(Integer keySym: sintomiDefault.keySet()){
                                            corr.append(keySym.toString());
                                            corr.append(DOUBLE_DOT);
                                            corr.append(sintomiDefault.get(keySym).toString());
                                            corr.append(DELIMITER);
                                        } 
                                        corr.append(END); 
                                        result.append(corr);
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                            
                        } 
    
                }
            }
            });
            return  result.toString() ;
        } else {
            return null;
        }
    }
    
    private Double setValueFeature(String frequency, String severity) {
        
        Double result =  featureOK;
        
        int plus = 1;
        try{
            plus = Integer.parseInt(severity) + (Integer.parseInt(frequency)*2);
        }catch(Exception e){
            
        } 
        return result + ( plus / 10);
    }
    
public Long getLastKey(){
        return last;
    }
    
    private Map<Integer, Double> creaDefault() {
        Map<Integer, Double> def = new HashMap<Integer, Double>();
        simptoms_keys.forEach(key ->{
            def.put(getIndexSymptoms().get(key), Double.valueOf(0.0));
        });
        return def;
    }

    private List<String> generateKeys() {
        List<String> result = new ArrayList<String>();
        Map<String, String[]> symt = IAllergyConstants.SYMPTOMS;
        symt.keySet().forEach(key ->{ 
            for(String cor:symt.get(key)){
                result.add(key+"_"+cor);
            }
        });
         
        return result;
    }

    private Map<String, Integer> getIndexSymptoms(){
        if(indexSymptoms == null){
            indexSymptoms = new HashMap<>();
            int idxCat = 100;
           for(String cat: IAllergyConstants.BODY_PARTS){ 
               for(String allerg: IAllergyConstants.SYMPTOMS.get(cat)){
                   indexSymptoms.put((cat+"_"+allerg), Integer.valueOf(idxCat));
                   idxCat++;
               } 
           }
        }
        return indexSymptoms;
    } 
    
    private Map<String, Integer> getIndexAllergens(){
        if(indexAllergens == null){
            indexAllergens = new HashMap<>();
            int idxCat = 0;
           for(String cat: IAllergyConstants.ALLERGEN_CATEGORIES){ 
               for(String allerg: IAllergyConstants.ALLERGENS.get(cat)){
                   indexAllergens.put((cat+"_"+allerg), Integer.valueOf(idxCat));
                   idxCat++;
               } 
           }
        }
        return indexAllergens;
    } 
}
