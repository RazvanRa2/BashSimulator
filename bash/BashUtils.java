package bash;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;
public class BashUtils {

    // Implement some inner classes here: Echo, Cd, Ls, History

    static class Echo implements CommandSubscriber {
        public void executeCommand(Command c) {
            if (c.getCommand().startsWith("echo")) {
                String toBeEchoed = c.getCommand().substring(5);
                System.out.println(toBeEchoed);
            }
        }
    }

    static class Cd implements CommandSubscriber {
        public void executeCommand(Command c) {
            if (c.getCommand().startsWith("cd")) {
                c.getBash().currentDirectory = Paths.get(c.getBash().currentDirectory.toString(),
                c.getCommand().substring(3, c.getCommand().length())).toAbsolutePath();
            }
        }
    }

    static class Ls implements CommandSubscriber {
        public void executeCommand(Command c) {
            if (c.getCommand().startsWith("ls")) {
                listDirContents(c.getBash().currentDirectory);
            }
        }
        private void listDirContents(Path dirPath) {
            File folder = dirPath.toFile();
            File[] listOfFiles = folder.listFiles();
            System.out.println(dirPath);

            for (File file : listOfFiles) {
                System.out.println(file.getName());
            }
        }
    }

    static class History implements CommandSubscriber {
        public void executeCommand(Command c) {
            c.getBash().history.append(c.getCommand());
            c.getBash().history.append(" | ");

            if (c.getCommand().startsWith("history")) {
                System.out.println("History is: " + c.getBash().history);
            }
        }
    }
}
