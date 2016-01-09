
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
public class tagCloud_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (tagCloud_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = tagCloud_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new tagCloud(addr, tagCloud_Type.this);
  			   tagCloud_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new tagCloud(addr, tagCloud_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = tagCloud.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("types.tagCloud");
 
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
      jcas.throwFeatMissing("Keywords", "types.tagCloud");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Keywords);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setKeywords(int addr, int v) {
        if (featOkTst && casFeat_Keywords == null)
      jcas.throwFeatMissing("Keywords", "types.tagCloud");
    ll_cas.ll_setRefValue(addr, casFeatCode_Keywords, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public tagCloud_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Keywords = jcas.getRequiredFeatureDE(casType, "Keywords", "uima.cas.FSList", featOkTst);
    casFeatCode_Keywords  = (null == casFeat_Keywords) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Keywords).getCode();

  }
}



    