package michael.koers;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT_TEST, Main.class);

        Map<String, Module> modules = parseInput(read);
    }

    private static Map<String, Module> parseInput(List<String> read) {

        Map<String, Module> modules = new HashMap<>();

        for (String s : read) {

            String[] parts = s.split("->");
            String broadcaster = parts[0];
            String destinations = parts[1];

            if(broadcaster.equals())
        }


        return modules;
    }
}

enum Pulse {
    HIGH,
    LOW;
}

class Broadcaster implements Module{

    @Override
    public void receive() {

    }

    @Override
    public Pulse send() {
        return null;
    }
}
class Conjunction implements Module {

    @Override
    public void receive() {

    }

    @Override
    public Pulse send() {
        return null;
    }
}
class FlipFlop implements Module {

    @Override
    public void receive() {

    }

    @Override
    public Pulse send() {
        return null;
    }
}

interface Module{

    public void receive();
    public Pulse send();

}