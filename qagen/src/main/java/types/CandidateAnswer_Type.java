
/* First created by JCasGen Tue Jan 19 19:11:02 CET 2016 */
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

/** 
 * Updated by JCasGen Tue Jan 19 19:39:14 CET 2016
 * @generated */
public class CandidateAnswer_Type extends Answer_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CandidateAnswer_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CandidateAnswer_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CandidateAnswer(addr, CandidateAnswer_Type.this);
  			   CandidateAnswer_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CandidateAnswer(addr, CandidateAnswer_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CandidateAnswer.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("types.CandidateAnswer");
 
  /** @generated */
  final Feature casFeat_wikipediaPageId;
  /** @generated */
  final int     casFeatCode_wikipediaPageId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getWikipediaPageId(int addr) {
        if (featOkTst && casFeat_wikipediaPageId == null)
      jcas.throwFeatMissing("wikipediaPageId", "types.CandidateAnswer");
    return ll_cas.ll_getIntValue(addr, casFeatCode_wikipediaPageId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setWikipediaPageId(int addr, int v) {
        if (featOkTst && casFeat_wikipediaPageId == null)
      jcas.throwFeatMissing("wikipediaPageId", "types.CandidateAnswer");
    ll_cas.ll_setIntValue(addr, casFeatCode_wikipediaPageId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_similarityScore;
  /** @generated */
  final int     casFeatCode_similarityScore;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public float getSimilarityScore(int addr) {
        if (featOkTst && casFeat_similarityScore == null)
      jcas.throwFeatMissing("similarityScore", "types.CandidateAnswer");
    return ll_cas.ll_getFloatValue(addr, casFeatCode_similarityScore);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSimilarityScore(int addr, float v) {
        if (featOkTst && casFeat_similarityScore == null)
      jcas.throwFeatMissing("similarityScore", "types.CandidateAnswer");
    ll_cas.ll_setFloatValue(addr, casFeatCode_similarityScore, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public CandidateAnswer_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_wikipediaPageId = jcas.getRequiredFeatureDE(casType, "wikipediaPageId", "uima.cas.Integer", featOkTst);
    casFeatCode_wikipediaPageId  = (null == casFeat_wikipediaPageId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_wikipediaPageId).getCode();

 
    casFeat_similarityScore = jcas.getRequiredFeatureDE(casType, "similarityScore", "uima.cas.Float", featOkTst);
    casFeatCode_similarityScore  = (null == casFeat_similarityScore) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_similarityScore).getCode();

  }
}



    