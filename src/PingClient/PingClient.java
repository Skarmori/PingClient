package PingClient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

/*
* Server to process ping requests over UDP. java PingClient host port
*/
public class PingClient {

	public static void main(String[] args) throws Exception {

		String ServerName = ("localhost");
		int port = 12345;

		// Mínimo
		long minimo = Long.MAX_VALUE;
		// Máximo
		long maximo = 0;
		// Médio
		double soma = 0;
		// Recebidos
		int recebidos = 0;
		// Enviados
		int enviados = 10;
		// Perdidos
		int perdidos = 0;
		// % de perdas
		int percenPerdas = 0;

		DatagramSocket socket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(ServerName);

		for (int i = 0; i < 10; i++) {

			// Calcular mínimo / máximo / médio //

			String Message = "Processando.. ";
			DatagramPacket request = new DatagramPacket(Message.getBytes(), Message.length(), IPAddress, port);

			long SendTime = System.currentTimeMillis();
			System.out.println("Ping " + i + " para " + IPAddress + ":" + port + " time = " + SendTime + "ms\n");
			socket.send(request);

			DatagramPacket reply = new DatagramPacket(new byte[1024], 1024);

			socket.setSoTimeout(1000);

			try {
				socket.receive(reply);

				long RcvTime = System.currentTimeMillis();
				long rtt = RcvTime - SendTime;
				System.out.println("Rcv time do pacote " + i + ": " + RcvTime + "ms\n");
				System.out.println("RTT do pacote " + i + ": " + rtt + "ms\n");
				recebidos++;

				if (minimo > rtt) {
					minimo = rtt;
				}

				if (maximo < rtt) {
					maximo = rtt;
				}

				soma = soma + rtt;
				perdidos = enviados - recebidos;
				percenPerdas = perdidos * 10;

				System.out.println("Tempo mínimo: " + minimo + "ms\n");
				System.out.println("Tempo máximo: " + maximo + "ms\n");

			} catch (IOException E) {

			}

			Thread.sleep(1000);

		}

		double medio = soma / recebidos;

		System.out.print("Estatísticas do Ping para localhost/127.0.0.1:12345: " + "\n");
		System.out.println("    Pacotes: Enviados = " + enviados + ", " + "Recebidos = " + recebidos + ", "
				+ "Perdidos = " + perdidos + ", " + "(" + percenPerdas + "% de perda)" + "\n");
		System.out.println("Tempo Mínimo, Máximo e Médio dos pacotes sem perdas em milissegundos: ");
		System.out.println("    Mínimo = " + minimo + "ms" + ", " + "Máximo = " + maximo + "ms" + ", " + "Médio = "
				+ medio + "ms");
	}

	/*
	 * Print ping data to the standard output stream.
	 */
	private static void printData(DatagramPacket request) throws Exception {

		byte[] buf = request.getData();

		ByteArrayInputStream bais = new ByteArrayInputStream(buf);

		InputStreamReader isr = new InputStreamReader(bais);

		BufferedReader br = new BufferedReader(isr);

		String line = br.readLine();

		System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + new String(line));

	}
}
