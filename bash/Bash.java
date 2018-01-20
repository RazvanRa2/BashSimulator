package bash;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;

public final class Bash {
    Path currentDirectory;
    StringBuffer history;

    private CommandPublisher publisher;
    private static final String EXIT = "exit";

    public Bash() {
        history = new StringBuffer();
        currentDirectory = Paths.get(".").toAbsolutePath();
        publisher = new BashPublisher();

        // TODO 4 & 5 & 6 & 7
        // CommandSubscribers know how to execute the commands.
        // Subscribe some to the Command publisher.
        publisher.subscribe(new BashUtils.Echo());
        publisher.subscribe(new BashUtils.Cd());
        publisher.subscribe(new BashUtils.Ls());
        publisher.subscribe(new BashUtils.History());
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String commandText = scanner.nextLine();

            if (commandText.equals("exit")) {
                break;
            } else {
                Bash thisBash = this;
                Thread t = new Thread() {
                    public void run() {
                        publisher.publish(new Command(commandText,thisBash));
                    }
                };
                t.start();
            }
        }
    }

    class BashPublisher implements CommandPublisher {
        ArrayList<CommandSubscriber> subscribers =
        new ArrayList<CommandSubscriber>();

        public void subscribe(CommandSubscriber subscriber) {
            subscribers.add(subscriber);
        }

        public void publish(Command command) {
            for (CommandSubscriber s : subscribers) {
                s.executeCommand(command);
            }
        }
    }

}
