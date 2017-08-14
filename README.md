# Search Engine Retrieval Program
Info Retrieval Project

<<<<<<< HEAD
Project (Due: Dec. 3, 2015)
=======
Test (for Search)

`java -jar Test.jar [-create] [-stem] [-stopwords] [-maxresults <#>] [-idf <#>]`

-stem and -stopwords toggle stopwords and stemming on/off.

-create forces the creation of new input files. If -create is not specified, the program will look for existing input files that correspond to the optional -stem and -stopwords arguments. If there are no existing input files, the program will create new files.

-maxresults <#> takes a parameter that is the maximum number of results that should be retrieved. The default is 15.
-idf <#> takes a parameter that is the idf threshold for the query terms. Only query terms that have an idf value higher than the threshold will be used. The default threshold is 1.


Eval

`java -jar Eval.jar [-create] [-stem] [-stopwords] [-maxresults <#>] [-idf <#>]`

Flags behave the same as Test.
The program will display the MAP and R-Precision values for each query on stdout, and show a final average for each of these values. These results are also saved to a file, eval.txt.


Tf-idf weighting schemes:

For terms:

* term frequency = 1 + log(f)
* inverse document frequency = log(N/df), where N is the collection size
* weighting = tf*idf

For queries:

* term frequency = frequency
* weighting = 1 + log(f)
>>>>>>> refs/remotes/origin/a2
