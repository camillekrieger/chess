import ui.Repl;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Repl repl = new Repl(8080);
        repl.run();
    }
}