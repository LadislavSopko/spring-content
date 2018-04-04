@echo off
setlocal enabledelayedexpansion

for /d %%a in (spring-content-*) do (
  
  set "p=%%a"
  set "fp=!p:~0,14!" & set "tp=!p:~15!"
  
  pushd strg-spring-!tp!
  ren src src1
  popd	
  git mv %%a\src strg-spring-!tp!
  pushd strg-spring-!tp!
  xcopy src1 src /s /e /h
  rmdir /Q /S src1
  popd
  rmdir %%a
)

