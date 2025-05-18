# Script PowerShell para corrigir erro 0x80004002 no Windows Update
# Restaura configurações padrão e verifica componentes críticos
# Requer execução como administrador

# Verifica se o script está sendo executado como administrador
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Host "Este script precisa ser executado como administrador." -ForegroundColor Red
    exit
}

# Função para registrar mensagens
function Write-Log {
    param($Message)
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    Write-Host "[$timestamp] $Message"
}

# Caminhos do Registro
$wuPolicyPath = "HKLM:\SOFTWARE\Policies\Microsoft\Windows\WindowsUpdate"
$defenderPath = "HKLM:\SOFTWARE\Policies\Microsoft\Windows Defender\Signature Updates"
$orchestratorPath = "HKLM:\SOFTWARE\Microsoft\WindowsUpdate\Orchestrator\Configurations"
$susClientIdPath = "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\WindowsUpdate"
$autoUpdatePath = "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\WindowsUpdate\Auto Update"

# 1. Remover chaves de WSUS
Write-Log "Removendo chaves de configuração do WSUS..."
if (Test-Path $wuPolicyPath) {
    Remove-Item -Path $wuPolicyPath -Recurse -Force
    Write-Log "Chave $wuPolicyPath removida."
}
if (Test-Path $defenderPath) {
    Remove-Item -Path $defenderPath -Recurse -Force
    Write-Log "Chave $defenderPath removida."
}
if (Test-Path $orchestratorPath) {
    Remove-Item -Path $orchestratorPath -Recurse -Force
    Write-Log "Chave $orchestratorPath removida."
}
if (Get-ItemProperty -Path $susClientIdPath -Name "SUSClientID" -ErrorAction SilentlyContinue) {
    Remove-ItemProperty -Path $susClientIdPath -Name "SUSClientID" -Force
    Write-Log "SUSClientID removido."
}

# 2. Configurar valores padrão
Write-Log "Configurando valores padrão para atualizações automáticas..."
if (-not (Test-Path $autoUpdatePath)) {
    New-Item -Path $autoUpdatePath -Force | Out-Null
}
Set-ItemProperty -Path $autoUpdatePath -Name "NoAutoUpdate" -Value 0 -Force
Set-ItemProperty -Path $autoUpdatePath -Name "AUOptions" -Value 4 -Force
Write-Log "NoAutoUpdate=0, AUOptions=4 configurados."

# 3. Redefinir componentes do Windows Update
Write-Log "Redefinindo componentes do Windows Update..."
Stop-Service -Name wuauserv -Force
Stop-Service -Name cryptSvc -Force
Stop-Service -Name bits -Force
Stop-Service -Name msiserver -Force
Remove-Item -Path "C:\Windows\SoftwareDistribution" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -Path "C:\Windows\System32\catroot2" -Recurse -Force -ErrorAction SilentlyContinue
Start-Service -Name wuauserv
Start-Service -Name cryptSvc
Start-Service -Name bits
Start-Service -Name msiserver
Write-Log "Componentes redefinidos."

# 4. Verificar serviços críticos
Write-Log "Verificando serviços críticos..."
$services = @("wuauserv", "bits", "cryptSvc", "waasmedicsvc", "DiagTrack")
foreach ($svc in $services) {
    $status = Get-Service -Name $svc -ErrorAction SilentlyContinue
    if ($status.Status -ne "Running") {
        Set-Service -Name $svc -StartupType Automatic
        Start-Service -Name $svc
        Write-Log "Serviço $svc iniciado."
    } else {
        Write-Log "Serviço $svc já está em execução."
    }
}

# 5. Redefinir configurações de rede
Write-Log "Redefinindo configurações de rede..."
netsh winsock reset | Out-Null
netsh int ip reset | Out-Null
ipconfig /flushdns | Out-Null
netsh winhttp reset proxy | Out-Null
Write-Log "Configurações de rede redefinidas."

# 6. Verificar integridade do sistema
Write-Log "Verificando integridade do sistema..."
Start-Process -FilePath "sfc.exe" -ArgumentList "/scannow" -NoNewWindow -Wait
Start-Process -FilePath "DISM.exe" -ArgumentList "/Online /Cleanup-Image /RestoreHealth" -NoNewWindow -Wait
Write-Log "SFC e DISM concluídos."

# 7. Forçar detecção de atualizações
Write-Log "Forçando detecção de atualizações..."
Start-Process -FilePath "wuauclt.exe" -ArgumentList "/resetauthorization /detectnow" -NoNewWindow
Write-Log "Comando wuauclt /resetauthorization /detectnow executado."

# 8. Gerar log do Windows Update
Write-Log "Gerando log do Windows Update..."
Get-WindowsUpdateLog
Write-Log "Log gerado em C:\Windows\Logs\WindowsUpdate\WindowsUpdate.log."

# 9. Mensagem final
Write-Log "Tentativa de correção do erro 0x80004002 concluída."
Write-Log "Verifique em Configurações > Windows Update se o erro persiste."
Write-Log "Consulte o log em C:\Windows\Logs\WindowsUpdate\WindowsUpdate.log para detalhes."
Write-Host "Pressione Enter para sair..."
