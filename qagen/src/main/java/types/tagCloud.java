

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
public class tagCloud extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(tagCloud.class);
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
  protected tagCloud() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public tagCloud(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public tagCloud(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public tagCloud(JCas jcas, int begin, int end) {
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
  public FSList getKeywords() {
    if (tagCloud_Type.featOkTst && ((tagCloud_Type)jcasType).casFeat_Keywords == null)
      jcasType.jcas.throwFeatMissing("Keywords", "types.tagCloud");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((tagCloud_Type)jcasType).casFeatCode_Keywords)));}
    
  /** setter for Keywords - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKeywords(FSList v) {
    if (tagCloud_Type.featOkTst && ((tagCloud_Type)jcasType).casFeat_Keywords == null)
      jcasType.jcas.throwFeatMissing("Keywords", "types.tagCloud");
    jcasType.ll_cas.ll_setRefValue(addr, ((tagCloud_Type)jcasType).casFeatCode_Keywords, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    