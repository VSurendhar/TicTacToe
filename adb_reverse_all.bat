#!/bin/bash

@echo off
set PORT=8081

adb start-server

for /f "skip=1 tokens=1,2" %%A in ('adb devices') do (
    if "%%B"=="device" (
        echo Setting reverse for %%A
        adb -s %%A reverse tcp:%PORT% tcp:%PORT%
    )
)

echo Done