# Pipeline

1. OpenTriviaQA parser
  * Read a file consisting of several questions in the OpenTriviaQA format
  * Create one document for each question
  * Annotate the question and the correct answer
  * Output: the document with the question and correct answer annotations
2. POS tagger
  * External component (e.g. Standford POS tagger)
3. Named entity annotator
  * External component
4. Keyword extraction
  * Input: annotated document (annotations: question, answer, POS tags, named entities)
  * Fill the answer annotation's keyword list by iterating over the POS tags and the named entities covered by the question annotation
  * Output: annotated document -> the answer annotation's keyword list is filled
5. Category and hypernym detection
  * Based on the correct answer (can be retrieved from the respective annotation) and the keywords (stored in the aforementioned keyword list), find category ids from Wikipedia using JWPL (also article ids from Wikipedia and hypernyms from WordNet in a later release)
  * Output: annotated document -> the answer annotation's category id list (and the article id list) are filled
6. Category ranking
  * Based on the category list, find the x (e.g. x = 10) best categories according to some measure
  * Add the categories answer annotation
  * Output: annotated document -> the answer annotation's most relevant category list is filled
7. Candidate extraction
  * Find all articles of the categories listed in the most relevant category list
  * Add an candidate answer annotation for each article
  * Output: annotated document -> candidate answers added
8. Duplicate detection
  * Remove all duplicate candidate answer annotations from the index
  * Output: annotated document -> duplicate candidate answers removed
9. Synonym resolution
  * Remove all candidate answer annotations from the index that are synonymous with the correct answer
  * Output: annotated document -> synonymous candidate answers removed
10. Category and hypernym detection
  * Perform category and hypernym search (aka. tag cloud creation) for each candidate answer
  * Output: for each candidate answer the category id list (and article id list) is filled with ids
11. Similarity detection
  * Perform a similarity measurement between the correct answer's tag cloud and the candidate answers' tag clouds
  * Output: each candidate answer has a similarity score
12. Candidate selection
  * Select the x (e.g. x = 3) candidate answers with the highes similarity score
