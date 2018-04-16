@echo off
setlocal enabledelayedexpansion

for /d %%a in (strg-spring-*) do (
  
  set "p=%%a"
  set "fp=!p:~0,11!" & set "tp=!p:~12!"
  
  git mv %%a spring-content-!tp!
  
)

