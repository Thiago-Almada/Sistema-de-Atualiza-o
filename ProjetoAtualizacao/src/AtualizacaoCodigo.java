import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileWriter;
public class AtualizacaoCodigo {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("-----------MENU-----------");
        System.out.println("1) Forca atualizacao do Windows, MS Store - (indicado para maquinas que nunca rodaram o script)");
        System.out.println("2) Reset Windows Update");
        System.out.println("3) Novo Script atualizacao - (indicado para maquinas que ja rodaram outro script de atualizacao)");
        System.out.println("4) Sair");
        System.out.println("Escolha uma opcao: ");
        int opcao = scanner.nextInt();
        String baseDiretorio = System.getProperty("user.home") + "\\Documents\\Sistema_Atualizacao\\";
        String nomeScript = "";
        switch (opcao) {
            case 1:
                nomeScript = baseDiretorio + "ForcaAtualizacaoWindows";
                try {
                    ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-ExecutionPolicy", "Bypass","-File", nomeScript);
                    pb.redirectErrorStream(true);
                    Process processo = pb.start();
                    BufferedReader leLinha = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                    StringBuilder linhaLida = new StringBuilder();
                    String linha;
                    while ((linha = leLinha.readLine()) != null) {
                        linhaLida.append(linha).append("\n");
                    }
                    int exitScript = processo.waitFor();
                    if (exitScript == 0) {
                        System.out.println("Script finalizado, reiniciar a máquina");
                    } else {
                        String msgErro = "Erro na execução do script. Código de saída: " + exitScript + "\nSaída: " + linhaLida.toString();
                        System.out.println(msgErro);
                        arquivoErro(msgErro);
                    }
                }catch (Exception e) {
                    String errorMsg = "Erro ao executar o PowerShell: " + e.getMessage();
                    System.out.println(errorMsg);
                    arquivoErro(errorMsg);
                }
                break;
            case 2:
                nomeScript = baseDiretorio + "novoScriptAtualizacao.ps1";
                try {
                    ProcessBuilder pb = new ProcessBuilder("powershell.exe","-Command", "-ExecutionPolicy", "Bypass","-File", nomeScript);
                    pb.redirectErrorStream(true);
                    Process processo = pb.start();
                    BufferedReader leLinha = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                    StringBuilder linhaLida = new StringBuilder();
                    String linha;
                    while ((linha = leLinha.readLine()) != null) {
                        linhaLida.append(linha).append("\n");
                    }
                    int exitScript = processo.waitFor();
                    if (exitScript == 0) {
                        System.out.println("Script finalizado, reiniciar a máquina");
                    } else {
                        String msgErro = "Erro na execução do script. Código de saída: " + exitScript + "\nSaída: " + linhaLida.toString();
                        System.out.println(msgErro);
                        arquivoErro(msgErro);
                    }
                }catch (Exception e) {
                    String errorMsg = "Erro ao executar o PowerShell: " + e.getMessage();
                    System.out.println(errorMsg);
                    arquivoErro(errorMsg);
                }
                break;
            case 3:
                nomeScript = baseDiretorio + "ResetWindowsUpdate";
                try {
                    ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-ExecutionPolicy", "Bypass","-File", nomeScript);
                    pb.redirectErrorStream(true);
                    Process processo = pb.start();
                    BufferedReader leLinha = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                    StringBuilder linhaLida = new StringBuilder();
                    String linha;
                    while ((linha = leLinha.readLine()) != null) {
                        linhaLida.append(linha).append("\n");
                    }
                    int exitScript = processo.waitFor();
                    if (exitScript == 0) {
                        System.out.println("Script finalizado, reiniciar a máquina");
                    } else {
                        String msgErro = "Erro na execução do script. Código de saída: " + exitScript + "\nSaída: " + linhaLida.toString();
                        System.out.println(msgErro);
                        arquivoErro(msgErro);
                    }
                }catch (Exception e) {
                    String errorMsg = "Erro ao executar o PowerShell: " + e.getMessage();
                    System.out.println(errorMsg);
                    arquivoErro(errorMsg);
                }
                break;
            case 4:
                System.out.println("Saindo do programa");
                break;
            default:
                System.out.println("Opcao invalida");
                break;
        }
    }
    public static void arquivoErro(String mensagemErro) {
        try {
            String nomeMaquina = InetAddress.getLocalHost().getHostName();
            String data = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String nomeArquivo = System.getProperty("user.home") + "\\Documents\\Sistema_Atualizacao\\" + nomeMaquina + "_" + data + ".log";
            FileWriter writer = new FileWriter(nomeArquivo);
            writer.write("Erro ocorrido em " + new Date() + ":\n" + mensagemErro);
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
    }
}
