

/* First created by JCasGen Thu Jan 21 09:18:08 CET 2016 */
package types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.NonEmptyIntegerList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Jan 21 09:19:20 CET 2016
 * XML source: C:/Users/t-kah_000/Documents/Programming/Java/NLP/nlp4web-qagen/qagen/src/main/resources/desc/type/correctAnswer.xml
 * @generated */
public class CorrectAnswer extends Answer {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CorrectAnswer.class);
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
  protected CorrectAnswer() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public CorrectAnswer(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CorrectAnswer(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public CorrectAnswer(JCas jcas, int begin, int end) {
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
  //* Feature: mostRelevantCategories

  /** getter for mostRelevantCategories - gets 
   * @generated
   * @return value of the feature 
   */
  public NonEmptyIntegerList getMostRelevantCategories() {
    if (CorrectAnswer_Type.featOkTst && ((CorrectAnswer_Type)jcasType).casFeat_mostRelevantCategories == null)
      jcasType.jcas.throwFeatMissing("mostRelevantCategories", "types.CorrectAnswer");
    return (NonEmptyIntegerList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CorrectAnswer_Type)jcasType).casFeatCode_mostRelevantCategories)));}
    
  /** setter for mostRelevantCategories - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMostRelevantCategories(NonEmptyIntegerList v) {
    if (CorrectAnswer_Type.featOkTst && ((CorrectAnswer_Type)jcasType).casFeat_mostRelevantCategories == null)
      jcasType.jcas.throwFeatMissing("mostRelevantCategories", "types.CorrectAnswer");
    jcasType.ll_cas.ll_setRefValue(addr, ((CorrectAnswer_Type)jcasType).casFeatCode_mostRelevantCategories, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    