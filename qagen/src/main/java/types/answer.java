

/* First created by JCasGen Thu Dec 31 18:57:56 CET 2015 */
package types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Dec 31 18:57:56 CET 2015
 * XML source: C:/Users/t-kah_000/Documents/Programming/Java/NLP/nlp4web-qagen/qagen/src/main/resources/types.xml
 * @generated */
public class answer extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(answer.class);
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
  protected answer() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public answer(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public answer(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public answer(JCas jcas, int begin, int end) {
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
  //* Feature: Keywords

  /** getter for Keywords - gets 
   * @generated
   * @return value of the feature 
   */
  public StringList getKeywords() {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_Keywords == null)
      jcasType.jcas.throwFeatMissing("Keywords", "types.answer");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((answer_Type)jcasType).casFeatCode_Keywords)));}
    
  /** setter for Keywords - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKeywords(StringList v) {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_Keywords == null)
      jcasType.jcas.throwFeatMissing("Keywords", "types.answer");
    jcasType.ll_cas.ll_setRefValue(addr, ((answer_Type)jcasType).casFeatCode_Keywords, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Categories

  /** getter for Categories - gets 
   * @generated
   * @return value of the feature 
   */
  public StringList getCategories() {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_Categories == null)
      jcasType.jcas.throwFeatMissing("Categories", "types.answer");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((answer_Type)jcasType).casFeatCode_Categories)));}
    
  /** setter for Categories - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCategories(StringList v) {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_Categories == null)
      jcasType.jcas.throwFeatMissing("Categories", "types.answer");
    jcasType.ll_cas.ll_setRefValue(addr, ((answer_Type)jcasType).casFeatCode_Categories, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Hypernyms

  /** getter for Hypernyms - gets 
   * @generated
   * @return value of the feature 
   */
  public StringList getHypernyms() {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_Hypernyms == null)
      jcasType.jcas.throwFeatMissing("Hypernyms", "types.answer");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((answer_Type)jcasType).casFeatCode_Hypernyms)));}
    
  /** setter for Hypernyms - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setHypernyms(StringList v) {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_Hypernyms == null)
      jcasType.jcas.throwFeatMissing("Hypernyms", "types.answer");
    jcasType.ll_cas.ll_setRefValue(addr, ((answer_Type)jcasType).casFeatCode_Hypernyms, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: AnswerType

  /** getter for AnswerType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getAnswerType() {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_AnswerType == null)
      jcasType.jcas.throwFeatMissing("AnswerType", "types.answer");
    return jcasType.ll_cas.ll_getStringValue(addr, ((answer_Type)jcasType).casFeatCode_AnswerType);}
    
  /** setter for AnswerType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setAnswerType(String v) {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_AnswerType == null)
      jcasType.jcas.throwFeatMissing("AnswerType", "types.answer");
    jcasType.ll_cas.ll_setStringValue(addr, ((answer_Type)jcasType).casFeatCode_AnswerType, v);}    
  }

    