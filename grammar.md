# Grammar For language
|desciption                 |grammar name      |grammar                                                                                 | regex           |
|---------------------------|------------------|----------------------------------------------------------------------------------------|-----------------|
|statment                   | \<stmt>          | <var_ass> \| <func_ass>   \| \<loop> \| \<print>                                       |                 |
|variable assignment        | <var_ass>        |<var_list> = <val_list>.   \| \<var> = \<add_sub>.                                      | (.+) = (.+)\\\\.|
|addition and subtraction   | \<add_sub>       |\<comp> \| \<add_sub> + \<comp>. \| \<add_sub> - \<comp>.                               | |
|comparators                | \<comp>          |\<mult_div> \| \<comp> > \<mult_div>. \| \<comp> < \<mult_div>. \| \<comp> == \<mult_div>. | |
|multiplication and division| \<mult_div>      |\<neg> \| \<mult_div> * \<neg>. \| \<mult_div> / \<neg>. \| \<mult_div> v/ \<neg>. \| \<mult_div> ^/ \<neg>. | |
|negative/negate            |\<neg>            | -\<val> \| ~\<val> \| \<val>                                                           | |
|function assignment        | <func_ass>       |\<var> => (<var_list>)                                                                  |                 |
|loops                      | \<loop>          |\<var> >> \<int>  .. \| \<comp> >>   ?  ..                                              |                 |
|print                      | \<print>         | p(\<val>)                                                                              | |
|variable list              | <var_list>       |\<var>     \| \<var>, <var_list>                                                        | split on ","    |
|value list                 | <val_list>       |\<val>     \| \<val>, <val_list>                                                        | split on ","    |
|values                     | \<val>           |\<int>  \| \<bool> \| \<var> \| (\<add_sub>)                                            | |
|variables                  | \<var>           |\<char> \| \<char>\<var>                                                                | |
|characters                 | \<char>          | a \| b \| ... \| y \| z \| A \| B \| ... \| Y \| Z \| \<digit>                         | |
|int                        | \<digit>         | 0 \| 1 \| 2 \| 3 \| 4 \| 5 \| 6 \| 7 \| 8 \| 9                                         | |
|boolean                    | \<bool>          | True \| False                                                                          | |