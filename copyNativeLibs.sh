#!/bin/bash

# Copies native libs added to target/native.. by hidapi to native/..
pwd
cp ./target/native/mac/libhidapi-jni-32.jnilib ./native/macosx/x86/
cp ./target/native/mac/libhidapi-jni-64.jnilib ./native/macosx/x86_64/

cp ./target/native/linux/libhidapi-jni-32.so ./native/linux/x86/
cp ./target/native/linux/libhidapi-jni-64.so ./native/linux/x86_64/

cp ./target/native/win/hidapi-jni-32.dll ./native/windows/x86/
cp ./target/native/win/hidapi-jni-64.dll ./native/windows/x86_64/

