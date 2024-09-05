# powershell_service.ps1

# Function to handle the connection
function Initialize-Session {
    param (
        [string]$Server,
        [string]$User,
        [string]$Password
    )
    Connect-VIServer -Server $Server -User $User -Password $Password
}

# Initialize the session
Initialize-Session -Server "serverIP" -User "username" -Password "pass"

# Keep the process running to accept commands
# Read commands from Standard Input
while ($true) {
    $command = [Console]::In.ReadLine()
    if ($command -eq "exit") { break }
    try {
        $result = Invoke-Expression $command
        Write-Output $result
    } catch {
        Write-Error $_
    }
}
