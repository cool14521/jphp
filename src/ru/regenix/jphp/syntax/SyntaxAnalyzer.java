package ru.regenix.jphp.syntax;

import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.syntax.generators.*;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;
import ru.regenix.jphp.syntax.generators.manually.ConstExprGenerator;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;

import java.io.File;
import java.util.*;

public class SyntaxAnalyzer {
    private Tokenizer tokenizer;
    private List<Token> tokens;
    private List<Token> tree;

    private List<Generator> generators;
    private final Map<Class<? extends Generator>, Generator> map = new HashMap<Class<? extends Generator>, Generator>();

    public SyntaxAnalyzer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;

        tokens = new LinkedList<Token>();
        tree = new ArrayList<Token>();
        generators = new ArrayList<Generator>(50);

        generators.add(new ClassGenerator(this));
        generators.add(new ConstGenerator(this));
        generators.add(new FunctionGenerator(this));
        generators.add(new NameGenerator(this));

        // non-automatic
        generators.add(new SimpleExprGenerator(this));
        generators.add(new ConstExprGenerator(this));
        generators.add(new BodyGenerator(this));

        generators.add(new ExprGenerator(this));
        for (Generator generator : generators)
            map.put(generator.getClass(), generator);

        process();
    }

    protected void process(){
        tokenizer.reset();
        tree.clear();

        Token token;
        while ((token = tokenizer.nextToken()) != null){
            tokens.add(token);
        }

        ListIterator<Token> iterator = tokens.listIterator();
        while (iterator.hasNext()){
            Token current = iterator.next();
            Token gen = generateToken(current, iterator);
            tree.add(gen == null ? current : gen);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Generator> T generator(Class<T> clazz){
        Generator generator = map.get(clazz);
        if (generator == null)
            throw new AssertionError("Generator '"+clazz.getName()+"' not found");

        return (T) generator;
    }

    public Token generateToken(Token current, ListIterator<Token> iterator){
        Token gen = null;

        for(Generator generator : generators){
            if (!generator.isAutomatic())
                continue;

            gen = generator.getToken(current, iterator);
            if (gen != null)
                break;
        }

        return gen;
    }

    public List<Token> getTree(){
        return tree;
    }

    public File getFile(){
        return tokenizer.getFile();
    }
}
