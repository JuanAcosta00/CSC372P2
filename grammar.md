# Grammar For language
|desciption                 |grammar name      |grammar                                                                                 | regex               |
|---------------------------|------------------|----------------------------------------------------------------------------------------|---------------------|
|statment                   | \<stmt>          | <var_ass> \| <func_ass>   \| \<loop> \| \<print> \| \<end>                             |checked for in series|
|variable assignment        | <var_ass>        |<var_list> = <val_list>.   \| \<var> = \<add_sub>.                                      | (.+)(\s?)=(\\s)?(.+)\\.|
|addition and subtraction   | \<add_sub>       |\<comp> \| \<add_sub> + \<comp>. \| \<add_sub> - \<comp>                                | (\\d+)(\\s)?(\\+|-)(\\s)?(.+) |
|comparators                | \<comp>          |\<mult_div> \| \<comp> > \<mult_div>  \| \<comp> < \<mult_div>  \| \<comp> == \<mult_div>  | (\\S+)(\\s)?(>\|<\|&\|==\|\\\|)(\\s)?(.+) |
|multiplication and division| \<mult_div>      |\<neg> \| \<mult_div> * \<neg>  \| \<mult_div> / \<neg>. \| \<mult_div> v/ \<neg>  \| \<mult_div> ^/ \<neg>  | (\\d+)(\\s)?(\\/\|\\*\|v/\|\\^/\|#/)(\\s)?(.+)|
|negative/negate            |\<neg>            | -\<val> \| ~\<val> \| \<val>                                                           | (-\|~)?(\\S+) |
|loops                      | \<loop>          |\<var> >> \<int>  .. \| \<comp> >>   ?  ..                                              | (.+) >> (.+) |
|print                      | \<print>         | p(\<val>)                                                                              | p(.+)\\. |
|variable list              | <var_list>       |\<var>     \| \<var>, <var_list>                                                        | split on ","    |
|value list                 | <val_list>       |\<val>     \| \<val>, <val_list>                                                        | split on ","    |
|values                     | \<val>           |\<int>  \| \<bool> \| \<var> \| (\<add_sub>)                                            | using regex for each type |
|variables                  | \<var>           |\<char> \| \<char>\<var>                                                                | \\w+ |
|characters                 | \<char>          | a \| b \| ... \| y \| z \| A \| B \| ... \| Y \| Z \| \<digit>                         | \\w+ |
|int                        | \<digit>         | 0 \| 1 \| 2 \| 3 \| 4 \| 5 \| 6 \| 7 \| 8 \| 9                                         | \\d+|
|boolean                    | \<bool>          | True \| False                                                                          | True|False |
|end terminator             | \<end>           | end                                                                                    | end |