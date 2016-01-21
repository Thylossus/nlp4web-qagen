
/* First created by JCasGen Thu Jan 21 09:17:09 CET 2016 */
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
 * Updated by JCasGen Thu Jan 21 09:20:21 CET 2016
 * @generated */
public class Answer_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Answer_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Answer_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Answer(addr, Answer_Type.this);
  			   Answer_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Answer(addr, Answer_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Answer.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("types.Answer");
 
  /** @generated */
  final Feature casFeat_keywords;
  /** @generated */
  final int     casFeatCode_keywords;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getKeywords(int addr) {
        if (featOkTst && casFeat_keywords == null)
      jcas.throwFeatMissing("keywords", "types.Answer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_keywords);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setKeywords(int addr, int v) {
        if (featOkTst && casFeat_keywords == null)
      jcas.throwFeatMissing("keywords", "types.Answer");
    ll_cas.ll_setRefValue(addr, casFeatCode_keywords, v);}
    
  
 
  /** @generated */
  final Feature casFeat_categories;
  /** @generated */
  final int     casFeatCode_categories;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getCategories(int addr) {
        if (featOkTst && casFeat_categories == null)
      jcas.throwFeatMissing("categories", "types.Answer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_categories);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCategories(int addr, int v) {
        if (featOkTst && casFeat_categories == null)
      jcas.throwFeatMissing("categories", "types.Answer");
    ll_cas.ll_setRefValue(addr, casFeatCode_categories, v);}
    
  
 
  /** @generated */
  final Feature casFeat_articles;
  /** @generated */
  final int     casFeatCode_articles;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getArticles(int addr) {
        if (featOkTst && casFeat_articles == null)
      jcas.throwFeatMissing("articles", "types.Answer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_articles);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setArticles(int addr, int v) {
        if (featOkTst && casFeat_articles == null)
      jcas.throwFeatMissing("articles", "types.Answer");
    ll_cas.ll_setRefValue(addr, casFeatCode_articles, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Answer_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_keywords = jcas.getRequiredFeatureDE(casType, "keywords", "uima.cas.NonEmptyStringList", featOkTst);
    casFeatCode_keywords  = (null == casFeat_keywords) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_keywords).getCode();

 
    casFeat_categories = jcas.getRequiredFeatureDE(casType, "categories", "uima.cas.NonEmptyIntegerList", featOkTst);
    casFeatCode_categories  = (null == casFeat_categories) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_categories).getCode();

 
    casFeat_articles = jcas.getRequiredFeatureDE(casType, "articles", "uima.cas.NonEmptyIntegerList", featOkTst);
    casFeatCode_articles  = (null == casFeat_articles) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_articles).getCode();

  }
}



    