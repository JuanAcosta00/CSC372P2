import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// import java.util.Arrays;
import java.util.Scanner; // Import the Scanner class to read text files
@SuppressWarnings({"rawtypes", "unchecked"})
public class Transpiler {
  private static List existingVariables = new ArrayList<String>();
  // private static Pattern func_assign = Pattern.compile("^(\\w+) => (.+)$");
  private static Pattern loop = Pattern.compile("^(.+) >> (.+)$");
  private static Pattern print = Pattern.compile("^p(.+)\\.$");
  private static Pattern var_assign = Pattern.compile("^(.+)(\s?)=(\\s)?(.+)\\.$");
  private static Pattern type_var_dec = Pattern.compile("^(\\w+)(\\s|,(\\w+))+$");
  private static Pattern add_sub = Pattern.compile("^(\\d+)(\\s)?(\\+|-)(\\s)?(.+)$");
  private static Pattern mult_div = Pattern.compile("^(\\d+)(\\s)?(\\/|\\*|v/|\\^/|#/)(\\s)?(.+)$");
  private static Pattern comp = Pattern.compile("^(\\S+)(\\s)?(>|<|&|==|\\|)(\\s)?(.+)$");
  private static Pattern str = Pattern.compile("^(\".+\")$");
  private static Pattern intVal = Pattern.compile("^\\d+$");
  private static Pattern var = Pattern.compile("^\\w+$");
  private static Pattern neg = Pattern.compile("^(-|~)?(\\S+)$");
  private static Pattern paren = Pattern.compile("^\\(.+\\)$");
  private static Pattern bool = Pattern.compile("^True|False$");
  private static Pattern end = Pattern.compile("^end$");
  private static Pattern if_statements = Pattern.compile("^if(\s)?(.+)$");
  private static Pattern else_statements = Pattern.compile("^else$");

  public static void main(String[] args) throws IOException {
    if(args.length == 0){
      Scanner in = new Scanner(System.in);
      System.out.print(">> ");
      String cmd = in.nextLine();
      while (!cmd.equals("exit")) {
        parseCmd(cmd);
        System.out.print(">> ");
        cmd = in.nextLine();
      }
      in.close();
    } else {
      readFile(args[0]);
    }
  }

  private static Pair parseCmd(String cmd) {
    boolean matchFound = false;
    if(matchFound == false) {
      Pair vA = varAssign(cmd);
      if(vA.isValid()) {
        return vA;
      }
      if(matchFound == false) {
        Pair lp = loop(cmd);
        if(lp.isValid()) {
          return lp;
        }
        if(matchFound == false) {
          Pair p = print(cmd);
          if(p.isValid()) {
            return p;
          }
          if(matchFound == false) {
            Pair iP = ifStatements(cmd);
            if(iP.isValid()) {
              return iP;
            }
            if(matchFound == false) {
              Pair eP = elseStatements(cmd);
              if(eP.isValid()) {
                return eP;
              }
              if(matchFound == false) {
                Pair endP = endTest(cmd);
                if(endP.isValid()) {
                  return endP;
                }
                System.out.printf("ERROR COMMAND %s CANNOT BE RESOLVED\n", cmd);
              }
            }
          }
        }
      }
    }
    return null;
  }

  private static Pair elseStatements(String cmd) {
    Matcher m = else_statements.matcher(cmd);
    boolean match = false;
    Pair retVal = new Pair(null, null);
    if(m.find()) {
      match = true;
      retVal.setIsValid(true);
      retVal.setType("else");
    }
    printMsg(match, "<if_statement>", cmd, "if statement");
    return retVal;
  }

  private static Pair ifStatements(String cmd) {
    Matcher m = if_statements.matcher(cmd);
    boolean match = false;
    Pair retVal = new Pair(null, null);
    if(m.find()) {
      match = true;
      retVal.setIsValid(true);
      retVal.setType("if");
      retVal.setValue(m.group(2));
    }
    printMsg(match, "<if_statement>", cmd, "if statement");
    return retVal;
    }

