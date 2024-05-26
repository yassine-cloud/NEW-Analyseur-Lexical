import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Lexer {
    private static final Map<String, String> TOKEN_PATTERNS = new LinkedHashMap<>();

    static {
        TOKEN_PATTERNS.put("BLOC_DEB", "@PROG|@DECL|@CORPS");
        TOKEN_PATTERNS.put("BLOC_FIN", "PROG@|DECL@|CORPS@");
        TOKEN_PATTERNS.put("MOT_CLE", "DE|THEN|ELSE");
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
        TOKEN_PATTERNS.put("OPERATEUR_COMPARAISON", "<>|<=|<|>=|>|==");
        TOKEN_PATTERNS.put("OPERATEUR_LOGIQUE", "\\|\\||&&|!");
        TOKEN_PATTERNS.put("DELIMITEUR", "[\\[\\];,\\(\\)]");
        TOKEN_PATTERNS.put("IDENTIFIANT", "%\\d+\\w*");
        TOKEN_PATTERNS.put("REEL", "\\d+\\.\\d+");
        TOKEN_PATTERNS.put("ENTIER", "\\d+");
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

        try (BufferedReader reader = new BufferedReader(new FileReader("input3.txt"));
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

}
