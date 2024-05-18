import java.util.*;
import java.util.regex.*;

public class Lexer {
    private static final Map<String, String> TOKEN_PATTERNS = new LinkedHashMap<>();

    static {
        TOKEN_PATTERNS.put("MOT_CLE", "@PROG|PROG@|@DECL|DECL@|@CORPS|CORPS@|ENTIER|REEL|CARACTERE|TABLEAU|DE|CHAINE|ECRIRE|FOR|IF|THEN");
        TOKEN_PATTERNS.put("OPERATEUR_AFFECTATION", ":=");
        TOKEN_PATTERNS.put("OPERATEUR_INCREMENT", "\\+\\+");
        TOKEN_PATTERNS.put("OPERATEUR_DECREMENT", "--");
        TOKEN_PATTERNS.put("OPERATEUR_ARITHMETIQUE", "ADD|SOUS|MULT");
        TOKEN_PATTERNS.put("OPERATEUR_COMPARAISON", "<|<=|>|>=|==|<>");
        TOKEN_PATTERNS.put("OPERATEUR_LOGIQUE", "\\|\\||&&");
        TOKEN_PATTERNS.put("DELIMITEUR", "[\\[\\];,\\(\\)]");
        TOKEN_PATTERNS.put("ENTIER", "\\d+");
        TOKEN_PATTERNS.put("REEL", "\\d+\\.\\d+");
        TOKEN_PATTERNS.put("IDENTIFIANT", "%[a-zA-Z]\\w*");
        TOKEN_PATTERNS.put("CHAINE", "\"[^\"]*\"");
        TOKEN_PATTERNS.put("CARACTERE", "'[a-zA-Z]'");  // Ajout de la reconnaissance des caractères
        TOKEN_PATTERNS.put("ESPACE", "\\s+");  // Pour gérer les espaces blancs
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

    public static void main(String[] args) {
        String code = "@PROG\n" +
                "@DECL %a:ENTIER, %b:REEL, %c:CARACTERE, %d:TABLEAU[10] DE ENTIER, %e:TABLEAU[10] DE REEL DECL@\n" +
                "@CORPS\n" +
                "%a := 10;\n" +
                "%b := 20.9 ;\n" +
                "%c := 'c' ;\n" +
                "%d[0] := 1 ;\n" +
                "%e[0] := 1.1 ;\n" +
                "ECRIRE(\"Sum is: \");\n" +
                "ECRIRE(%a ADD %b SOUS %a);\n" +
                "FOR(%a=0;%a<10;%a++)%a:=%a++;\n" +
                "IF(%a > 5) THEN %a := %a--;\n" +
                "IF(%a == 5) THEN %a := %a++;\n" +
                "IF(%a < 5 || %a < 5 && %a < 5 ) THEN %a := %a--;\n" +
                "CORPS@\n" +
                "PROG@";

        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(code);
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
