# SouthPaw — LibGDX Setup Script
# Run this ONCE to configure Gradle and generate the wrapper files.
#
# Prerequisites: Java 11+ installed (already confirmed on your machine)
#
# Usage:
#   1. Open PowerShell in "Card Game/" folder
#   2. Run:  .\setup.ps1

$gradleHome = "$env:USERPROFILE\gradle\gradle-8.7"
$gradleBin  = "$gradleHome\bin\gradle.bat"

# Check if Gradle was already downloaded by the agent
if (-not (Test-Path $gradleBin)) {
    Write-Host "Gradle not found. Downloading Gradle 8.7 from gradle.org..."
    $zip  = "$env:TEMP\gradle-8.7-bin.zip"
    $dest = "$env:USERPROFILE\gradle"
    Invoke-WebRequest -Uri "https://services.gradle.org/distributions/gradle-8.7-bin.zip" -OutFile $zip
    Expand-Archive -Path $zip -DestinationPath $dest -Force
    Remove-Item $zip
}

Write-Host "Gradle found at: $gradleBin"

# Add Gradle to PATH for this session
$env:PATH = "$gradleHome\bin;$env:PATH"

# Generate Gradle Wrapper files (gradlew.bat etc.) in project root
Set-Location $PSScriptRoot
& $gradleBin wrapper

Write-Host ""
Write-Host "============================"
Write-Host "Setup complete!"
Write-Host "To run the game:"
Write-Host "  .\gradlew.bat desktop:run"
Write-Host "============================"
