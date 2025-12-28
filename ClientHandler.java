import java.io.*;
import java.net.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler implements Runnable {
    
    private final Socket clientSocket;
    private final String protocolType;
    private final List<Client> clientState;

    public ClientHandler(Socket socket, String type, List<Client> state) {
        this.clientSocket = socket;
        this.protocolType = type;
        this.clientState = state;
    }

    @Override
    public void run() {

        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            // deciding which protocol to use based on the protocolType
            if (protocolType.equals("KKP")) {
                handleKkp(in, out);
            } else {
                handleRuppin(in, out, clientState);
            }
        } catch (IOException e) {
            System.err.println("Error handling client " + clientSocket.getPort() + ": " + e.getMessage()); 
        } finally {
            try {
                clientSocket.close(); 
                System.out.println("Client disconnected: " + clientSocket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }    

    }

    private void handleKkp(BufferedReader in, PrintWriter out) throws IOException {
        KnockKnockProtocol kkp = new KnockKnockProtocol();

        // Send initial message from the protocol
        String response = kkp.processInput(null);
        out.println(response);

        String fromClient;
        while ((fromClient = in.readLine()) != null) {
            if ("q".equalsIgnoreCase(fromClient)) {
                break;
            }
            response = kkp.processInput(fromClient);
            out.println(response);

            // Typical KnockKnockProtocol termination condition
            if ("Bye.".equalsIgnoreCase(response)) {
                break;
            }
        }
    }

    private void handleRuppin(BufferedReader in, PrintWriter out, List<Client> clientState) throws IOException {
        
        RuppinRegistrationProtocol rrp = new RuppinRegistrationProtocol();

        // Send initial message from the protocol
        String response = rrp.processInput(null, clientState);
        out.println(response);

        String fromClient;
        while ((fromClient = in.readLine()) != null) {
            response = rrp.processInput(fromClient, clientState);
            out.println(response);

            if (response.contains("Registration complete.")) {
            saveBackupAsCSV(); // הפעלת סעיף היצירתיות
        }

            // Termination condition can be defined as needed
            if (response.contains("Bye.")) {
                break;
            }
        }

    }

    private void saveBackupAsCSV() {
    // check if the size of the clientState list can divided by 3
    if (clientState.size() > 0 && clientState.size() % 3 == 0) {
        
        // create a timestamp for the filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "backup_" + timeStamp + ".csv";
        
        //use synchronized block to ensure thread safety while accessing clientState
        synchronized(clientState) {
            try (PrintWriter writer = new PrintWriter(new File(fileName))) {
                // writing columns headers names
                writer.println("Username,AcademicStatus,YearsAtRuppin");
                
                // iterating over all clients and writing them in CSV format (comma-separated)
                for (Client c : clientState) {
                    writer.println(c.getUsername() + "," + 
                                   c.getAcademicStatus() + "," + 
                                   c.getYearsAtRuppin());
                }
                System.out.println("Backup created successfully - " + fileName);
            } catch (FileNotFoundException e) {
                System.err.println("Error creating backup file: " + e.getMessage());
            }
        }
    }
    }
}
