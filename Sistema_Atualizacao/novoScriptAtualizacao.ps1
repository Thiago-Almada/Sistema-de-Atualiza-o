# Definir cores para saída
$verde = [ConsoleColor]::Green
$vermelho = [ConsoleColor]::Red
$amarelo = [ConsoleColor]::Yellow

# Função para exibir mensagens coloridas
function EscreverMensagem {
    param(
        [string]$mensagem,
        [ConsoleColor] $cor = $verde
    )
    Write-Host -ForegroundColor $cor $mensagem
}

EscreverMensagem "Iniciando a reparação do Windows Update..."

# 1. Parar serviços relacionados ao Windows Update
EscreverMensagem "Parando serviços do Windows Update...", $amarelo
Stop-Service -Name wuauserv, bits, cryptsvc -Force -ErrorAction SilentlyContinue

# 2. Renomear pasta SoftwareDistribution (limpa o cache de atualizações)
EscreverMensagem "Renomeando pasta SoftwareDistribution...", $amarelo
if (Test-Path "C:\Windows\SoftwareDistribution") {
    $dataAtual = [DateTime]::Now.ToString("yyyyMMdd")
    Rename-Item -Path "C:\Windows\SoftwareDistribution" -NewName "SoftwareDistribution_$dataAtual" -Force
}

# 3. Iniciar serviços relacionados ao Windows Update
EscreverMensagem "Iniciando serviços do Windows Update...", $amarelo
Start-Service -Name wuauserv, bits, cryptsvc -ErrorAction SilentlyContinue

# 4. Executar a solução de problemas do Windows Update
EscreverMensagem "Executando a solução de problemas do Windows Update...", $amarelo
& "msdt.exe" -id WindowsUpdateDiagnostic

# 5. Executar o comando SFC
EscreverMensagem "Executando SFC /scannow...", $amarelo
sfc /scannow

# 6. Executar comandos DISM
EscreverMensagem "Executando comandos DISM...", $amarelo
DISM /Online /Cleanup-Image /CheckHealth
DISM /Online /Cleanup-Image /RestoreHealth


EscreverMensagem "Script finalizado.", $verde
