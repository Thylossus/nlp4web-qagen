

/* First created by JCasGen Sat Jan 09 10:39:33 CET 2016 */
package types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jan 09 10:39:47 CET 2016
 * XML source: C:/Users/t-kah_000/Documents/Programming/Java/NLP/nlp4web-qagen/qagen/src/main/resources/desc/types.xml
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
   
    
  //*--------------*
  //* Feature: TagCloud

  /** getter for TagCloud - gets 
   * @generated
   * @return value of the feature 
   */
  public tagCloud getTagCloud() {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_TagCloud == null)
      jcasType.jcas.throwFeatMissing("TagCloud", "types.answer");
    return (tagCloud)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((answer_Type)jcasType).casFeatCode_TagCloud)));}
    
  /** setter for TagCloud - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTagCloud(tagCloud v) {
    if (answer_Type.featOkTst && ((answer_Type)jcasType).casFeat_TagCloud == null)
      jcasType.jcas.throwFeatMissing("TagCloud", "types.answer");
    jcasType.ll_cas.ll_setRefValue(addr, ((answer_Type)jcasType).casFeatCode_TagCloud, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    