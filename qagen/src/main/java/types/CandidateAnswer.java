

/* First created by JCasGen Thu Jan 21 09:20:21 CET 2016 */
package types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Tue Feb 02 20:05:10 CET 2016
 * XML source: /Users/imorril/Seafile/Master Studium/Natural Language Processing and the Web/nlp4web-qagen/qagen/src/main/resources/desc/type/candidateAnswer.xml
 * @generated */
public class CandidateAnswer extends Answer {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CandidateAnswer.class);
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
  protected CandidateAnswer() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public CandidateAnswer(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CandidateAnswer(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public CandidateAnswer(JCas jcas, int begin, int end) {
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
  //* Feature: wikipediaPageId

  /** getter for wikipediaPageId - gets 
   * @generated
   * @return value of the feature 
   */
  public int getWikipediaPageId() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_wikipediaPageId == null)
      jcasType.jcas.throwFeatMissing("wikipediaPageId", "types.CandidateAnswer");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_wikipediaPageId);}
    
  /** setter for wikipediaPageId - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setWikipediaPageId(int v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_wikipediaPageId == null)
      jcasType.jcas.throwFeatMissing("wikipediaPageId", "types.CandidateAnswer");
    jcasType.ll_cas.ll_setIntValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_wikipediaPageId, v);}    
   
    
  //*--------------*
  //* Feature: similarityScore

  /** getter for similarityScore - gets 
   * @generated
   * @return value of the feature 
   */
  public float getSimilarityScore() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_similarityScore == null)
      jcasType.jcas.throwFeatMissing("similarityScore", "types.CandidateAnswer");
    return jcasType.ll_cas.ll_getFloatValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_similarityScore);}
    
  /** setter for similarityScore - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSimilarityScore(float v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_similarityScore == null)
      jcasType.jcas.throwFeatMissing("similarityScore", "types.CandidateAnswer");
    jcasType.ll_cas.ll_setFloatValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_similarityScore, v);}    
   
    
  //*--------------*
  //* Feature: title

  /** getter for title - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTitle() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_title == null)
      jcasType.jcas.throwFeatMissing("title", "types.CandidateAnswer");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_title);}
    
  /** setter for title - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTitle(String v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_title == null)
      jcasType.jcas.throwFeatMissing("title", "types.CandidateAnswer");
    jcasType.ll_cas.ll_setStringValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_title, v);}    
  }

    