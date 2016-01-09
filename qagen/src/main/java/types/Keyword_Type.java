
/* First created by JCasGen Sat Jan 09 10:39:33 CET 2016 */
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
 * Updated by JCasGen Sat Jan 09 10:39:47 CET 2016
 * @generated */
public class Keyword_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Keyword_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Keyword_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Keyword(addr, Keyword_Type.this);
  			   Keyword_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Keyword(addr, Keyword_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Keyword.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("types.Keyword");
 
  /** @generated */
  final Feature casFeat_Value;
  /** @generated */
  final int     casFeatCode_Value;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getValue(int addr) {
        if (featOkTst && casFeat_Value == null)
      jcas.throwFeatMissing("Value", "types.Keyword");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Value);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setValue(int addr, String v) {
        if (featOkTst && casFeat_Value == null)
      jcas.throwFeatMissing("Value", "types.Keyword");
    ll_cas.ll_setStringValue(addr, casFeatCode_Value, v);}
    
  
 
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
      jcas.throwFeatMissing("Categories", "types.Keyword");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Categories);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCategories(int addr, int v) {
        if (featOkTst && casFeat_Categories == null)
      jcas.throwFeatMissing("Categories", "types.Keyword");
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
      jcas.throwFeatMissing("Hypernyms", "types.Keyword");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Hypernyms);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setHypernyms(int addr, int v) {
        if (featOkTst && casFeat_Hypernyms == null)
      jcas.throwFeatMissing("Hypernyms", "types.Keyword");
    ll_cas.ll_setRefValue(addr, casFeatCode_Hypernyms, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Keyword_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Value = jcas.getRequiredFeatureDE(casType, "Value", "uima.cas.String", featOkTst);
    casFeatCode_Value  = (null == casFeat_Value) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Value).getCode();

 
    casFeat_Categories = jcas.getRequiredFeatureDE(casType, "Categories", "uima.cas.FSList", featOkTst);
    casFeatCode_Categories  = (null == casFeat_Categories) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Categories).getCode();

 
    casFeat_Hypernyms = jcas.getRequiredFeatureDE(casType, "Hypernyms", "uima.cas.FSList", featOkTst);
    casFeatCode_Hypernyms  = (null == casFeat_Hypernyms) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Hypernyms).getCode();

  }
}



    