  private static Pair endTest(String cmd) {
    Matcher m = end.matcher(cmd);
    boolean match = false;
    Pair retVal = new Pair(null, null);
    if(m.find()) {
      match = true;
      retVal.setIsValid(true);
      retVal.setType("end");
    }
    printMsg(match, "<endTerm>", cmd, "end terminator");
    return retVal;
  }

  // private static boolean func_assign(String cmd) {
  //   Matcher m = func_assign.matcher(cmd);
  //   boolean match = false;
  //   if(m.find()) {
  //     match = true;
  //   }
  //   printMsg(match, "<func_assign>", cmd, "func assignment");
  //   return match;
  // }

  private static Pair print(String cmd) {
    Matcher m = print.matcher(cmd);
    boolean match = false;
    Pair retVal = new Pair("print", null);
    if(m.find()) {
      match = true;
      retVal.setValue(m.group(1));
      retVal.setIsValid(true);
    }
    printMsg(match, "<print>", cmd, "print statement");
    return retVal;
  }

  private static Pair loop(String cmd) {
    Matcher m = loop.matcher(cmd);
    boolean match = false;
    Pair retVal = new Pair(null, null);
    if(m.find()) {
      match = true;
      retVal.setType("loop");
      retVal.setValue(m.group(2));
      retVal.setVar(m.group(1));
      retVal.setIsValid(true);
    }
    printMsg(match, "<loop>", cmd, "loop statement");
    return retVal;
  }

  private static Pair varAssign(String cmd) {
    Matcher m = var_assign.matcher(cmd);
    boolean match = false;
    Pair retVal = new Pair(null, null);
    if (m.find()) {
      match = true;
      retVal = addSub(m.group(4));
      match = match && varDecList(m.group(1));
      match = match && retVal.isValid();
      if(match) {
        retVal.setIsValid(true);
        retVal.setVar(m.group(1));
        retVal.setValue(m.group(4));
        return retVal;
      } else if(match == false) {
        // System.out.println("group 4 " + m.group(4));
        retVal = valList(m.group(4));
        match = true;
        match = match && retVal.isValid();
        match = match && varDecList(m.group(1));
        retVal.setVar(m.group(1));
        if (match == false) {
          match = true;
          match = match && varDecList(m.group(1));
          match = match && compVal(m.group(4));
          if(match == true) {
            retVal = new Pair("boolean", m.group(4));
            retVal.setIsValid(true);
            return retVal;
          }
        }
      }
    }
    printMsg(match, "<var_assign>", cmd, "variable assignment statement");
    return retVal;
  }

  private static boolean compVal(String cmd) {
    Matcher m = comp.matcher(cmd);
    boolean match = false;
    if (m.find()) {
      match = true;
    }
    printMsg(match, "<comp>", cmd, "comparison values");
    return match;
  }

  private static Pair addSub(String cmd) {
    if(cmd == null) {
      return null;
    }
    Pair retVal = new Pair(null, cmd);
    Matcher m = add_sub.matcher(cmd);
    boolean match = false;
    if(m.find()) {
      match = true;
      // System.out.println(m.group(5));
      retVal = neg(m.group(1));
      match = match && retVal.isValid();
      match = match && addSub(m.group(5)).isValid();
      if(match)
        retVal.setValue(m.group(5));
        retVal.setIsValid(true);
    } else {
      // System.out.println("multDiv" + cmd);
      match = true;
      retVal = multDiv(cmd);
      match = match && retVal.isValid();
      if(match)
        retVal.setIsValid(true);
      if (match == false) {
        match = true;
        retVal = neg(cmd);
        if(retVal.getType() != "int") {
          retVal.setIsValid(false);
        }
        match = match && retVal.isValid();
        if(match)
          retVal.setIsValid(true);
      }
    }
    printMsg(match, "<add_sub>", cmd, "addition or subtraction");
    return retVal;
  }

  private static Pair multDiv(String cmd) {
    Matcher m = mult_div.matcher(cmd);
    boolean match = false;
    Pair retVal = new Pair(null, null);
    if(m.find()) {
      match = true;
      retVal = neg(m.group(1));
      match = match && retVal.isValid();
      match = match && addSub(m.group(5)).isValid();
    } else {
      match = true;
      retVal = neg(cmd);
      if(retVal.getType() != "int") {
        retVal.setIsValid(false);
      }
      match = match && retVal.isValid();
    }
    printMsg(match, "<mult_div>", cmd, "multiplication or division");
    return retVal;
  }

