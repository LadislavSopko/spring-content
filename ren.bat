@echo off
setlocal enabledelayedexpansion

for /d %%a in (spring-content-*) do (
  set "p=%%a"
  set "fp=!p:~0,14!" & set "tp=!p:~15!"
  git mv %%a strg-spring-!tp!
)

