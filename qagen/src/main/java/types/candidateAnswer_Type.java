
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

/** 
 * Updated by JCasGen Sat Jan 09 10:39:47 CET 2016
 * @generated */
public class candidateAnswer_Type extends answer_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (candidateAnswer_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = candidateAnswer_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new candidateAnswer(addr, candidateAnswer_Type.this);
  			   candidateAnswer_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new candidateAnswer(addr, candidateAnswer_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = candidateAnswer.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("types.candidateAnswer");
 
  /** @generated */
  final Feature casFeat_SimilarityScore;
  /** @generated */
  final int     casFeatCode_SimilarityScore;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public float getSimilarityScore(int addr) {
        if (featOkTst && casFeat_SimilarityScore == null)
      jcas.throwFeatMissing("SimilarityScore", "types.candidateAnswer");
    return ll_cas.ll_getFloatValue(addr, casFeatCode_SimilarityScore);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSimilarityScore(int addr, float v) {
        if (featOkTst && casFeat_SimilarityScore == null)
      jcas.throwFeatMissing("SimilarityScore", "types.candidateAnswer");
    ll_cas.ll_setFloatValue(addr, casFeatCode_SimilarityScore, v);}
    
  
 
  /** @generated */
  final Feature casFeat_WikipediaPageId;
  /** @generated */
  final int     casFeatCode_WikipediaPageId;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getWikipediaPageId(int addr) {
        if (featOkTst && casFeat_WikipediaPageId == null)
      jcas.throwFeatMissing("WikipediaPageId", "types.candidateAnswer");
    return ll_cas.ll_getIntValue(addr, casFeatCode_WikipediaPageId);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setWikipediaPageId(int addr, int v) {
        if (featOkTst && casFeat_WikipediaPageId == null)
      jcas.throwFeatMissing("WikipediaPageId", "types.candidateAnswer");
    ll_cas.ll_setIntValue(addr, casFeatCode_WikipediaPageId, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public candidateAnswer_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_SimilarityScore = jcas.getRequiredFeatureDE(casType, "SimilarityScore", "uima.cas.Float", featOkTst);
    casFeatCode_SimilarityScore  = (null == casFeat_SimilarityScore) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_SimilarityScore).getCode();

 
    casFeat_WikipediaPageId = jcas.getRequiredFeatureDE(casType, "WikipediaPageId", "uima.cas.Integer", featOkTst);
    casFeatCode_WikipediaPageId  = (null == casFeat_WikipediaPageId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_WikipediaPageId).getCode();

  }
}



    