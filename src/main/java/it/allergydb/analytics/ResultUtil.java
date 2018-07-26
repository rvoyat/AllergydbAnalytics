package it.allergydb.analytics;
 

import it.allergydb.analytics.json.Allergene;
import it.allergydb.analytics.json.AllergenePercentuale;
import it.allergydb.analytics.json.AllergieInsieme;
import it.allergydb.analytics.json.Analytics;
import it.allergydb.analytics.json.ClinicCase;
import it.allergydb.analytics.json.Concorrenza;
import it.allergydb.analytics.json.PercentualiPerCategoria;
import it.allergydb.firebase4j.util.IAllergyConstants;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author rvoyat
 *
 */
public class ResultUtil {

    private static final String ORGAN ="Organ";
    public static final String PATTERN_DATE = "dd/MM/yyyy HH:mm:ss";
    
    public void rifinisciResult(Analytics result) { 
        //DB Size
        long len = Long.parseLong(result.getDbSize());
        String strLen  =  result.getDbSize();
        if(len > 1000000){
            //MB 
            strLen = (((new BigDecimal(len/1000000).setScale(2).toString()))   + " MB");
         } else if(len > 1000){
            //KB
            strLen =(((new BigDecimal(len/1000).setScale(2).toString())) + " KB");
         } else {
            //byte
            strLen = (String.valueOf(len) + " byte");
         } 
        result.setDbSize(strLen);
        
        //Averages
        for(Allergene allergene:result.getAllergenes()){
            if(!allergene.getOrgan().equals(ORGAN)){
                BigDecimal tot = BigDecimal.ZERO;
                for(String num:allergene.getNumPeoples()){ 
                        tot = tot.add(new BigDecimal(num));
                }
                allergene.setAverage(tot.doubleValue()>0?(tot.doubleValue()/(allergene.getNumPeoples().length-1)):0.0);
            }
        }
        
        //Percentuali allergeni
        for(PercentualiPerCategoria perc: result.getPercentualiPerCategoria()){
            for(AllergenePercentuale allperc: perc.getAllergenePercentuale()){
                allperc.setPercentuale(Double.valueOf(allperc.getPersone().intValue()/perc.getPersone().intValue()));
            }
        } 
    }

    public Analytics generateVoidAnalytics(it.allergydb.analytics.FeaturesInput input) {
        Analytics result = new Analytics();
        result.setDbSize("0");
        result.setDateUpdate(new SimpleDateFormat(PATTERN_DATE).format(new Date()));
        
        //Clinic cases
        result.setClinicCases(new ArrayList<ClinicCase>());
        for(int i=1;i<4;i++){
            ClinicCase cli = new ClinicCase();
            cli.setNumAllergies(i);
            result.getClinicCases().add(cli);
        }
        ClinicCase cli = new ClinicCase();
        cli.setNumAllergies(99);
        result.getClinicCases().add(cli);
      //Clinic cases
        result.setAllergenes(new ArrayList<>());
        Allergene allergene = new Allergene();
        allergene.setOrgan(ORGAN);
        allergene.setNumPeoples(IAllergyConstants.ALLERGEN_CATEGORIES);
        allergene.setAverage(Double.valueOf(0.0));
        result.getAllergenes().add(allergene);
        for(String bodyPArt:IAllergyConstants.BODY_PARTS){
            allergene = new Allergene();
            allergene.setOrgan(bodyPArt);
            allergene.setNumPeoples(new String[]{"0","0","0","0","0","0","0","0"});
            allergene.setAverage(Double.valueOf(0.0));
            result.getAllergenes().add(allergene);
        }
        
        
        return result;
    }
    public void updateAnalytics(Analytics result, String json, Map<String, Object> data) { 
        
            result.setDbSize(String.valueOf(Long.parseLong(result.getDbSize())+json.length()));
            
            if(data != null&&  data.keySet() !=null && !data.keySet().isEmpty()){ 
            data.keySet().forEach(key ->{
                 //Caso clinico
                
                Map<String,Integer> allergensSymptomCount = new HashMap<String,Integer>();
                List<String> allergeni = new ArrayList<String>();
                 
                Map<String, Object> allergies = (Map<String, Object>)((Map<String, Object>)data.get(key)).get(IAllergyConstants.KEY_ALLERGY);
                int numAllergie=0;
                if(allergies != null && !allergies.keySet().isEmpty()){
                    
                    //Aggiorno i clinicCases in base al numero di allergie per persona
                    numAllergie = allergies.keySet().size();
                    allergies.keySet().forEach( keyAll -> {
                            //Per ogni allergia 
                            Map<String, Object> allergyCorr = (Map<String, Object>)(Map<String, Object>)allergies.get(keyAll) ;
                            String category = allergyCorr.get(IAllergyConstants.KEY_CATEGORY).toString();
                            String allergene = allergyCorr.get(IAllergyConstants.KEY_NAME).toString();
                            allergeni.add(allergene);
                            aggiornaAnalyticsAllergene(result,category,allergene);
                            
                            String severity = allergyCorr.get(IAllergyConstants.KEY_SEVERITY).toString();
                            //Cilco i simtomi
                            Map<String, Object> symptoms = (Map<String, Object>)(allergyCorr).get(IAllergyConstants.KEY_SYMPTOMS);
                            if(symptoms!= null && !symptoms.keySet().isEmpty()){ 
                            symptoms.keySet().forEach(
                                    keySymp ->  { 
                                        Map<String, Object> symptomCorr = (Map<String, Object>)(Map<String, Object>)symptoms.get(keySymp) ;
                                        String catsympt = symptomCorr.get(IAllergyConstants.KEY_BODY_PART).toString();
                                        String manifestation = symptomCorr.get(IAllergyConstants.KEY_MANIFESTATION).toString();
                                        String frequency = symptomCorr.get(IAllergyConstants.KEY_FREQUENCY).toString();
                                        
                                       //Aggiorno la map di conteggi
                                       aggiornaMapConteggi(allergensSymptomCount,category,catsympt) ;
                            }); 
                            }
                    
                            
                    });

                }
                
                aggiornaAnalyticsCasoClinico(result, allergensSymptomCount,numAllergie,allergeni); 
                
            }); 
            } else {
                //
            } 
    }

