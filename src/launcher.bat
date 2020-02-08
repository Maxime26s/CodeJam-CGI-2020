@echo off
start "Serveur" javaw -jar serveur.jar
start "Client" javaw -jar client.jar
start "Distributeur" javaw -jar distributeur.jar
pause