  private static boolean varDecList(String cmd) {
    String[] split = cmd.split(", ");

    boolean match = true;
    for (int i = 0; i < split.length; i++) {
      match = match && varDec(split[i]);
    }
    printMsg(match, "<var_dec_list>", cmd, "variable declaration list");
    return match;
  }

  private static boolean varDec(String cmd) {
    boolean match = false;
    Matcher m = type_var_dec.matcher(cmd);
    if (m.find()) {
      match = true;
      match = match && var(m.group(1));
    } else
      match = var(cmd);
    printMsg(match, "<var_dec>", cmd, "variable declaration");
    return match;
  }

  private static boolean type(String cmd) {
    // Matcher m = type.matcher(cmd);
    // boolean match = m.find();
    // printMsg(match, "<type>", cmd, "type");
    return false;
  }

  private static boolean var(String cmd) {
    Matcher m = var.matcher(cmd);
    boolean match = m.find();
    printMsg(match, "<var>", cmd, "variable");
    return match;
  }

  private static Pair valList(String cmd) {
    String[] split = cmd.split(", ");
    boolean match = true;
    Pair test = null;
    for (int i = 0; i < split.length; i++) {
      // System.out.println(split[i]);
      test = neg(split[i]);
      match = match && test.isValid();
    }
    printMsg(match, "<val_list>", cmd, "value list");
    return test;
  }

  private static Pair neg(String cmd) {
    Matcher m = neg.matcher(cmd);
    boolean match = true;
    Pair retVal = null;
    if(m.find()) {
      retVal = val(m.group(2));
      match = true;
      match = match && retVal.isValid();
      printMsg(match, "<neg>", cmd, "neg value");
    } else {
      retVal = val(cmd);
      match = true;
      match = match && retVal.isValid();
    }
    if (match == false) {
      m = paren.matcher(cmd);
      if( m.groupCount() < 1) {
        retVal = new Pair(null, null);
        retVal.setIsValid(false);
        return retVal;
      }
      retVal = addSub(m.group(1));
      match = true;
      match = match && retVal.isValid();
    }
    return retVal;
  }

  private static Pair val(String cmd) {
    Matcher m = intVal.matcher(cmd);
    boolean match = m.find();
    Pair retVal = null;
    if (match){
      printMsg(match, "<int>", cmd, "integer");
      retVal = new Pair("int", null);
      retVal.setIsValid(true);
    } else {
      m = bool.matcher(cmd);
      match = m.find();
      if (match){
        printMsg(match, "<bool>", cmd, "boolean");
        retVal = new Pair("boolean", cmd);
        retVal.setIsValid(true);
      } else {
        m = var.matcher(cmd);
        match = m.find();
        if (match) {
          printMsg(match, "<var>", cmd, "variable");
          retVal = new Pair("var", null);
          retVal.setIsValid(true);
        } else {
          m = paren.matcher(cmd);
          match = m.find();
          // System.out.printf("made it to paren check for '%s', %B\n", cmd, match);
          if (match) {
            printMsg(match, "<paren>", cmd, "variable");
            retVal = new Pair("paren", null);
            retVal.setIsValid(true);
          } else {
            m = str.matcher(cmd);
            match = m.find();
            if(match) {
              printMsg(match, "<str>", cmd, "string");
              retVal = new Pair("String", cmd);
              retVal.setIsValid(true);
            }
          }
        }
      }
    }
    printMsg(match, "<val>", cmd, "value");
    return retVal;
  }

  private static void printMsg(boolean match, String ntName, String cmd,
      String item) {
    if (match)
      System.out.println(ntName + ": " + cmd);
    else
      System.out.println("Failed to parse: {" + cmd + "} is not a valid "
          + item + ".");
  }

