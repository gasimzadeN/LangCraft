package az.ada.plt.project.langcraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LangCraftApplication {
    public static void main(String[] args) {
        String input = "int main(){\n" +
                "    int a = 8;\n" +
                "    int b = 9;\n" +
                "    if(a > 9){\n" +
                "        a = 9;\n" +
                "    }\n" +
                "    return 0;\n" +
                "}";

        try {
            Lexer lexer = new Lexer(input);
            List<Token> tokens = lexer.tokenize();

            Parser parser = new Parser(tokens);
            parser.parse();

            ASTNode rootNode = parser.getRootNode();

            Evaluator evaluator = new Evaluator(rootNode);
            evaluator.evaluate();

            State state = evaluator.getState();
            state.printState();
        } catch (LexerException | ParserException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
