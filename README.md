# nlp4web-qagen
Project: generate multiplie choice answers for given question and answer.

## Important Messages
An updated [pipeline description](https://github.com/Thylossus/nlp4web-qagen/blob/master/documents/pipeline.md) was added to the documentation.

## Tasks
- [ ] Create configuration system for specifying paths and user accounts (e.g. for the database)
- [x] Create question sets
  - [x] IB / Set 1 [Tobias R.]
  - [x] [Set 2](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/resources/questions/questions-max.txt) [Tobias K.]
  - [x] [Set 3](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/resources/questions/questions-tobiask.txt) [Tobias K.]
  - [x] [Set 4](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/resources/questions/questions-frank-set4.txt) [Frank]
- [ ] Implementation
  - [x] Answer candidate UIMA type with tag cloud feature (see [#1](https://github.com/Thylossus/nlp4web-qagen/issues/1))
  - [x] [Open Trivia QA parser](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/input/OpenTriviaQAParser.java) [Frank]
  - [x] [Keyword extraction](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/question/processing/KeywordExtraction.java) for parsed questions [Tobias K.]
  - [x] [Category and hypernym detection](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/tag/cloud/enrichment/CategoryAndHypernymDetection.java) [Tobias R.]
    - [x] [Category search](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/tag/cloud/enrichment/CategorySearch.java) using [JWPL](https://dkpro.github.io/dkpro-jwpl/)
    - [ ] ~~[Hypernym search](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/tag/cloud/enrichment/HypernymSearch.java) using [JWI](https://projects.csail.mit.edu/jwi/)~~
  - [x] [Category ranking](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/candidate/extraction/CategoryRanking.java) [Tobias K.] 
  - [x] [Candidate extraction](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/candidate/extraction/CandidateExtraction.java) [Tobias K.]
  - [x] [Synonym resolution](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/candidate/extraction/SynonymResolution.java) [Frank]
  - [x] [Similarity detection](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/similarity/detection/SimilarityDetection.java) [Tobias R.]
  - [x] [Candidate selection](https://github.com/Thylossus/nlp4web-qagen/blob/master/qagen/src/main/java/consumer/CandidateSelection.java) [Tobias R.]
  - [x] The pipeline
- [x] Devlop evaluation formula for tests results
- [x] Analyze and summarize test results
- [x] Write final report
  - [x] Statement of the research problem [Tobias K.]
  - [x] Methodology and used third-party software
  - [x] High-level description of the software (including class/component diagram) [Tobias K.]
  - [x] Data sources (how obtained and processed?)
  - [x] ~~Preliminary testing/training (if applicable)~~
  - [x] What we did to improve the system's performance, and how these ideas worked out
  - [x] Final results of the system (i.e. evaluation results)
  - [x] Discussion of results
  - [x] Future work (e.g. extend system with WordNet, apply it to other types of questions, maybe other use cases)
  - [x] Statement indicating what group members did what
- [x] Create final presentation
  - Cover the same content as in the written report
  - Highlight particlularly interesing results
  - About ten minutes long
