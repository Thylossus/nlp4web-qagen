

/* First created by JCasGen Sat Jan 09 10:39:33 CET 2016 */
package types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jan 09 10:39:47 CET 2016
 * XML source: C:/Users/t-kah_000/Documents/Programming/Java/NLP/nlp4web-qagen/qagen/src/main/resources/desc/types.xml
 * @generated */
public class Keyword extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Keyword.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Keyword() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Keyword(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Keyword(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Keyword(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: Value

  /** getter for Value - gets 
   * @generated
   * @return value of the feature 
   */
  public String getValue() {
    if (Keyword_Type.featOkTst && ((Keyword_Type)jcasType).casFeat_Value == null)
      jcasType.jcas.throwFeatMissing("Value", "types.Keyword");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Keyword_Type)jcasType).casFeatCode_Value);}
    
  /** setter for Value - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setValue(String v) {
    if (Keyword_Type.featOkTst && ((Keyword_Type)jcasType).casFeat_Value == null)
      jcasType.jcas.throwFeatMissing("Value", "types.Keyword");
    jcasType.ll_cas.ll_setStringValue(addr, ((Keyword_Type)jcasType).casFeatCode_Value, v);}    
   
    
  //*--------------*
  //* Feature: Categories

  /** getter for Categories - gets 
   * @generated
   * @return value of the feature 
   */
  public FSList getCategories() {
    if (Keyword_Type.featOkTst && ((Keyword_Type)jcasType).casFeat_Categories == null)
      jcasType.jcas.throwFeatMissing("Categories", "types.Keyword");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Keyword_Type)jcasType).casFeatCode_Categories)));}
    
  /** setter for Categories - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCategories(FSList v) {
    if (Keyword_Type.featOkTst && ((Keyword_Type)jcasType).casFeat_Categories == null)
      jcasType.jcas.throwFeatMissing("Categories", "types.Keyword");
    jcasType.ll_cas.ll_setRefValue(addr, ((Keyword_Type)jcasType).casFeatCode_Categories, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Hypernyms

  /** getter for Hypernyms - gets 
   * @generated
   * @return value of the feature 
   */
  public FSList getHypernyms() {
    if (Keyword_Type.featOkTst && ((Keyword_Type)jcasType).casFeat_Hypernyms == null)
      jcasType.jcas.throwFeatMissing("Hypernyms", "types.Keyword");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Keyword_Type)jcasType).casFeatCode_Hypernyms)));}
    
  /** setter for Hypernyms - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setHypernyms(FSList v) {
    if (Keyword_Type.featOkTst && ((Keyword_Type)jcasType).casFeat_Hypernyms == null)
      jcasType.jcas.throwFeatMissing("Hypernyms", "types.Keyword");
    jcasType.ll_cas.ll_setRefValue(addr, ((Keyword_Type)jcasType).casFeatCode_Hypernyms, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    