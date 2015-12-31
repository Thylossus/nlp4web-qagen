
/* First created by JCasGen Thu Dec 31 18:57:56 CET 2015 */
package types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Dec 31 18:57:56 CET 2015
 * @generated */
public class answer_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (answer_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = answer_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new answer(addr, answer_Type.this);
  			   answer_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new answer(addr, answer_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = answer.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("types.answer");
 
  /** @generated */
  final Feature casFeat_Keywords;
  /** @generated */
  final int     casFeatCode_Keywords;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getKeywords(int addr) {
        if (featOkTst && casFeat_Keywords == null)
      jcas.throwFeatMissing("Keywords", "types.answer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Keywords);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setKeywords(int addr, int v) {
        if (featOkTst && casFeat_Keywords == null)
      jcas.throwFeatMissing("Keywords", "types.answer");
    ll_cas.ll_setRefValue(addr, casFeatCode_Keywords, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Categories;
  /** @generated */
  final int     casFeatCode_Categories;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getCategories(int addr) {
        if (featOkTst && casFeat_Categories == null)
      jcas.throwFeatMissing("Categories", "types.answer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Categories);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCategories(int addr, int v) {
        if (featOkTst && casFeat_Categories == null)
      jcas.throwFeatMissing("Categories", "types.answer");
    ll_cas.ll_setRefValue(addr, casFeatCode_Categories, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Hypernyms;
  /** @generated */
  final int     casFeatCode_Hypernyms;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getHypernyms(int addr) {
        if (featOkTst && casFeat_Hypernyms == null)
      jcas.throwFeatMissing("Hypernyms", "types.answer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Hypernyms);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setHypernyms(int addr, int v) {
        if (featOkTst && casFeat_Hypernyms == null)
      jcas.throwFeatMissing("Hypernyms", "types.answer");
    ll_cas.ll_setRefValue(addr, casFeatCode_Hypernyms, v);}
    
  
 
  /** @generated */
  final Feature casFeat_AnswerType;
  /** @generated */
  final int     casFeatCode_AnswerType;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getAnswerType(int addr) {
        if (featOkTst && casFeat_AnswerType == null)
      jcas.throwFeatMissing("AnswerType", "types.answer");
    return ll_cas.ll_getStringValue(addr, casFeatCode_AnswerType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAnswerType(int addr, String v) {
        if (featOkTst && casFeat_AnswerType == null)
      jcas.throwFeatMissing("AnswerType", "types.answer");
    ll_cas.ll_setStringValue(addr, casFeatCode_AnswerType, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public answer_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Keywords = jcas.getRequiredFeatureDE(casType, "Keywords", "uima.cas.StringList", featOkTst);
    casFeatCode_Keywords  = (null == casFeat_Keywords) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Keywords).getCode();

 
    casFeat_Categories = jcas.getRequiredFeatureDE(casType, "Categories", "uima.cas.StringList", featOkTst);
    casFeatCode_Categories  = (null == casFeat_Categories) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Categories).getCode();

 
    casFeat_Hypernyms = jcas.getRequiredFeatureDE(casType, "Hypernyms", "uima.cas.StringList", featOkTst);
    casFeatCode_Hypernyms  = (null == casFeat_Hypernyms) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Hypernyms).getCode();

 
    casFeat_AnswerType = jcas.getRequiredFeatureDE(casType, "AnswerType", "uima.cas.String", featOkTst);
    casFeatCode_AnswerType  = (null == casFeat_AnswerType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_AnswerType).getCode();

  }
}



    