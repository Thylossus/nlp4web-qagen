
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
    
  
 
  /** @generated */
  final Feature casFeat_TagCloud;
  /** @generated */
  final int     casFeatCode_TagCloud;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getTagCloud(int addr) {
        if (featOkTst && casFeat_TagCloud == null)
      jcas.throwFeatMissing("TagCloud", "types.answer");
    return ll_cas.ll_getRefValue(addr, casFeatCode_TagCloud);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTagCloud(int addr, int v) {
        if (featOkTst && casFeat_TagCloud == null)
      jcas.throwFeatMissing("TagCloud", "types.answer");
    ll_cas.ll_setRefValue(addr, casFeatCode_TagCloud, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public answer_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_AnswerType = jcas.getRequiredFeatureDE(casType, "AnswerType", "uima.cas.String", featOkTst);
    casFeatCode_AnswerType  = (null == casFeat_AnswerType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_AnswerType).getCode();

 
    casFeat_TagCloud = jcas.getRequiredFeatureDE(casType, "TagCloud", "types.tagCloud", featOkTst);
    casFeatCode_TagCloud  = (null == casFeat_TagCloud) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_TagCloud).getCode();

  }
}



    