  private static void readFile(String path) throws IOException {
    try {
      File myObj = new File(path);
      String[] pathArray = path.split("/");
      String newfile = pathArray[pathArray.length - 1].split("\\.")[0];
      PrintWriter out = new PrintWriter(new FileWriter(newfile+".java"));
      Scanner myReader = new Scanner(myObj);
      int tabs = 2;
      String init = "public class " + newfile + "{\n" +
                      "  public static void main(String[] args) {";
      out.println(init);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine().trim();
        // System.out.println("trimmed " + data.trim());
        if (data == "") {
          out.println("");
        } else {
          tabs += writeToFile(data, out, tabs);
        }
      }
      String ending = "  }\n}";
      out.println(ending);
      myReader.close();
      out.close();
    } catch (FileNotFoundException e) {
      System.out.printf("No path for %s", path);
      e.printStackTrace();
    }
  }

  private static int writeToFile(String line, PrintWriter file, int tab) throws IOException {
    int addTab = 0;
    Pair lineParsed = parseCmd(line);
    if(lineParsed == null) {
      // ??? how tho
      line = "  ".repeat(tab) + line.replaceAll("\\.", ";");
    } else if(lineParsed.getType() == "print") {
      line = "  ".repeat(tab) + "System.out.println" + lineParsed.getValue() + ";";
    } else if(lineParsed.getType() == "String") {
      if(existingVariables.contains(lineParsed.getVar())) {
        line = "  ".repeat(tab) + lineParsed.getVar() + " = " + lineParsed.getValue() + ";";
      } else {
        line = "  ".repeat(tab) + "String " + lineParsed.getVar() + " = " + lineParsed.getValue() + ";";
        existingVariables.add(lineParsed.getVar());
      }
    } else if (lineParsed.getType() == "int") {
      String start = ((String) lineParsed.getValue()).toLowerCase();
      String val  = ((String) lineParsed.getValue()).toLowerCase().replaceAll("#/", "%");
      String val2 = ((String) lineParsed.getValue()).toLowerCase().replaceAll("\\^/", "/");
      String val3 = ((String) lineParsed.getValue()).toLowerCase().replaceAll("v/", "/");
      if(start != val) {
        System.out.println("ciel");
        start = val;
      } else if(start != val2) {
        System.out.println("floor");
        start = val2;
        start = "(int) Math.ceil(" + start + ")";
      }  else if (start != val3) {
        start = val3;
        start = "(int) Math.floor(" + start + ")";
      }
      System.out.println("after replaceAll "+start);
      if(existingVariables.contains(lineParsed.getVar())) {
        line = "  ".repeat(tab) + lineParsed.getVar() + " = " + start + ";";
      } else {
        line = "  ".repeat(tab) + "int " + lineParsed.getVar() + " = " + start + ";";
        existingVariables.add(lineParsed.getVar());
      }
    }  else if (lineParsed.getType() == "boolean") {
      if(existingVariables.contains(lineParsed.getVar())) {
        line = "  ".repeat(tab) + lineParsed.getVar() + " = " + ((String) lineParsed.getValue()).toLowerCase() + ";";
      } else {
        line = "  ".repeat(tab) + "boolean " + lineParsed.getVar() + " = " + ((String) lineParsed.getValue()).toLowerCase() + ";";
        existingVariables.add(lineParsed.getVar());
      }
    } else if (lineParsed.getType() == "loop") {
      String iterator = lineParsed.getVar();
      String rangeLen = (String)lineParsed.getValue();
      String loop = "";
      if(rangeLen.equals("?")) {
        System.out.println("rangelen"+rangeLen);
        // format while loop
        loop = "while(" + iterator + ") {";
      } else {
        loop = "for (int "+ iterator+ "= 0; "+ iterator+ " < "+ rangeLen +"; "+ iterator+ "++) {";
      }
      line = "  ".repeat(tab) + loop;
      addTab = 1;
    } else if(lineParsed.getType() == "end") {
      line = "  ".repeat(tab-1) + "}";
      addTab = -1;
    } else if(lineParsed.getType() == "if"){
      String condition = (String)lineParsed.getValue();
      line = "  ".repeat(tab) + "if (" + condition + ") {";
      addTab = 1;
    } else if (lineParsed.getType() == "else") {
      line = "  ".repeat(tab-1) + "} else {";
    }
    System.out.printf("line type: %s\nline value: %s\nline var: %s\n", lineParsed.getType(), lineParsed.getValue(), lineParsed.getVar());
    file.println(line);
    return addTab;
  }
}