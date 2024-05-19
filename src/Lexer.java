import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Lexer {
    private static final Map<String, String> TOKEN_PATTERNS = new LinkedHashMap<>();

    static {
        TOKEN_PATTERNS.put("BLOC_DEB", "@PROG|@DECL|@CORPS");
        TOKEN_PATTERNS.put("BLOC_FIN", "PROG@|DECL@|CORPS@");
        TOKEN_PATTERNS.put("MOT_CLE", "DE|THEN");
        TOKEN_PATTERNS.put("CONDITION", "IF");
        TOKEN_PATTERNS.put("AFFICH", "ECRIRE");
        TOKEN_PATTERNS.put("BOUCLE_CONTINUE", "FOR");
        TOKEN_PATTERNS.put("BOUCLE_CONDITION", "WHILE");
        TOKEN_PATTERNS.put("TYPE_SIMPLE", "ENTIER|REEL|CARACTERE");
        TOKEN_PATTERNS.put("TYPE_COMPLEX", "TABLEAU|CHAINE");
        TOKEN_PATTERNS.put("OPERATEUR_AFFECTATION", ":=");
        TOKEN_PATTERNS.put("OPERATEUR_INCREMENT", "\\+\\+");
        TOKEN_PATTERNS.put("OPERATEUR_DECREMENT", "--");
        TOKEN_PATTERNS.put("OPERATEUR_ARITHMETIQUE", "ADD|SOUS|MULT|DIV|MOD");
        TOKEN_PATTERNS.put("OPERATEUR_COMPARAISON", "<|<=|>|>=|==|<>");
        TOKEN_PATTERNS.put("OPERATEUR_LOGIQUE", "\\|\\||&&|!");
        TOKEN_PATTERNS.put("DELIMITEUR", "[\\[\\];,\\(\\)]");
        TOKEN_PATTERNS.put("REEL", "\\d+\\.\\d+");
        TOKEN_PATTERNS.put("ENTIER", "\\d+");
        TOKEN_PATTERNS.put("IDENTIFIANT", "%[a-zA-Z]\\w*");
        TOKEN_PATTERNS.put("CHAINE", "\"[^\"]*\"");
        TOKEN_PATTERNS.put("CARACTERE", "'[a-zA-Z]'");
        TOKEN_PATTERNS.put("ESPACE", "\\s+");
    }

    public List<Token> tokenize(String code) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder patterns = new StringBuilder();
        for (String regex : TOKEN_PATTERNS.values()) {
            if (patterns.length() > 0) {
                patterns.append("|");
            }
            patterns.append("(").append(regex).append(")");
        }
        Pattern tokenPattern = Pattern.compile(patterns.toString());
        Matcher matcher = tokenPattern.matcher(code);

        while (matcher.find()) {
            for (String tokenType : TOKEN_PATTERNS.keySet()) {
                String value = matcher.group();
                if (value != null && value.matches(TOKEN_PATTERNS.get(tokenType))) {
                    if (!tokenType.equals("ESPACE")) {  // Ignorer les espaces blancs
                        tokens.add(new Token(tokenType, value));
                    }
                    break;
                }
            }
        }
        return tokens;
    }

//    read write in file

    public static void main(String[] args) {
        Lexer lexer = new Lexer();

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {

            StringBuilder code = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                code.append(line).append("\n");
            }

            List<Token> tokens = lexer.tokenize(code.toString());
            for (Token token : tokens) {
                writer.write(token.toString());
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    read write in the code

//    public static void main(String[] args) {
//        String code = "@PROG\n" +
//                "@DECL %a:ENTIER, %b:REEL, %c:CARACTERE, %d:TABLEAU[10] DE ENTIER, %e:TABLEAU[10] DE REEL DECL@\n" +
//                "@CORPS\n" +
//                "%a := 10;\n" +
//                "%b := 20.9 ;\n" +
//                "%c := 'c' ;\n" +
//                "%d[0] := 1 ;\n" +
//                "%e[0] := 1.1 ;\n" +
//                "ECRIRE(\"Sum is: \");\n" +
//                "ECRIRE(%a ADD %b SOUS %a);\n" +
//                "FOR(%a=0;%a<10;%a++)%a:=%a++;\n" +
//                "IF(%a > 5) THEN %a := %a--;\n" +
//                "IF(%a == 5) THEN %a := %a++;\n" +
//                "IF(%a < 5 || %a < 5 && %a < 5 ) THEN %a := %a--;\n" +
//                "CORPS@\n" +
//                "PROG@";
//        //String code = "@PROG @DECL %a:ENTIER, %b:REEL, %c:CARACTERE, %d:TABLEAU[10] DE ENTIER, %e:TABLEAU[10] DE REEL DECL@ @CORPS %a := 10; %b := 20.9 ; %c := 'c' ; %d[0] := 1 ; %e[0] := 1.1 ; ECRIRE(\"Sum is: \"); ECRIRE(%a ADD %b SOUS %a); FOR(%a=0;%a<10;%a++)%a:=%a++; IF(%a > 5) THEN %a := %a--; IF(%a == 5) THEN %a := %a++; IF(%a < 5 || %a < 5 && %a < 5 ) THEN %a := %a--; CORPS@ PROG@";
//
////        String code = "@PROG\n" +
////                "@DECL %x:ENTIER, %y:REEL, %z:CHAINE DECL@\n" +
////                "@CORPS\n" +
////                "%x := 42;\n" +
////                "%y := 3.14;\n" +
////                "%z := \"Hello, World!\";\n" +
////                "ECRIRE(\"Value of x: \");\n" +
////                "ECRIRE(%x);\n" +
////                "ECRIRE(\"Value of y: \");\n" +
////                "ECRIRE(%y);\n" +
////                "FOR(%x=0;%x<10;%x++)%x:=%x ADD 1;\n" +
////                "IF(%x >= 10) THEN %x := %x SOUS 10;\n" +
////                "CORPS@\n" +
////                "PROG@";
//        Lexer lexer = new Lexer();
//        List<Token> tokens = lexer.tokenize(code);
//        for (Token token : tokens) {
//            System.out.println(token);
//        }
//    }
}
