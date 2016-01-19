

/* First created by JCasGen Tue Jan 19 19:11:02 CET 2016 */
package types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.cas.IntegerList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Jan 19 19:39:14 CET 2016
 * XML source: C:/Users/t-kah_000/Documents/Programming/Java/NLP/nlp4web-qagen/qagen/src/main/resources/desc/types.xml
 * @generated */
public class Answer extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Answer.class);
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
  protected Answer() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Answer(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Answer(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Answer(JCas jcas, int begin, int end) {
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
  //* Feature: keywords

  /** getter for keywords - gets 
   * @generated
   * @return value of the feature 
   */
  public StringList getKeywords() {
    if (Answer_Type.featOkTst && ((Answer_Type)jcasType).casFeat_keywords == null)
      jcasType.jcas.throwFeatMissing("keywords", "types.Answer");
    return (StringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Answer_Type)jcasType).casFeatCode_keywords)));}
    
  /** setter for keywords - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKeywords(StringList v) {
    if (Answer_Type.featOkTst && ((Answer_Type)jcasType).casFeat_keywords == null)
      jcasType.jcas.throwFeatMissing("keywords", "types.Answer");
    jcasType.ll_cas.ll_setRefValue(addr, ((Answer_Type)jcasType).casFeatCode_keywords, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: categories

  /** getter for categories - gets 
   * @generated
   * @return value of the feature 
   */
  public IntegerList getCategories() {
    if (Answer_Type.featOkTst && ((Answer_Type)jcasType).casFeat_categories == null)
      jcasType.jcas.throwFeatMissing("categories", "types.Answer");
    return (IntegerList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Answer_Type)jcasType).casFeatCode_categories)));}
    
  /** setter for categories - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCategories(IntegerList v) {
    if (Answer_Type.featOkTst && ((Answer_Type)jcasType).casFeat_categories == null)
      jcasType.jcas.throwFeatMissing("categories", "types.Answer");
    jcasType.ll_cas.ll_setRefValue(addr, ((Answer_Type)jcasType).casFeatCode_categories, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: articles

  /** getter for articles - gets 
   * @generated
   * @return value of the feature 
   */
  public IntegerList getArticles() {
    if (Answer_Type.featOkTst && ((Answer_Type)jcasType).casFeat_articles == null)
      jcasType.jcas.throwFeatMissing("articles", "types.Answer");
    return (IntegerList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Answer_Type)jcasType).casFeatCode_articles)));}
    
  /** setter for articles - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setArticles(IntegerList v) {
    if (Answer_Type.featOkTst && ((Answer_Type)jcasType).casFeat_articles == null)
      jcasType.jcas.throwFeatMissing("articles", "types.Answer");
    jcasType.ll_cas.ll_setRefValue(addr, ((Answer_Type)jcasType).casFeatCode_articles, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    