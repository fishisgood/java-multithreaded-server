import java.io.*;
import java.net.*;

public class MainClient {
	public static void main(String[] args) throws IOException {

		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		BufferedReader stdIn = null;
		int choice = 0;

		while (true) {
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("welcome to our server. please press 1 to knock knock jokes and press 2 for ruppin registeration");
			choice = Integer.parseInt(stdIn.readLine());

			if(choice == 1)
			{
				try {
					socket = new Socket("127.0.0.1", 4444);
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (UnknownHostException e) {
					System.err.println("Don't know about Knock Knock host.");
					System.exit(1);
				} catch (IOException e) {
					System.err.println("Couldn't get I/O for the connection to: your host.");
					System.exit(1);
				}
				break;
			}
			else if(choice == 2)
			{
				try {
					socket = new Socket("127.0.0.1", 4445);
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (UnknownHostException e) {
					System.err.println("Don't know about Ruppin host.");
					System.exit(1);
				} catch (IOException e) {
					System.err.println("Couldn't get I/O for the connection to: your host.");
					System.exit(1);
				}
				break;
			}
			else
			{
				System.out.println("invalid choice. please try again.");
				continue;
			}

		}
		

		String fromServer;
		String fromUser;

		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			if (fromServer.contains("Bye."))
				break;

			System.out.print("Client: ");
			fromUser = stdIn.readLine();
			if (fromUser != null) {
				out.println(fromUser);
			}
		}

		out.close();
		in.close();
		stdIn.close();
		socket.close();
	}
}
