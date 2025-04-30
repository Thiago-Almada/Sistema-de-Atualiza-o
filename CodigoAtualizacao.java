import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.net.InetAddress;
import java.util.Scanner;
public class CodigoAtualizacao {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-----------MENU-----------");
        System.out.println("1) Força atualização do Windows, MS Store - (indicado para máquinas que nunca rodaram o script)");
        System.out.println("2) Reset Windows Update");
        System.out.println("3) Novo Script atualização - (indicado para máquinas que já rodaram outro script de atualização)");
        System.out.println("4) Sair");
        System.out.println("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();
        switch(opcao){
            case 1:
                try{
                    String comando = "powershell -Command \"Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope LocalMachine; Start-Process powershell -ArgumentList '-File .\\script\\ForcaAtualizacao.ps1' -Verb RunAs\"";
                    Process processo = Runtime.getRuntime().exec(comando);
                    BufferedReader leLinha = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                    String linha;
                    while((linha = leLinha.readLine()) != null){
                    System.out.println(linha);
                    }
                    int codigoSaida = processo.waitFor();
                    if(codigoSaida == 0){
                        System.out.println("Script executado com sucesso!");
                    }else{
                        throw new Exception("Código de saída diferente de 0: " + codigoSaida);
                    }
                }catch(Exception e){
                    try{
                        String nomeComputador = java.net.InetAddress.getLocalHost().getHostName();
                        String caminhoLog = "erro_" + nomeComputador + ".txt";
                        java.io.FileWriter log = new java.io.FileWriter(caminhoLog, true);
                        log.write("Erro ao executar o script: " + e.getMessage() + "\n");
                        log.close();
                        System.err.println("Erro registrado em: " + caminhoLog);
                    }catch(Exception erroGravacao){
                        System.err.println("Falha ao registrar log: " + erroGravacao.getMessage());
                    }
                }
                break;
            case 2:
                try{
                    String comando = "powershell -Command \"Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope LocalMachine; Start-Process powershell -ArgumentList '-File .\\script\\ResetWindowsUpdate.ps1' -Verb RunAs\"";
                    Process processo = Runtime.getRuntime().exec(comando);
                    BufferedReader leLinha = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                    String linha;
                    while((linha = leLinha.readLine()) != null){
                    System.out.println(linha);
                    }
                    int codigoSaida = processo.waitFor();
                    if(codigoSaida == 0){
                        System.out.println("Script executado com sucesso!");
                    }else{
                        throw new Exception("Código de saída diferente de 0: " + codigoSaida);
                    }
                }catch(Exception e){
                    try{
                        String nomeComputador = java.net.InetAddress.getLocalHost().getHostName();
                        String caminhoLog = "erro_" + nomeComputador + ".txt";
                        java.io.FileWriter log = new java.io.FileWriter(caminhoLog, true);
                        log.write("Erro ao executar o script: " + e.getMessage() + "\n");
                        log.close();
                        System.err.println("Erro registrado em: " + caminhoLog);
                    }catch(Exception erroGravacao){
                        System.err.println("Falha ao registrar log: " + erroGravacao.getMessage());
                    }
                }
                break;
            case 3:
                try{
                    String comando = "powershell -Command \"Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope LocalMachine; Start-Process powershell -ArgumentList '-File .\\script\\novoScriptAtualizacao.ps1' -Verb RunAs\"";
                    Process processo = Runtime.getRuntime().exec(comando);
                    BufferedReader leLinha = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                    String linha;
                    while((linha = leLinha.readLine()) != null){
                    System.out.println(linha);
                    }
                    int codigoSaida = processo.waitFor();
                    if(codigoSaida == 0){
                        System.out.println("Script executado com sucesso!");
                    }else{
                        throw new Exception("Código de saída diferente de 0: " + codigoSaida);
                    }
                }catch(Exception e){
                    try{
                        String nomeComputador = java.net.InetAddress.getLocalHost().getHostName();
                        String caminhoLog = "erro_" + nomeComputador + ".txt";
                        java.io.FileWriter log = new java.io.FileWriter(caminhoLog, true);
                        log.write("Erro ao executar o script: " + e.getMessage() + "\n");
                        log.close();
                        System.err.println("Erro registrado em: " + caminhoLog);
                    }catch(Exception erroGravacao){
                        System.err.println("Falha ao registrar log: " + erroGravacao.getMessage());
                    }
                }
                break;
            case 4:
                System.out.println("Saindo do programa...");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
            }
    }
}