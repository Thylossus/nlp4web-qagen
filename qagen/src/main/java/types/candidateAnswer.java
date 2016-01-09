

/* First created by JCasGen Sat Jan 09 10:39:33 CET 2016 */
package types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Sat Jan 09 10:39:47 CET 2016
 * XML source: C:/Users/t-kah_000/Documents/Programming/Java/NLP/nlp4web-qagen/qagen/src/main/resources/desc/types.xml
 * @generated */
public class candidateAnswer extends answer {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(candidateAnswer.class);
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
  protected candidateAnswer() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public candidateAnswer(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public candidateAnswer(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public candidateAnswer(JCas jcas, int begin, int end) {
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
  //* Feature: SimilarityScore

  /** getter for SimilarityScore - gets 
   * @generated
   * @return value of the feature 
   */
  public float getSimilarityScore() {
    if (candidateAnswer_Type.featOkTst && ((candidateAnswer_Type)jcasType).casFeat_SimilarityScore == null)
      jcasType.jcas.throwFeatMissing("SimilarityScore", "types.candidateAnswer");
    return jcasType.ll_cas.ll_getFloatValue(addr, ((candidateAnswer_Type)jcasType).casFeatCode_SimilarityScore);}
    
  /** setter for SimilarityScore - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSimilarityScore(float v) {
    if (candidateAnswer_Type.featOkTst && ((candidateAnswer_Type)jcasType).casFeat_SimilarityScore == null)
      jcasType.jcas.throwFeatMissing("SimilarityScore", "types.candidateAnswer");
    jcasType.ll_cas.ll_setFloatValue(addr, ((candidateAnswer_Type)jcasType).casFeatCode_SimilarityScore, v);}    
   
    
  //*--------------*
  //* Feature: WikipediaPageId

  /** getter for WikipediaPageId - gets Reference to a wikipedia page in a hash map or database.
   * @generated
   * @return value of the feature 
   */
  public int getWikipediaPageId() {
    if (candidateAnswer_Type.featOkTst && ((candidateAnswer_Type)jcasType).casFeat_WikipediaPageId == null)
      jcasType.jcas.throwFeatMissing("WikipediaPageId", "types.candidateAnswer");
    return jcasType.ll_cas.ll_getIntValue(addr, ((candidateAnswer_Type)jcasType).casFeatCode_WikipediaPageId);}
    
  /** setter for WikipediaPageId - sets Reference to a wikipedia page in a hash map or database. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setWikipediaPageId(int v) {
    if (candidateAnswer_Type.featOkTst && ((candidateAnswer_Type)jcasType).casFeat_WikipediaPageId == null)
      jcasType.jcas.throwFeatMissing("WikipediaPageId", "types.candidateAnswer");
    jcasType.ll_cas.ll_setIntValue(addr, ((candidateAnswer_Type)jcasType).casFeatCode_WikipediaPageId, v);}    
  }

    