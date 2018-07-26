package it.allergydb.firebase4j.util;

import java.util.HashMap;
import java.util.Map;

public class IAllergyConstants {

    public static final String[] BODY_PARTS = {"Nose","Eye","Lungs","Skin","Other"};  
    
    public static final String[] ALLERGEN_CATEGORIES = {
        "Pollen","Food","Medication","Acarus","Mold"
            ,"Pet","Latex","Poison"}; 
    

    public static final Map<String, String[]> SYMPTOMS = generateSymp();
    public static final Map<String, String[]> ALLERGENS = generateAll(); 
    
    public static final String KEY_ANALYTICS = "analytics";
    
    public static final String KEY_ALLERGY = "Allergy";
    public static final String KEY_SYMPTOMS = "Symptoms";
    public static final String KEY_CATEGORY = "Category";
    public static final String KEY_BODY_PART = "Body_part";

    public static final String KEY_NAME = "Name";
    public static final String KEY_SEVERITY = "Severity";

    public static final String KEY_MANIFESTATION = "Manifestation";
    public static final String KEY_FREQUENCY = "Frequency";
    
    
    private static Map<String, String[]> generateSymp(){
        Map<String, String[]> symptoms = new HashMap<String, String[]>(); 
        symptoms.put(IAllergyConstants.BODY_PARTS[0],new String[]{"Pricking","Runny","Sneezing"});
        symptoms.put(IAllergyConstants.BODY_PARTS[1],new String[]{"Pricking","Wateryeyes"} );
        symptoms.put(IAllergyConstants.BODY_PARTS[2],new String[]{"Cough","Shortness-of-breath","Wheezing","Bronchospasm"});
        symptoms.put(IAllergyConstants.BODY_PARTS[3],new String[]{"Pricking","Eczema", "Orticary","Dermatitis","Erythema","Vasculitis","Lyell-syndrome","Steven-Johnson-syndrome"});
        symptoms.put(IAllergyConstants.BODY_PARTS[4],new String[]{"Sickness","Diarrhea","Sleepiness","Angioedema","Dizziness","Dyspnea","Schock-anaphylactic","Fainting" ,"Coma" });
        return symptoms;
    } 
    

    private static Map<String, String[]> generateAll(){
        Map<String, String[]> allergens = new HashMap<String, String[]>(); 
        allergens = new HashMap<String, String[]>();
        allergens.put(IAllergyConstants.ALLERGEN_CATEGORIES[0],new String[]{"Birch","Graminacee","Hazel","Pellitory","Ambrosia"});
        allergens.put(IAllergyConstants.ALLERGEN_CATEGORIES[1],new String[]{"Egg","Apple","Milk","Strawberry","Walnut"});
        allergens.put(IAllergyConstants.ALLERGEN_CATEGORIES[2],new String[]{"ASA","Procaina Lenident","Dentosedina","Tetracaina","Optocain"});
        allergens.put(IAllergyConstants.ALLERGEN_CATEGORIES[3],new String[]{"Dermatophagoides","Lepidoglyphus","Tyrophagus","Glycyphagus","Gohiera"});
        allergens.put(IAllergyConstants.ALLERGEN_CATEGORIES[4],new String[]{"Candida","Penicillium","Alternaria","Mucor","Aspergillus"});
        allergens.put(IAllergyConstants.ALLERGEN_CATEGORIES[5],new String[]{"Cat","Dog" });
        allergens.put(IAllergyConstants.ALLERGEN_CATEGORIES[6],new String[]{"Latex"  });
        allergens.put(IAllergyConstants.ALLERGEN_CATEGORIES[7],new String[]{"Apis" ,"Vespa" ,"Vespula"});
        return allergens;
    }
}
