
package nio23;

/**
 *
 * @author Hrishi
 */
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;
import java.io.IOException;
import static java.nio.file.StandardWatchEventKinds.*;
import java.util.List;

public class WatchEventExample {

    public static void main(String[] args)
            throws Exception {
        new WatchEventExample().doWatch();
    }

    @SuppressWarnings("unchecked")
    private void doWatch()
            throws IOException, InterruptedException {
//      A watch service that watches registered 
//      objects for changes and events. 
        WatchService watchService;
        watchService = FileSystems.getDefault().newWatchService();

        Path path = Paths.get("D:\\JavaProgram");
//        A watch key is created when a watchable 
//        object is registered with a watch service
        WatchKey watchKey;
//        Registers the file located by this path
//        with a watch service.
        watchKey = path.register(watchService, ENTRY_DELETE, ENTRY_MODIFY, ENTRY_CREATE);

        System.out.println("Watch service registered dir: " + path.toString());

        for (;;) {

            try {
                System.out.println("Waiting for key to be signalled...");
//               Retrieves and removes next watch key, 
//               waiting if none are yet present.
                watchKey = watchService.take();
            } catch (InterruptedException ex) {
                System.out.println("Interrupted Exception");
                return;
            }
//          An event or a repeated event for an object 
//          that is registered with a WatchService.
            List<WatchEvent<?>> eventList;
//            Retrieves and removes all pending events 
//            for this watch key, returning a List of 
//            the events that were retrieved.
            eventList = watchKey.pollEvents();
            System.out.println("Process the pending events for the key: "
                    + eventList.size());

            for (WatchEvent<?> genericEvent : eventList) {
//              An event kind, for the purposes of identification.
                WatchEvent.Kind<?> eventKind;
//              Returns the event kind.
                eventKind = genericEvent.kind();
                System.out.println("Event kind: " + eventKind);

                if (eventKind == OVERFLOW) {
                    continue; // pending events for loop
                }

                WatchEvent<Path> pathEvent = (WatchEvent<Path>) genericEvent;
//               Returns the context for the event.
                Path file = pathEvent.context();
                System.out.println("File name: " + file.toString());
            }

            boolean validKey = watchKey.reset();
            System.out.println("Key reset");
            System.out.println("");

            if (!validKey) {
                System.out.println("Invalid key");
                break; // infinite for loop
            }

        } // end infinite for loop
        
//      After a watch service is closed, 
//      any further attempt to invoke operations 
//      upon it will throw ClosedWatchServiceException
        watchService.close();
        System.out.println("Watch service closed.");
    }

}
