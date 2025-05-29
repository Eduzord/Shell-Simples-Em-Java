import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class SimpleShell
{
	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		BufferedReader console = new BufferedReader
				(new InputStreamReader(System.in));
		File caminhoAtual = new File(System.getProperty ("user.dir"));
		// Termina com <control><C>
		
		//Array para salvar o histórico dos comandos
		
		ArrayList<String> historico = new ArrayList<>();
		while (true) {
			// Ler o commando do usuário
			System.out.print("jsh: "+ caminhoAtual.getAbsolutePath()+ "> ");
			commandLine = console.readLine();
			// se o usuário teclou “return”, volte ao loop
			if (commandLine.equals(""))
				continue;
			
			//Início da implementação de histórico (tarefa 3)
            if (commandLine.equals("history")) {
                // Mostra todo o histórico com índice
                for (int i = 0; i < historico.size(); i++) {
                    System.out.println(i + " " + historico.get(i));
                }
                continue; // volta para o prompt
            } else if (commandLine.equals("!!")) {
                // Executa o último comando
                if (historico.isEmpty()) {
                    System.out.println("Erro: Nenhum comando anterior para executar.");
                    continue;
                }
                commandLine = historico.get(historico.size() - 1);
                System.out.println(commandLine); // mostra o comando que será executado
            } else if (commandLine.startsWith("!")) {
                // Tenta executar o comando pelo índice: !<n>
                String numStr = commandLine.substring(1);
                try {
                    int index = Integer.parseInt(numStr);
                    if (index < 0 || index >= historico.size()) {
                        System.out.println("Erro: índice " + index + " fora do histórico.");
                        continue;
                    }
                    commandLine = historico.get(index);
                    System.out.println(commandLine); // mostra o comando que será executado
                } catch (NumberFormatException e) {
                    System.out.println("Erro: comando inválido para execução no histórico.");
                    continue;
                }
            } else {
                // Adiciona o comando normal ao histórico
                historico.add(commandLine);
            }
            //Fim da implementação do histórico de comandos
            
			//Pega a entrada do usuário e fragmenta ela em uma lista de strings
			String[] listaComandos = commandLine.split("\\s+");
			String comandoPrincipal = listaComandos[0];
			
			
				
				if (comandoPrincipal.equals("cd")) {
					if (listaComandos.length < 2) {
						System.out.println("Caminho não especificado");
						continue;
					}
					
					String novoCaminho = listaComandos[1];
					File dir;
					if (novoCaminho.equals("..")) {
						dir = caminhoAtual.getParentFile();
						if (dir == null) {
							dir = caminhoAtual;
						}
					}
					else if (novoCaminho.equals(".")) {
						dir = caminhoAtual;
					}else {
						dir = new File(novoCaminho);
						if (!dir.isAbsolute()) {
							dir = new File(caminhoAtual, novoCaminho);
						}
					}
					if (dir.exists() && dir.isDirectory()) {	
						caminhoAtual = dir.getCanonicalFile();
					}else {
						System.out.println("cd: diretório não encontrado" + novoCaminho);
					}
					continue;
					}
				try {	
				//Converte a Array listaComandos em uma Lista (a classe ProcessBuilder aceita uma LISTA de argumentos)
				List<String> comando = Arrays.asList(listaComandos);	
				ProcessBuilder pb = new ProcessBuilder(comando);
								//Passa a variável "comando" como argumento no construtor do ProcessBuilder
				pb.directory(caminhoAtual);
				Process process = pb.start();
				// obtain the input stream
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				// read the output of the process
				String line;

				while ( (line = br.readLine()) != null)
					System.out.println(line);
				br.close();
			} catch (IOException error) {
				System.out.println("Erro ao executar o comando:" + "\""+ commandLine +"\""+ ". O seguinte erro foi apresentado: " + error);

			}


		}
	}
}