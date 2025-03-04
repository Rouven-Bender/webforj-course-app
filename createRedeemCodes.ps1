# createRedeemCodes (number of codes) (name of course)
Write-Output $(for (($i = 0); $i -lt $args[0]; $i++) {
    -join ((65..90) + (97..122) | Get-Random -Count 10 | % {[char]$_})
}) > redeemCodes.txt

foreach($line in Get-Content .\redeemCodes.txt) {
    sqlite3 .\db.sqlite "insert into redeemCodes values('$line', '$($args[1])')"
}