    private void aggiornaAnalyticsAllergene(Analytics result, String category, String allergene) {
        if(result.getPercentualiPerCategoria()==null){
            result.setPercentualiPerCategoria(new ArrayList<PercentualiPerCategoria>());
        }
        boolean trovataCategoria = false;
        for(PercentualiPerCategoria corr:result.getPercentualiPerCategoria()){
            if(corr.getCategoria().equals(category)){
                trovataCategoria = true;
                boolean trovatoAllergene = false;
                for(AllergenePercentuale allergenePercentuale:corr.getAllergenePercentuale()){
                    if(allergenePercentuale.getAllergene().equals(allergene)){
                        trovatoAllergene = true;
                        allergenePercentuale.setPersone(allergenePercentuale.getPersone()+1);
                        break;
                    }
                }
                if(!trovatoAllergene){
                    AllergenePercentuale allergenePercentuale = new AllergenePercentuale();
                    allergenePercentuale.setAllergene(allergene);
                    allergenePercentuale.setPersone(Integer.valueOf(1));
                    corr.getAllergenePercentuale().add(allergenePercentuale);
                }
                break;
            }
        }
        if(!trovataCategoria){
            PercentualiPerCategoria newP = new PercentualiPerCategoria();
            newP.setCategoria(category);
            newP.setPersone(Integer.valueOf(1));
            AllergenePercentuale allergenePercentuale = new AllergenePercentuale();
            allergenePercentuale.setAllergene(allergene);
            allergenePercentuale.setPersone(Integer.valueOf(1));
            newP.setAllergenePercentuale(new ArrayList<>());
            newP.getAllergenePercentuale().add(allergenePercentuale);
            result.getPercentualiPerCategoria().add(newP);
        }
        // TODO Auto-generated method stub
        
    }

    private void aggiornaMapConteggi(Map<String, Integer> allergies, String category, String catsympt) { 
        String key = category+"_"+catsympt;
        if(allergies.get(key)==null){
            allergies.put(key, Integer.valueOf(0));
        }
        allergies.put(key, (allergies.get(key)+1));
    }

    private void aggiornaAnalyticsCasoClinico(Analytics result, Map<String, Integer> allergensSymptomCount, int numAllergie, List<String> allergeni) {
        
        result.setNumCases(String.valueOf(Long.parseLong(result.getNumCases())+1l));
        
        if(numAllergie>0){
            for(ClinicCase clinicCase:result.getClinicCases()){
                if(numAllergie <= clinicCase.getNumAllergies()){
                    Long corr =clinicCase.getNumPeoples()!= null?clinicCase.getNumPeoples():0l;
                    clinicCase.setNumPeoples(corr+1l);
                    break;
                }
            }
    
            for(String allergene:allergeni){
                if(result.getAllergieInsieme()==null){
                    result.setAllergieInsieme(new ArrayList<AllergieInsieme>());
                }
                AllergieInsieme allergCorr = null;
                for(AllergieInsieme corr:result.getAllergieInsieme()){
                    if(corr.getAllergene().equals(allergene)){
                        allergCorr = corr;
                        break;
                    }
                }
                boolean aggiungi = false;
                if((allergCorr ==null)){
                    aggiungi = true;
                    allergCorr = new AllergieInsieme();
                    allergCorr.setAllergene(allergene);
                    allergCorr.setConcorrenza(new ArrayList<Concorrenza>());
                }
                
                for(String allergene2:allergeni){
                    if(!allergene2.equals(allergene)){
                        //Aggiorno conteggio insieme
                        boolean trovato = false;
                        for(Concorrenza concorrenza:allergCorr.getConcorrenza()){
                            if(concorrenza.getAllergene().equals(allergene2)){
                                trovato = true;
                                concorrenza.setNumeroPersone(concorrenza.getNumeroPersone()+1);
                                break;
                            }
                        }
                        if(!trovato){
                            Concorrenza concorrenza= new Concorrenza();
                            concorrenza.setAllergene(allergene2);
                            concorrenza.setNumeroPersone(Integer.valueOf(1));
                            allergCorr.getConcorrenza().add(concorrenza);
                        }
                    }
                }
                
                if(aggiungi)
                    result.getAllergieInsieme().add(allergCorr);
            }
        }

        allergensSymptomCount.keySet().forEach( keyAll -> {
            String[] cat_sympt = keyAll.split("_");
            String category = cat_sympt[0];
            String boby_part = cat_sympt[1];
            for(Allergene allergene:result.getAllergenes()){ 
                if(allergene.getOrgan().equals(boby_part)){
                    int index = 0;
                    for(String cat:IAllergyConstants.ALLERGEN_CATEGORIES){
                        if(cat.equals(category)){
                            String peoples = allergene.getNumPeoples()[index];
                            allergene.getNumPeoples()[index] = String.valueOf(Long.valueOf(peoples)+1l);
                            break;
                        }
                        index++;
                    }
                    break;
                }
            }
            
        });
    }
}
